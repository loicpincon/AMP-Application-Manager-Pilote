import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSidenav } from '@angular/material';
import { FormControl } from '@angular/forms';
import { ApmService } from '../core/services/apm.service';
@Component({
    selector: 'app-root',
    template: '<router-outlet></router-outlet>'
})
export class ApplicationComponent {}
