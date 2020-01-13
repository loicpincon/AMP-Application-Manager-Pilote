import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApmService } from 'src/app/core/services/apm.service';
import { Application, Instance, ParamsInstance, ParametreSeries } from '../modele/Application';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/environments/environment';
import { DataSharedService } from 'src/app/core/services/dataShared.service';
@Component({
  selector: 'application-pilotage',
  templateUrl: './pilotage.component.html',
  styleUrls: ['./pilotage.component.css']
})
export class PilotageComponent implements OnInit {

  constructor(private route: ActivatedRoute, private dataShared: DataSharedService, private appService: ApmService) { }

  instanceSelect: Instance = null;
  idServer: number;
  instanceParams: ParametreSeries = null;
  application: Application = null;
  paramDefault: string;

  ngOnInit() {
    this.listening();
    this.route.params.subscribe(params => {
      console.log(params)
      if (params['idApp'] !== undefined) {
        this.appService.recupererApplication(params['idApp']).subscribe(data => {
          this.instanceSelect = null;
          this.application = data;
          console.log(data)
        },
          error => {
            console.log(error.error.message)
          })
      }

    });
  }

  instanceSelectEvent(res: ParamsInstance) {
    this.instanceSelect = res.is;
    console.log(res.is)
    this.instanceParams = res.params;
    this.idServer = res.idServer;
  }



  ws: any;

  listening() {
    //connect to stomp where stomp endpoint is exposed
    let ws = new SockJS(environment.urlServeurSocketBase);
    //let socket = new WebSocket("ws://localhost:8080/socket");
    this.ws = Stomp.over(ws);
    this.ws.debug = null
    let that = this;
    this.ws.connect({}, function (frame) {
      that.ws.subscribe("/errors", function (message) {
        alert("Error " + message.body);
      });
      that.ws.subscribe("/content/application", function (message) {
        console.log(message.body)
        var i: Instance = JSON.parse(message.body);
        that.instanceEvent(i);


      });
    }, function (error) {
      console.error(error)
    });
  }


  instanceEvent(ins: Instance) {
    console.log(ins.url)
    console.log(ins)
    if (this.instanceSelect != undefined && this.instanceSelect.id == ins.id) {
      console.log('c\'est mon instance')
      this.instanceSelect.etat = ins.etat;
      this.dataShared.changeInstance(ins)
    } else {
      var find = false;
      Object.keys(this.application.environnements).forEach((key) => {
        this.application.environnements[key].instances.forEach(inst => {
          console.log(inst.id)
          console.log(ins.id)
          if (inst.id == ins.id) {
            find = true;
            inst.etat = ins.etat;
          }
        })
      })
      if (!find) {
        alert(' a update une instance')
      }
    }
  }
}
