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

    constructor(private router: Router, private route: ActivatedRoute, private apmService: ApmService) {

    }
    ngOnInit(): void {

        this.route.queryParams
            .subscribe(params => {
                this.isEditOption = params.edit;

                this.apmService.consulterSerieParametre(params.idApp, params.serveur, params.versionParam).subscribe(params => {
                    this.serieEnCours = params;
                    Object.keys(params.parametres).forEach((key) => {
                        let serie = new SerieParametre();
                        serie.cle = key;
                        serie.valeur = params.parametres[key];
                        this.series.push(serie);
                        this.dataSource.data = this.series;
                    })
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
        this.apmService.modifierSerieParametre('fef806bb579120a1a5340c23361117c395633d28b74d9c7a8b79cb265c38d436', 1, this.serieEnCours).subscribe(retour => {

        })

    }
}