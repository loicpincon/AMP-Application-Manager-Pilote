import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthentComponent } from '../public/authent/authent.component';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule, MatIconModule } from '@angular/material';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@NgModule({
  declarations: [
    ,
  ],
  imports: [
    CommonModule,
    MatToolbarModule,
    MatMenuModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ]
})
export class CoreModule { }
