



import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthentComponent } from './authent/authent.component';
import { AuthGuardService as AuthGuard } from '../core/services/auth-guard.service';
import { CoreModule } from '../core/core.module';
import { PublicComponent } from './public.component';

const routes: Routes = [
    {
        path: 'login', component: AuthentComponent,
    },
    {
        path: '**', redirectTo: 'login'
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PublicRoutingModule { }




@NgModule({
    declarations: [
        PublicComponent,
        AuthentComponent
    ],
    imports: [
        PublicRoutingModule,
        CoreModule
    ]
})
export class PublicModule { }
