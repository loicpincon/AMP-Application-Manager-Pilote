import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { Instance, UserAction } from '../modele/Application';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
@Component({
  selector: 'application-action-user',
  templateUrl: './action-user.component.html',
  styleUrls: ['./action-user.component.css']
})
export class ActionUserComponent implements OnInit {
  displayedColumns: string[] = ['Version', 'Utilisateur', 'Action', 'Date', 'Status'];
  @Input() instance: Instance;

  constructor() { }

  dataSource = new MatTableDataSource<UserAction>();

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }
  ngOnChanges() {
    if (!this.instance.userActions) {
      this.instance.userActions = new Array()
    }
    this.dataSource.data = this.instance.userActions
  }

}