import { Component, OnInit, Inject, ViewChild, Optional } from '@angular/core';
import { Instance, Log, ParametreSeries } from '../../modele/Application';
import { ApmService } from 'src/app/core/services/apm.service';
import { DatePipe } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
@Component({
  selector: 'application-parametre-ajouter',
  templateUrl: './dialog-param-ajouter.component.html',
})
export class DialogAjouterSerieParamComponent implements OnInit {


  constructor(public dialogRef: MatDialogRef<DialogAjouterSerieParamComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: any, private _apmService: ApmService) { }

  ngOnInit() {
  }

  close(): void {
    this.dialogRef.close();
  }

  ajouter() {
    if (this.data.version) {
      this._apmService.ajouterSerieParametre(this.data.app, this.data.serveur, this.data.version).subscribe(serie => {
        this.data.newSerie = serie;
        this.dialogRef.close({ event: 'close', data: this.data });
      })
    }
  }


}