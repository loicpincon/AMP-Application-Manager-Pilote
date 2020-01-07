import { Component, OnInit, Input} from '@angular/core';
import { Environnement } from '../modele/Application';

@Component({
  selector: 'app-instance',
  templateUrl: './instance.component.html',
  styleUrls: ['./instance.component.css']
})
export class InstanceComponent implements OnInit {

  @Input() environnements: Environnement[];

  constructor(){}

  ngOnInit(){
    console.log(this.environnements)
  }
}