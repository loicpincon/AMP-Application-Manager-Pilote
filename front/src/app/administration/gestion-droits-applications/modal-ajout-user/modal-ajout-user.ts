import { Component, Inject, Optional, OnInit } from '@angular/core';

import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ApmService } from 'src/app/core/services/apm.service';
import { User } from '../../modele/model';

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


    userBD: User[];

    constructor(
        public dialogRef: MatDialogRef<ModalAjoutUser>,
        @Optional() @Inject(MAT_DIALOG_DATA) public data: ModalAjoutUserData, private apmService: ApmService) { }

    ngOnInit(): void {
        this.apmService.recupererAllUser().subscribe(users => {
            this.userBD = users;
        })
    }



    onNoClick(): void {
        this.dialogRef.close();
    }

}
