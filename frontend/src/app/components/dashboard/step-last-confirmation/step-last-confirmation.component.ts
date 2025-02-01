import { Component, inject } from '@angular/core';
import { BillCreationService } from '../../../services/bill-creation.service';
import { CommonModule } from '@angular/common';

/*
  This component is responsible for displaying the last step of the bill creation process.
  It calculates the netto, tax and brutto sum of the bill and displays them.
  It also formats the date of the service period and displays the positions of the bill.
*/

@Component({
  selector: 'app-step-last-confirmation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './step-last-confirmation.component.html',
  styleUrl: './step-last-confirmation.component.scss'
})
export class StepLastConfirmationComponent {
  billCreationService = inject(BillCreationService);

  // Calculates the netto sum of the bill
  calculateNettoSum(): number {
    return this.billCreationService.positions.reduce(
      (sum, position) =>
        sum + (position.nettoEinzelpreisInCent / 100) * position.menge,
      0
    );
  }

  // Calculates the tax of the bill
  calculateTax(): number {
    const nettoSum = this.calculateNettoSum();
    const taxRate = this.billCreationService.umsatzsteuer.satz.value || 0;
    return (nettoSum * taxRate) / 100;
  }

  // Calculates the brutto sum of the bill
  calculateBruttoSum(): number {
    return this.calculateNettoSum() + this.calculateTax();
  }

  // Formats the date of the service period
  formatLeistungVon(): string {
    return this.formatDate(this.billCreationService.leistungVon.value);
  }

  // Formats the date of the service
  formatLeistungBis(): string {
    return this.formatDate(this.billCreationService.leistungBis.value);
  }

  // Formats the date of the bill creation to a string in the format 'dd.mm.yyyy'
  formatDate(date: any): string {
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
