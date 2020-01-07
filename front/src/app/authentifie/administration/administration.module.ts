import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GestionDroitsApplicationsComponent } from './gestion-droits-applications/gestion-droits-applications.component';
import { AdministrationRoutingModule } from './administrationRouting.module';
import { AdministrationComponent } from './administration.component';
import { MatTableModule } from '@angular/material/table';
import { MatRadioModule } from '@angular/material/radio';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

import { MatSelectModule } from '@angular/material/select';
import { ModalAjoutUser } from './gestion-droits-applications/modal-ajout-user/modal-ajout-user';
import { MatInputModule } from '@angular/material';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [AdministrationComponent, GestionDroitsApplicationsComponent, ModalAjoutUser],
  imports: [
    CommonModule,
    AdministrationRoutingModule,
    MatSelectModule,
    MatTableModule,
    MatRadioModule,
    MatButtonModule,
    MatDialogModule,
    MatAutocompleteModule,
    MatInputModule,
    ReactiveFormsModule

  ],
  entryComponents: [
    GestionDroitsApplicationsComponent,
    ModalAjoutUser
  ],
  providers: [

  ]
})
export class AdministrationModule { }
