import { Component, OnInit, Input, ViewChild} from '@angular/core';
import { Livrable } from '../modele/Application';
import { MatTableDataSource, MatPaginator } from '@angular/material';

@Component({
  selector: 'application-livrable',
  templateUrl: './livrable.component.html',
  styleUrls: ['./livrable.component.css']
})
export class LivrableComponent implements OnInit {

  //displayedColumns: string[] = ['Version', 'Date'];
  displayedColumns: string[] = ['Version', 'Date'];
  dataSource = new MatTableDataSource<Livrable>();
  constructor(){}

  @Input() livrables: Livrable[];
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }
  ngOnChanges(){
    this.dataSource.data = this.livrables
  }
}