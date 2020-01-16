export interface InstanceLog {
    id: string;
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