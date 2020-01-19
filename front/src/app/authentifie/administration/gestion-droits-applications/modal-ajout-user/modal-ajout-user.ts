import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatTableDataSource } from '@angular/material';
import { ApmService } from 'src/app/core/services/apm.service';
import { User, Right, DroitApplicatifLevel, UserTypesApp } from '../../modele/model';

@Component({
    selector: 'administration-modal-ajout-user',
    templateUrl: './modal-ajout-user.html',
})
export class ModalAjoutUser implements OnInit {

    displayedColumns: string[] = ['selectionner', 'id', 'nom', 'prenom', 'niveau'];
    dataSource = new MatTableDataSource<User>();
    types: DroitApplicatifLevel[];
    selectedPerson: User;
    applicationId: string;
    loader: boolean = false;
    canAdd: boolean = false;

    constructor(
        public dialogRef: MatDialogRef<ModalAjoutUser>,
        private apmService: ApmService,
        @Inject(MAT_DIALOG_DATA) public data: UserTypesApp) { }

    ngOnInit(): void {
        this.types = this.data.types
        this.applicationId = this.data.applicationId
        this.apmService.recupererAllUser().subscribe(usersBd => {
            this.dataSource.data = usersBd.filter(obj => {
                return !this.data.user.some(obj2 => {
                    return obj.token == obj2.token;
                });
            });
            this.dataSource.data.forEach(user =>{
                user.rightApp = "DEV"
                user.rights.forEach(right =>{
                    if(right.applicationId == this.applicationId){
                        user.rightApp = right.level;
                    }
                })
            })
        })



    }

    onSearchChange(filtre: string) {
        this.dataSource.filter = filtre.trim().toLowerCase();
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    onAddClick(right: Right): void{
        if(this.selectedPerson){
            this.selectedPerson.rights = [right];
            this.dialogRef.close(this.selectedPerson);
        }
    }
    
    radioChange(user: User){
        this.canAdd = true;
        if(user.rights){
            user.rights.forEach(right => {
                if(right.applicationId === this.applicationId){
                    this.canAdd = true;
                }
            })
        }
    }

    ajouterUtilisateur() {
        if(this.selectedPerson){
            var right: Right = {applicationId:this.applicationId,date:new Date(),level:this.selectedPerson.rightApp}
            this.apmService.ajouterDroitApplicatifs(right,this.selectedPerson.token).subscribe(rep=>{
                this.loader = false
                this.onAddClick(right);
            });
        }
    }

    setRightsByUser(role:string,userSelect: User){
        this.dataSource.data.forEach(user => {
            if(user === userSelect){
                user.rightApp = role
            }
        });
    }

}
