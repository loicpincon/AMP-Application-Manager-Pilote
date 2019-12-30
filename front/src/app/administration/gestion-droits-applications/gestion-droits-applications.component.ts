import { Component, OnInit } from '@angular/core';
import { ApmService } from 'src/app/core/services/apm.service';

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


@Component({
  selector: 'app-gestion-droits-applications',
  templateUrl: './gestion-droits-applications.component.html',
  styleUrls: ['./gestion-droits-applications.component.css']
})
export class GestionDroitsApplicationsComponent implements OnInit {

  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol'];
  users :any[];
  applications :any[];

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

  constructor(private serviceApm:ApmService) { }

  ngOnInit() {
    this.serviceApm.recupererAllUser().subscribe(users=>{
      this.users = users;
    })
    this.serviceApm.recupererAllApplications().subscribe(applications=>{
      this.applications = applications;
    })
  }

checkedApp = false;
checkedUsers = false;

  changeStateUsers(){
    this.checkedApp = false;
    this.checkedUsers = true;
  }

  changeStateApp(){
    this.checkedApp = true;
    this.checkedUsers = false;
  }

}
