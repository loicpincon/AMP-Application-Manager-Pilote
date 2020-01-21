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
            bashApplication: new FormGroup({
              urlBatch: new FormControl({value:'', disabled: true}),
            }),
            warApplication: new FormGroup({
              nomFichierProperties: new FormControl({value:'', disabled: true}),
            }),
            nodeJsApplication: new FormGroup({
              version: new FormControl({value:'', disabled: true}),
            })
          });
          this.selectType(this.application.type)
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
    if(this.formulaire.valid){
      let appTmp: any;
      switch(value.typeApp){
        case "WAR":{
          appTmp = new WarApplication();
          console.log(value)
          appTmp.nomFichierProperties = value.warApplication.nomFichierProperties
          break;
        }
        case "BASH":{
          appTmp = new BashApplication();
          appTmp.urlBatch = value.bashApplication.urlBatch
          break;
        }
        case "NODEJS":{
          appTmp = new NodeJsApplication();
          break;
        }
      }
      appTmp.baseName = value.basename
      appTmp.type = value.typeApp
      appTmp.name = value.name
      appTmp.dockerfile = this.application.dockerfile
      appTmp.dockerfile.file = value.dockerfilesText
      appTmp.id = this.application.id
      appTmp.environnements = this.application.environnements
      appTmp.livrables = this.application.livrables

      this.apmService.modifierApplication(appTmp).subscribe(data =>{
        this._snackBar.open('Application modifiée avec succès !', '', {
          duration: 2000,
          panelClass: 'customSnackBar'
        });
      }, error=>{
        console.log(error)
      })
    }else{
      this._snackBar.open('Le formulaire n\'est pas valide !', '', {
        duration: 2000,
        panelClass: 'errorSnackBar'
      });
    }
  }

  selectType(type: string){
    console.log(type)
    switch(type){
      case "WAR":{
        this.formulaire.controls['bashApplication'].disable()
        this.formulaire.controls['nodeJsApplication'].disable()
        this.formulaire.controls['warApplication'].enable()
        break;
      }
      case "BASH":{
        this.formulaire.controls['bashApplication'].enable()
        this.formulaire.controls['warApplication'].disable()
        this.formulaire.controls['nodeJsApplication'].disable()
        break;
      }
      case "NODEJS":{
        this.formulaire.controls['nodeJsApplication'].enable()
        this.formulaire.controls['bashApplication'].disable()
        this.formulaire.controls['warApplication'].disable()
        break;
      }
    }
  }

  initFormWarApplication(war: WarApplication) {
    this.formulaire.controls['warApplication'].setValue({nomFichierProperties:war.nomFichierProperties})
  }

  initFormBashApplication(war: BashApplication) {
    this.formulaire.controls['bashApplicationtion'].setValue({urlBatch:war.urlBatch})
  }

  initFormNodeJsApplication(war: NodeJsApplication) {

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
