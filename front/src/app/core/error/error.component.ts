import { Component, OnInit } from '@angular/core';
import { Router, NavigationStart, ActivatedRoute } from '@angular/router';
import { filter, map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Location } from '@angular/common';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {

  constructor(private location: Location, private router: Router, public activatedRoute: ActivatedRoute) { }

  code: string;
  message: string;
  state$: Observable<object>;

  ngOnInit(): void {
    const navigation = this.router.getCurrentNavigation();
    //this.code = navigation.extras.state ? navigation.extras.state.errorHttp : 0;
    //this.message = navigation.extras.state ? navigation.extras.state.errorMessage : 0;

    console.log(this.location.getState());
    this.state$ = this.activatedRoute.paramMap
      .pipe(map(() => window.history.state))

    this.state$.subscribe((test: ErrorCapture) => {
      this.code = test.errorHttp;
      this.message = test.errorMessage
    })
  }

}


export class ErrorCapture {
  errorMessage: string;
  errorHttp: string;
}