import { HttpInterceptor, HttpHandler, HttpRequest, HttpEvent, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { of } from "rxjs";
import { tap, catchError } from "rxjs/operators";
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
@Injectable()
@Injectable()
export class GeneralHeaderInterceptor implements HttpInterceptor {
  constructor(public toasterService: ToastrService, private router: Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    req = req.clone({ headers: req.headers.set('Access-Control-Allow-Origin', '*') });
    return next.handle(req).pipe(
      tap(evt => {
        if (evt instanceof HttpResponse) {
          if (evt.body && evt.body.success) {
            //this.toasterService.success(evt.body.success.message, evt.body.success.title, { positionClass: 'toast-bottom-center' });

          }
        }
      }),
      catchError((err: any) => {
        console.log(err.error
        )
        console.log(err.error.message)
        if (err.error.codeHttp === 404 || err.error.codeHttp === 403 || err.error.codeHttp === 500) {
          this.router.navigateByUrl('/core/error', { state: { errorHttp: err.error.codeHttp, errorMessage: err.error.message } });
        } else if (err.error.codeHttp === 401) {
          this.router.navigateByUrl('/unsecure');
        } else {
          this.toasterService.error(err.error.message, err.error.title, { positionClass: 'toast-bottom-center' });
        }

        if (err instanceof HttpErrorResponse) {


          try {
            console.log(err)

            // this.toasterService.error(err.error.message, err.error.title, { positionClass: 'toast-bottom-center' });
          } catch (e) {
            //this.toasterService.error('An error occurred', '', { positionClass: 'toast-bottom-center' });
          }
          //log error 
        }
        return of(err);
      }));;
  }

}
