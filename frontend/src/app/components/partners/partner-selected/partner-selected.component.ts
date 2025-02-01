import { Component, inject, OnInit } from '@angular/core';
import { Geschaeftspartner } from '../../../models/geschaeftspartner';
import { GeschaeftspartnerAPIService } from '../../../services/api/geschaeftspartnerAPI.service';
import { ActivatedRoute } from '@angular/router';
import { Rechnung } from '../../../models/rechnung';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button'

/* 
  This component displays the details of a selected partner.
  It shows the partner's name, address, and a list of invoices.
  Primaarily it lists the invoices associated with the partner.
*/

@Component({
  selector: 'app-partner-selected',
  standalone: true,
  imports: [CommonModule, MatButtonModule],
  templateUrl: './partner-selected.component.html',
  styleUrl: './partner-selected.component.scss'
})
export class PartnerSelectedComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private geschaeftPartnerAPI = inject(GeschaeftspartnerAPIService);
  geschaeftspartner!: Geschaeftspartner;
  rechnungen: Rechnung[] = [];
  expandedRechnungen: { [id: number]: boolean } = {};

  // Fetches the partner details and invoices associated with the partner
  ngOnInit(): void {
    this.geschaeftPartnerAPI.getGeschaeftspartnerById(
      Number(this.route.snapshot.paramMap.get('id'))
    ).subscribe((data: any) => {
      console.log(data);
      this.geschaeftspartner = data;
      this.rechnungen = data.rechnungen;
    });
  }

  // Displays the status of the invoice details as button text
  getToggleStatus(rechnung: Rechnung) {
    let status = this.expandedRechnungen[rechnung.id!] ? 'Verbergen' : 'Anzeigen'
    return status;
  }

  // Toggles the visibility of the invoice details
  toggleRechnung(id: any): void {
    this.expandedRechnungen[id] = !this.expandedRechnungen[id];
  }

  // Returns the status of the invoice details. On true, the details are displayed
  getexpandedStatus(rechnung: Rechnung) {
    return this.expandedRechnungen[rechnung.id!];
  }

  // Calculates the net sum of the invoice
  calculateNettoSum(positions: any[]): number {
    return positions.reduce((sum, pos) => sum + pos.menge * pos.nettoEinzelpreisInCent, 0) / 100;
  }

  // Calculates the tax amount of the invoice
  calculateTax(positions: any[], taxRate: number): number {
    return (this.calculateNettoSum(positions) * taxRate) / 100;
  }

  // Calculates the gross sum of the invoice
  calculateBruttoSum(positions: any[], taxRate: number): number {
    return this.calculateNettoSum(positions) + this.calculateTax(positions, taxRate);
  }
}
