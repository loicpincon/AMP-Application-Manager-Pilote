import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ApplicationComponent } from './application.component';
import { PilotageComponent } from './pilotage/pilotage.component';
import { CreationApplicationComponent } from './creation-application/creation-application.component';
//import { AuthGuardService } from '../core/services/userLogginResolver.config';

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
