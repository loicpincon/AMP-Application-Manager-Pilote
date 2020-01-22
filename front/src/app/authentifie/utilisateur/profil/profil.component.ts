import { Component, OnInit } from '@angular/core';
import { User } from '../../administration/modele/model';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ApmService } from 'src/app/core/services/apm.service';

@Component({
    selector: 'utilisateur-profil-root',
    templateUrl: './profil.component.html',
    styleUrls: ['./profil.component.css']
})
export class UtilisateurProfilComponent implements OnInit {

    user: User;
    formulaire: FormGroup;
    consult: boolean = true;
    urlPhoto: string;
    constructor(private apmservice: ApmService, private formBuilder: FormBuilder) { }

    ngOnInit(): void {

        this.urlPhoto = this.apmservice.getImageProfil(sessionStorage.getItem('USER_TOKEN'));

        this.apmservice.recupererUser(sessionStorage.getItem('USER_TOKEN')).subscribe(us => {
            this.user = us;
            this.formulaire = this.formBuilder.group({
                nom: new FormControl({ value: this.user.nom, disabled: this.consult }, Validators.required),
                prenom: new FormControl({ value: this.user.prenom, disabled: this.consult }, Validators.required)

            });
        })
    }

}