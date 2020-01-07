import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: 'secure', loadChildren: './authentifie/authentifie.module#AuthentifieModule'
  },
  {
    path: 'unsecure', loadChildren: './public/public.module#PublicModule'
  },
  {
    path: '**', redirectTo: 'secure'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
