import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {

  constructor(public router: Router) { }

  variableAVerifier= false;

  canActivate(): boolean {
    if (!localStorage.getItem('token')) {
      this.router.navigate(['login']);
      return false;
    }
    return true;
  }
}