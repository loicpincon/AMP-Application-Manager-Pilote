import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GestionDroitsApplicationsComponent } from './gestion-droits-applications/gestion-droits-applications.component';
import { AdministrationRoutingModule } from './administrationRouting.module';
import { AdministrationComponent } from './administration.component';



@NgModule({
  declarations: [AdministrationComponent,GestionDroitsApplicationsComponent],
  imports: [
    CommonModule,
    AdministrationRoutingModule
    
  ]
})
export class AdministrationModule { }
