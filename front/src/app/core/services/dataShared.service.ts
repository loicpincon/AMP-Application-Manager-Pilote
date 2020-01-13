import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ParametreSeries, Livrable } from 'src/app/authentifie/application/modele/Application';

@Injectable()
export class DataSharedService {

  private paramSource = new BehaviorSubject<ParametreSeries>(new ParametreSeries());
  private livrableSource = new BehaviorSubject<Livrable>(new Livrable());

  currentParam = this.paramSource.asObservable();
  currentLivrable = this.livrableSource.asObservable();

  constructor() { }

  changeParam(param: ParametreSeries) {
    this.paramSource.next(param)
  } 
  changeLivrable(livrable: Livrable) {
    this.livrableSource.next(livrable)
  }
}