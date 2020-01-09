import { Component, OnInit, ViewChild, Input} from '@angular/core';
import { MatPaginator, MatTableDataSource } from '@angular/material';
import { Instance, UserAction } from '../modele/Application';
@Component({
  selector: 'application-action-user',
  templateUrl: './action-user.component.html',
  styleUrls: ['./action-user.component.css']
})
export class ActionUserComponent implements OnInit {
  displayedColumns: string[] = ['Version', 'Utilisateur','Action','Date','Status'];
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @Input() instance: Instance;

  constructor(){}

  dataSource = new MatTableDataSource<UserAction>();

  ngOnInit() {
    this.dataSource.paginator = this.paginator; 
  }

  ngOnChanges(){
    this.dataSource.data = this.instance.userActions;
  }

}