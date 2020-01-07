import { NgModule } from '@angular/core';
import { ApplicationComponent } from './application.component';
import { PilotageComponent } from './pilotage/pilotage.component';
import { InstanceComponent } from './instance/instance.component';
import { CreationApplicationComponent } from './creation-application/creation-application.component';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MaterialModule } from 'src/app/material.module';
import { RouterModule, Routes } from '@angular/router';

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
    CreationApplicationComponent
  ],
  imports: [
    CommonModule,
    ApplicationRoutingModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexLayoutModule,
  ]
})
export class ApplicationModule { }
