import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { Geschaeftspartner } from '../../models/geschaeftspartner';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { PartnerModalComponent } from './partner-modal/partner-modal.component';
import { GeschaeftspartnerAPIService } from '../../services/api/geschaeftspartnerAPI.service';
import { Router } from '@angular/router';
import { HasRoleDirective } from '../../directives/HasRoleDirective';
import { KeycloakService } from '../../services/keycloak.service';

/*
* This component is responsible for displaying the partners of the company.
* It is possible to add, edit and delete partners.
* The user can also check the bills of a partner.
* For the implementation of the add, edit and delete functionality, a dialog is used.
* The dialog is opened with the correct elements when the user clicks on the corresponding button.
* For the implementation of the check bills functionality, the user is redirected to the bills page of the partner.
*/

@Component({
  selector: 'app-partners',
  standalone: true,
  imports: [MatIconModule, MatCardModule, CommonModule, MatButtonModule, HasRoleDirective],
  templateUrl: './partners.component.html',
  styleUrl: './partners.component.scss'
})
export class PartnersComponent implements OnInit {
  geschaeftspartner: Geschaeftspartner[] = [];
  private dialog = inject(MatDialog);
  private cdr = inject(ChangeDetectorRef);
  private geschaeftspartnerAPI = inject(GeschaeftspartnerAPIService);
  private router = inject(Router);
  private keycloakService = inject(KeycloakService);

  // The constructor is used to inject the necessary services.
  ngOnInit(): void {
    this.geschaeftspartnerAPI.getGeschaeftspartner().subscribe((geschaeftspartner: Geschaeftspartner[]) => {
      this.geschaeftspartner = geschaeftspartner;
      this.cdr.markForCheck();
    });
  }

  // The addPartner function is used to open the dialog for adding a partner.
  addPartner() {
    const dialogRef = this.dialog.open(PartnerModalComponent, {
      width: '600px',
      data: { index: this.geschaeftspartner.length + 1},
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.geschaeftspartnerAPI.addGeschaeftspartner(result).subscribe(() => {
          this.geschaeftspartner.push(result);
          this.cdr.markForCheck();
        });
      }
    });
  }

  // The editPartner function is used to open the dialog for editing a partner and updating the partner in the database.
  // The function receives the partner to be edited as a parameter and provides the partner to the dialog.
  editPartner(partner: Geschaeftspartner) {
    const dialogRef = this.dialog.open(PartnerModalComponent, {
      width: '600px',
      data: { partner },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        const index = this.geschaeftspartner.findIndex((p) => p === partner);
        if (index !== -1) {
          this.geschaeftspartnerAPI.updateGeschaeftspartner(result).subscribe(() => {
            this.geschaeftspartner[index] = result
            this.cdr.markForCheck()
          });
        }
      }
    });
  }

  // The deletePartner function is used to delete a partner from the database.
  deletePartner(partnerId: any) {
    this.geschaeftspartnerAPI.deleteGeschaeftspartner(partnerId).subscribe(() => {
      this.geschaeftspartner = this.geschaeftspartner.filter((partner) => partner.id !== partnerId);
      this.cdr.markForCheck();
    });
  }

  // The checkBills function is used to redirect the user to the bills page of a partner.
  checkBills(partnerId: any) {
    this.router.navigate([`/geschaeftspartner/${partnerId}`]);
  }
}
