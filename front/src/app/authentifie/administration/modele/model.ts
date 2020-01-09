export class DroitApplicatifLevel {
    id: string;
    libelle: string;
}

export class Right {
    applicationId: string;
    date: any;
    level: string;
}

export class User {
    login: string;
    password: string;
    token: string;
    nom: string;
    prenom: string;
    rights: Right[];
}

export interface UserTypes{
    user: User[];
    types: Right[];
}