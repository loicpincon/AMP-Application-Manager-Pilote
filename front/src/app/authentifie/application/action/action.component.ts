import { Component, OnInit} from '@angular/core';
@Component({
  selector: 'application-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.css']
})
export class ActionComponent implements OnInit {

  constructor(){}
  selected = 'option1';
  ngOnInit(){
  }
  
}
