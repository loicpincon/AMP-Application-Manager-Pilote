import { Component, Inject, Optional, OnInit } from '@angular/core';

import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ApmService } from 'src/app/core/services/apm.service';
import { User } from '../../modele/model';
import { FormControl, FormGroup, FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { startWith, map } from 'rxjs/operators';

export interface ModalAjoutUserData {
    animal: string;
    name: string;
}


@Component({
    selector: 'modal-ajout-user',
    templateUrl: './modal-ajout-user.html',
})
export class ModalAjoutUser implements OnInit {

    displayedColumns: string[] = ['position', 'name', 'weight'];
    formulaire: FormGroup;


    userBD: User[];
    myControl = new FormControl();


    constructor(private formBuilder: FormBuilder,
        public dialogRef: MatDialogRef<ModalAjoutUser>,
        @Optional() @Inject(MAT_DIALOG_DATA) public data: ModalAjoutUserData, private apmService: ApmService) { }

    ngOnInit(): void {

        this.formulaire = this.formBuilder.group({
            keywordSearch: null
        });

        this.formulaire.get('keywordSearch').valueChanges.subscribe(val => {
            this.onSearchChange(val)
        });

        this.apmService.recupererAllUser().subscribe(users => {
            this.userBD = users;
        })
    }




    onSearchChange(str) {
        this.apmService.recupererAllUserByLoginOrNomOrPrenom(str).subscribe(usersFind => {
            this.userBD = usersFind;
        })
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

}
