import { Injectable } from '@angular/core';
import { ApiManagerService, Api } from './api-manager.service';

@Injectable({
    providedIn: 'root'
})
export class ApiMapLoaderConfig {

    public message: string;
  
    constructor(private apimanager: ApiManagerService) {
    
    }

    async loadApiMap() {
        let InProgress = true;
        await this.apimanager.init().toPromise().then(function (result) {
            ApiManagerService.apis = result as Api[];
            InProgress = false;
        });

    }
}

export function apiMapLoaderConfigFactory(config: ApiMapLoaderConfig) {
    return async () => await config.loadApiMap();
}

