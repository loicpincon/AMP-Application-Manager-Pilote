import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { WarApplication, BashApplication, Dockerfile, AngularApplication, JarApplication, IonicApplication } from '../modele/Application';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'application-creation-application',
  templateUrl: './creation-application.component.html',
  styleUrls: ['./creation-application.component.css']
})
export class CreationApplicationComponent implements OnInit {
  typeApplication: string[];
  formulaire: FormGroup;
  dockerFiles: Dockerfile[];
  constructor(private _router: Router, private apmService: ApmService, private formBuilder: FormBuilder, private _snackBar: MatSnackBar) { }
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
      dockerfiles: null,
      dockerfilesText: '',
      check: false,
      basename: '',
      urlJenkins: '',
      warApplication: new FormGroup({
        nomFichierProperties: new FormControl(''),
      }),
      jarApplication: new FormGroup({
        nomFichierProperties: new FormControl(''),
      }),
      bashApplication: new FormGroup({
        urlBatch: new FormControl('')
      }),
      nodeJsApplication: new FormGroup({
        versionNode: new FormControl('')
      })
      ,
      ionicApplication: new FormGroup({
        repositoryUrl: new FormControl(''),
        repoUser: new FormControl(''),
        repoPass: new FormControl('')
      })
      ,
      angularApplication: new FormGroup({
        versionAngular: new FormControl(''),
        isBuilder: new FormControl(''),
        baseLocation: new FormControl(''),
        userProprietaire: new FormControl(''),
        nomRepository: new FormControl('')
      })
    });
    this.formulaire.controls['dockerfilesText'].disable()
  }

  changeCheck(e) {
    if (e) {
      this.formulaire.controls['dockerfilesText'].enable()
    } else {
      this.formulaire.controls['dockerfilesText'].disable()
    }
  }
  onSubmit(customerData) {
    var body = this.buildBody(customerData);
    this.apmService.ajouterApplication(body).subscribe(app => {
      this._snackBar.open('Application ajoutée avec succès !', '', {
        duration: 2000,
        panelClass: 'customSnackBar'
      });
      this._router.navigate(['/secure/application/pilotage', app.id]);
    }, error => {
      console.error(error);
    })

    console.log(body)
  }


  buildBody(data) {
    console.log(data);
    var app;

    if (data.typeApp === "NODEJS") {
      app = null;
    } else if (data.typeApp === "WAR") {
      app = new WarApplication();
      app.nomFichierProperties = data.warApplication.nomFichierProperties
    } else if (data.typeApp === "JAR") {
      app = new JarApplication();
      app.nomFichierProperties = data.warApplication.nomFichierProperties
    }
    else if (data.typeApp === "BASH") {
      app = new BashApplication();
      app.urlBatch = data.bashApplication.urlBatch;
    } else if (data.typeApp === "IONIC") {
      app = new IonicApplication();
      app.repositoryUrl = data.ionicApplication.repositoryUrl;
      app.repoUser = data.ionicApplication.repoUser;
      app.repoPass = data.ionicApplication.repoPass;
    } else if (data.typeApp === "ANGULAR") {
      app = new AngularApplication();
      app.versionAngular = data.angularApplication.versionAngular;
      app.isBuilder = data.angularApplication.isBuilder;
      app.baseLocation = data.angularApplication.baseLocation;
      app.userProprietaire = data.angularApplication.userProprietaire
      app.nomRepository = data.angularApplication.nomRepository
    }
    app.name = data.name;
    app.type = data.typeApp;
    app.urlJenkins = data.urlJenkins;
    app.dockerfile = data.dockerfiles;
    app.baseName = data.basename;
    console.log(app);
    return app;
  }

}
