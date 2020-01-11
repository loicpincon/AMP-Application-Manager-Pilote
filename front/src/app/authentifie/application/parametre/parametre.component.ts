import {Component, OnInit, Input, ViewChild} from '@angular/core';
import {ParametreSeries } from '../modele/Application';
import { MatTableDataSource, MatPaginator } from '@angular/material';
@Component({
  selector: 'application-parametre',
  templateUrl: './parametre.component.html',
  styleUrls: ['./parametre.component.css']
})
export class ParametreComponent implements OnInit {
  displayedColumns: string[] = ['Version', 'Consulter','Modifier'];
  dataSource = new MatTableDataSource<ParametreSeries>();
  constructor(){}
  @Input() params: ParametreSeries[];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }
  ngOnChanges(){
    this.dataSource.data = this.params
  }
}