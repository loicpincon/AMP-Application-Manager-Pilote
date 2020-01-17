import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatTableDataSource, MatPaginator, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Log, Instance, Application, Serveur } from '../../application/modele/Application';
import { ApmService } from 'src/app/core/services/apm.service';
import { DatePipe } from '@angular/common';
import { EnvLog, AppLog, InstanceLog } from '../modele/Model';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
    selector: 'consulter-log-recherche-root',
    templateUrl: './recherche.component.html',
    styleUrls: ['./recherche.component.css']

})
export class RechercheLogComponent implements OnInit {
    displayedColumns: string[] = ['Version', 'Date'];
    dataSource = new MatTableDataSource<Log>();
    loader: boolean = false;
    dateJourDeb: string = this.datePipe.transform(new Date(), 'yyyy-MM-dd HH:mm:ss')
    dateJourFin: string = this.datePipe.transform(new Date(), 'yyyy-MM-dd HH:mm:ss')

    @ViewChild(MatPaginator, { static: true })
    paginator: MatPaginator;

    idContainerTest = "fef806bb579120a1a5340c23361117c395633d28b74d9c7a8b79cb265c38d436-e543fe83d3b244c8418a63c999c6fd264272121d8200fcfb7f02d61234bb598d";

    env: EnvLog[];

    app: AppLog[];

    instance: InstanceLog[];
    insSelect: InstanceLog;



    serveurInSelect: EnvLog;
    applicationInSelect: AppLog;
    instanceInSelect: InstanceLog;



    constructor(private route: ActivatedRoute,
        private _apmService: ApmService, private datePipe: DatePipe) { }


    ngOnInit() {



        this.route.queryParams
            .subscribe(params => {
                console.log(params.length);
                this._apmService.recupererInfoLogFormulaire().subscribe(infos => {
                    console.log(infos)
                    this.env = infos.envs;
                    if (params.idServ !== undefined) {
                        this.env.forEach(env => {
                            if (env.idEnv == params.idServ) {
                                this.serveurInSelect = env;
                                this.envChange(this.serveurInSelect);

                                env.apps.forEach(app => {
                                    if (app.id == params.idApp) {
                                        this.applicationInSelect = app;
                                        this.appChange(this.applicationInSelect);
                                        console.log(app.instances)
                                        app.instances.forEach(insta => {
                                            if (params.instance == insta.id) {
                                                this.instanceInSelect = insta;
                                                return;
                                            }
                                        })
                                        return;
                                    }
                                })
                                return;
                            }
                        })
                    }
                })
            });



    }



    populateEnvironnement() {

    }

    envChange(valeur) {
        console.log(valeur);
        this.app = valeur.apps;
    }

    appChange(valeur) {
        console.log(valeur);
        this.instance = valeur.instances;
    }

    instanceChange(valeur) {
        this.dataSource.paginator = this.paginator;
        this.insSelect = valeur;
        this.loader = true;
        this._apmService.recupererLogsInstance(valeur.id).subscribe(logs => {
            this.dataSource.data = logs
            console.log(logs)
            this.loader = false
        },
            erreur => {
                this.dataSource.data = new Array();

                this.loader = false;
                console.log(erreur)
            })
    }

    messageFiltrer(filterValue: string) {
        this.dataSource.filterPredicate = function (data, filter: string): boolean {
            return data.message.toLowerCase().includes(filter);
        };
        this.dataSource.filter = filterValue.trim().toLowerCase();

    }
    dateFilter(filterValue: string) {
        // console.log(filterValue)
        let regexp = new RegExp('(([01][0-9]|2[0-4]):([0-5][0-9]):([0-5][0-9]) (0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-\\d{4})')
        //      console.log(regexp.test(filterValue))
    }

    refreshLog() {
        console.log(this.dateJourDeb)
        this.dataSource.paginator = this.paginator;

        this.loader = true;
        this._apmService.recupererLogsInstanceParDateDebutEtFin(this.idContainerTest, this.dateJourDeb, this.dateJourFin).subscribe(logs => {
            this.dataSource.data = logs
            console.log(logs)
            this.loader = false
        },
            erreur => {
                this.loader = false;
                console.log(erreur)
            })


    }



}
