import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  constructor() { }
  public barChartOptions = {
    scaleShowVerticalLines: false,
    responsive: true
  };
  public barChartLabels = ['Running', 'Stopped', 'Empty'];
  public barChartType = 'bar';
  public barChartLegend = true;
  public barChartData = [
    { data: [59, 13, 8], label: 'Etat des containers' },
  ];
  ngOnInit() {
  }


}
