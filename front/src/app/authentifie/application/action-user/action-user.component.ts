import { Component, OnInit, Input} from '@angular/core';
import { Instance } from '../modele/Application';
@Component({
  selector: 'application-action-user',
  templateUrl: './action-user.component.html',
  styleUrls: ['./action-user.component.css']
})
export class ActionUserComponent implements OnInit {
  displayedColumns: string[] = ['Version', 'Utilisateur','Action','Date','Status'];
  @Input() instance: Instance;

  constructor(){}


  ngOnInit() {
  }


}