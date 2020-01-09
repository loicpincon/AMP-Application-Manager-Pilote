import {Component, OnInit, Input} from '@angular/core';
import {ParametreSeries } from '../modele/Application';
@Component({
  selector: 'application-parametre',
  templateUrl: './parametre.component.html',
  styleUrls: ['./parametre.component.css']
})
export class ParametreComponent implements OnInit {
  displayedColumns: string[] = ['Version', 'Consulter','Modifier'];
  constructor(){}
  @Input() params: ParametreSeries[];

  ngOnInit() {
  }
}