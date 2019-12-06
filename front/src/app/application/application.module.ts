import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationComponent } from './application.component';
import { PilotageComponent } from './pilotage/pilotage.component';
import { ApplicationRoutingModule } from './applicationRouting.module';
import { MatButtonModule } from '@angular/material';
import {MatMenuModule} from '@angular/material/menu';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSidenavModule} from '@angular/material/sidenav';

@NgModule({
  declarations: [
    ApplicationComponent,
    PilotageComponent,
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    ApplicationRoutingModule,
    MatMenuModule,
    MatToolbarModule,
    MatSidenavModule
  ]
})
export class ApplicationModule { }
