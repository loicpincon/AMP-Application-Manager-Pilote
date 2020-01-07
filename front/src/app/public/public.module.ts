import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthentComponent } from './authent/authent.component';
import { CoreModule } from '../core/core.module';
import { PublicComponent } from './public.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../material.module';
import { CommonModule } from '@angular/common';

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
        CoreModule,
        ReactiveFormsModule,
        MaterialModule,
        CommonModule,
    ]
})
export class PublicModule { }
