import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { BillCreationService } from '../../../services/bill-creation.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { AnredeET } from '../../../models/anredeET';
import { Geschaeftspartner } from '../../../models/geschaeftspartner';
import { GeschaeftspartnerAPIService } from '../../../services/api/geschaeftspartnerAPI.service';
import { HasRoleDirective } from '../../../directives/HasRoleDirective';
import { KeycloakService } from '../../../services/keycloak.service';

/*
  This component is responsible for the partner selection step in the bill creation process.
  It provides a form to select a partner and an additional form to select a contact person.
  The form for entering a new partner is disabled if the user does not have the required permissions to create a partner object.
  The user can select a partner from a list of partners fetched from the backend.
*/

@Component({
  selector: 'app-step-partner-selection',
  standalone: true,
  imports: [ReactiveFormsModule, MatFormFieldModule, FormsModule, MatButtonModule, CommonModule, HasRoleDirective],
  templateUrl: './step-partner-selection.component.html',
  styleUrl: './step-partner-selection.component.scss'
})
export class StepPartnerSelectionComponent implements OnInit {
  private billCreationService = inject(BillCreationService);
  private fb = inject(FormBuilder);
  private geschaeftspartnerAPI = inject(GeschaeftspartnerAPIService);
  private cdr = inject(ChangeDetectorRef);
  private keycloakService = inject(KeycloakService);

  geschaeftspartnerForm!: FormGroup;
  ansprechpartnerForm!: FormGroup;
  geschaeftspartner: Geschaeftspartner[] = [];
  anredeOptions = Object.values(AnredeET);
  selectedPartner: any = null;

  // Fetches the partners from the backend and initializes the forms for the partner and contact person selection.
  ngOnInit(): void {
    this.geschaeftspartnerAPI.getGeschaeftspartner().subscribe((geschaeftspartner: Geschaeftspartner[]) => {
      this.geschaeftspartner = geschaeftspartner.sort((a, b) => a.name.localeCompare(b.name));;
      this.cdr.markForCheck();
    });

    this.initGeschaeftspartnerForm();
    this.initAnsprechpartnerForm();
    this.enableGeschaeftspartnerForm();
    this.enableAnsprechpartnerForm();

    // Subscribe to form changes to update the bill creation service with the entered values.
    this.geschaeftspartnerForm.valueChanges.subscribe((value) => {
      this.billCreationService.geschaeftspartner.name.next(value.name);
      this.billCreationService.geschaeftspartner.beschreibung.next(value.beschreibung);
      this.billCreationService.geschaeftspartner.anschrift.strasse.next(value.anschrift.strasse);
      this.billCreationService.geschaeftspartner.anschrift.nummer.next(value.anschrift.nummer);
      this.billCreationService.geschaeftspartner.anschrift.plz.next(value.anschrift.plz);
      this.billCreationService.geschaeftspartner.anschrift.ort.next(value.anschrift.ort);
      this.billCreationService.geschaeftspartner.anschrift.land.next(value.anschrift.land);
      // Enable the contact person form if the partner form is valid.
      this.enableAnsprechpartnerForm(); 
    });

    // Subscribe to form changes to update the bill creation service with the entered values.
    this.ansprechpartnerForm.valueChanges.subscribe((value) => {
      this.billCreationService.ansprechpartner.name.next(value.name);
      this.billCreationService.ansprechpartner.vorname.next(value.vorname);
      this.billCreationService.ansprechpartner.email.next(value.email);
      this.billCreationService.ansprechpartner.anrede.next(value.anrede);
      this.billCreationService.ansprechpartner.anschrift.strasse.next(value.anschrift.strasse);
      this.billCreationService.ansprechpartner.anschrift.nummer.next(value.anschrift.nummer);
      this.billCreationService.ansprechpartner.anschrift.plz.next(value.anschrift.plz);
      this.billCreationService.ansprechpartner.anschrift.ort.next(value.anschrift.ort);
      this.billCreationService.ansprechpartner.anschrift.land.next(value.anschrift.land);
    });
  }

