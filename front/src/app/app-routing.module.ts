import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthentComponent } from './core/authent/authent.component';
import { AuthGuardService as AuthGuard } from './core/services/auth-guard.service';

const routes: Routes = [
  {
    path: 'secure', loadChildren: './authentifie.module#AuthentifieModule'
  },
  {
    path: 'unsecure', loadChildren: './public.module#PublicModule'
  },
  {
    path: '**', redirectTo: 'secure'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
