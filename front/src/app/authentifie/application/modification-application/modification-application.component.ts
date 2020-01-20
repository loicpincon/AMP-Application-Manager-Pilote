import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { WarApplication, BashApplication, Dockerfile, Application, Serveur, NodeJsApplication } from '../modele/Application';
import { MatSnackBar } from '@angular/material';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'application-modification-application',
  templateUrl: './modification-application.component.html',
  styleUrls: ['./modification-application.component.css']
})
export class ModificationApplicationComponent implements OnInit {


  application: Application;

  allServer: Serveur[];

  idNewServer: number[] = new Array();

  formulaire: FormGroup;

  typeApplication: string[];

  dockerFiles: Dockerfile[];


  constructor(private route: ActivatedRoute, private _router: Router, private apmService: ApmService, private formBuilder: FormBuilder, private _snackBar: MatSnackBar) { }
  ngOnInit() {
    this.route.params.subscribe(params => {
      console.log(params)
      if (params['idApp'] !== undefined) {
        this.apmService.recupererApplication(params['idApp']).subscribe((app) => {
          console.log(app)
          this.application = app;
          this.formulaire = this.formBuilder.group({
            name: new FormControl(app.name),
            typeApp: new FormControl(app.type),
            dockerfilesText: new FormControl(app.dockerfile.file),
            basename: new FormControl(app.baseName),
          });


          if (this.application.type == "WAR") {
            this.initFormWarApplication(this.application as WarApplication);
          } else if (this.application.type == "BASH") {
            this.initFormBashApplication(this.application as BashApplication);
          } else if (this.application.type == "NODEJS") {
            this.initFormNodeJsApplication(this.application as NodeJsApplication);

          }


        })



        this.apmService.recupererServeur().subscribe(serveurs => {
          this.allServer = serveurs;
        })

        this.apmService.recupererTypeApplications().subscribe(typesApp => {
          this.typeApplication = typesApp;
        })
      }
    });
  }

  onSubmit(value) {

  }

  initFormWarApplication(war: WarApplication) {
    this.formulaire.addControl('warApplication', new FormGroup({
      nomFichierProperties: new FormControl(war.nomFichierProperties),
    }))

  }

  initFormBashApplication(war: BashApplication) {
    this.formulaire.addControl('bashApplication', new FormGroup({
      urlBatch: new FormControl(war.urlBatch),
    }))

  }

  initFormNodeJsApplication(war: NodeJsApplication) {
    this.formulaire.addControl('nodeJsApplication', new FormGroup({
      // nomFichierProperties: new FormControl(war.nomFichierProperties),
    }))

  }


  changeCheck(e) {
    if (e) {
      this.formulaire.controls['dockerfilesText'].enable()
    } else {
      this.formulaire.controls['dockerfilesText'].disable()
    }
  }


  /*activeDansApp(serveur: Serveur) {
    var find = false;
    if (this.application.environnements != null) {
      Object.keys(this.application.environnements).forEach((key) => {
        if (serveur.id.toString() == key) {
          find = true;
        }
      })
    }

    return find;
  }


  ajouterEnvironnement(serveur) {
    this.idNewServer.push(serveur.id)
    //this.application.environnements[serveur.id] = null;
  }

  retirerEnvironnement(serveur) {
    const index: number = this.idNewServer.indexOf(serveur.id);
    if (index !== -1) {
      this.idNewServer.splice(index, 1);
    }
    //delete this.application.environnements[serveur.id];
  }

  changePosition(serveur) {
    let body = document.getElementById('btn_' + serveur.id);

    if (this.idNewServer.indexOf(serveur.id) !== -1) {
      body.classList.remove("isPresent");
      body.classList.add("isNotPresent");
      this.retirerEnvironnement(serveur)
    } else {
      body.classList.add("isPresent");
      body.classList.remove("isNotPresent");
      this.ajouterEnvironnement(serveur)
    }
    console.log(this.idNewServer)
  }*/


}
