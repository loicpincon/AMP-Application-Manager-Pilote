import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { GestionDroitsApplicationsComponent } from './gestion-droits-applications/gestion-droits-applications.component';
import { AdministrationComponent } from './administration.component';


const routes: Routes = [
  {
    path: '', component: AdministrationComponent, children: [
      {
        path: 'gestion-droits', component: GestionDroitsApplicationsComponent
      },
      {
        path: '', redirectTo: 'gestion-droits', pathMatch: 'full'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdministrationRoutingModule { }
