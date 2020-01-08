import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
@Component({
  selector: 'application-parametre',
  templateUrl: './parametre.component.html',
  styleUrls: ['./parametre.component.css']
})
export class ParametreComponent implements OnInit {
  displayedColumns: string[] = ['Version', 'Consulter','Modifier'];
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  constructor(){}
  dataSource = new MatTableDataSource<Version>();
  ngOnInit() {
    this.dataSource.data = ELEMENT_DATA;
    this.dataSource.paginator = this.paginator; 
  }
}

export interface Version {
  position: number;
}

const ELEMENT_DATA: Version[] = [
  {position: 1},
  {position: 2},
  {position: 3},
  {position: 4},
  {position: 5},
  {position: 6},
  {position: 7},
  {position: 8},
  {position: 9},
  {position: 10},
  {position: 11},
  {position: 12},
  {position: 13},
  {position: 14},
  {position: 15},
  {position: 16},
  {position: 17},
  {position: 18},
  {position: 19},
  {position: 20},
  {position: 21},
  {position: 22},
  
];