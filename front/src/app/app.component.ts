import { Component } from '@angular/core';
import { WebSocketAPI } from './core/webSocket/WebSocket.socket';
import { Router } from '@angular/router';
import { ApmService } from './core/services/apm.service';
import { FormControl } from '@angular/forms';
import { Application } from './application/modele/Application';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    title = 'amp';

    applications: Application[];

    user: any;

    constructor(private _router: Router, private appService: ApmService) { }

    mode = new FormControl('push');

    ngOnInit() {
        this.appService.recupererSession().subscribe(user => {
            this.user = user;
            this.appService.recupererAllApplications().subscribe(apps => {
                this.applications = apps;
            })
        })

    }


    deconnexion() {
        this.appService.deconnecterSession().subscribe(data => {
            sessionStorage.removeItem('USER_TOKEN')
            this._router.navigate(['login'])
            this.user = null;
        }, error => {
            alert(error.error.message);
        })
    }





}
