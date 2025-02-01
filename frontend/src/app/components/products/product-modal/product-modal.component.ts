import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';

/*
  This component is a modal that is used to add or edit a product.
  It is used in the ProductsComponent.
*/

@Component({
  selector: 'app-product-modal',
  standalone: true,
  imports: [ MatDialogModule, MatFormFieldModule, MatSelectModule, FormsModule, MatInputModule, MatButtonModule, ReactiveFormsModule],
  templateUrl: './product-modal.component.html',
  styleUrl: './product-modal.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductModalComponent implements OnInit {
  readonly dialogRef = inject(MatDialogRef);
  readonly data = inject(MAT_DIALOG_DATA);
  fb = inject(FormBuilder);
  productForm!: FormGroup;
  isEditMode: boolean = false;
  units: string[] = ['Stück', 'Stunde', 'PT'];

  // On load, create a form group with the product data if it is in edit mode.
  // Otherwise, create a form group with default values.
  ngOnInit() {
    this.isEditMode = !!this.data?.product;

    this.productForm = this.fb.group({
      id: [this.data?.product?.id || this.data?.index, []],
      name: [this.data?.product?.name || '', [Validators.required]],
      description: [this.data?.product?.description || '', []],
      einheit: [this.data?.product?.einheit || this.units[0], [Validators.required]],
      price: [this.data?.product?.price || 0, [Validators.required, Validators.min(0.01)]],
      menge: [this.data?.product?.menge || 1, [Validators.required, Validators.min(1)]],
    });
  }

  // Close the modal without saving.
  close(): void {
    this.dialogRef.close();
  }

  // Save the product data and close the modal.
  save(): void {
    if (this.productForm.valid) {
      this.dialogRef.close(this.productForm.value);
    }
  }
}