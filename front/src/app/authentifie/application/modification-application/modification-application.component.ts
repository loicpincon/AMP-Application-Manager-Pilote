import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { WarApplication, BashApplication, Dockerfile, Application, Serveur, NodeJsApplication, Environnement, AngularApplication, JarApplication, IonicApplication } from '../modele/Application';
import { MatSnackBar } from '@angular/material/snack-bar';
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
            urlJenkins: new FormControl(app.urlJenkins),
            bashApplication: new FormGroup({
              urlBatch: new FormControl({ value: '', disabled: true }),
            }),
            warApplication: new FormGroup({
              nomFichierProperties: new FormControl({ value: '', disabled: true }),
            }),
            jarApplication: new FormGroup({
              nomFichierProperties: new FormControl({ value: '', disabled: true }),
            }),
            nodeJsApplication: new FormGroup({
              version: new FormControl({ value: '', disabled: true }),
            }),
            ionicApplication: new FormGroup({
              repositoryUrl: new FormControl(''),
              repoUser: new FormControl(''),
              repoPass: new FormControl('')
            }),
            angularApplication: new FormGroup({
              versionAngular: new FormControl({ value: '', disabled: true }),
              isBuilder: new FormControl({ value: '', disabled: true }),
              baseLocation: new FormControl({ value: '', disabled: true }),
              userProprietaire: new FormControl({ value: '', disabled: true }),
              nomRepository: new FormControl({ value: '', disabled: true })
            })
          });
          this.selectType(this.application.type)
          if (this.application.type == "WAR") {
            this.initFormWarApplication(this.application as WarApplication);
          } else if (this.application.type == "JAR") {
            this.initFormJarApplication(this.application as JarApplication);
          } else if (this.application.type == "BASH") {
            this.initFormBashApplication(this.application as BashApplication);
          } else if (this.application.type == "NODEJS") {
            this.initFormNodeJsApplication(this.application as NodeJsApplication);
          } else if (this.application.type == "ANGULAR") {
            this.initFormAngularApplication(this.application as AngularApplication);
          } else if (this.application.type == "IONIC") {
            this.initFormIonicApplication(this.application as IonicApplication);
          }
          let tmp: string[] = new Array()


          this.apmService.recupererServeur().subscribe(serveurs => {
            serveurs.forEach(serv => {
              serv.etat = false
              Object.keys(app.environnements).forEach((key) => {
                if (serv.id.toString() == key) {
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

  switchEtatEnv(env, bool) {
    this.allServer.forEach(serv => {
      if (serv.id === env.id) {
        serv.etat = bool
      }
    })
  }

  onSubmit(value) {
    if (this.formulaire.valid) {
      let appTmp: any;
      switch (value.typeApp) {
        case "WAR": {
          appTmp = new WarApplication();
          appTmp.nomFichierProperties = value.warApplication.nomFichierProperties
          break;
        }
        case "JAR": {
          appTmp = new JarApplication();
          appTmp.nomFichierProperties = value.jarApplication.nomFichierProperties
          break;
        }
        case "IONIC": {
          appTmp = new IonicApplication();
          appTmp.repositoryUrl = value.ionicApplication.repositoryUrl;
          appTmp.repoUser = value.ionicApplication.repoUser;
          appTmp.repoPass = value.ionicApplication.repoPass;

          break;
        }
        case "BASH": {
          appTmp = new BashApplication();
          appTmp.urlBatch = value.bashApplication.urlBatch
          break;
        }
        case "NODEJS": {
          appTmp = new NodeJsApplication();
          break;
        }
        case "ANGULAR": {
          appTmp = new AngularApplication();
          appTmp.versionAngular = value.angularApplication.versionAngular
          appTmp.isBuilder = value.angularApplication.isBuilder
          appTmp.baseLocation = value.angularApplication.baseLocation
          appTmp.userProprietaire = value.angularApplication.userProprietaire
          appTmp.nomRepository = value.angularApplication.nomRepository

          break;
        }
      }
      console.log(appTmp)
      appTmp.baseName = value.basename
      appTmp.urlJenkins = value.urlJenkins
      appTmp.type = value.typeApp
      appTmp.name = value.name
      appTmp.dockerfile = this.application.dockerfile
      appTmp.dockerfile.file = value.dockerfilesText
      appTmp.id = this.application.id
      appTmp.environnements = this.application.environnements
      appTmp.livrables = this.application.livrables
      let evt = new Environnement()
      this.allServer.forEach(item => {
        if (item.etat) {
          if (this.application.environnements[item.id]) {
            evt[item.id] = this.application.environnements[item.id]
          } else {
            evt[item.id] = new Environnement()
          }
        }
      })
      appTmp.environnements = evt
      this.apmService.modifierApplication(appTmp).subscribe(data => {
        this._snackBar.open('Application modifiée avec succès !', '', {
          duration: 2000,
          panelClass: 'customSnackBar'
        });
        this._router.navigate(['/secure/application/pilotage', appTmp.id]);
      }, error => {
        console.log(error)
      })
    } else {
      this._snackBar.open('Le formulaire n\'est pas valide !', '', {
        duration: 2000,
        panelClass: 'errorSnackBar'
      });
    }

  }

  selectType(type: string) {
    switch (type) {
      case "WAR": {
        this.formulaire.controls['bashApplication'].disable()
        this.formulaire.controls['nodeJsApplication'].disable()
        this.formulaire.controls['warApplication'].enable()
        this.formulaire.controls['angularApplication'].disable()
        this.formulaire.controls['jarApplication'].disable()
        this.formulaire.controls['ionicApplication'].disable()
        break;
      }
      case "JAR": {
        this.formulaire.controls['bashApplication'].disable()
        this.formulaire.controls['nodeJsApplication'].disable()
        this.formulaire.controls['jarApplication'].enable()
        this.formulaire.controls['warApplication'].disable()
        this.formulaire.controls['angularApplication'].disable()
        this.formulaire.controls['ionicApplication'].disable()
        break;
      }
      case "BASH": {
        this.formulaire.controls['jarApplication'].disable()
        this.formulaire.controls['bashApplication'].enable()
        this.formulaire.controls['warApplication'].disable()
        this.formulaire.controls['nodeJsApplication'].disable()
        this.formulaire.controls['angularApplication'].disable()
        this.formulaire.controls['ionicApplication'].disable()
        break;
      }
      case "NODEJS": {
        this.formulaire.controls['jarApplication'].disable()
        this.formulaire.controls['nodeJsApplication'].enable()
        this.formulaire.controls['bashApplication'].disable()
        this.formulaire.controls['warApplication'].disable()
        this.formulaire.controls['angularApplication'].disable()
        this.formulaire.controls['ionicApplication'].disable()
        break;
      }
      case "ANGULAR": {
        this.formulaire.controls['jarApplication'].disable()
        this.formulaire.controls['angularApplication'].enable()
        this.formulaire.controls['nodeJsApplication'].disable()
        this.formulaire.controls['bashApplication'].disable()
        this.formulaire.controls['warApplication'].disable()
        this.formulaire.controls['ionicApplication'].disable()
        break;
      } case "IONIC": {
        this.formulaire.controls['jarApplication'].disable()
        this.formulaire.controls['angularApplication'].disable()
        this.formulaire.controls['nodeJsApplication'].disable()
        this.formulaire.controls['bashApplication'].disable()
        this.formulaire.controls['warApplication'].disable()
        this.formulaire.controls['ionicApplication'].enable()
        break;
      }
    }
  }

  initFormWarApplication(war: WarApplication) {
    this.formulaire.controls['warApplication'].setValue({ nomFichierProperties: war.nomFichierProperties })
  }

  initFormJarApplication(war: JarApplication) {
    this.formulaire.controls['jarApplication'].setValue({ nomFichierProperties: war.nomFichierProperties })
  }

  initFormBashApplication(war: BashApplication) {
    this.formulaire.controls['bashApplicationtion'].setValue({ urlBatch: war.urlBatch })
  }

  initFormAngularApplication(war: AngularApplication) {
    this.formulaire.controls['angularApplication'].patchValue({ versionAngular: war.versionAngular })
    this.formulaire.controls['angularApplication'].patchValue({ isBuilder: war.isBuilder })
    this.formulaire.controls['angularApplication'].patchValue({ baseLocation: war.baseLocation })
    this.formulaire.controls['angularApplication'].patchValue({ userProprietaire: war.userProprietaire })
    this.formulaire.controls['angularApplication'].patchValue({ nomRepository: war.nomRepository })

  }

  initFormIonicApplication(war: IonicApplication) {
    this.formulaire.controls['ionicApplication'].patchValue({ repoUser: war.repoUser })
    this.formulaire.controls['ionicApplication'].patchValue({ repositoryUrl: war.repositoryUrl })
    this.formulaire.controls['ionicApplication'].patchValue({ repoPass: war.repoPass })


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
