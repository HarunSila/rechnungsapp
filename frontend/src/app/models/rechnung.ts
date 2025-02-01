import { Umsatzsteuer } from './umsatzsteuer';
import { Position } from './position';
import { Geschaeftspartner } from './geschaeftspartner';
import { Ansprechpartner } from './ansprechpartner';

export interface Rechnung {
  id?: number;
  nummer: string;
  bezeichnung: string;
  perMail: boolean;
  preText: string;
  postText: string;
  leistungVon: Date;
  leistungBis: Date;
  bestellNummer: string;
  gesamtNettoInCent?: number;
  umsatzSteuerInCent?: number;
  gesamtBruttoInCent?: number;
  generierteRechnung?: string;
  umsatzsteuer: Umsatzsteuer;
  positionen: Position[];
  geschaeftspartner: Geschaeftspartner;
  ansprechpartner: Ansprechpartner;
}
