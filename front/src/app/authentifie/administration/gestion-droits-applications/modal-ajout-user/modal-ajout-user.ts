import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ApmService } from 'src/app/core/services/apm.service';
import { User, Right, DroitApplicatifLevel, UserTypesApp } from '../../modele/model';
import { FormControl, FormGroup, FormBuilder } from '@angular/forms';

@Component({
    selector: 'administration-modal-ajout-user',
    templateUrl: './modal-ajout-user.html',
})
export class ModalAjoutUser implements OnInit {

    displayedColumns: string[] = ['selectionner', 'id', 'nom', 'prenom', 'niveau'];
    formulaire: FormGroup;
    users: User[];
    myControl = new FormControl();
    types: DroitApplicatifLevel[];
    selectedPerson: User;
    applicationId: string;

    constructor(
        private formBuilder: FormBuilder,
        public dialogRef: MatDialogRef<ModalAjoutUser>,
        private apmService: ApmService,
        @Inject(MAT_DIALOG_DATA) public data: UserTypesApp) { }

    ngOnInit(): void {
        this.types = this.data.types
        this.applicationId = this.data.applicationId
        this.formulaire = this.formBuilder.group({
            keywordSearch: null
        });

        this.formulaire.get('keywordSearch').valueChanges.subscribe(val => {
            this.onSearchChange(val)
        });

        this.apmService.recupererAllUser().subscribe(usersBd => {
            this.users = usersBd.filter(obj => {
                return !this.data.user.some(obj2 => {
                    return obj.token == obj2.token;
                });
            });
        })



    }

    onSearchChange(str) {
        this.apmService.recupererAllUserByLoginOrNomOrPrenom(str).subscribe(usersFind => {
            this.users = usersFind.filter(obj => {
                return !this.data.user.some(obj2 => {
                    return obj.token == obj2.token;
                });
            });
        })
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    ajouterUtilisateur() {
        if(this.selectedPerson && this.selectedPerson.rights){
            this.selectedPerson.rights.forEach(right => {
                if(right.applicationId === this.applicationId){
                    console.log(right)
                    console.log(this.selectedPerson.token)
                    this.apmService.ajouterDroitApplicatifs(right,this.selectedPerson.token).subscribe(rep=>{
                        console.log("Insertion ok")
                        this.onNoClick();
                    },
                    erreur =>{
                        console.log(erreur)
                    })
                }
            });
        }
    }

    setRightsByUser(role:string,userSelect: User){
        let alreadyRight:boolean = false;
        var tmp: Right = {applicationId:this.applicationId,date:new Date(),level:role}
        this.users.forEach(user => {
            if(user === userSelect){
                if(user.rights){
                    user.rights.forEach((right,index) => {
                        if(right.applicationId === this.applicationId){
                            user.rights[index]= tmp
                            alreadyRight = true;
                        }
                    });
                    if(!alreadyRight){
                        user.rights.push(tmp)
                    }
                }else{
                    user.rights = [tmp]
                }
            }
        });
    }

}
