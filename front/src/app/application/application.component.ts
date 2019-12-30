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
    ngOnInit(): void {
        throw new Error("Method not implemented.");
    }

 
}