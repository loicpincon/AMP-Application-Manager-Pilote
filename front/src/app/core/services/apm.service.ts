import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ApiManagerService } from 'src/app/core/services/api-manager.service';
import { Observable } from 'rxjs';
import { Application } from 'src/app/authentifie/application/modele/Application';
import { User, DroitApplicatifLevel } from 'src/app/authentifie/administration/modele/model';


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


    recupererAllUserByLoginOrNomOrPrenom(keyword: string): Observable<User[]> {
        let params = new HttpParams().set('keyword', keyword);
        const uri = this.apiManagerService.genereUrlWithParam('Utilisateur.recuperer', params).url;
        return this.httpClient.get<User[]>(uri);
    }


    recupererAllApplications(): Observable<Application[]> {
        const uri = this.apiManagerService.genereUrl('Application.recuperer').url;
        return this.httpClient.get<Application[]>(uri);
    }

    recupererApplication(idApp: string): Observable<Application> {
        let params = new HttpParams().set('idApp', idApp);
        const uri = this.apiManagerService.genereUrlWithParam('Application.consulter', params).url;
        return this.httpClient.get<Application>(uri);
    }

    recupererAllApplicationsByUser(): Observable<Application[]> {
        let params = new HttpParams().set('idUser', sessionStorage.getItem('USER_TOKEN'));
        const uri = this.apiManagerService.genereUrlWithParam('Application.recupererParUser', params).url;
        return this.httpClient.get<Application[]>(uri);
    }


    recupererAllUserByApplications(idApp: string): Observable<User[]> {
        let params = new HttpParams().set('idApp', idApp);
        const uri = this.apiManagerService.genereUrlWithParam('Utilisateur.recuperer', params).url;
        return this.httpClient.get<User[]>(uri);
    }

    recupererSession(): Observable<any> {
        const uri = this.apiManagerService.genereUrl('Session.consulter').url;
        return this.httpClient.get<any>(uri);
    }

    recupererTypeApplications(): Observable<string[]> {
        const uri = this.apiManagerService.genereUrl('ApplicationType.recuperer').url;
        return this.httpClient.get<string[]>(uri);
    }

    recupererDroitApplicatifs(): Observable<DroitApplicatifLevel[]> {
        const uri = this.apiManagerService.genereUrl('DroitApplicatifs.recuperer').url;
        return this.httpClient.get<DroitApplicatifLevel[]>(uri);
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


    deployerApplication(app: string, ins: string, idServeur: number, version: string): Observable<Application> {
        const uri = this.apiManagerService.genereUrl('Docker.creer').url;
        const body = { 'idApplicationCible': app, 'idInstanceCible': ins, 'idServeurCible': idServeur, 'version': version }
        return this.httpClient.post<Application>(uri, body);
    }

    manageApplication(idApp: string, idServer: number, idContainer: string, action: string): Observable<Application> {
        let params = new HttpParams().set('idApp', idApp).set('idServer', idServer + '').set('id', idContainer).set('action', action);
        const uri = this.apiManagerService.genereUrlWithParam('Docker.manage', params).url;
        return this.httpClient.get<Application>(uri);
    }

}
