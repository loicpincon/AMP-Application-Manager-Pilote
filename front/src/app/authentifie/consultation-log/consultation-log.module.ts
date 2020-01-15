import { Routes, RouterModule } from '@angular/router';



import { NgModule } from '@angular/core';

import { CommonModule, DatePipe } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MaterialModule } from 'src/app/material.module';

import { FlexLayoutModule } from '@angular/flex-layout';

import { ConsultationLogComponent } from './consultation-log.component';
import { DashboardLogComponent } from './dashboard/dashboard.component';
import { RechercheLogComponent } from './recherche/recherche.component';

const routes: Routes = [
    {
        path: '', component: ConsultationLogComponent, children: [
            {
                path: 'dashboard', component: DashboardLogComponent
            },
            {
                path: 'recherche', component: RechercheLogComponent
            },
            {
                path: '', redirectTo: 'dashboard', pathMatch: 'full'
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ConsultationLogRoutingModule { }

@NgModule({
    declarations: [
        ConsultationLogComponent,
        DashboardLogComponent,
        RechercheLogComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        ConsultationLogRoutingModule,
        ReactiveFormsModule,
        MaterialModule,
        FlexLayoutModule,
    ],
    entryComponents: [

    ],
    providers: [

    ]
})
export class ConsultationLogModule { }
