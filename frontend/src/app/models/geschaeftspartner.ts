import { AdresseT } from './adresseT';
import { Rechnung } from './rechnung';

export interface Geschaeftspartner {
  id?: number;
  name: string;
  beschreibung: string;
  anschrift: AdresseT;
  rechnungen?: Rechnung[];
}