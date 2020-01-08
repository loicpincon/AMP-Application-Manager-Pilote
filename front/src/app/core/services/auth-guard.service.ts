import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { ApmService } from './apm.service';
import { map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {

  constructor(public router: Router, private service: ApmService) { }

  variableAVerifier = false;

  canActivate() {
    console.log(sessionStorage.getItem('USER_TOKEN'))
    if (sessionStorage.getItem('USER_TOKEN') != undefined) {
      return this.service.recupererSession().pipe(
        map(data => {
          return true;
        }),
        catchError(() => {
          this.router.navigate(['/unsecure']);
          return of(false);
        }),
      );
    } else {
      this.router.navigate(['/secure']);
      return false;
    }


  }
}