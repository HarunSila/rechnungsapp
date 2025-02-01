import { Component, ChangeDetectionStrategy, inject, ChangeDetectorRef } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { products } from '../../models/mockProductProvider';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ProductModalComponent } from './product-modal/product-modal.component';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HasRoleDirective } from '../../directives/HasRoleDirective';

/*
* This component is responsible for displaying the products in a list.
* It also provides the functionality to add, edit and delete products.
* The products are currently not fetched from a backend, but are provided by a mockProductProvider, because they are not 
* part of the requirements of the task, but are placeholders for demonstration purposes.
*/

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [MatCardModule, CommonModule, MatButtonModule, MatIconModule, FormsModule, ReactiveFormsModule, HasRoleDirective],
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductsComponent {
  products: { id: number; name: string; price: number; description: string; einheit: string; menge: number; }[] = [];
  dialog = inject(MatDialog);
  cdr = inject(ChangeDetectorRef);

  // Load the products
  ngOnInit(): void {
    this.products = products;
  }

  // Add a new product through a modal dialog
  addProduct() {
    const dialogRef = this.dialog.open(ProductModalComponent, {
      width: '600px',
      data: { index: this.products.length + 1},
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        console.log('New Product:', result);
        this.products.push(result);
        this.cdr.markForCheck();
      }
    });
  }

  // Edit a product through a modal dialog by passing the product to be edited
  editProduct(product: any) {
    const dialogRef = this.dialog.open(ProductModalComponent, {
      width: '600px',
      data: { product },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        const index = this.products.findIndex((p) => p === product);
        if (index !== -1) {
          this.products[index] = result;
        }
        this.cdr.markForCheck();
      }
    });
  }

  // Delete a product
  deleteProduct(productId: number) {
    this.products = this.products.filter(product => product.id !== productId);
  }
}
