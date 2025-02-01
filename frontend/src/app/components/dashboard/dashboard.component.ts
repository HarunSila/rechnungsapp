import { AfterViewChecked, Component, ElementRef, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { products } from '../../models/mockProductProvider';
import { FormsModule } from '@angular/forms'; 
import { MatInputModule } from '@angular/material/input';
import { BillCreationService } from '../../services/bill-creation.service';
import { StepGeneralInformationComponent } from './step-general-information/step-general-information.component';
import { StepPartnerSelectionComponent } from './step-partner-selection/step-partner-selection.component';
import { StepTextTemplateComponent } from './step-text-template/step-text-template.component';
import { StepLastConfirmationComponent } from './step-last-confirmation/step-last-confirmation.component';
import { KeycloakService } from '../../services/keycloak.service';

/*
  This component is the entry point for the dashboard page. It is responsible for rendering the dashboard page and handling the user interactions.
  The user can create a new invoice by selecting products, entering general information, selecting a partner, and confirming the invoice.
  For each step, a separate component is created and rendered based on the active step.
*/

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatIconModule, MatButtonModule, RouterModule, CommonModule, FormsModule, MatInputModule,
    StepGeneralInformationComponent, StepPartnerSelectionComponent, StepTextTemplateComponent, StepLastConfirmationComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit, AfterViewChecked, OnDestroy {
  private billCreationService = inject(BillCreationService);
  private elementRef = inject(ElementRef);
  private keycloakService = inject(KeycloakService);
  username?: string;
  email?: string;
  products: { id: number; name: string; price: number; description: string; einheit: string; menge: number; }[] = [];

  // Async initialization for the products and user information from the keycloak profile on page load
  async ngOnInit() {
    this.products = products;
    const profile = this.keycloakService.profile;
    if (profile) {
      this.username = `${profile.firstName} ${profile.lastName}`;
      this.email = profile.email;
    }
  }

  // Scroll to the active step after the view is checked, meaning after the step is updated
  // Lifecycle hook is activated after every check of the component's view, meaning after every update of the step
  ngAfterViewChecked(): void {
    this.scrollToStep(this.getActiveStep());
  }

  // Reset the active step and the positions when the component is destroyed
  ngOnDestroy(): void {
    document.body.style.overflow = 'auto';
    this.billCreationService.reset();
  }

  // Get the active step, which is the current step in the invoice creation process
  getActiveStep(): number {
    return this.billCreationService.activeStep;
  }

  // Toggle the selection of a product
  toggleSelection(product: any): void {
    const productIndex = this.billCreationService.getPositions().findIndex(p => p.id === product.id);
    let positions = this.billCreationService.getPositions();;
    if (productIndex === -1) {
      positions.push({
        id: product.id,
        bezeichnung: product.name,
        menge: 1,  // Default quantity
        einheit: product.einheit,
        nettoEinzelpreisInCent: product.price * 100  // Assuming price is in EUR and converting to cents
      });
    } else {
      // If the product is already selected, remove it
      positions.splice(productIndex, 1);
    }
    this.billCreationService.setPositions(positions);
  }

  // Update the quantity of a selected product
  updateQuantity(id: number, quantity: number): void {
    let positions = this.billCreationService.getPositions();;
    const position = positions.find(p => p.id === id);
    if (position) {
      position.menge = quantity; // Update the quantity
    }
    this.billCreationService.setPositions(positions);
  }

  // Check if the product is selected
  isSelected(id: number): boolean {
    return this.billCreationService.getPositions().some(p => p.id === id);
  }

  // Check if any product is selected for the invoice
  // If no product is selected, the user cannot proceed to the next step
  // The user must select at least one product to proceed
  createInvoice() {
    let positions = this.billCreationService.getPositions();
    if(positions.length === 0) 
      return false;
    positions.forEach(position => {
      position.id = undefined;
    });
    this.billCreationService.setPositions(positions);
    this.billCreationService.activeStep = 1;
    document.body.style.overflowY = 'hidden';
    return true;
  }

  // Scroll to the step based on the current step number, ensuring the correct component is visible to the user
  private scrollToStep(stepNumber: number) {
    let element;
    switch (stepNumber) {
      case 1:
        element = this.elementRef.nativeElement.querySelector('#step1');
        break;
      case 2:
        element = this.elementRef.nativeElement.querySelector('#step2');
        break;
      case 3:
        element = this.elementRef.nativeElement.querySelector('#step3');
        break;
      case 4:
        element = this.elementRef.nativeElement.querySelector('#step4');
        break;
    }
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }

  // Navigate to the previous step, which is the step before the current step
  // Reset the positions and quantities when navigating back to the product selection step
  onPreviousStep() {
    this.billCreationService.activeStep = this.billCreationService.activeStep - 1;
    if(this.billCreationService.activeStep === 0) {
      this.billCreationService.resetPositions();
      this.products.forEach(product => {
        product.menge = 1;
      });
    } else if(this.billCreationService.activeStep === 1) {
      this.billCreationService.resetGeschaeftspartner();
    }
  }

  // Navigate to the next step, which is the step after the current step
  onNextStep() {
    this.billCreationService.activeStep = this.billCreationService.activeStep + 1;
  }

  // Submit the invoice and reset the invoice creation process
  onSubmit() {
    this.billCreationService.createBill();
    this.billCreationService.reset();
    this.products.forEach(product => {
      product.menge = 1;
    });
  }

  // Check if the user can navigate to the next step
  // The user cannot navigate to the next step if the current step is not valid
  toggleNavigation(index: number) {
    let isValid = false;
    switch (index) {
      case 1:
        isValid = this.billCreationService.step1Valid.value;
        break;
      case 2:
        isValid = this.billCreationService.step2Valid.value;
        break;
      case 3:
        isValid = this.billCreationService.step3Valid.value;
        break;
    }
    return !isValid;
  }

}
