import { Component, OnInit, Inject, Optional } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatTableDataSource} from '@angular/material';
import { SerieParametre } from '../modifier-parametre-serie/modifier-parametre.component';
import { ApmService } from 'src/app/core/services/apm.service';
@Component({
  selector: 'application-parametre-consulter',
  templateUrl: './dialog-param-consulter.component.html',
})
export class DialogConsulterSerieParamComponent implements OnInit {

  displayedColumns: string[] = ['Cle', 'Valeur'];
  dataSource = new MatTableDataSource<SerieParametre>();

  constructor(public dialogRef: MatDialogRef<DialogConsulterSerieParamComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: any,public apmService: ApmService) { }

  ngOnInit() {
    console.log(this.data)
    this.apmService.consulterSerieParametre(this.data.idApp, this.data.serveur, this.data.versionParam).subscribe(params => {
      console.log(params)
    })
  }

  close(): void {
    this.dialogRef.close();
  }



}