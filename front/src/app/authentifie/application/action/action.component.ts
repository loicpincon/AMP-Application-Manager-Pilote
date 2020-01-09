import { Component, OnInit, Input } from '@angular/core';
import { Instance, Application } from '../modele/Application';
import { ApmService } from 'src/app/core/services/apm.service';
@Component({
  selector: 'application-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.css']
})
export class ActionComponent implements OnInit {

  @Input() instance: Instance;

  @Input() serveur: number;

  @Input() app: Application;

  constructor(private apmService: ApmService) { }
  selected = 'option1';
  ngOnInit() {
  }

  deployer() {
    this.apmService.deployerApplication(this.app.id, this.instance.id, this.serveur, '0.0.0').subscribe(res => {

    }, error => {

    })
  }

  stop() {
    this.apmService.manageApplication(this.instance.containerId, 'stop').subscribe(res => {

    }, error => {

    })
  }


  restart() {
    this.apmService.manageApplication(this.instance.containerId, 'reload').subscribe(res => {

    }, error => {

    })
  }


  delete() {
    this.apmService.manageApplication(this.instance.containerId, 'delete').subscribe(res => {

    }, error => {

    })
  }



}
