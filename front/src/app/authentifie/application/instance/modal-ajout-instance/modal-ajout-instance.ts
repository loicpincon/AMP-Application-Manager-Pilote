import { Component, Inject, OnInit } from '@angular/core';

import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ApmService } from 'src/app/core/services/apm.service';
import { Serveur } from '../../modele/Application';

@Component({
    selector: 'modal-ajout-instance',
    templateUrl: 'modal-ajout-instance.html',
})
export class ModalAjoutInstance implements OnInit {

    serveurs: Serveur[] = this.data.servers;
    envChoisi: string;
    constructor(
        public dialogRef: MatDialogRef<ModalAjoutInstance>,
        @Inject(MAT_DIALOG_DATA) public data: any, private apmService: ApmService) { }

    ngOnInit(): void {

    }


    onNoClick(): void {
        this.dialogRef.close();
    }
    onOkClick(): void {
        console.log(this.envChoisi);
        console.log(this.data.idApp);
        this.apmService.ajouterInstance(this.envChoisi, this.data.idApp).subscribe(instance => {
            this.dialogRef.close({ instance: instance, idServer: this.envChoisi });
        })
    }
}