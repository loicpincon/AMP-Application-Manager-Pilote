import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { Livrable, Instance } from '../modele/Application';
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
  @Input() instance: Instance;
  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }
  ngOnChanges() {
    let availableVersion = false;
    this.dataSource.data = this.livrables
    if(this.instance){
      this.livrables.forEach(l => {
        if (l.nom == this.instance.versionApplicationActuel) {
          this.selection.select(l)
          this.dataShared.changeLivrable(l)
          availableVersion = true;
        }
      })
      if (!availableVersion) {
        this.selection.clear()
        this.dataShared.changeLivrable(new Livrable());
      }
    }
  }

  selectionLivrable(row: any) {
    this.selection.toggle(row)
    if (!this.selection.isSelected(row)) {
      this.dataShared.changeLivrable(new Livrable());
    } else {
      this.dataShared.changeLivrable(row);
    }
  }
}