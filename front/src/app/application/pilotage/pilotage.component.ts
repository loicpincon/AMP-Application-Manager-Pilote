import { Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pilotage',
  templateUrl: './pilotage.component.html',
  styleUrls: ['./pilotage.component.scss']
})
export class PilotageComponent implements OnInit {

  constructor(private _router: Router){}

  ngOnInit(){

  }

  deconnexion(){
    localStorage.removeItem('token')
    this._router.navigate(['login'])
  }
}