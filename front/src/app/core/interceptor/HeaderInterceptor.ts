import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders, } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AddHeaderInterceptor implements HttpInterceptor {
  constructor() { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let headers = new HttpHeaders({ 'Access-Control-Allow-Origin': '*' });

    console.log(req.urlWithParams);
    console.log(sessionStorage.getItem('USER_TOKEN'))
    if (sessionStorage.getItem('USER_TOKEN')) {
      console.log('ajou du header')
      if (sessionStorage.getItem('X-TOKEN-UTILISATEUR')) {
        headers = headers.append('X-TOKEN-UTILISATEUR', sessionStorage.getItem('USER_TOKEN'));
      }
      const modifiedReq = req.clone({ headers, });
      return next.handle(modifiedReq);
    } else {
      let headersM = new HttpHeaders({ 'Access-Control-Allow-Origin': '*' });
      const modifiedReq = req.clone({
        headers: headersM,
      });
      return next.handle(modifiedReq);
    }

  }
}