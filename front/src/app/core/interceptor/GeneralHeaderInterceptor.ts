import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders, } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable()
export class GeneralHeaderInterceptor implements HttpInterceptor {
  constructor() { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    req = req.clone({ headers: req.headers.set('Access-Control-Allow-Origin', '*') });
    return next.handle(req);
  }

}
