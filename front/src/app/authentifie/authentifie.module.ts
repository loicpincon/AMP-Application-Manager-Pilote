import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';

import { AuthGuardService as AuthGuard } from '../core/services/auth-guard.service';
import { AuthentifieComponent } from './authentifie.component';
import { MaterialModule } from '../material.module';
import { ApplicationModule } from './application/application.module';
import { AdministrationModule } from './administration/administration.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenUserHeaderInterceptor } from './interceptor/TokenUserHeaderInterceptor';

const routes: Routes = [
    {
        path: '', component: AuthentifieComponent, children: [
            {
                path: 'application', loadChildren: './application/application.module#ApplicationModule', canActivate: [AuthGuard]
            },
            {
                path: 'administration', loadChildren: './administration/administration.module#AdministrationModule', canActivate: [AuthGuard]
            },
            {
                path: '', redirectTo: 'application', pathMatch: 'full'
            }
        ]
    }
];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AuthentifieRoutingModule { }

@NgModule({
    declarations: [
        AuthentifieComponent,
    ],
    imports: [
        CommonModule,
        AuthentifieRoutingModule,
        MaterialModule,
        ApplicationModule,
        AdministrationModule
    ], exports: [
        TokenUserHeaderInterceptor
    ]
})
export class AuthentifieModule { }
