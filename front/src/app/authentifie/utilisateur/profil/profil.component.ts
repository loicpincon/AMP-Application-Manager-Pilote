import { Component, OnInit } from '@angular/core';
import { UserProfile } from '../../administration/modele/model';
import { ApmService } from 'src/app/core/services/apm.service';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';

@Component({
    selector: 'utilisateur-profil-root',
    templateUrl: './profil.component.html',
    styleUrls: ['./profil.component.css']
})
export class UtilisateurProfilComponent implements OnInit {

    user: UserProfile;
    formulaire: FormGroup;
    modif: boolean = false;
    constructor(private apmservice: ApmService, private formBuilder: FormBuilder) { }

    ngOnInit(): void {
        this.apmservice.recupererUser(sessionStorage.getItem('USER_TOKEN')).subscribe(us => {
            this.user = us;
            console.log(us)
            this.formulaire = this.formBuilder.group({
                nom: new FormControl({ value: this.user.nom, disabled: !this.modif }, Validators.required),
                prenom: new FormControl({ value: this.user.prenom, disabled: !this.modif }, Validators.required),
                email: new FormControl({ value: this.user.email, disabled: true }, Validators.required)

            });
        })
    }

    activerModifProfil(){
        this.formulaire.controls['nom'].enable()
        this.formulaire.controls['prenom'].enable()
        this.modif = true;
    }
    annulerProfil(){
        this.modif = false;
        this.formulaire.controls['nom'].disable()
        this.formulaire.controls['prenom'].disable()
    }
    modifierProfil(v){
        console.log(v)
    }
}