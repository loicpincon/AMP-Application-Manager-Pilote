import { Component } from '@angular/core';
import { WebSocketAPI } from './core/webSocket/WebSocket.socket';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'amp';

  constructor(){
new WebSocketAPI();
  }
}
