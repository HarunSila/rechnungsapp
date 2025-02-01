import { AnredeET } from './anredeET';
import { AdresseT } from './adresseT';

export interface Ansprechpartner {
  id?: number;
  name: string;
  vorname: string;
  email: string;
  anrede: AnredeET;
  anschrift: AdresseT;
}