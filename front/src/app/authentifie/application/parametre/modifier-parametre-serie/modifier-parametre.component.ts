import { OnInit, Component } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
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

    displayedColumns: string[] = ['Cle', 'Valeur'];
    isEditOption;
    dataSource = new MatTableDataSource<SerieParametre>();
    serieEnCours: ParametreSeries;
    series: SerieParametre[] = new Array();


    idApp: string;
    idServ: string;
    versionParam: string;


    constructor(private router: Router, private route: ActivatedRoute, private apmService: ApmService) {

    }
    ngOnInit(): void {

        this.route.queryParams
            .subscribe(params => {
                this.isEditOption = params.edit;
                this.idServ = params.serveur;
                this.idApp = params.idApp;
                this.versionParam = params.versionParam;
                this.apmService.consulterSerieParametre(params.idApp, params.serveur, params.versionParam).subscribe(params => {
                    this.serieEnCours = params;
                    let serie = new SerieParametre();

                    if (params.parametres != undefined) {
                        Object.keys(params.parametres).forEach((key) => {
                            serie.cle = key;
                            serie.valeur = params.parametres[key];
                            this.series.push(serie);
                        })
                    }
                    this.dataSource.data = this.series;

                })
            });



    }

    modiferSerie() {
        console.log(this.series)
        let mapParam = new StringMap();
        this.series.forEach(elem => {
            console.log(elem)
            mapParam[elem.cle] = elem.valeur;
        })
        this.serieEnCours.parametres = mapParam;
        console.log(this.serieEnCours)
        this.apmService.modifierSerieParametre(this.idApp, this.idServ, this.serieEnCours).subscribe(retour => {

        })

    }

    ajouterParam() {
        let cleTemp = this.series[this.series.length - 1];
        if (cleTemp != undefined) {
            if (cleTemp.cle != undefined && cleTemp.cle != "") {
                console.log('ajout')
                this.series.push(new SerieParametre());
                this.dataSource.data = this.series;
            } else {
                console.log("complete dabord le premier rigolo")
            }
        } else {
            console.log('ajout')
            this.series.push(new SerieParametre());
            this.dataSource.data = this.series;
        }

    }
}