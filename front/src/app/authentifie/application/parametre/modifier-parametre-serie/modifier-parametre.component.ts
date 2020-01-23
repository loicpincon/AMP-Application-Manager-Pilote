import { OnInit, Component, Inject, Optional } from '@angular/core';
import { MatTableDataSource, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ParametreSeries, StringMap } from '../../modele/Application';
import { ApmService } from 'src/app/core/services/apm.service';
import { map } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';

export class SerieParametre {
    cle: string;
    valeur: string;
}


@Component({
    selector: 'application-modifier-parametre',
    templateUrl: './modifier-parametre.component.html',
    styleUrls: ['./modifier-parametre.component.css']
})
export class ModifierParametreComponent implements OnInit {

    displayedColumns: string[] = ['Cle', 'Valeur', 'Supprimer'];
    dataSource = new MatTableDataSource<SerieParametre>();
    serieEnCours: ParametreSeries = new ParametreSeries();
    series =  new Array<SerieParametre>()
    valid: boolean = true;
    loader: boolean = false;

    constructor(public dialogRef: MatDialogRef<ModifierParametreComponent>,
        @Optional() @Inject(MAT_DIALOG_DATA) public data: any, private apmService: ApmService) {

    }
    ngOnInit(): void {
        this.apmService.consulterSerieParametre(this.data.idApp, this.data.serveur, this.data.versionParam).subscribe(params => {
            this.serieEnCours = params
            this.loader = false
            if (params.parametres != undefined) {
                Object.keys(params.parametres).forEach((key) => {
                    let serie = new SerieParametre()
                    serie.cle = key;
                    serie.valeur = params.parametres[key];
                    this.series.push(serie);
                })
            }
            this.dataSource.data = this.series;
        })
    }
    paramChange(type: string,ancienParam: SerieParametre, nouvelleValeur: string){
        this.series.forEach(serie =>{
            if(serie == ancienParam){
                if(type == 'Valeur'){
                    serie.valeur = nouvelleValeur
                }else{
                    serie.cle = nouvelleValeur
                }
            }
        })
    }

    setValidForm(type: string,valeur: string,ancienParam: SerieParametre){
        this.valid = true
        if(valeur == ""){
            this.valid = false
        }else{
            this.series.forEach(param =>{
                if(param != ancienParam){
                    if(param.cle == "" || param.valeur == ""){
                        this.valid = false
                    }
                }else{
                    if(type == 'Valeur'){
                        if(param.cle == ""){
                            this.valid = false
                        }
                    }else{
                        if(param.valeur == ""){
                            this.valid = false
                        }
                    }
                }
            })   
        }
    }

    ajouterParam(){
        let param = new SerieParametre()
        param.cle = ""
        param.valeur = ""
        this.series.push(param)
        this.dataSource.data = this.series;
        this.valid = false
    }
    modiferSerie(){
        if(this.valid){
            let mapParam = new StringMap();
            this.series.forEach(elem => {
                mapParam[elem.cle] = elem.valeur;
            })
            this.serieEnCours.parametres = mapParam;
            this.apmService.modifierSerieParametre(this.data.idApp, this.data.serveur, this.serieEnCours).subscribe(retour => {
            this.dialogRef.close();
            })
        }
    }
    supprimerParam(param: SerieParametre){
        let index = this.series.indexOf(param,0);
        if(index > -1){
            this.series.splice(index,1);
            this.dataSource.data = this.series;
        }
    }

    backToApp(){
        this.dialogRef.close();
    }
}