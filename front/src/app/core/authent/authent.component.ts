import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormGroup, FormBuilder, Form } from '@angular/forms';
import { ApmService } from '../services/apm.service';
import { MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';
@Component({
  selector: 'core-authent',
  templateUrl: './authent.component.html',
  styleUrls: ['./authent.component.css']
})
export class AuthentComponent implements OnInit {

  constructor(
    private _fb: FormBuilder,
    private _apm: ApmService,
    private _snackBar: MatSnackBar,
    private _router: Router) { }

  loader: boolean = false;
  formConnexion: FormGroup;
  itemCo: any = { 'login': null, 'mdp': null }
  validationMessageMdp: string = "Le mot de passe est requis."
  validation_messages = {
    'login': [
      { type: 'required', message: 'Le login est requis.' }
    ],
    'mdp': [
      { type: 'required', message: 'Le mot de passe est requis.' },
    ]
  };

  ngOnInit() {
    if (sessionStorage.getItem('USER_TOKEN')) {
      this._router.navigate(['application'])
    }
    this.createForm()
  }


  createForm() {
    this.formConnexion = this._fb.group({
      login: [this.itemCo.login, Validators.required],
      mdp: [this.itemCo.mdp, Validators.required]
    });
  }
  connexion(f: FormGroup) {
    if (f.valid) {
      this.loader = true;
      this._apm.connecterUser(f.value.login, f.value.mdp).subscribe((data: any) => {
        this.loader = false
        sessionStorage.setItem('USER_TOKEN', data.token)
        window.location.href = '/'
      }, (err: any) => {
        this.loader = false
        console.error(err);
        this._snackBar.open('Mauvais identifiants', '', {
          duration: 1500
        });
      })
    }

  }

}