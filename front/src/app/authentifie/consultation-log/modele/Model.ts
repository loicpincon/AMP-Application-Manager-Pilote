export interface InstanceLog {
    id: string;
    libelle: string;
    etat: string;
}

export interface AppLog {
    id: string;
    name: string;
    instances: InstanceLog[];
}

export interface EnvLog {
    idEnv: number;
    libelle: string;
    apps: AppLog[];
}

export interface FormulaireLogInfo {
    envs: EnvLog[];
}

export interface ApplicationInformation {
    version: string;
}