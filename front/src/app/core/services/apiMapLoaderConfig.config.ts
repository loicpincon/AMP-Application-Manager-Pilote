import { Injectable } from '@angular/core';
import { ApiManagerService, Api } from './api-manager.service';

@Injectable({
    providedIn: 'root'
})
export class ApiMapLoaderConfig {

    constructor(private apimanager: ApiManagerService) {
    }

    async loadApiMap() {
        await this.apimanager.init().toPromise().then(function (result) {
            ApiManagerService.apis = result as Map<string, Api>;
        });
    }
}

export function apiMapLoaderConfigFactory(config: ApiMapLoaderConfig) {
    return async () => await config.loadApiMap();
}

