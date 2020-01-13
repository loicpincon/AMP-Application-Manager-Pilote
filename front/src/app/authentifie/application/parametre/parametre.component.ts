import { Component, OnInit, Input, ViewChild, Output, EventEmitter } from '@angular/core';
import { ParametreSeries, Instance } from '../modele/Application';
import { MatTableDataSource, MatPaginator } from '@angular/material';
import { SelectionModel } from '@angular/cdk/collections';
import { DataSharedService } from 'src/app/core/services/dataShared.service';
@Component({
  selector: 'application-parametre',
  templateUrl: './parametre.component.html',
  styleUrls: ['./parametre.component.css']
})
export class ParametreComponent implements OnInit {
  displayedColumns: string[] = ['Version', 'Consulter', 'Modifier'];
  dataSource = new MatTableDataSource<ParametreSeries>();
  selection = new SelectionModel<ParametreSeries>(false, []);


  constructor(private dataShared: DataSharedService) { }
  @Input() params: ParametreSeries[];
  @Input() instance: Instance;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }
  ngOnChanges() {
    let availableVersion = false;
    this.dataSource.data = this.params
    this.params.forEach(p => {
      if (p.version == this.instance.versionParametresActuel) {
        this.selection.select(p)
        this.dataShared.changeParam(p)
        availableVersion = true;
      }
    })
    if (!availableVersion) {
      this.selection.clear()
      this.dataShared.changeParam(new ParametreSeries());
    }
  }
  selectionLigne(row: any) {
    this.selection.toggle(row)
    if (!this.selection.isSelected(row)) {
      this.dataShared.changeParam(new ParametreSeries());
    } else {
      this.dataShared.changeParam(row);
    }
  }


}