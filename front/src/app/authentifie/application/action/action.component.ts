import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Instance, Application, ParametreSeries, Livrable } from '../modele/Application';
import { ApmService } from 'src/app/core/services/apm.service';
import { DataSharedService } from 'src/app/core/services/dataShared.service';
import { MatDialog } from '@angular/material';
import { dialogLogsInstanceComponent } from './dialog-logs-instance/dialog-logs-instance.component';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
@Component({
  selector: 'application-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.css']
})
export class ActionComponent implements OnInit {

  @Input() instance: Instance;

  @Output() instanceEvent = new EventEmitter<Instance>();


  @Input() serveur: number;

  @Input() app: Application;

  paramSelectionne: ParametreSeries;
  versionApplicationSelectionne: Livrable;

  constructor(private apmService: ApmService, private dataShared: DataSharedService, public dialog: MatDialog) { }
  selected = 'option1';

  ngOnInit() {
    console.log(this.instance);
    this.dataShared.currentParam.subscribe(async (param) => {
      this.paramSelectionne = await param;
    })
    this.dataShared.currentLivrable.subscribe(async (param) => {
      this.versionApplicationSelectionne = await param;
    })
    this.dataShared.currentInstance.subscribe(async (param) => {
      if (param.id != undefined) {
        this.instance = await param;
      }
    })
  }

  deployer() {
    console.log(this.instance)
    if (this.paramSelectionne.version && this.versionApplicationSelectionne.nom) {
      this.apmService.deployerApplication(this.app.id, this.instance.id, this.serveur, this.versionApplicationSelectionne.nom).subscribe(res => {
        this.instance = res;
        this.instanceEvent.emit(this.instance);
      }, error => {

      })
    }
  }

  stop() {
    this.apmService.manageApplication(this.app.id, this.serveur, this.instance.id, 'stop').subscribe(res => {
      this.instance = res;
      this.instanceEvent.emit(this.instance);
    }, error => {

    })
  }


  restart() {
    this.apmService.manageApplication(this.app.id, this.serveur, this.instance.id, 'reload').subscribe(res => {
      this.instance = res;
      this.instanceEvent.emit(this.instance);
    }, error => {

    })
  }


  delete() {
    this.apmService.manageApplication(this.app.id, this.serveur, this.instance.id, 'delete').subscribe(res => {
      this.instance = res;
      this.instanceEvent.emit(this.instance);

    }, error => {

    })
  }

  consulterlesLogs() {
    this.dialog.open(dialogLogsInstanceComponent, {
      width: '95%',
      data: this.instance
    });
  }




}
