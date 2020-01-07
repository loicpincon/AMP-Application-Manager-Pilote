import { NgModule } from '@angular/core';
import { ApplicationComponent } from './application.component';
import { PilotageComponent } from './pilotage/pilotage.component';
import { InstanceComponent } from './instance/instance.component';
import { CreationApplicationComponent } from './creation-application/creation-application.component';
import { CommonModule } from '@angular/common';
import { ApplicationRoutingModule } from './applicationRouting.module';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/material.module';

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
