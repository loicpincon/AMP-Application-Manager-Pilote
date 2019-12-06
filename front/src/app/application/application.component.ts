import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { MatSidenav } from '@angular/material';
import { FormControl } from '@angular/forms';
@Component({
    selector: 'app-root',
    templateUrl: './application.component.html',
    styleUrls: ['./application.component.css']

})
export class ApplicationComponent {

    constructor(private _router: Router) {}

    mode = new FormControl('push');
    deconnexion(){
        localStorage.removeItem('token')
        this._router.navigate(['login'])
    }
    
}