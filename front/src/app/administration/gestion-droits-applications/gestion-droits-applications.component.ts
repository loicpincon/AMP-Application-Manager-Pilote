import { Component, OnInit } from '@angular/core';

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  selected: string;
}

export interface Food {
  value: string;
  viewValue: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  {position: 1, name: 'Hydrogen', weight: 1.0079, selected: 'CP'},
  {position: 2, name: 'Helium', weight: 4.0026, selected: 'DEV'},
  {position: 3, name: 'Lithium', weight: 6.941, selected: 'DEV'},
  {position: 4, name: 'Beryllium', weight: 9.0122, selected: 'CP'},
  {position: 5, name: 'Boron', weight: 10.811, selected: ''},
  {position: 6, name: 'Carbon', weight: 12.0107, selected: 'DEV'}
];

@Component({
  selector: 'app-gestion-droits-applications',
  templateUrl: './gestion-droits-applications.component.html',
  styleUrls: ['./gestion-droits-applications.component.css']
})
export class GestionDroitsApplicationsComponent implements OnInit {

  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol'];
  dataSource = ELEMENT_DATA;

  foods: Food[] = [
    {value: 'steak-0', viewValue: 'Steak'},
    {value: 'pizza-1', viewValue: 'Pizza'},
    {value: 'tacos-2', viewValue: 'Tacos'}
  ];

  types: any[] = [
   'DEV',
   'CP',
   'EXPRT'
  ];

  constructor() { }

  ngOnInit() {
  }

}
