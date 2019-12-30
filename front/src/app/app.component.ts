import { Component } from '@angular/core';
import { WebSocketAPI } from './core/webSocket/WebSocket.socket';
import { Router } from '@angular/router';
import { ApmService } from './core/services/apm.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'amp';



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
