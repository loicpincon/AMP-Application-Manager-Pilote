import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ApiManagerService } from 'src/app/core/services/api-manager.service';
import { Observable } from 'rxjs';


/**
 * Service pour authentifier un utilisateur sur l'application
 */

@Injectable({
    providedIn: 'root'
})
export class ApmService {
    constructor(private httpClient: HttpClient, private apiManagerService: ApiManagerService) {
    }

    connecterUser(login: string, pass: string): Observable<any> {
        console.log(login)
        console.log(pass)
        const user = { 'login': login, 'password': pass }
        const uri = this.apiManagerService.genereUrl('Session.login').url;
        return this.httpClient.post<any>(uri, user);
    }


    recupererUser(token: string): Observable<any> {
        let params = new HttpParams().set('id', token);
        const uri = this.apiManagerService.genereUrlWithParam('Utilisateur.consulter', params).url;
        return this.httpClient.get<any>(uri);
    }
}
