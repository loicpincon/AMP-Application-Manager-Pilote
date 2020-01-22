import { Routes, RouterModule } from '@angular/router';



import { NgModule } from '@angular/core';
import { UtilisateurComponent } from './utilisateur.component';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { UtilisateurProfilComponent } from './profil/profil.component';
import { Ng2ImgMaxModule } from 'ng2-img-max';


const routes: Routes = [
    {
        path: '', component: UtilisateurComponent, children: [
            {
                path: 'profil/edition', component: UtilisateurProfilComponent
            },
            {
                path: 'profil', component: UtilisateurProfilComponent
            },
            {
                path: '', redirectTo: 'profil', pathMatch: 'full'
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class UtilisateurRoutingModule { }

@NgModule({
    declarations: [
        UtilisateurComponent,
        UtilisateurProfilComponent
    ],
    imports: [
        CommonModule,
        Ng2ImgMaxModule,
        FormsModule,
        UtilisateurRoutingModule,
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
export class UtilisateurModule { }
