import { OnInit, Component, Inject, Optional } from '@angular/core';
import { ParametreSeries, Parametre } from '../../modele/Application';
import { ApmService } from 'src/app/core/services/apm.service';
import { map } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';



@Component({
    selector: 'application-modifier-parametre',
    templateUrl: './modifier-parametre.component.html',
    styleUrls: ['./modifier-parametre.component.css']
})
export class ModifierParametreComponent implements OnInit {

    displayedColumns: string[] = ['Cle', 'Valeur', 'Supprimer'];
    dataSource = new MatTableDataSource<Parametre>();
    serieEnCours: ParametreSeries = new ParametreSeries();
    series = new Array<Parametre>()
    valid: boolean = true;
    loader: boolean = false;

    constructor(public dialogRef: MatDialogRef<ModifierParametreComponent>,
        @Optional() @Inject(MAT_DIALOG_DATA) public data: any, private apmService: ApmService) {

    }
    ngOnInit(): void {
        this.apmService.consulterSerieParametre(this.data.idApp, this.data.serveur, this.data.versionParam).subscribe(params => {
            this.serieEnCours = params
            this.loader = false
            params.parametres.forEach(elem => {
                this.series.push(elem);
            })
            this.dataSource.data = this.series;
        })
    }
    paramChange(type: string, ancienParam: Parametre, nouvelleValeur: string) {
        this.series.forEach(serie => {
            if (serie == ancienParam) {
                if (type == 'Valeur') {
                    serie.valeur = nouvelleValeur
                } else {
                    serie.cle = nouvelleValeur
                }
            }
        })
    }

    setValidForm(type: string, valeur: string, ancienParam: Parametre) {
        this.valid = true
        if (valeur == "") {
            this.valid = false
        } else {
            this.series.forEach(param => {
                if (param != ancienParam) {
                    if (param.cle == "" || param.valeur == "") {
                        this.valid = false
                    }
                } else {
                    if (type == 'Valeur') {
                        if (param.cle == "") {
                            this.valid = false
                        }
                    } else {
                        if (param.valeur == "") {
                            this.valid = false
                        }
                    }
                }
            })
        }
    }

    ajouterParam() {
        let param = new Parametre()
        param.cle = ""
        param.valeur = ""
        this.series.push(param)
        this.dataSource.data = this.series;

        this.valid = false
    }
    modiferSerie() {
        if (this.valid) {

            this.serieEnCours.parametres = this.series;
            console.log(this.serieEnCours)
            this.apmService.modifierSerieParametre(this.data.idApp, this.data.serveur, this.serieEnCours).subscribe(retour => {
                this.dialogRef.close();
            })
        }
    }
    supprimerParam(param: Parametre) {
        let index = this.series.indexOf(param, 0);
        if (index > -1) {
            this.series.splice(index, 1);
            this.dataSource.data = this.series;
        }
    }

    backToApp() {
        this.dialogRef.close();
    }
}