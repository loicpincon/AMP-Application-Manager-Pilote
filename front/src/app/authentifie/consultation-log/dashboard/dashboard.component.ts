import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'consulter-log-dashboard-root',
    template: 'HEy'
})
export class DashboardLogComponent implements OnInit {
    ngOnInit(): void {
        console.log('Chargement du dashboard des logs')
    }

}
