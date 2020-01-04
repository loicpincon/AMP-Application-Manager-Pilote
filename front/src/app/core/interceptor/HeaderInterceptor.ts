import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders, } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { finalize } from 'rxjs/operators';

@Injectable()
export class AddHeaderInterceptor implements HttpInterceptor {
  constructor() { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const modifiedReq = req.clone({
      headers: req.headers.set('X-TOKEN-UTILISATEUR', localStorage.getItem('USER_TOKEN')),
    });
    return next.handle(modifiedReq);
  }
}