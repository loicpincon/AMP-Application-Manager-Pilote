import { Routes, RouterModule } from '@angular/router';



import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { Ng2ImgMaxModule } from 'ng2-img-max';
import { ManagementDockerComponent } from './management-docker.component';
import { DockerFileComponent } from './dockerfile/dockerfile.component';
import { DashboardComponent } from './dashboard/dashboard.component';

import { ChartsModule } from 'ng2-charts';

const routes: Routes = [
    {
        path: '', component: ManagementDockerComponent, children: [
            {
                path: 'dashboard', component: DashboardComponent
            },
            {
                path: 'dockerfiles', component: DockerFileComponent
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
export class ManagementDockerRoutingModule { }

@NgModule({
    declarations: [
        ManagementDockerComponent,
        DockerFileComponent,
        DashboardComponent
    ],
    imports: [
        ChartsModule,
        CommonModule,
        Ng2ImgMaxModule,
        FormsModule,
        ManagementDockerRoutingModule,
        ReactiveFormsModule,
        MaterialModule,
        FlexLayoutModule,
    ],
    entryComponents: [

    ],
    providers: [
        DatePipe
    ]
})
export class ManagementDockerModule { }
