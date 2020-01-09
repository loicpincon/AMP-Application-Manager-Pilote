import { Component, OnInit, Input} from '@angular/core';
import { Livrable } from '../modele/Application';

@Component({
  selector: 'application-livrable',
  templateUrl: './livrable.component.html',
  styleUrls: ['./livrable.component.css']
})
export class LivrableComponent implements OnInit {

  displayedColumns: string[] = ['Version', 'Date'];
  
  constructor(){}

  @Input() livrables: Livrable[];

  ngOnInit() {
  }

}
