import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatTableDataSource, MatPaginator } from '@angular/material';
import { Instance, Log } from '../../modele/Application';
import { ApmService } from 'src/app/core/services/apm.service';
import { DatePipe } from '@angular/common';
@Component({
  selector: 'application-action-dialogLogs',
  templateUrl: './dialog-logs-instance.component.html',
  styleUrls: ['./dialog-logs-instance.component.css']
})
export class dialogLogsInstanceComponent implements OnInit {

  displayedColumns: string[] = ['Version', 'Date'];
  dataSource = new MatTableDataSource<Log>();
  loader: boolean = false;
  dateJourDeb: string = this.datePipe.transform(new Date(), 'HH:mm:ss dd-MM-yyyy')
  dateJourFin: string = this.datePipe.transform(new Date(), 'HH:mm:ss dd-MM-yyyy')
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  constructor(public dialogRef: MatDialogRef<dialogLogsInstanceComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Instance, private _apmService: ApmService, private datePipe: DatePipe) { }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    if (this.data.containerId) {

      this.loader = true;
      this._apmService.recupererLogsInstance(this.data.containerId).subscribe(logs => {
        this.dataSource.data = logs
        this.loader = false
      },
        erreur => {
          this.loader = false;
          console.log(erreur)
        })
      /*this._apmService.recupererLogsInstance(this.data.containerId).subscribe(logs =>{
        console.log(logs)
      },
      erreur =>{
        console.log(erreur)
      })*/
    }


  }

  fermer(): void {
    this.dialogRef.close();
  }

  messageFiltrer(filterValue: string) {
    this.dataSource.filterPredicate = function (data, filter: string): boolean {
      return data.message.toLowerCase().includes(filter);
    };
    this.dataSource.filter = filterValue.trim().toLowerCase();

  }

  dateFilter(filterValue: string) {
    console.log(filterValue)
    let regexp = new RegExp('(([01][0-9]|2[0-4]):([0-5][0-9]):([0-5][0-9]) (0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-\\d{4})')
    console.log(regexp.test(filterValue))
  }

}