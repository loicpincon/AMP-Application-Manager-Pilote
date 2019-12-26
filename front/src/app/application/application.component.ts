import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSidenav } from '@angular/material';
import { FormControl } from '@angular/forms';
import { ApmService } from '../core/services/apm.service';
@Component({
    selector: 'app-root',
    templateUrl: './application.component.html',
    styleUrls: ['./application.component.css']

})
export class ApplicationComponent implements OnInit {

    user: any;

    constructor(private _router: Router, private appService: ApmService) { }

    mode = new FormControl('push');

    ngOnInit() {
        this.appService.recupererSession().subscribe(user=>{
            this.user = user;
        })
    }


    deconnexion() {
        this.appService.deconnecterSession().subscribe(data=>{
            localStorage.removeItem('USER_TOKEN')
            this._router.navigate(['login'])
        },error=>{
            alert(error.error.message);
        })
    }





}