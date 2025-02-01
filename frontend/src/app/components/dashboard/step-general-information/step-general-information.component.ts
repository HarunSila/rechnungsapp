import { Component, inject } from '@angular/core';
import { BillCreationService } from '../../../services/bill-creation.service';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

/*
  This component is responsible for the first step in the bill creation process.
  The user can enter the general information about the bill, such as the title, the period of service, and the VAT rate.
  The user can also choose whether they want to receive the bill by email.
  The user can navigate to the next step if the form is valid.
*/

@Component({
  selector: 'app-step-general-information',
  standalone: true,
  imports: [ReactiveFormsModule, MatFormFieldModule, FormsModule, MatButtonModule, CommonModule],
  templateUrl: './step-general-information.component.html',
  styleUrl: './step-general-information.component.scss'
})
export class StepGeneralInformationComponent {
  billCreationService = inject(BillCreationService);
  fb = inject(FormBuilder);
  generalInformationForm!: FormGroup;
  nummer!: string;
  bestellnummer!: string

  // Initialize the form with the values from the service
  // The order number is generated from the current timestamp, so it is unique
  ngOnInit(): void {
    this.nummer = Date.now().toString();
    this.billCreationService.nummer = this.nummer;
    this.bestellnummer = `ORD-${this.nummer.slice(-6)}`;
    this.billCreationService.bestellnummer = this.bestellnummer;
    this.initForm();

    // Updates the general information in the bill creation service when the form changes
    this.generalInformationForm.valueChanges.subscribe((value) => {
      this.billCreationService.bezeichnung.next(value.bezeichnung);
      this.billCreationService.perMail.next(value.perMail);
      this.billCreationService.leistungVon.next(value.leistungVon);
      this.billCreationService.leistungBis.next(value.leistungBis);
      this.billCreationService.umsatzsteuer.name.next(value.umsatzsteuer.name);
      this.billCreationService.umsatzsteuer.satz.next(value.umsatzsteuer.satz);
    });
  }

  // Initialize the form with the values from the service
  private initForm(): void {
    this.generalInformationForm = this.fb.group({
      bezeichnung: ['', Validators.required],
      perMail: [false],
      leistungVon: ['', Validators.required],
      leistungBis: ['', Validators.required],
      umsatzsteuer: this.fb.group({
        name: ['', Validators.required],
        satz: [null, Validators.required],
      }),
    });

    // Set the values from the service
    this.generalInformationForm.patchValue({
      bezeichnung: this.billCreationService.bezeichnung.value,
      perMail: this.billCreationService.perMail.value,
      leistungVon: this.billCreationService.leistungVon.value,
      leistungBis: this.billCreationService.leistungBis.value,
      umsatzsteuer: {
        name: this.billCreationService.umsatzsteuer.name.value,
        satz: this.billCreationService.umsatzsteuer.satz.value,
      },
    });

    // Emit the validity of the form
    this.generalInformationForm.statusChanges.subscribe(status => {
      this.billCreationService.step1Valid.next(status === 'VALID');
    });
  }

}
