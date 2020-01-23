import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';
import { Dockerfile } from '../../application/modele/Application';
import { MatTableDataSource, MatSnackBar } from '@angular/material';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';



@Component({
    selector: 'docker-dockerfile-root',
    templateUrl: './dockerfile.component.html',
    styleUrls: ['./dockerfile.component.css']
})
export class DockerFileComponent implements OnInit {

    constructor(private changeDetectorRefs: ChangeDetectorRef, private _snackBar: MatSnackBar, private apmService: ApmService, private apmservice: ApmService, private formBuilder: FormBuilder) {

    }

    formulaire: FormGroup;


    displayedColumns: string[] = ['position', 'name', 'weight', 'action'];

    dataSource = new MatTableDataSource<Dockerfile>();
    edit = false;
    create = false;

    dockerfiles: Dockerfile[];

    idDockerfileCurrent: number;

    ngOnInit(): void {
        this.apmservice.recupererDockerFile().subscribe(dks => {
            this.dataSource.data = dks;
            this.dockerfiles = dks;
        })


    }


    chooseDKEdit(dk: Dockerfile) {
        console.log(dk)
        this.idDockerfileCurrent = dk.id;
        this.formulaire = this.formBuilder.group({
            id: new FormControl({ value: dk.id, disabled: true }),
            nom: dk.name,
            isPublic: dk.isPublic,
            dockerfilesText: dk.file,
            check: false
        })
        this.edit = true;
    }

    annulerForm() {
        this.formulaire = null;
        this.edit = false;
        this.create = false;

    }


    deleteDK(dk: Dockerfile) {
        this.apmService.supprimerDockerFile(dk).subscribe(dkr => {
            this._snackBar.open('suppression effectuée !', '', {
                duration: 2000,
                panelClass: 'customSnackBar'
            });


            // get index of object with id:37
            var removeIndex = this.dockerfiles.map(function (item) { return item.id; }).indexOf(dk.id);

            // remove object
            this.dockerfiles.splice(removeIndex, 1);


            this.dataSource.data = this.dockerfiles
            this.changeDetectorRefs.detectChanges();
        })
    }


    createDK() {
        this.formulaire = this.formBuilder.group({
            id: new FormControl({ value: '', disabled: true }),
            nom: null,
            isPublic: null,
            dockerfilesText: null,
            check: false
        })
        this.create = true;
    }

    onSubmit(customerData) {

        console.log(customerData)

        let body = new Dockerfile();
        body.id = this.idDockerfileCurrent
        body.name = customerData.nom;
        body.isPublic = customerData.isPublic;
        body.file = customerData.dockerfilesText


        if (this.edit) {
            this.apmService.modifierDockerFile(body).subscribe(dk => {
                this._snackBar.open('Modification enregistrée !', '', {
                    duration: 2000,
                    panelClass: 'customSnackBar'
                });


            })

        }


        if (this.create) {

            this.apmService.ajouterDockerFile(body).subscribe(dk => {
                this._snackBar.open('Dockerfile ajouté avec Succes !', '', {
                    duration: 2000,
                    panelClass: 'customSnackBar'
                });
                this.dockerfiles.push(dk)
                this.dataSource.data = this.dockerfiles
                this.changeDetectorRefs.detectChanges();
                this.formulaire = null
                this.create = false;
            })

        }
    }


}