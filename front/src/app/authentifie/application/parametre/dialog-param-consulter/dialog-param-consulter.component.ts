import { Component, OnInit, Inject, Optional } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
@Component({
  selector: 'application-parametre-consulter',
  templateUrl: './dialog-param-consulter.component.html',
})
export class DialogConsulterSerieParamComponent implements OnInit {


  constructor(public dialogRef: MatDialogRef<DialogConsulterSerieParamComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
  }

  close(): void {
    this.dialogRef.close();
  }



}