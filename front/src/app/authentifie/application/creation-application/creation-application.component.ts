import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { WarApplication, BashApplication, Dockerfile } from '../modele/Application';
import { MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';

@Component({
  selector: 'application-creation-application',
  templateUrl: './creation-application.component.html',
  styleUrls: ['./creation-application.component.css']
})
export class CreationApplicationComponent implements OnInit {
  typeApplication: string[];
  formulaire: FormGroup;
  dockerFiles: Dockerfile[];
  constructor(private _router: Router,private apmService: ApmService, private formBuilder: FormBuilder,private _snackBar: MatSnackBar) { }
  ngOnInit() {
    this.apmService.recupererTypeApplications().subscribe(typesApp => {
      this.typeApplication = typesApp;
    })
    this.apmService.recupererDockerFile().subscribe(dks => {
      this.dockerFiles = dks;
    })
    this.formulaire = this.formBuilder.group({
      name: '',
      typeApp: '',
      dockerfiles: '',
      dockerfilesText: '',
      check: false,
      warApplication: new FormGroup({
        nomFichierProperties: new FormControl(''),
      }),
      bashApplication: new FormGroup({
        urlBatch: new FormControl('')
      }),
      nodeJsApplication: new FormGroup({
        versionNode: new FormControl('')
      })
    });
    this.formulaire.controls['dockerfilesText'].disable()
  }

  changeCheck(e){
    if(e){
      this.formulaire.controls['dockerfilesText'].enable()
    }else{
      this.formulaire.controls['dockerfilesText'].disable()
    }
  }
  onSubmit(customerData) {
    if (this.formulaire.controls['dockerfilesText'].enabled) {
      this.apmService.ajouterDockerFile(this.formulaire.value.dockerfiles.name, this.formulaire.value.dockerfilesText).subscribe(dockerFile => {
        var body = this.buildBody(customerData);
        body.dockerFileId = dockerFile.id;
        this.apmService.ajouterApplication(body).subscribe(app => {
          this._snackBar.open('Application ajoutée avec succès !','', {
            duration: 2000,
            panelClass: 'customSnackBar'
          });
          this._router.navigate(['/secure/application/pilotage',app.id]);
        }, error => {
          console.error(error);
        })
      })
    } else {
      var body = this.buildBody(customerData);
      body.dockerFileId = this.formulaire.value.dockerfiles.id;
      this.apmService.ajouterApplication(body).subscribe(app => {
        this._snackBar.open('Application ajoutée avec succès !','', {
          duration: 2000,
          panelClass: 'customSnackBar'
        });
        this._router.navigate(['/secure/application/pilotage', app.id]);
      }, error => {
        console.error(error);
      })
    }
  }


  buildBody(data) {
    console.log(data);
    var app;
    if (data.typeApp === "NODEJS") {
      app = null;
    } else if (data.typeApp === "WAR") {
      app = new WarApplication();
      app.nomFichierProperties = data.warApplication.nomFichierProperties
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
