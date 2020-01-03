import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { WarApplication, Application, BashApplication } from '../modele/Application';

@Component({
  selector: 'app-creation-application',
  templateUrl: './creation-application.component.html',
  styleUrls: ['./creation-application.component.css']
})
export class CreationApplicationComponent implements OnInit {

  typeApplication: string[];


  formulaire: FormGroup;

  constructor(private apmService: ApmService, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.apmService.recupererTypeApplications().subscribe(typesApp => {
      this.typeApplication = typesApp;
    })
    this.formulaire = this.formBuilder.group({
      name: '',
      typeApp: '',
      warApplication: new FormGroup({
        versionWar: new FormControl(''),
        urlRepoNexus: new FormControl('')
      }),
      bashApplication: new FormGroup({
        urlBatch: new FormControl('')
      }),
      nodeJsApplication: new FormGroup({
        versionNode: new FormControl('')
      })
    });


  }

  onSubmit(customerData) {
    var body = this.buildBody(customerData);
    this.apmService.ajouterApplication(body).subscribe(app => {
      alert('Application ajoute');
      this.formulaire.reset();
    }, error => {
      console.error(error);
    })

  }


  buildBody(data) {
    console.log(data);
    var app;
    if (data.typeApp === "NODEJS") {
      app = null;
    } else if (data.typeApp === "WAR") {
      app = new WarApplication();
      app.versionWar = data.warApplication.versionWar;
      app.urlRepoNexus = data.warApplication.urlRepoNexus;
    } else if (data.typeApp === "BASH") {
      app = new BashApplication();
      app.urlBatch = data.bashApplication.urlBatch;
    }
    app.name = data.name;
    app.type = data.typeApp;
    console.log(app);
    return app;
  }

}
