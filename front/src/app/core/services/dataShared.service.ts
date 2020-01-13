import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ParametreSeries } from 'src/app/authentifie/application/modele/Application';

@Injectable()
export class DataSharedService {

  private paramSource = new BehaviorSubject<ParametreSeries>(new ParametreSeries());
  currentParam = this.paramSource.asObservable();

  constructor() { }

  changeParam(message: ParametreSeries) {
    this.paramSource.next(message)
  }

}