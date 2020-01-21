import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Environnement, Instance, ParametreSeries, ParamsInstance, Application } from '../modele/Application';
import { MatDialog } from '@angular/material';
import { ModalAjoutInstance } from './modal-ajout-instance/modal-ajout-instance';
import { ApmService } from 'src/app/core/services/apm.service';
import { DataSharedService } from 'src/app/core/services/dataShared.service';

@Component({
  selector: 'application-instance',
  templateUrl: './instance.component.html',
  styleUrls: ['./instance.component.css']
})
export class InstanceComponent implements OnInit {

  @Output() instanceSelect = new EventEmitter<ParamsInstance>();
  @Input() environnements: Map<number, Environnement>;
  @Input() app: Application;
  
  instanceEnCours: Instance;
  constructor(private dataShared: DataSharedService, public dialog: MatDialog, private apmService: ApmService) { }

  ngOnInit() {

  }

  selectInstance(is: Instance, params: ParametreSeries, idServer: number) {
    this.instanceSelect.emit({ is, params, idServer });
    this.instanceEnCours = is
    console.log(is)
  }

  ajouterInstance() {
    this.openDialog();
  }


  getSizeMap(){
    return Object.keys(this.environnements).length
  }
  openDialog(): void {
    this.apmService.recupererServeur().subscribe(serveurs => {
      const dialogRef = this.dialog.open(ModalAjoutInstance, {
        width: '250px',
        data: { idApp: this.app.id, envs: this.environnements }
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result != undefined) {
          Object.keys(this.environnements).forEach((key) => {
            if (key === result.idServer) {
              this.environnements[key].instances.push(result.instance)
            }
          })
        }
      });
    })
  }
}
