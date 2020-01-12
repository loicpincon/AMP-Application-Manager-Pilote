import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Environnement, Instance, ParametreSeries, ParamsInstance, Application } from '../modele/Application';
import { MatDialog } from '@angular/material';
import { ModalAjoutInstance } from './modal-ajout-instance/modal-ajout-instance';
import { ApmService } from 'src/app/core/services/apm.service';

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
  constructor(public dialog: MatDialog, private apmService: ApmService) { }

  ngOnInit() {
  }

  selectInstance(is: Instance, params: ParametreSeries, idServer: number) {
    this.instanceSelect.emit({ is, params, idServer });
    this.instanceEnCours = is
  }

  ajouterInstance() {
    this.openDialog();
  }


  openDialog(): void {
    this.apmService.recupererServeur().subscribe(serveurs => {
      const dialogRef = this.dialog.open(ModalAjoutInstance, {
        width: '250px',
        data: { idApp: this.app.id, servers: serveurs }
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result != undefined) {
          console.log(typeof (this.environnements));
          Object.keys(this.environnements).forEach((key) => {
            console.log(key);
            if (key === result.idServer) {
              this.environnements[key].instances.push(result.instance)
            }
          })
        }
      });
    })
  }
}
