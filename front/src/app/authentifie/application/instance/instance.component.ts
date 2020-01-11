import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Environnement, Instance, ParametreSeries, ParamsInstance } from '../modele/Application';

@Component({
  selector: 'application-instance',
  templateUrl: './instance.component.html',
  styleUrls: ['./instance.component.css']
})
export class InstanceComponent implements OnInit {

  @Output() instanceSelect = new EventEmitter<ParamsInstance>();
  @Input() environnements: Environnement[];
  instanceEnCours: Instance;
  constructor() { }

  ngOnInit() {
  }

  selectInstance(is: Instance, params: ParametreSeries, idServer: number) {
    this.instanceSelect.emit({ is, params, idServer });
    this.instanceEnCours = is
  }
}
