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
    rightApp: string;
    email: string;
    roles: any[];
}

export class UserProfile {
    login: string;
    password: string;
    token: string;
    nom: string;
    prenom: string;
    rights: Right[];
    email: string;
}

export interface UserTypesApp {
    user: User[];
    types: DroitApplicatifLevel[];
    applicationId: string;
}