import { Component, Inject, Optional } from '@angular/core';

import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

export interface ModalAjoutUserData {
    animal: string;
    name: string;
}


@Component({
    selector: 'modal-ajout-user',
    templateUrl: './modal-ajout-user.html',
})
export class ModalAjoutUser {

    constructor(
        public dialogRef: MatDialogRef<ModalAjoutUser>,
        @Optional() @Inject(MAT_DIALOG_DATA) public data: ModalAjoutUserData) { }

    onNoClick(): void {
        this.dialogRef.close();
    }

}
