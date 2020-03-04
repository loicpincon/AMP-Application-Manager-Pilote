import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { Datasource } from '../modele/Application';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-datasource',
  templateUrl: './datasource.component.html',
  styleUrls: ['./datasource.component.css']
})
export class DatasourceComponent implements OnInit {
  seasons: string[] = ['select', 'modif'];

  requeteA: string = 'SELECT User,Host from User';
  requeteR: string[];
  requeteRF: string;
  datasource: Datasource;
  newBase: string;
  envChoisi: any;
  idApp: string;
  type: string;

  constructor(private route: ActivatedRoute, private apmService: ApmService) { }

  ngOnInit(): void {
    this.route.queryParams
      .subscribe(params => {
        this.apmService.consulterDatasource('3298bcb545d2', params.idApp).subscribe(data => {
          this.datasource = data;
          this.idApp = params.idApp;
        })
      });

  }

  insererBase() {
    this.apmService.insererBaseDataSource(this.datasource.containerId, this.newBase, this.idApp).subscribe(data => {
      this.datasource = data;
    })
  }

  executerRequeteSQL() {
    this.type = "";
    console.log(this.type)
    if (this.requeteA.startsWith('SELECT') || this.requeteA.startsWith('select')) {
      this.type = "select";
    }
    this.apmService.executerRequeteSQL(this.datasource.containerId, this.requeteA, this.envChoisi.name, this.type).subscribe(result => {
      console.log(result);
      this.requeteR = result;
      this.format();
    }, error => {
      console.log(error.console.message);

    })
  }


  format() {
    this.requeteRF = "";
    this.requeteR.forEach(elem => {
      this.requeteRF += elem + "\n";
    })
  }


  exporterBase() {

  }
}
