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

  requeteA: string;
  requeteR: string[];
  requeteRF: string;
  datasource: Datasource;
  newBase: string;
  envChoisi: any;
  idApp: string;
  type: string;
  typeChoisi: string;

  templatesDS: string[];

  datasources: Datasource[];

  tables: string[];

  constructor(private route: ActivatedRoute, private apmService: ApmService) { }

  ngOnInit(): void {

    this.apmService.recupererDataSourceTemplate().subscribe(templates => {
      this.templatesDS = templates;
    })
    console.log(this.route.queryParams)
    this.route.queryParams
      .subscribe(params => {
        this.idApp = params.idApp;
        this.apmService.recupererDataSourceByApp(params.idApp).subscribe(datasources => {
          this.datasources = datasources;
        })


      });

  }

  consulter(type) {
    this.datasource = type;
    console.log(type)



  }

  selectBase(newValue) {
    console.log(newValue)
    this.apmService.getTablesOfBasesMysql(this.datasource.containerId, newValue.name).subscribe(tables => {
      this.tables = tables;
    })
  }

  ajouter() {
    this.apmService.ajouterDataSourceTemplate(this.idApp, this.typeChoisi).subscribe(datasource => {
      console.log(datasource)
    })
  }

  insererBase() {
    this.apmService.insererBaseDataSource(this.datasource.containerId, this.newBase, this.idApp).subscribe(data => {
      this.datasource = data;
    })
  }

  buildReqSelect(table) {
    this.requeteA = "SELECT * from " + table;
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
