/**
 * 
 */
export abstract class Application {
    id: string;
    name: string;
    type: string;
    environnements: Map<number, Environnement>;
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
    parametres: Map<string, string>;
    userActions: UserAction[];
}

/**
 * 
 */
export class UserAction {
    membre: string;
    date: Date;
    commentaire: string;
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
