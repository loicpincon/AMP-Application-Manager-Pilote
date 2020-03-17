import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GestionDroitsApplicationsComponent } from './gestion-droits-applications/gestion-droits-applications.component';
import { AdministrationComponent } from './administration.component';
import { ModalAjoutUser } from './gestion-droits-applications/modal-ajout-user/modal-ajout-user';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';
import { MaterialModule } from 'src/app/material.module';
import { GestionRolesComponent } from './gestion-roles/gestion-roles.component';
import { BrowserModule } from '@angular/platform-browser';

const routes: Routes = [
  {
    path: '', component: AdministrationComponent, children: [
      {
        path: 'gestion-droits', component: GestionDroitsApplicationsComponent
      },
      {
        path: 'gestion-roles', component: GestionRolesComponent
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
    ModalAjoutUser,
    GestionRolesComponent
  ],
  imports: [
    CommonModule,

    AdministrationRoutingModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule

  ],
  entryComponents: [
    GestionDroitsApplicationsComponent,
    ModalAjoutUser
  ],
  providers: [

  ]
})
export class AdministrationModule { }
