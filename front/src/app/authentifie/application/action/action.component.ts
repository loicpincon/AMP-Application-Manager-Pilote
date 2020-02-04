import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Instance, Application, ParametreSeries, Livrable } from '../modele/Application';
import { ApmService } from 'src/app/core/services/apm.service';
import { DataSharedService } from 'src/app/core/services/dataShared.service';
import { MatDialog } from '@angular/material';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Router } from '@angular/router';
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

  constructor(private router: Router, private apmService: ApmService, private dataShared: DataSharedService) { }
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
      this.apmService.deployerApplication(this.app.id, this.instance.id, this.serveur, this.versionApplicationSelectionne.nom, this.paramSelectionne.version).subscribe(res => {
        this.instance = res;
        this.instanceEvent.emit(this.instance);
      }, error => {

      })
    }
  }

  stop() {
    this.apmService.manageApplication(this.app.id, this.serveur, this.instance.id, 'S').subscribe(res => {
      this.instance = res;
      this.instanceEvent.emit(this.instance);
    }, error => {

    })
  }


  restart() {
    this.apmService.manageApplication(this.app.id, this.serveur, this.instance.id, 'R').subscribe(res => {
      this.instance = res;
      this.instanceEvent.emit(this.instance);
    }, error => {

    })
  }


  delete() {
    this.apmService.manageApplication(this.app.id, this.serveur, this.instance.id, 'DEL').subscribe(res => {
      this.instance = res;
      this.instanceEvent.emit(this.instance);

    }, error => {

    })
  }

  consulterlesLogs() {

    this.router.navigate(['/secure/consulterlog/recherche'], { queryParams: { idServ: this.serveur, idApp: this.app.id, instance: this.instance.id } });


  }

}
