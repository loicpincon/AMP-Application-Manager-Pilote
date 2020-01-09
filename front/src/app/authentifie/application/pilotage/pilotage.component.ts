import { Component, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApmService } from 'src/app/core/services/apm.service';
import { Application, Instance, ParamsInstance, ParametreSeries } from '../modele/Application';

@Component({
  selector: 'application-pilotage',
  templateUrl: './pilotage.component.html',
  styleUrls: ['./pilotage.component.css']
})
export class PilotageComponent implements OnInit {

  constructor(private route: ActivatedRoute,private appService : ApmService){}

  instanceSelect: Instance = null;
  paramsSelect: ParametreSeries = null;
  application: Application = null;
  ngOnInit(){
    this.route.queryParams.subscribe(params => {
      if (params.idApp !== undefined) {
        this.appService.recupererApplication(params.idApp).subscribe(data =>{
          this.instanceSelect = null;
          this.application = data;
        },
        error =>{
          console.log(error.error.message)
        })
      }

    });
  }

  instanceSelectEvent(res: ParamsInstance) {
    this.instanceSelect = res.is;
    this.paramsSelect = res.params;
  }
}
