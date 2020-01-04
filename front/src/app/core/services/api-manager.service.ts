import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';

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


@Injectable({
  providedIn: 'root'
})
export class ApiManagerService {

  public static apis: Api[];

  constructor(private http: HttpClient) {
  }




  public init() {
    console.log("constructeur de l'api manager");
    return this.http.get(environment.urlServeurBase);
  }




  private getApiByKey(key: string): Api {
    for (var i = 0; i < ApiManagerService.apis.length; i++) {
      if (ApiManagerService.apis[i].key === key) {
        const api = ApiManagerService.apis[i];
        return new Api(api.key, api.url, api.verbe);
      }
    }
  }

  public genereUrlWithParam(key: string, tab: HttpParams): Api {
    const api: Api = this.getApiByKey(key);
    return this.buildParam(api, tab);
  }

  public genereUrl(key: string): Api {
    const api: Api = this.getApiByKey(key);
    return api;
  }

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
