import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { DroitApplicatifLevel, User, UserTypesApp, Right } from '../modele/model';
import { MatDialog } from '@angular/material';
import { ModalAjoutUser } from './modal-ajout-user/modal-ajout-user';
import { Application } from '../../application/modele/Application';

@Component({
  selector: 'administration-gestion-droits-applications',
  templateUrl: './gestion-droits-applications.component.html',
  styleUrls: ['./gestion-droits-applications.component.css']
})
export class GestionDroitsApplicationsComponent implements OnInit {

  displayedColumns: string[] = ['id', 'nom', 'prenom', 'niveau', 'supprimer'];
  users: User[];
  user: User;
  applications: Application[];
  applicationEnCours: string;
  types: DroitApplicatifLevel[];
  loader: boolean = false;

  constructor(private serviceApm: ApmService, public dialog: MatDialog) { }

  ngOnInit() {
    this.serviceApm.recupererUser(sessionStorage.getItem('USER_TOKEN')).subscribe(user => {
      this.user = user
      this.serviceApm.recupererAllApplicationsByUser().subscribe(applications => {
        this.applications = applications;
      })
    })
    this.serviceApm.recupererDroitApplicatifs().subscribe(droits => {
      this.types = droits;
    })
  }

  loadUserByApp(evt) {
    this.users = null;
    this.applicationEnCours = evt.value;
    this.serviceApm.recupererAllUserByApplications(evt.value).subscribe(users => {
      this.users = users;
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
  changeRights(user: User,role :string){
    var tmp: Right = {applicationId:this.applicationEnCours,date:new Date(),level:role}
    this.loader = true
    this.serviceApm.ajouterDroitApplicatifs(tmp,user.token).subscribe(data=>{
      this.loader = false
    },
    erreur=>{
      this.loader = false
      console.log(erreur)
    })
  }

  openModalAjoutUser() {
    const dialogRef = this.dialog.open(ModalAjoutUser, {
      width: '75%',
      data: {user:this.users,types: this.types,applicationId:this.applicationEnCours}
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result){
        this.users.push(result)
        this.users = this.users.slice()
      }
    });
  }
  supprimerUser(user: User){
    this.loader = true
    this.serviceApm.supprimerDroitApplicatifs(this.applicationEnCours,user.token).subscribe(data=>{
      this.users = this.users.filter(obj => obj !== user)
      this.loader = false
    },
    erreur =>{
      console.log(erreur)
      this.loader = false
    })
    


  }
}


