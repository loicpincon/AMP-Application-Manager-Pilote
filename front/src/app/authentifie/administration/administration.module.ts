import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GestionDroitsApplicationsComponent } from './gestion-droits-applications/gestion-droits-applications.component';
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
import { Routes, RouterModule } from '@angular/router';
import { MaterialModule } from 'src/app/material.module';

const routes: Routes = [
  {
    path: '', component: AdministrationComponent, children: [
      {
        path: 'gestion-droits', component: GestionDroitsApplicationsComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdministrationRoutingModule { }

@NgModule({
  declarations: [
    AdministrationComponent, 
    GestionDroitsApplicationsComponent, 
    ModalAjoutUser
  ],
  imports: [
    CommonModule,
    AdministrationRoutingModule,
    MaterialModule,
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
