import { Component, OnInit, Input} from '@angular/core';
import { Environnement } from '../modele/Application';

@Component({
  selector: 'application-instance',
  templateUrl: './instance.component.html',
  styleUrls: ['./instance.component.css']
})
export class InstanceComponent implements OnInit {

  @Input() environnements: Environnement[];

  constructor(){}

  ngOnInit(){
  }
}
