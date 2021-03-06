import { NgModule } from '@angular/core';
import { ApplicationComponent } from './application.component';
import { PilotageComponent } from './pilotage/pilotage.component';
import { InstanceComponent } from './instance/instance.component';
import { ActionComponent } from './action/action.component';
import { CreationApplicationComponent } from './creation-application/creation-application.component';
import { CommonModule, DatePipe } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MaterialModule } from 'src/app/material.module';
import { RouterModule, Routes } from '@angular/router';
import { ParametreComponent } from './parametre/parametre.component';
import { ActionUserComponent } from './action-user/action-user.component';
import { LivrableComponent } from './livrable/livrable.component';
import { ModalAjoutInstance } from './instance/modal-ajout-instance/modal-ajout-instance';
import { DataSharedService } from 'src/app/core/services/dataShared.service';
import { DialogAjouterSerieParamComponent } from './parametre/dialog-param-ajouter/dialog-param-ajouter.component';
import { ModifierParametreComponent } from './parametre/modifier-parametre-serie/modifier-parametre.component';
import { ModificationApplicationComponent } from './modification-application/modification-application.component';

const routes: Routes = [
  {
    path: '', component: ApplicationComponent, children: [
      {
        path: 'pilotage/:idApp', component: PilotageComponent
      },
      {
        path: 'creation', component: CreationApplicationComponent
      },
      {
        path: 'modification/:idApp', component: ModificationApplicationComponent
      },
      {
        path: 'parametres', component: ModifierParametreComponent
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
    ModificationApplicationComponent,
    ActionUserComponent,
    LivrableComponent,
    ModalAjoutInstance,
    DialogAjouterSerieParamComponent,
    ModifierParametreComponent
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
    ModalAjoutInstance,
    ParametreComponent,
    DialogAjouterSerieParamComponent
  ],
  providers: [
    DataSharedService,
    DatePipe
  ]
})
export class ApplicationModule { }
