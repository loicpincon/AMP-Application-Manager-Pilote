import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Application } from '../modele/Application';

@Component({
    selector: 'application-administration',
    templateUrl: './administration.component.html',
    styleUrls: ['./administration.component.css']
})
export class AdministrationApplicationComponent {

    @Input()
    app: Application;

    constructor(private router: Router) {

    }
    gestionMembres() {

        this.router.navigate(['/secure/administration/gestion-droits'], { queryParams: { idApp: this.app.id } });


    }


}