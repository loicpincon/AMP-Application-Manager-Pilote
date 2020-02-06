import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

export class Api {

  public key: string;
  public url: string;
  public verbe: string;

  constructor(k: string, u: string, v: string) {
    this.key = k;
    this.url = u;
    this.verbe = v;
  }
}

export function apiManagerProviderFactory(provider: ApiManagerService) {
  return () => provider.load();
}

@Injectable({
  providedIn: 'root'
})
export class ApiManagerService {

  public static apis: Map<string, Api>;

  constructor(private http: HttpClient) {
  }


  load() {
    return new Promise((resolve) => {
      console.log('Début du chargement des Apis')
      this.http.get(environment.urlServeurBase).subscribe((apis: Map<string, Api>) => {
        ApiManagerService.apis = apis
        console.log('Fin du chargement des Apis')
        resolve(true);
      });
    })
  }


  /**
   * 
   */
  public init() {
    return this.http.get(environment.urlServeurBase + "/api/map")
  }

  /**
   * 
   * @param key 
   */
  private getApiByKey(key: string): Api {
    return ApiManagerService.apis[key];
  }

  /**
   * 
   * @param key 
   * @param tab 
   */
  private buildURL(key: string, tab?: any): Api {
    if (tab != undefined) {
      const api: Api = this.getApiByKey(key);
      return this.buildUrlParam(api, tab);
    } else {
      const api: Api = this.getApiByKey(key);
      return api;
    }
  }


  /**
   * 
   * @param api 
   * @param tab 
   */
  private buildUrlParam(api: Api, tab: any): Api {
    const matchAll = require("match-all");
    let url = api.url;
    let array = matchAll(url, /{([a-z]+)}/gi).toArray();
    for (var key in tab) {
      if (tab.hasOwnProperty(key)) {
        url = url.replace('{' + key + '}', tab[key]);
      }
    }
    array.forEach(element => {
      url = url.replace(element + '={' + element + '}', '');
    });
    api.url = url;
    return api;
  }

  /**
   * Permet de construire l'URL
   * @param key cle de l'api 
   * @param params 
   */
  private builder(key: string, params?: Array<any>): string {
    let url = "";
    if (params != undefined) {
      url = this.buildURL(key, params).url;
    } else {
      url = this.buildURL(key).url;
    }
    return url;
  }


  /**
   * 
   * @param key cle de l'api map
   * @param params sous forme de json cle/valeur { idclient: 79789, modeAppel: "distant" }
   */
  public get<T>(key: string, params?: Array<any>): Observable<T> {
    return this.http.get<T>(this.builder(key, params));
  }

  /**
   * 
   * @param key cle de l'api map
   * @param params sous forme de json cle/valeur { idclient: 79789, modeAppel: "distant" }
   */
  public post<T>(key: string, body?: Object, params?: Array<any>): Observable<T> {
    return this.http.post<T>(this.builder(key, params), body);
  }

  /**
   * 
   * @param key cle de l'api map
   * @param params sous forme de json cle/valeur { idclient: 79789, modeAppel: "distant" }
   */
  public put<T>(key: string, body?: Object, params?: Array<any>): Observable<T> {
    return this.http.put<T>(this.builder(key, params), body);
  }

  /**
   * 
   * @param key cle de l'api map
   * @param params sous forme de json cle/valeur { idclient: 79789, modeAppel: "distant" }
   */
  public delete<T>(key: string, params?: Array<any>): Observable<T> {
    return this.http.delete<T>(this.builder(key, params));
  }


  /**
   * 
   * @param key 
   * @param tab 
   * @deprecated
   */
  public genereUrlWithParam(key: string, tab: HttpParams): Api {
    const api: Api = this.getApiByKey(key);
    return this.buildParam(api, tab);
  }

  /**
   * 
   * @param key 
   * @deprecated
   */
  public genereUrl(key: string): Api {
    const api: Api = this.getApiByKey(key);
    api.url = api.url.split('?')[0];
    return api;
  }

  /**
   * 
   * @param api 
   * @param tab 
   * @deprecated
   */
  private buildParam(api: Api, tab: HttpParams): Api {

    const matchAll = require("match-all");
    let url = api.url;
    let array = matchAll(url, /{([a-z]+)}/gi).toArray();
    tab.keys().forEach(function (element) {
      url = url.replace('{' + element + '}', tab.get(element));
    });
    array.forEach(element => {
      url = url.replace(element + '={' + element + '}', '');
    });
    api.url = url;
    return api;
  }

}
