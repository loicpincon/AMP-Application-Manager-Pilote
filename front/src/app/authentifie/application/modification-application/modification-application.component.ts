import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { WarApplication, BashApplication, Dockerfile, Application, Serveur } from '../modele/Application';
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

  constructor(private route: ActivatedRoute, private _router: Router, private apmService: ApmService, private formBuilder: FormBuilder, private _snackBar: MatSnackBar) { }
  ngOnInit() {
    this.route.params.subscribe(params => {
      console.log(params)
      if (params['idApp'] !== undefined) {
        this.apmService.recupererApplication(params['idApp']).subscribe((app) => {
          this.application = app;
          console.log(app)
        })

        this.apmService.recupererServeur().subscribe(serveurs => {
          this.allServer = serveurs;
        })

      }
    });





  }
  activeDansApp(serveur: Serveur) {
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
  }


}
