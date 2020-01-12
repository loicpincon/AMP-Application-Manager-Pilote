import { NgModule } from '@angular/core';
import { ApplicationComponent } from './application.component';
import { PilotageComponent } from './pilotage/pilotage.component';
import { InstanceComponent } from './instance/instance.component';
import { ActionComponent } from './action/action.component';
import { CreationApplicationComponent } from './creation-application/creation-application.component';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MaterialModule } from 'src/app/material.module';
import { RouterModule, Routes } from '@angular/router';
import { ParametreComponent } from './parametre/parametre.component';
import { ActionUserComponent } from './action-user/action-user.component';
import { LivrableComponent } from './livrable/livrable.component';
import { ModalAjoutInstance } from './instance/modal-ajout-instance/modal-ajout-instance';
import { BrowserModule } from '@angular/platform-browser';

const routes: Routes = [
  {
    path: '', component: ApplicationComponent, children: [
      {
        path: 'pilotage', component: PilotageComponent
      },
      {
        path: 'creation', component: CreationApplicationComponent
      },
      {
        path: '', redirectTo: 'pilotage', pathMatch: 'full'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApplicationRoutingModule { }

@NgModule({
  declarations: [
    ApplicationComponent,
    PilotageComponent,
    InstanceComponent,
    CreationApplicationComponent,
    ActionComponent,
    ParametreComponent,
    ActionUserComponent,
    LivrableComponent,
    ModalAjoutInstance
  ],
  imports: [
    CommonModule,
    FormsModule,
    ApplicationRoutingModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexLayoutModule,
  ],
  entryComponents: [
    InstanceComponent,
    ModalAjoutInstance
  ]
})
export class ApplicationModule { }
