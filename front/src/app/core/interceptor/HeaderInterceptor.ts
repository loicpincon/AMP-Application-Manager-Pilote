import { HttpEvent,  HttpInterceptor,  HttpHandler,   HttpRequest,} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
  
@Injectable({
    providedIn: 'root'
})
  export class AddHeaderInterceptor implements HttpInterceptor {
    
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      const clonedRequest = req.clone({ headers: req.headers.set('X-TOKEN-UTILISATEUR',localStorage.getItem('USER_TOKEN')) });
        return next.handle(clonedRequest);
    }
  }