import { Component, OnInit, Inject, Optional } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { Parametre } from '../../modele/Application';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
@Component({
  selector: 'application-parametre-consulter',
  templateUrl: './dialog-param-consulter.component.html',
  styleUrls: ['./dialog-param-consulter.component.css']

})
export class DialogConsulterSerieParamComponent implements OnInit {

  displayedColumns: string[] = ['Cle', 'Valeur'];
  dataSource = new MatTableDataSource<Parametre>();
  loader: boolean = false
  constructor(public dialogRef: MatDialogRef<DialogConsulterSerieParamComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: any, public apmService: ApmService) { }

  ngOnInit() {
    console.log(this.data)
    this.loader = true
    this.apmService.consulterSerieParametre(this.data.idApp, this.data.serveur, this.data.versionParam).subscribe(params => {
      this.loader = false

      this.dataSource.data = params.parametres;
    })
  }

  close(): void {
    this.dialogRef.close();
  }



}