import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthentComponent } from './core/authent/authent.component';
import { AuthGuardService as AuthGuard } from './core/services/auth-guard.service';

const routes: Routes = [
  {
    path: 'login', component: AuthentComponent,
  },
  {
    path: 'application', loadChildren: './application/application.module#ApplicationModule',canActivate: [AuthGuard] 
  },
  {
   path: 'administration', loadChildren: './administration/administration.module#AdministrationModule',canActivate: [AuthGuard] 
  },
  {
    path: '**', redirectTo: 'application'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
