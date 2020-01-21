import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { WarApplication, BashApplication, Dockerfile, Application, Serveur, NodeJsApplication, Environnement } from '../modele/Application';
import { MatSnackBar } from '@angular/material';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'application-modification-application',
  templateUrl: './modification-application.component.html',
  styleUrls: ['./modification-application.component.css']
})
export class ModificationApplicationComponent implements OnInit {


  application: Application;

  allServer: Serveur[] = new Array();

  idNewServer: number[] = new Array();

  formulaire: FormGroup;

  typeApplication: string[];

  dockerFiles: Dockerfile[];


  constructor(private route: ActivatedRoute, private _router: Router, private apmService: ApmService, private formBuilder: FormBuilder, private _snackBar: MatSnackBar) { }
  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params['idApp'] !== undefined) {
        this.apmService.recupererApplication(params['idApp']).subscribe((app) => {
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
          let tmp : string[] = new Array()
          

          this.apmService.recupererServeur().subscribe(serveurs => {
            serveurs.forEach(serv=>{
              serv.etat = false
              Object.keys(app.environnements).forEach((key) => {
                if(serv.id.toString() == key){
                  serv.etat = true
                }
                })
              this.allServer.push(serv)
            })
          })
        })
        

        this.apmService.recupererTypeApplications().subscribe(typesApp => {
          this.typeApplication = typesApp;
        })
      }
    });
  }

  switchEtatEnv(env,bool){
    this.allServer.forEach(serv=>{
      if(serv.id === env.id){
        serv.etat = bool
      }
    })
  }

  onSubmit(value) {
    if(this.formulaire.valid){
      let appTmp: any;
      switch(value.typeApp){
        case "WAR":{
          appTmp = new WarApplication();
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
      let evt = new Environnement()
      this.allServer.forEach(item => {
        if(item.etat){
          if(this.application.environnements[item.id]){
            evt[item.id] = this.application.environnements[item.id]
          }else{
            evt[item.id] = new Environnement()
          }
        }
      })
      appTmp.environnements = evt
      this.apmService.modifierApplication(appTmp).subscribe(data =>{
        this._snackBar.open('Application modifiée avec succès !', '', {
          duration: 2000,
          panelClass: 'customSnackBar'
        });
        this._router.navigate(['/secure/application/pilotage', appTmp.id]);
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

}
