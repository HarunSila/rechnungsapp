import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';

/*
  * This component is used to create a new partner or edit an existing one.
  * It is a modal dialog that is opened by the PartnerComponent.
  * The dialog contains a form to enter the partner's data.
  * The form is validated and the data is returned to the PartnerComponent.
*/

@Component({
  selector: 'app-partner-modal',
  standalone: true,
  imports: [ MatDialogModule, MatFormFieldModule, MatSelectModule, FormsModule, MatInputModule, MatButtonModule, ReactiveFormsModule],
  templateUrl: './partner-modal.component.html',
  styleUrl: './partner-modal.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PartnerModalComponent implements OnInit {
  readonly dialogRef = inject(MatDialogRef);
  readonly data = inject(MAT_DIALOG_DATA);
  fb = inject(FormBuilder);
  partnerForm!: FormGroup;
  isEditMode: boolean = false;

  // Initialize the form with the partner's data if it is an edit operation
  // Otherwise, initialize the form with empty values
  ngOnInit(): void {
    this.isEditMode = !!this.data?.partner;

    this.partnerForm = this.fb.group({
      id: [this.data?.partner?.id || null, []],
      name: [this.data?.partner?.name || '', [Validators.required]],
      beschreibung: [this.data?.partner?.beschreibung || '', []],
      anschrift: this.fb.group({
        strasse: [this.data?.partner?.anschrift.strasse || '', [Validators.required]],
        nummer: [this.data?.partner?.anschrift.nummer || '', [Validators.required]],
        ort: [this.data?.partner?.anschrift.ort || '', [Validators.required]],
        plz: [this.data?.partner?.anschrift.plz || '', [Validators.required]],
        land: [this.data?.partner?.anschrift.land || '', [Validators.required]],
      }),
    });
  }

  // Close the dialog without saving the data
  close(): void {
    this.dialogRef.close();
  }

  // Save the data and close the dialog
  save(): void {
    if (this.partnerForm.valid) {
      this.dialogRef.close(this.partnerForm.value);
    }
  }
}
