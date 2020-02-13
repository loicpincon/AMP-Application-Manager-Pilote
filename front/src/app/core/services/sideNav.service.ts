import { Injectable } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';

@Injectable()
export class SidenavService {

  public sideNav: MatSidenav;
  constructor() { }
}