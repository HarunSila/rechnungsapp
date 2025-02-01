import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { BillCreationService } from '../../../services/bill-creation.service';

/*
  This component is responsible for the text template step in the bill creation process.
  It provides a form where the user can enter a text template for the bill.
  The user can enter a pre- and posttext.

  For future improvements, a template could be uploaded by the user as PDF.
*/

@Component({
  selector: 'app-step-text-template',
  standalone: true,
  imports: [ReactiveFormsModule, MatFormFieldModule, FormsModule, CommonModule],
  templateUrl: './step-text-template.component.html',
  styleUrl: './step-text-template.component.scss'
})
export class StepTextTemplateComponent implements OnInit{
  private pretext = '';
  private posttext = '';
  billCreationService = inject(BillCreationService);
  fb = inject(FormBuilder);
  textTemplateForm!: FormGroup;

  // Initializes the pre- and posttext with the correct values.
  ngOnInit(): void {
    this.initPrePostText()
    this.billCreationService.preText.next(this.pretext);
    this.billCreationService.postText.next(this.posttext);
    this.billCreationService.step3Valid.next(true);
    this.initForm();

    // Updates the pre- and posttext in the bill creation service when the form changes.
    this.textTemplateForm.valueChanges.subscribe((value) => {
      this.billCreationService.preText.next(value.preText);
      this.billCreationService.postText.next(value.postText);
    });
  }

  // Initializes the pre- and posttext with the correct values.
  // The pre- and posttext are based on the user input in the previous steps.
  private initPrePostText(): void {
    let anrede = '';
    let bestellnummer = this.billCreationService.bestellnummer;
    let bestelldatum = this.formatDate(this.billCreationService.leistungVon.value);
    switch (this.billCreationService.ansprechpartner.anrede.value) {
      case 'HERR':
        anrede = 'Sehr geehrter Herr ' + this.billCreationService.ansprechpartner.name.value + ',';
        break;
      case 'FRAU':
        anrede = 'Sehr geehrte Frau ' + this.billCreationService.ansprechpartner.name.value + ',';
        break;
      default:
        anrede = 'Sehr geehrte Damen und Herren,';
        break;
    }
    if (this.billCreationService.ansprechpartner.name.value === '') {
      anrede = 'Sehr geehrte Damen und Herren,';
    }
    this.pretext = `${anrede}\n\ngemäß Ihrer Bestellung ${bestellnummer} vom ${bestelldatum} berechnen wir wie folgt:`;
    this.posttext = 'Bitte überweisen Sie den Gesamtbetrag auf das unten angegebene Konto.\n\nMit freundlichen Grüßen';
  }

  // Initializes the form with the pre- and posttext.
  private initForm(): void {
    this.textTemplateForm = this.fb.group({
      preText: [this.pretext, Validators.required],
      postText: [this.posttext, Validators.required],
    });

    // Updates the step3Valid in the bill creation service when the form changes.
    this.textTemplateForm.patchValue({ 
      preText: this.billCreationService.preText.value,
      postText: this.billCreationService.postText.value
    });

    this.textTemplateForm.statusChanges.subscribe(status => {
      this.billCreationService.step3Valid.next(status === 'VALID');
    });
  }

  // Formats a date to a string, representing the date in the format 'dd.mm.yyyy'.
  private formatDate(date: any): string {
    if (!(date instanceof Date)) {
        date = new Date(date);
    }
    
    if (isNaN(date.getTime())) {
        return '';
    }

    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    
    return `${day}.${month}.${year}`;
  }
}
