import { Component, ViewChild, OnInit } from '@angular/core';
import { Application } from './application/modele/Application';
import { MatSidenav } from '@angular/material';
import { Router, NavigationExtras } from '@angular/router';
import { ApmService } from '../core/services/apm.service';
import { SidenavService } from '../core/services/sideNav.service';
import { FormControl } from '@angular/forms';

@Component({
    selector: 'app-secure-authent',
    templateUrl: './authentifie.component.html',
    styleUrls: ['./authentifie.component.css']
})
export class AuthentifieComponent implements OnInit {

    title = 'amp';

    applications: Application[];

    user: any;
    @ViewChild('menuApp', { static: true }) public menuApp: MatSidenav;
    constructor(private _router: Router, private appService: ApmService, private sidenavService: SidenavService) { }

    mode = new FormControl('side');

    ngOnInit() {
        this.sidenavService.sideNav = this.menuApp;
        //A SUPPRIMER. UNIQUEMENT POUR BYPASSER CE QUE LOIC A FAIT ET QUI MARCHE PAS
        localStorage.setItem('USER_TOKEN', 'AZERTYIOP');
        console.log("Application chargée")
        this.appService.recupererSession().subscribe(user => {
            this.user = user;
            this.appService.recupererAllApplicationsByUser().subscribe(apps => {
                this.applications = apps;
            })
        })

    }

    loadApp(id: string) {
        const navigationExtras: NavigationExtras = {
            queryParams: { idApp: id },
            skipLocationChange: true
        };
        //Navigue sur la page Application avec l'id de l'app
        this._router.navigate(['/'], navigationExtras);
        this.sidenavService.sideNav.close();
    }


    deconnexion() {
        this.appService.deconnecterSession().subscribe(data => {
            sessionStorage.removeItem('USER_TOKEN')
            this._router.navigate(['/unsecure'])
            this.user = null;
        }, error => {
            alert(error.error.message);
        })
    }





}