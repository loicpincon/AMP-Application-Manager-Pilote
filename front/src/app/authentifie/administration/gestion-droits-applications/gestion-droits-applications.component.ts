import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { DroitApplicatifLevel, User, UserTypesApp, Right } from '../modele/model';
import { ModalAjoutUser } from './modal-ajout-user/modal-ajout-user';
import { Application } from '../../application/modele/Application';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'administration-gestion-droits-applications',
  templateUrl: './gestion-droits-applications.component.html',
  styleUrls: ['./gestion-droits-applications.component.css']
})
export class GestionDroitsApplicationsComponent implements OnInit {

  displayedColumns: string[] = ['id', 'nom', 'prenom', 'niveau'];
  users: User[];
  user: User;
  applications: Application[];
  applicationEnCours: string;
  types: DroitApplicatifLevel[];
  loader: boolean = false;
  currentUserToReloadRight: User;
  adminRight: boolean = false;

  constructor(private route: ActivatedRoute, private serviceApm: ApmService, public dialog: MatDialog, private _snackBar: MatSnackBar) { }

  ngOnInit() {
    this.route.queryParams
      .subscribe(params => {

        this.serviceApm.recupererUser(sessionStorage.getItem('USER_TOKEN')).subscribe(user => {
          this.user = user
          if (params.idApp !== undefined) {
            this.serviceApm.recupererApplication(params.idApp).subscribe(app => {
              console.log(app.id)
              this.loadUserByApp(app);
            })
          } else {
            this.serviceApm.recupererAllApplicationsByUser().subscribe(applications => {
              this.applications = applications;
            })
          }
        });


      });

    this.serviceApm.recupererDroitApplicatifs().subscribe(droits => {
      this.types = droits;
    })
  }

  loadUserByApp(evt: Application) {
    if (this.displayedColumns.includes('supprimer')) {
      this.displayedColumns.pop()
    }
    this.users = null;
    this.applicationEnCours = evt.id;
    console.log(this.applicationEnCours)
    this.serviceApm.recupererAllUserByApplications(evt.id).subscribe(users => {
      this.users = users;
      this.adminRight = false;
      users.forEach(user => {
        if (user.token == this.user.token) {
          user.rights.forEach(right => {
            if (right.level == "PROP") {
              this.adminRight = true;
              this.displayedColumns.push('supprimer')
            }
          })
        }
      })
    })
  }


  getLevelRight(user: User) {
    var level;
    user.rights.forEach(function (element) {
      if (element.applicationId === this.applicationEnCours) {
        level = element.level;
      }
    });
  }
  changeRights(user: User, role: string) {
    var tmp: Right = { applicationId: this.applicationEnCours, date: new Date(), level: role }
    this.currentUserToReloadRight = user;
    this.loader = true
    this.serviceApm.ajouterDroitApplicatifs(tmp, user.token).subscribe(data => {
      this.loader = false
      this._snackBar.open('Les droits de l\'utilisateur ' + user.prenom + ' ' + user.nom + ' ont été mis à jour', '', {
        duration: 2000,
        panelClass: 'customSnackBar'
      });
    },
      erreur => {
        this.loader = false
        console.log(erreur)
      })
  }

  openModalAjoutUser() {
    const dialogRef = this.dialog.open(ModalAjoutUser, {
      width: '75%',
      data: { user: this.users, types: this.types, applicationId: this.applicationEnCours }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.users.push(result)
        this.users = this.users.slice()
      }
    });
  }
  supprimerUser(user: User) {
    this.loader = true
    this.serviceApm.supprimerDroitApplicatifs(this.applicationEnCours, user.token).subscribe(data => {
      this.users = this.users.filter(obj => obj !== user)
      this.loader = false
    },
      erreur => {
        console.log(erreur)
        this.loader = false
      })



  }
}


