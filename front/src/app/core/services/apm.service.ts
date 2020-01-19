import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ApiManagerService } from 'src/app/core/services/api-manager.service';
import { Observable } from 'rxjs';
import { Application, Instance, Serveur, Dockerfile, Log, ParametreSeries } from 'src/app/authentifie/application/modele/Application';
import { User, DroitApplicatifLevel, Right } from 'src/app/authentifie/administration/modele/model';
import { FormulaireLogInfo } from 'src/app/authentifie/consultation-log/modele/Model';


/**
 * Service pour authentifier un utilisateur sur l'application
 */

@Injectable({
    providedIn: 'root'
})
export class ApmService {

    moment = require('moment');

    constructor(private httpClient: HttpClient, private apiManagerService: ApiManagerService) {
    }

    connecterUser(login: string, pass: string): Observable<any> {
        const user = { 'login': login, 'password': pass }
        const uri = this.apiManagerService.genereUrl('Session.login').url;
        return this.httpClient.post<any>(uri, user);
    }

    inscrireUser(login: string, nom: string, prenom: string, password): Observable<User> {
        const user = { 'nom': nom, 'prenom': prenom, "login": login, "password": password }
        const uri = this.apiManagerService.genereUrl('Utilisateur.ajouter').url;
        return this.httpClient.post<User>(uri, user);
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

    //A supprimer
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

    recupererServeur(): Observable<Serveur[]> {
        const uri = this.apiManagerService.genereUrl('Server.recuperer').url;
        return this.httpClient.get<Serveur[]>(uri);
    }

    consulterServeur(id: string): Observable<Serveur> {
        let params = new HttpParams().set('id', id);
        const uri = this.apiManagerService.genereUrlWithParam('Server.consulter', params).url;
        return this.httpClient.get<Serveur>(uri);
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
        let params = new HttpParams().set('idUser', sessionStorage.getItem('USER_TOKEN'));
        const uri = this.apiManagerService.genereUrlWithParam('Application.ajouter', params).url;
        return this.httpClient.post<Application>(uri, application);
    }

    modifierApplication(application: Application): Observable<Application> {
        let params = new HttpParams().set('idApp', application.id);
        const uri = this.apiManagerService.genereUrlWithParam('Application.modifier', params).url;
        return this.httpClient.put<Application>(uri, application);
    }

    ajouterDockerFile(nom: string, file: string): Observable<Dockerfile> {
        const body = { 'file': file, 'name': nom }
        const uri = this.apiManagerService.genereUrl('Dockerfile.creer').url;
        return this.httpClient.post<Dockerfile>(uri, body);
    }

    ajouterInstance(serveur: string, idApp: string): Observable<Instance> {
        let params = new HttpParams().set('id', idApp).set('serveur', serveur);
        const uri = this.apiManagerService.genereUrlWithParam('Application.ajouterInstance', params).url;
        return this.httpClient.put<Instance>(uri, null);
    }

    deployerApplication(app: string, ins: string, idServeur: number, version: string, versionParam: string): Observable<Instance> {
        const uri = this.apiManagerService.genereUrl('Docker.creer').url;
        const body = { 'idApplicationCible': app, 'idInstanceCible': ins, 'idServeurCible': idServeur, 'version': version, 'versionParam': versionParam }
        return this.httpClient.post<Instance>(uri, body);
    }

    manageApplication(idApp: string, idServer: number, idContainer: string, action: string): Observable<Instance> {
        let params = new HttpParams().set('idApp', idApp).set('idServer', idServer + '').set('id', idContainer).set('action', action);
        const uri = this.apiManagerService.genereUrlWithParam('Docker.manage', params).url;
        return this.httpClient.get<Instance>(uri);
    }

    recupererDockerFile(): Observable<any[]> {
        const uri = this.apiManagerService.genereUrl('Dockerfile.recuperer').url;
        return this.httpClient.get<any[]>(uri);
    }

    ajouterDroitApplicatifs(droit: Right, tokenUser: string): Observable<any> {
        let params = new HttpParams().set('delete', 'false').set('level', droit.level).set('id', tokenUser);
        const body = { 'applicationId': droit.applicationId }
        const uri = this.apiManagerService.genereUrlWithParam('Utilisateur.ajouterDroitApplicatifs', params).url;
        return this.httpClient.post<any>(uri, body);
    }

    supprimerDroitApplicatifs(applicationId: string, tokenUser: string): Observable<any> {
        let params = new HttpParams().set('delete', 'true').set('id', tokenUser);
        const body = { 'applicationId': applicationId }
        const uri = this.apiManagerService.genereUrlWithParam('Utilisateur.ajouterDroitApplicatifs', params).url;
        return this.httpClient.post<any>(uri, body);
    }

    recupererLogsInstanceParDateDebutEtFin(idC: string, debut: string, fin: string) {
        let params = new HttpParams().set('idContainer', idC).set('debut', debut).set('fin', fin);
        return this.recupererLogsInstanceAbstract(params);
    }


    recupererLogsInstance(idC: string): Observable<Log[]> {
        let params = new HttpParams().set('idContainer', idC)
        return this.recupererLogsInstanceAbstract(params);
    }

    private recupererLogsInstanceAbstract(params: HttpParams): Observable<Log[]> {
        const uri = this.apiManagerService.genereUrlWithParam('DockerLog.recuperer', params).url;
        return this.httpClient.get<Log[]>(uri);
    }

    recupererInfoLogFormulaire(): Observable<FormulaireLogInfo> {
        let params = new HttpParams().set('idUser', sessionStorage.getItem('USER_TOKEN'));
        const uri = this.apiManagerService.genereUrlWithParam('DockerLog.recupererInfoFormulaire', params).url;
        return this.httpClient.get<FormulaireLogInfo>(uri);
    }

    ajouterSerieParametre(app, serveur, version): Observable<ParametreSeries> {
        const body = { 'version': version }
        let params = new HttpParams().set('id', app).set('serveur', serveur);
        const uri = this.apiManagerService.genereUrlWithParam('Application.ajouterParametre', params).url;
        return this.httpClient.post<ParametreSeries>(uri, body);
    }

    modifierSerieParametre(app, serveur, parameteserie: ParametreSeries): Observable<ParametreSeries> {
        let params = new HttpParams().set('id', app).set('serveur', serveur).set('version', parameteserie.version);
        const uri = this.apiManagerService.genereUrlWithParam('Application.modifierParametreSerie', params).url;
        return this.httpClient.put<ParametreSeries>(uri, parameteserie);
    }

    consulterSerieParametre(app, serveur, parameteserieversion: string): Observable<ParametreSeries> {
        let params = new HttpParams().set('id', app).set('serveur', serveur).set('version', parameteserieversion);
        const uri = this.apiManagerService.genereUrlWithParam('Application.modifierParametreSerie', params).url;
        return this.httpClient.get<ParametreSeries>(uri);
    }

}
