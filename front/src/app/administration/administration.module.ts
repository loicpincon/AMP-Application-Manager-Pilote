import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GestionDroitsApplicationsComponent } from './gestion-droits-applications/gestion-droits-applications.component';
import { AdministrationRoutingModule } from './administrationRouting.module';
import { AdministrationComponent } from './administration.component';
import {MatTableModule} from '@angular/material/table';
import {MatRadioModule} from '@angular/material/radio';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';

import {MatSelectModule} from '@angular/material/select';


@NgModule({
  declarations: [AdministrationComponent,GestionDroitsApplicationsComponent],
  imports: [
    CommonModule,
    AdministrationRoutingModule,
    MatSelectModule,
    MatTableModule,
    MatRadioModule,
    MatButtonModule,
    MatDialogModule
  ]
})
export class AdministrationModule { }
