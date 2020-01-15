import { Component, OnInit } from '@angular/core';
import { Validators, FormGroup, FormBuilder, Form } from '@angular/forms';
import { ApmService } from '../../core/services/apm.service';
import { MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { LoginUser } from '../../core/modele/Authent';

@Component({
  selector: 'core-authent',
  animations: [
    trigger('loginRegister', [
      state('login', style({
      })),
      state('register', style({
        opacity: 0,
        display: 'none',
      })),
      transition('login => register', [
        animate('0.5s')
      ]),
      transition('register => login', [
        animate('0.5s')
      ]),
    ]),
  ],
  templateUrl: './authent.component.html',
  styleUrls: ['./authent.component.css']
})
export class AuthentComponent implements OnInit {

  constructor(
    private _fb: FormBuilder,
    private _apm: ApmService,
    private _snackBar: MatSnackBar,
    private _router: Router) { }

  registerForm: boolean = false;

  loader: boolean = false;
  formConnexion: FormGroup;
  formInscription: FormGroup;
  loginUser: LoginUser = new LoginUser();
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
      this._router.navigate(['/secure'])
    }
    this.createForm()
  }


  createForm() {
    this.formConnexion = this._fb.group({
      login: [this.loginUser.login, Validators.required],
      mdp: [this.loginUser.mdp, Validators.required]
    });
    this.formInscription = this._fb.group({
      login: '',
      mdp: '',
      confirmationMdp: '',
      nom: '',
      prenom: '',
    });
  }
  connexion(f: FormGroup) {
    if (f.valid) {
      this.loader = true;
      this._apm.connecterUser(f.value.login, f.value.mdp).subscribe((data: any) => {
        this.loader = false
        sessionStorage.setItem('USER_TOKEN', data.token)
        this._router.navigate(['/secure'])
      }, (err: any) => {
        this.loader = false
        console.error(err);
        this._snackBar.open('Mauvais identifiants', '', {
          duration: 1500
        });
      })
    }
  }
  inscription(f: FormGroup) {
    if (f.valid) {
      if (f.value.mdp == f.value.confirmationMdp) {
        console.log(f.value.login)
        console.log(f.value.mdp)
        console.log(f.value.confirmationMdp)
        console.log(f.value.nom)
        console.log(f.value.prenom)
        this._apm.inscrireUser(f.value.login, f.value.nom, f.value.prenom, f.value.mdp).subscribe(user => {
          this._apm.connecterUser(f.value.login, f.value.mdp).subscribe(session => {
            sessionStorage.setItem('USER_TOKEN', session.token)
            this._router.navigate(['/secure'])
          }, error => {
            console.error(error);
          })
        }, error => {
          alert(error.error.message)
        })
      }
    }
  }

  switchFormToRegister(bool: boolean) {
    this.registerForm = bool
  }

}