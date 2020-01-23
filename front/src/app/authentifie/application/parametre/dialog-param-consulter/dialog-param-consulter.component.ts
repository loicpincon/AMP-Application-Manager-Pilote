import { Component, OnInit, Inject, Optional } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatTableDataSource} from '@angular/material';
import { SerieParametre } from '../modifier-parametre-serie/modifier-parametre.component';
import { ApmService } from 'src/app/core/services/apm.service';
@Component({
  selector: 'application-parametre-consulter',
  templateUrl: './dialog-param-consulter.component.html',
  styleUrls: ['./dialog-param-consulter.component.css']

})
export class DialogConsulterSerieParamComponent implements OnInit {

  displayedColumns: string[] = ['Cle', 'Valeur'];
  dataSource = new MatTableDataSource<SerieParametre>();
  loader : boolean = false
  constructor(public dialogRef: MatDialogRef<DialogConsulterSerieParamComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: any,public apmService: ApmService) { }

  ngOnInit() {
    console.log(this.data)
    this.loader = true
    this.apmService.consulterSerieParametre(this.data.idApp, this.data.serveur, this.data.versionParam).subscribe(params => {
      this.loader = false
      let series = new Array<SerieParametre>();
      if (params.parametres != undefined) {
          Object.keys(params.parametres).forEach((key) => {
            let serie = new SerieParametre()
              serie.cle = key;
              serie.valeur = params.parametres[key];
              series.push(serie);
          })
      }
      this.dataSource.data = series;
    })
  }

  close(): void {
    this.dialogRef.close();
  }



}