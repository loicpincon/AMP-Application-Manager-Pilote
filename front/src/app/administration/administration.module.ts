import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GestionDroitsApplicationsComponent } from './gestion-droits-applications/gestion-droits-applications.component';
import { AdministrationRoutingModule } from './administrationRouting.module';



@NgModule({
  declarations: [GestionDroitsApplicationsComponent],
  imports: [
    CommonModule,
    AdministrationRoutingModule
  ]
})
export class AdministrationModule { }
