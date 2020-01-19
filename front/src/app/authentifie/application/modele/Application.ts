/**
 * 
 */
export abstract class Application {
    id: string;
    name: string;
    type: string;
    environnements: Map<number, Environnement>;
    livrables: Livrable[];
    dockerFileId: string;
    baseName: string;
}

/**
 * 
 */
export class Environnement {
    parametres: ParametreSeries[];
    instances: Instance[];
}

/**
 * 
 */
export class ParametreSeries {
    version: string;
    derniereModification: Date;
    parametres: StringMap;
    userActions: UserAction[];
}


export class StringMap {
    [key: string]: string;
}

/**
 * 
 */
export class UserAction {
    membre: string;
    date: Date;
    commentaire: string;
    libelle: string;
    version: string;
    status: string;

}

/**
 * 
 */
export class Instance {
    id: string;
    containerId: string;
    url: string;
    etat: string;
    userActions: UserAction[];
    versionApplicationActuel: string;
    versionParametresActuel: string;
}

/**
 * 
 */
export class Livrable {
    id: string;
    nom: string;
    dateUpload: Date;
    folder: Boolean;
}
/**
 * 
 */
export class Log {
    type: string;
    timeStamp: string;
    message: string;
}

/**
 * 
 */
export class BashApplication extends Application {
    urlBatch: string;
}

/**
 * 
 */
export class WarApplication extends Application {
    versionWar: string;
    urlRepoNexs: string;
}

/**
 * 
 */
export interface ParamsInstance {
    is: Instance,
    params: ParametreSeries
    idServer: number;
}


/**
 * 
 */
export interface Serveur {
    id: number,
    nom: string;
    ip: string;
    dns: string;
}



/**
 * 
 */
export interface Dockerfile {
    id: number,
    name: string;
    file: string;
    isPublic: boolean;
}


