import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationComponent } from './application.component';
import { PilotageComponent } from './pilotage/pilotage.component';
import { ApplicationRoutingModule } from './applicationRouting.module';
import { MatButtonModule } from '@angular/material';



@NgModule({
  declarations: [
    ApplicationComponent,
    PilotageComponent,
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    ApplicationRoutingModule

  ]
})
export class ApplicationModule { }
