import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { Environnement, Instance } from '../modele/Application';

@Component({
  selector: 'application-instance',
  templateUrl: './instance.component.html',
  styleUrls: ['./instance.component.css']
})
export class InstanceComponent implements OnInit {

  @Output() instanceSelect = new EventEmitter<Instance>();
  @Input() environnements: Environnement[];

  constructor(){}

  ngOnInit(){
  }

  selectInstance(is: Instance) {
    this.instanceSelect.emit(is);
  }
}
