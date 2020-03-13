import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { Livrable, Instance, Application } from '../modele/Application';
import { SelectionModel } from '@angular/cdk/collections';
import { DataSharedService } from 'src/app/core/services/dataShared.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { environment } from 'src/environments/environment';
import { ApmService } from 'src/app/core/services/apm.service';

@Component({
  selector: 'application-livrable',
  templateUrl: './livrable.component.html',
  styleUrls: ['./livrable.component.css']
})
export class LivrableComponent implements OnInit {

  displayedColumns: string[] = ['Version', 'Date', 'DL'];
  dataSource = new MatTableDataSource<Livrable>();
  selection = new SelectionModel<Livrable>(false, []);
  serveur: string;
  constructor(private dataShared: DataSharedService, private apmService: ApmService) { }

  @Input() livrables: Livrable[];
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @Input() instance: Instance;
  @Input() app: Application;
  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.serveur = environment.urlServeurBase;
  }
  ngOnChanges() {
    let availableVersion = false;
    this.dataSource.data = this.livrables
    if (this.instance) {
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

  telechargerLivrable(ins: Instance) {
    return this.apmService.telechargerLivrable(this.app.id, ins.id);
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