import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ApiManagerService } from 'src/app/core/services/api-manager.service';
import { Observable } from 'rxjs';
import { Application } from 'src/app/application/modele/Application';


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
        const user = { 'login': login, 'password': pass }
        const uri = this.apiManagerService.genereUrl('Session.login').url;
        return this.httpClient.post<any>(uri, user);
    }


    recupererUser(token: string): Observable<any> {
        let params = new HttpParams().set('id', token);
        const uri = this.apiManagerService.genereUrlWithParam('Utilisateur.consulter', params).url;
        return this.httpClient.get<any>(uri);
    }

    recupererAllUser(): Observable<any> {
        const uri = this.apiManagerService.genereUrl('Utilisateur.recuperer').url;
        return this.httpClient.get<any>(uri);
    }

    recupererAllApplications(): Observable<Application[]> {
        const uri = this.apiManagerService.genereUrl('Application.recuperer').url;
        return this.httpClient.get<Application[]>(uri);
    }

    recupererAllApplicationsByUser(): Observable<Application[]> {
        let params = new HttpParams().set('idUser', localStorage.getItem('USER_TOKEN'));
        const uri = this.apiManagerService.genereUrlWithParam('Application.recupererParUser', params).url;
        console.log(uri)
        return this.httpClient.get<Application[]>(uri);
    }

    recupererSession(): Observable<any> {
        const uri = this.apiManagerService.genereUrl('Session.consulter').url;
        return this.httpClient.get<any>(uri);
    }

    recupererTypeApplications(): Observable<string[]> {
        const uri = this.apiManagerService.genereUrl('ApplicationType.recuperer').url;
        return this.httpClient.get<string[]>(uri);
    }

    deconnecterSession(): Observable<any> {
        const uri = this.apiManagerService.genereUrl('Session.deconnecter').url;
        return this.httpClient.delete<any>(uri);
    }

    ajouterApplication(application: Application): Observable<Application> {
        let params = new HttpParams().set('idUser', localStorage.getItem('USER_TOKEN'));
        const uri = this.apiManagerService.genereUrlWithParam('Application.ajouter', params).url;
        return this.httpClient.post<Application>(uri, application);
    }

}
