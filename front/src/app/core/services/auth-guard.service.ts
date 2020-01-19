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

  canLoad() {

    return this.service.recupererSession().pipe(
      map(data => {
        return true;
      }),
      catchError(() => {
        this.router.navigate(['/unsecure']);
        return of(false);
      }),
    );



  }
}