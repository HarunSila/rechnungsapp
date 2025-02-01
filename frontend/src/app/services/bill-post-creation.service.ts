import { Injectable } from "@angular/core";
import { Rechnung } from "../models/rechnung";

/*
  * This service provides the functionality to download a bill as a PDF file.
  * The downloadBillAsPDF method takes a Rechnung object as a parameter and converts the generierteRechnung property to a PDF file.
  * The method creates a Blob object from the base64 encoded string of the generierteRechnung property and creates a URL for the Blob object.
  * It then creates an anchor element and sets the href attribute to the URL of the Blob object and the download attribute to the name of the bill.
  * Finally, it triggers a click event on the anchor element to download the bill as a PDF file.
*/

@Injectable({
  providedIn: 'root',
})
export class BillPostCreationService {
  
    downloadBillAsPDF(rechnung: Rechnung): void {
        const byteCharacters = atob(rechnung.generierteRechnung!);
        const byteNumbers = new Array(byteCharacters.length).fill(0).map((_, i) => byteCharacters.charCodeAt(i));
        const byteArray = new Uint8Array(byteNumbers);
        const blob = new Blob([byteArray], { type: 'application/pdf' });
        const blobUrl = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = blobUrl;
        a.download = `${rechnung.bezeichnung}.pdf`;
        a.click();
        URL.revokeObjectURL(blobUrl);
    }
}