import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders, } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable()
export class TokenUserHeaderInterceptor implements HttpInterceptor {
  constructor() { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (sessionStorage.getItem('USER_TOKEN') != null) {
      req = req.clone({ headers: req.headers.set("X-TOKEN-UTILISATEUR", sessionStorage.getItem('USER_TOKEN')) });
    }
    return next.handle(req);
  }
}
