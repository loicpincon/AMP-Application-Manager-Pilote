import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders, } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { finalize } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { LoaderService } from './loader.service';

@Injectable()
export class ApmHeaderInterceptor implements HttpInterceptor {



    constructor(private loaderService: LoaderService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let headers = new HttpHeaders({ 'Access-Control-Allow-Origin': '*' });
        if (this.checkHeaders('X-OPERATEUR-LOGIN')) {
            headers = headers.append('X-OPERATEUR-LOGIN', sessionStorage.getItem('X-OPERATEUR-LOGIN'));
        }
        if (this.checkHeaders('X-OPERATEUR-TOKEN')) {
            headers = headers.append('X-OPERATEUR-TOKEN', sessionStorage.getItem('X-OPERATEUR-TOKEN'));
        }

        const clonedRequest = req.clone({ headers });
        return next.handle(clonedRequest).pipe(
            finalize(() => {
                this.loaderService.hide();
            })
        );


    }


    checkHeaders(key: string) {
        if (sessionStorage.getItem(key) != null) {
            return true;
        }
    }


}