  // Initializes the form for selecting a partner.
  private initGeschaeftspartnerForm() {
    this.geschaeftspartnerForm = this.fb.group({
      name: ['', Validators.required],
      beschreibung: ['', Validators.required],
      anschrift: this.fb.group({
        strasse: ['', Validators.required],
        nummer: ['',Validators.required],
        plz: ['', Validators.required],
        ort: ['', Validators.required],
        land: ['', Validators.required],
      }),
    });

    // Set the form values to the values stored in the bill creation service.
    this.geschaeftspartnerForm.patchValue({
      name: this.billCreationService.geschaeftspartner.name.value,
      beschreibung: this.billCreationService.geschaeftspartner.beschreibung.value,
      anschrift: {
        strasse: this.billCreationService.geschaeftspartner.anschrift.strasse.value,
        nummer: this.billCreationService.geschaeftspartner.anschrift.nummer.value,
        plz: this.billCreationService.geschaeftspartner.anschrift.plz.value,
        ort: this.billCreationService.geschaeftspartner.anschrift.ort.value,
        land: this.billCreationService.geschaeftspartner.anschrift.land.value,
      },
    });

    // Subscribe to form status changes to update the bill creation service with the form status.
    this.geschaeftspartnerForm.statusChanges.subscribe(status => {
      this.billCreationService.step2Valid.next(status === 'VALID');
    });
  }

  // Initializes the form for selecting a contact person.
  private initAnsprechpartnerForm() {
    this.ansprechpartnerForm = this.fb.group({
      name: [''],
      vorname: [''],
      email: [''],
      anrede: [AnredeET.HERR],
      anschrift: this.fb.group({
        strasse: [''],
        nummer: [''],
        plz: [''],
        ort: [''],
        land: [''],
      }),
    });

    // Set the form values to the values stored in the bill creation service.
    this.ansprechpartnerForm.patchValue({
      name: this.billCreationService.ansprechpartner.name.value,
      vorname: this.billCreationService.ansprechpartner.vorname.value,
      email: this.billCreationService.ansprechpartner.email.value,
      anrede: this.billCreationService.ansprechpartner.anrede.value,
      anschrift: {
        strasse: this.billCreationService.ansprechpartner.anschrift.strasse.value,
        nummer: this.billCreationService.ansprechpartner.anschrift.nummer.value,
        plz: this.billCreationService.ansprechpartner.anschrift.plz.value,
        ort: this.billCreationService.ansprechpartner.anschrift.ort.value,
        land: this.billCreationService.ansprechpartner.anschrift.land.value,
      },
    });
  }

  // Selects a partner from the list of partners. If a partner is selected, the partner form is disabled and the contact person form is enabled.
  // If no partner is selected, the partner form is reset and enabled, and the contact person form is disabled.
  selectGeschaeftspartner(partner: any): void {
    if (this.selectedPartner) {
      this.selectedPartner = null;
      this.billCreationService.resetGeschaeftspartner();
      this.geschaeftspartnerForm.reset();
      this.enableGeschaeftspartnerForm();
      this.ansprechpartnerForm.disable();
      this.billCreationService.step2Valid.next(false);
    } else {
      this.selectedPartner = partner;
      this.billCreationService.geschaeftspartner.id.next(partner.id);
      this.geschaeftspartnerForm.patchValue(partner);
      this.geschaeftspartnerForm.disable();
      this.ansprechpartnerForm.enable();
      this.billCreationService.step2Valid.next(true);
    }
  }

  // Enables the contact person form if the partner form is valid.
  enableAnsprechpartnerForm(): void {
    if(this.geschaeftspartnerForm.valid) {
      this.ansprechpartnerForm.enable();
    } else {
      this.ansprechpartnerForm.disable();
    }
  }

  // Enables the partner form if the user has the required permissions.
  enableGeschaeftspartnerForm(): void {
    if(this.keycloakService.hasRoles('Partner-Administration')) {
      this.geschaeftspartnerForm.enable();
    } else {
      this.geschaeftspartnerForm.disable();
    }
  }
}
