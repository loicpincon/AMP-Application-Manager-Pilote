import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'consulter-log-recherche-root',
    template: 'HEy recherche'
})
export class RechercheLogComponent implements OnInit {
    ngOnInit(): void {
        console.log('Chargement du recherche des logs')
    }

}
