import { Rechnung } from "./rechnung";

export interface Position {
    id?: number;
    bezeichnung: string;
    menge: number;
    einheit: string;
    nettoEinzelpreisInCent: number;
    rechnung?: Rechnung;
}