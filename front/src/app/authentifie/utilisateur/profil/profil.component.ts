import { Component, OnInit } from '@angular/core';
import { User } from '../../administration/modele/model';
import { ApmService } from 'src/app/core/services/apm.service';

@Component({
    selector: 'utilisateur-profil-root',
    templateUrl: './profil.component.html',
    styleUrls: ['./profil.component.css']
})
export class UtilisateurProfilComponent implements OnInit {

    user: User;

    constructor(private apmservice: ApmService) { }

    ngOnInit(): void {
        this.apmservice.recupererUser(sessionStorage.getItem('USER_TOKEN')).subscribe(us => {
            this.user = us;
        })
    }

}