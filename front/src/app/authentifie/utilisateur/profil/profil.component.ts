import { Component, OnInit } from '@angular/core';
import { UserProfile } from '../../administration/modele/model';
import { ApmService } from 'src/app/core/services/apm.service';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ApmService } from 'src/app/core/services/apm.service';
import { Ng2ImgMaxService } from 'ng2-img-max';


@Component({
    selector: 'utilisateur-profil-root',
    templateUrl: './profil.component.html',
    styleUrls: ['./profil.component.css']
})
export class UtilisateurProfilComponent implements OnInit {

    user: UserProfile;
    formulaire: FormGroup;
    modif: boolean = false;
    urlPhoto: string;
    constructor(private apmservice: ApmService, private ng2ImgMax: Ng2ImgMaxService
        , private formBuilder: FormBuilder) { }

    ngOnInit(): void {

        this.urlPhoto = this.apmservice.getImageProfil(sessionStorage.getItem('USER_TOKEN'));

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
    //Simulation du click pour ouvrir le fileInput
    ouvrirFileSearch() {
        document.getElementById("fileProfil").click();
    }


    changementImage(files: FileList) {
        if (files.length != 0) {
            //this.loader = true;
            let fileToUpload = files.item(0);
            //Utilisation du module Ng2ImgMaxModule pour réduire la taille de l'image
            this.ng2ImgMax.resizeImage(fileToUpload, 200, 200).subscribe(
                result => {

                    this.apmservice.uploadimage(sessionStorage.getItem('USER_TOKEN'), result).subscribe(data => {
                        //this.zone.run(() => { this.profilImage = this.applicationService.getImageProfil(val); this.loader = false });

                    })
                },
                error => {
                    console.log(error);
                }
            );
        }
    }


}
