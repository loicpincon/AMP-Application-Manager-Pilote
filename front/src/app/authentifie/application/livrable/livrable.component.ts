import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { Livrable } from '../modele/Application';
import { MatTableDataSource, MatPaginator } from '@angular/material';
import { SelectionModel } from '@angular/cdk/collections';
import { DataSharedService } from 'src/app/core/services/dataShared.service';

@Component({
  selector: 'application-livrable',
  templateUrl: './livrable.component.html',
  styleUrls: ['./livrable.component.css']
})
export class LivrableComponent implements OnInit {

  displayedColumns: string[] = ['Version', 'Date'];
  dataSource = new MatTableDataSource<Livrable>();
  selection = new SelectionModel<Livrable>(false, []);

  constructor(private dataShared: DataSharedService) { }

  @Input() livrables: Livrable[];
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }
  ngOnChanges() {
    this.dataSource.data = this.livrables
  }

  selectionLivrable(row: any) {
    console.log(row)
    this.selection.toggle(row)
    if (!this.selection.isSelected(row)) {
      this.dataShared.changeLivrable(new Livrable());
    } else {
      this.dataShared.changeLivrable(row);
    }
  }
}