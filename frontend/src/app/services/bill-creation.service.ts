import { inject, Injectable } from '@angular/core';
import { Rechnung } from '../models/rechnung';
import { Position } from '../models/position';
import { BehaviorSubject } from 'rxjs';
import { AnredeET } from '../models/anredeET';
import { RechnungAPIService } from './api/rechnungAPI.service';
import { BillPostCreationService } from './bill-post-creation.service';

/*
  This service is responsible for the creation of a bill. It stores the data of the bill creation process from the dasboard and provides methods to create a bill.
  It also provides methods to reset the data of the bill creation process.
  The service uses the RechnungAPIService to send the created bill to the backend and the BillPostCreationService to download the created bill as a PDF.
*/

@Injectable({
  providedIn: 'root',
})
export class BillCreationService {
  private rechnungAPI = inject(RechnungAPIService);
  private billPostCreationService = inject(BillPostCreationService);
  activeStep: number = 0;
  positions: Position[] = [];
  nummer: string = '';
  bestellnummer: string = '';

  //Step 1: General Information
  bezeichnung = new BehaviorSubject<string>('');
  perMail = new BehaviorSubject<boolean>(false);
  leistungVon = new BehaviorSubject<Date>(new Date());
  leistungBis = new BehaviorSubject<Date>(new Date());
  umsatzsteuer = {
    name: new BehaviorSubject<string>(''),
    satz: new BehaviorSubject<number | null>(null),
  };
  step1Valid = new BehaviorSubject<boolean>(false);

  //Step2: Partner Information
  geschaeftspartner = {
    id: new BehaviorSubject<number>(0),
    name: new BehaviorSubject<string>(''),
    beschreibung: new BehaviorSubject<string>(''),
    anschrift: {
      strasse: new BehaviorSubject<string>(''),
      nummer: new BehaviorSubject<string>(''),
      plz: new BehaviorSubject<string>(''),
      ort: new BehaviorSubject<string>(''),
      land: new BehaviorSubject<string>(''),
    }
  };
  step2Valid = new BehaviorSubject<boolean>(false);

  ansprechpartner = { 
    name: new BehaviorSubject<string>(''),
    vorname: new BehaviorSubject<string>(''),
    email: new BehaviorSubject<string>(''),
    anrede: new BehaviorSubject<AnredeET>(AnredeET.HERR),
    anschrift: {
      strasse: new BehaviorSubject<string>(''),
      nummer: new BehaviorSubject<string>(''),
      plz: new BehaviorSubject<string>(''),
      ort: new BehaviorSubject<string>(''),
      land: new BehaviorSubject<string>(''),
    }
  };

  // Step 3: Text Template
  preText = new BehaviorSubject<string>('');
  postText = new BehaviorSubject<string>('');
  step3Valid = new BehaviorSubject<boolean>(false);


  // Store selected products from the product selection of dashboard
  setPositions(positions: Position[]) { 
    this.positions = positions;
  }

  // Provide the selected products from the product selection of dashboard
  getPositions() {
    return this.positions;
  }

  // Reset the selected products from the product selection of dashboard
  resetPositions() {
    this.activeStep = 0;
    this.positions = [];
  }

  // Reset the data of general information step
  resetGneralInformation() {
    this.bezeichnung = new BehaviorSubject<string>('');
    this.perMail = new BehaviorSubject<boolean>(false);
    this.leistungVon = new BehaviorSubject<Date>(new Date());
    this.leistungBis = new BehaviorSubject<Date>(new Date());
    this.umsatzsteuer = {
      name: new BehaviorSubject<string>(''),
      satz: new BehaviorSubject<number | null>(null),
    };
    this.step1Valid = new BehaviorSubject<boolean>(false);
  }  

  // Reset the data of partner information step
  resetGeschaeftspartner() {
    this.geschaeftspartner = {
      id: new BehaviorSubject<number>(0),
      name: new BehaviorSubject<string>(''),
      beschreibung: new BehaviorSubject<string>(''),
      anschrift: {
        strasse: new BehaviorSubject<string>(''),
        nummer: new BehaviorSubject<string>(''),
        plz: new BehaviorSubject<string>(''),
        ort: new BehaviorSubject<string>(''),
        land: new BehaviorSubject<string>(''),
      }
    };
    this.step2Valid = new BehaviorSubject<boolean>(false);
  }

  resetAnsprechpartner() {
    this.ansprechpartner = { 
      name: new BehaviorSubject<string>(''),
      vorname: new BehaviorSubject<string>(''),
      email: new BehaviorSubject<string>(''),
      anrede: new BehaviorSubject<AnredeET>(AnredeET.HERR),
      anschrift: {
        strasse: new BehaviorSubject<string>(''),
        nummer: new BehaviorSubject<string>(''),
        plz: new BehaviorSubject<string>(''),
        ort: new BehaviorSubject<string>(''),
        land: new BehaviorSubject<string>(''),
      }
    };
  }

  // Reset the data of text template step
  resetTextTemplate() {
    this.preText = new BehaviorSubject<string>('');
    this.postText = new BehaviorSubject<string>('');
    this.step3Valid = new BehaviorSubject<boolean>(false);
  }
  
  // Reset the data of the whole bill creation process
  reset() {
    this.resetPositions();
    this.resetGneralInformation();
    this.resetGeschaeftspartner();
    this.resetAnsprechpartner();
    this.resetTextTemplate();
  }

  // Create a bill with the data of the bill creation process
  // Send the created bill to the backend and download it as a PDF
  createBill() {
    const rechnung: Rechnung = {
      nummer: this.nummer,
      bezeichnung: this.bezeichnung.value,
      perMail: this.perMail.value,
      preText: this.preText.value,
      postText: this.postText.value,
      leistungVon: this.leistungVon.value,
      leistungBis: this.leistungBis.value,
      bestellNummer: this.bestellnummer,
      umsatzsteuer: {
        name: this.umsatzsteuer.name.value,
        satz: this.umsatzsteuer.satz.value!,
      },
      positionen: this.positions,
      geschaeftspartner: {
        id: this.geschaeftspartner.id.value,
        name: this.geschaeftspartner.name.value,
        beschreibung: this.geschaeftspartner.beschreibung.value,
        anschrift: {
          strasse: this.geschaeftspartner.anschrift.strasse.value,
          nummer: this.geschaeftspartner.anschrift.nummer.value,
          plz: this.geschaeftspartner.anschrift.plz.value,
          ort: this.geschaeftspartner.anschrift.ort.value,
          land: this.geschaeftspartner.anschrift.land.value,
        }
      },
      ansprechpartner: {
        name: this.ansprechpartner.name.value,
        vorname: this.ansprechpartner.vorname.value,
        email: this.ansprechpartner.email.value,
        anrede: this.ansprechpartner.anrede.value,
        anschrift: {
          strasse: this.ansprechpartner.anschrift.strasse.value,
          nummer: this.ansprechpartner.anschrift.nummer.value,
          plz: this.ansprechpartner.anschrift.plz.value,
          ort: this.ansprechpartner.anschrift.ort.value,
          land: this.ansprechpartner.anschrift.land.value,
        }
      }
    };
    this.rechnungAPI.addRechnung(rechnung).subscribe((rechnung: Rechnung) => {
      this.billPostCreationService.downloadBillAsPDF(rechnung);
    });
  }
}
