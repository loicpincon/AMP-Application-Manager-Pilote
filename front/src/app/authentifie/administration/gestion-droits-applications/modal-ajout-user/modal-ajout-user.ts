import { Component, Inject, Optional, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ApmService } from 'src/app/core/services/apm.service';
import { User } from '../../modele/model';
import { FormControl, FormGroup, FormBuilder } from '@angular/forms';

@Component({
    selector: 'administration-modal-ajout-user',
    templateUrl: './modal-ajout-user.html',
})
export class ModalAjoutUser implements OnInit {

    displayedColumns: string[] = ['position', 'name', 'weight'];
    formulaire: FormGroup;
    users: User[];
    myControl = new FormControl();

    

    constructor( 
        private formBuilder: FormBuilder, 
        public dialogRef: MatDialogRef<ModalAjoutUser>,
        private apmService: ApmService,
        @Inject(MAT_DIALOG_DATA) public data: User[]) { }

    ngOnInit(): void {
        console.log(this.data)
        this.formulaire = this.formBuilder.group({
            keywordSearch: null
        });

        this.formulaire.get('keywordSearch').valueChanges.subscribe(val => {
            this.onSearchChange(val)
        });

        this.apmService.recupererAllUser().subscribe(usersBd => {
            this.users = usersBd.filter(obj => {
                return !this.data.some(obj2 => {
                    return obj.token == obj2.token;
                });
            });
        })

        
        
    }

    onSearchChange(str) {
        this.apmService.recupererAllUserByLoginOrNomOrPrenom(str).subscribe(usersFind => {
            this.users = usersFind.filter(obj => {
                return !this.data.some(obj2 => {
                    return obj.token == obj2.token;
                });
            });
        })
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
    
}
