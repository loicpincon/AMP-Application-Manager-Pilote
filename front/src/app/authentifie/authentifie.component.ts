import { Component, ViewChild, OnInit, NgZone } from '@angular/core';
import { Application, Instance } from './application/modele/Application';
import { MatSidenav } from '@angular/material';
import { Router, NavigationExtras } from '@angular/router';
import { ApmService } from '../core/services/apm.service';
import { SidenavService } from '../core/services/sideNav.service';
import { FormControl } from '@angular/forms';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { User } from './administration/modele/model';
import { ApplicationInformation } from './consultation-log/modele/Model';
import { DataSharedService } from '../core/services/dataShared.service';
@Component({
    selector: 'app-secure-authent',
    templateUrl: './authentifie.component.html',
    styleUrls: ['./authentifie.component.css']
})
export class AuthentifieComponent implements OnInit {

    title = 'amp';

    applications: Application[];
    idApp: string;
    urlPhoto: string;
    appInfo: ApplicationInformation;
    user: User;
    @ViewChild('menuApp', { static: true }) public menuApp: MatSidenav;
    constructor(private zone: NgZone, private _router: Router, private dataShared: DataSharedService, private appService: ApmService, private sidenavService: SidenavService) { }

    mode = new FormControl('side');

    ngOnInit() {

        this.appService.recupererInfoApp().subscribe(infos => {
            this.appInfo = infos;
        })

        this.sidenavService.sideNav = this.menuApp;
        this.appService.recupererSession().subscribe(user => {
            this.user = user;
            this.urlPhoto = this.appService.getImageProfil(user.token);
            this.appService.recupererAllApplicationsByUser().subscribe(apps => {
                this.applications = apps;
            })
            this.dataShared.currentUrlPhoto.subscribe(url => {
                this.zone.run(() => { console.log("passe dans la zone"); this.urlPhoto = ""; this.urlPhoto = this.appService.getImageProfil(this.user.token) + "?" + new Date(); });
            })
        })
    }

    loadApp(id: string) {
        this._router.navigate(['/secure/application/pilotage', id]);
        this.idApp = id;
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

    loadAdmin() {
        //Navigue sur la page Application avec l'id de l'app
        this._router.navigate(['/secure/administration/gestion-droits']);
        this.sidenavService.sideNav.close();
    }

    openMenu() {
        this.sidenavService.sideNav.open();
        this.appService.recupererAllApplicationsByUser().subscribe(apps => {
            this.applications = apps;
        })
    }


    verifierDroit() {

        this.user.rights.forEach(right => {
            if (right.applicationId == this.idApp && (right.level === "CP" || right.level === "PROP" || right.level === "EXPERT")) {
                console.log('ok')
                return true;
            } else {
                console.log(right.level)

            }
        })
        console.log('non')

        return false;

    }






}
