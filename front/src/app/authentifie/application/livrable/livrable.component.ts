import { Component, OnInit, ViewChild, Input} from '@angular/core';
import { MatPaginator, MatTableDataSource } from '@angular/material';
import { Livrable } from '../modele/Application';

@Component({
  selector: 'application-livrable',
  templateUrl: './livrable.component.html',
  styleUrls: ['./livrable.component.css']
})
export class LivrableComponent implements OnInit {

  displayedColumns: string[] = ['Version', 'Date'];
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  constructor(){}
  dataSource = new MatTableDataSource<Livrable>();
  @Input() livrables: Livrable[];

  ngOnInit() {
    this.dataSource.paginator = this.paginator; 
  }

  ngOnChanges(){
    this.dataSource.data = this.livrables;
    console.log(this.livrables)
  }

}
