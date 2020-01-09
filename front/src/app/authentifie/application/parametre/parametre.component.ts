import {Component, OnInit, ViewChild, Input} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {ParametreSeries } from '../modele/Application';
@Component({
  selector: 'application-parametre',
  templateUrl: './parametre.component.html',
  styleUrls: ['./parametre.component.css']
})
export class ParametreComponent implements OnInit {
  displayedColumns: string[] = ['Version', 'Consulter','Modifier'];
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  constructor(){}
  dataSource = new MatTableDataSource<ParametreSeries>();
  @Input() params: ParametreSeries[];

  ngOnInit() {
    this.dataSource.paginator = this.paginator; 
  }

  ngOnChanges(){
    this.dataSource.data = this.params;
  }
}