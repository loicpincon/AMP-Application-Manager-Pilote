import { Component, OnInit, NgZone } from '@angular/core';
import { UserProfile, User } from '../../administration/modele/model';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Ng2ImgMaxService } from 'ng2-img-max';
import { ApmService } from 'src/app/core/services/apm.service';
import { DataSharedService } from 'src/app/core/services/dataShared.service';
import { MatSnackBar } from '@angular/material/snack-bar';


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
    constructor(
        private apmservice: ApmService,
        private ng2ImgMax: Ng2ImgMaxService,
        private formBuilder: FormBuilder,
        private zone: NgZone,
        private dataShared: DataSharedService,
        private _snackBar: MatSnackBar) { }

    ngOnInit(): void {
        this.urlPhoto = this.apmservice.getImageProfil(sessionStorage.getItem('USER_TOKEN'));
        this.apmservice.recupererUser(sessionStorage.getItem('USER_TOKEN')).subscribe(us => {
            this.user = us;
            console.log(us)
            this.formulaire = this.formBuilder.group({
                nom: new FormControl({ value: this.user.nom, disabled: !this.modif }, Validators.required),
                prenom: new FormControl({ value: this.user.prenom, disabled: !this.modif }, Validators.required),
                email: new FormControl({ value: this.user.email, disabled: !this.modif }, [Validators.required, Validators.email])
            });
        })
    }

    activerModifProfil() {
        this.formulaire.controls['nom'].enable()
        this.formulaire.controls['prenom'].enable()
        this.formulaire.controls['email'].enable()
        this.modif = true;
    }
    annulerProfil() {
        this.modif = false;
        this.formulaire.controls['nom'].disable()
        this.formulaire.controls['prenom'].disable()
        this.formulaire.controls['email'].disable()
    }
    modifierProfil(v) {
        let u = new User();
        u.nom = v.nom;
        u.email = v.email;
        u.prenom = v.prenom;
        u.token = this.user.token;
        console.log(u)

        this.apmservice.modifierUtilisateur(u).subscribe(us => {
            console.log(us);
            this.annulerProfil();
            this._snackBar.open('Profil modifié avec succès !', '', {
                duration: 2000,
                panelClass: 'customSnackBar'
            });
        }, error => {
            this._snackBar.open('Echec  : ' + error.error.message, '', {
                duration: 5000,
                panelClass: 'customSnackBar'
            });
        })

    }

    ouvrirFileSearch() {
        document.getElementById("fileProfil").click();
    }

    changementImage(files: any) {
        console.log("uplaod")
        if (files.length != 0) {
            let fileToUpload = files.item(0);
            this.ng2ImgMax.resizeImage(fileToUpload, 200, 200).subscribe(
                result => {
                    this.apmservice.uploadimage(sessionStorage.getItem('USER_TOKEN'), result).subscribe(data => {
                        this.zone.run(() => { console.log("passe dans la zone"); this.urlPhoto = ""; this.urlPhoto = this.apmservice.getImageProfil(this.user.token) + "?" + new Date(); });
                        this.dataShared.changeUrlPhoto(this.apmservice.getImageProfil(this.user.token) + "?" + new Date())

                    })
                },
                error => {
                    console.log(error);
                }
            );
        }
    }
}
