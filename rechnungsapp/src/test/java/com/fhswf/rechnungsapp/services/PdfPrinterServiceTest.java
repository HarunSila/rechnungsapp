package com.fhswf.rechnungsapp.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fhswf.rechnungsapp.models.EinheitET;
import com.fhswf.rechnungsapp.models.Position;
import com.fhswf.rechnungsapp.models.Rechnung;
import com.fhswf.rechnungsapp.models.Umsatzsteuer;

public class PdfPrinterServiceTest {
    
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
    private Rechnung rechnung;
    private PDDocument document;
    
    // create TestRechnung for testing
    @BeforeEach
    void setUp() {
        rechnung = new Rechnung();
        rechnung.setNummer("12345");
        rechnung.setBestellNummer("98765");
        rechnung.setPreText("This is a test invoice.");
        rechnung.setPostText("Thank you for your business.");
        rechnung.setLeistungVon(new Date());
        rechnung.setLeistungBis(new Date());

        Umsatzsteuer umsatzsteuer = new Umsatzsteuer();
        umsatzsteuer.setSatz(19.0f);
        rechnung.setUmsatzsteuer(umsatzsteuer);

        Position position = new Position();
        position.setBezeichnung("Test Product");
        position.setMenge(2);
        position.setEinheit(EinheitET.STUECK);
        position.setNettoEinzelpreisInCent(500);
        rechnung.setPositionen(List.of(position));
    }

    @Test
    void testCreateDocument() throws IOException {
        // Check if the PDF document is created
        assertNotNull(PdfPrinterService.createDocument(rechnung), "Generated PDF document should not be null");

        // Extract text from PDF
        try{
            document = PDDocument.load(rechnung.getGenerierteRechnung());
            assertNotNull(document, "Reopened PDF document should not be null");

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String pdfContent = pdfStripper.getText(document);

            // Assertions
            String todayDate = formatter.format(new Date());

            assertTrue(pdfContent.contains("Rechnung"), "PDF should contain title 'Rechnung'");
            assertTrue(pdfContent.contains("Rechnungsnummer: 12345"), "PDF should contain invoice number");
            assertTrue(pdfContent.contains("Bestellnummer: 98765"), "PDF should contain order number");
            assertTrue(pdfContent.contains("Belegdatum: " + todayDate), "PDF should contain date of bill creation");
            assertTrue(pdfContent.contains("This is a test invoice."), "PDF should contain pre-text");
            assertTrue(pdfContent.contains("Test Product"), "PDF should contain product name");
            assertTrue(pdfContent.contains("2"), "PDF should contain quantity");
            assertTrue(pdfContent.contains("STUECK"), "PDF should contain unit");
            assertTrue(pdfContent.contains("5,00 €"), "PDF should contain net single product price");
            assertTrue(pdfContent.contains("Leistungszeitraum: " + formatter.format(rechnung.getLeistungVon()) + " bis " + formatter.format(rechnung.getLeistungBis())),
            "PDF should contain service period");
            assertTrue(pdfContent.contains("Summe netto"), "PDF should contain total net price label");
            assertTrue(pdfContent.contains("10,00 €"), "PDF should contain total net price");
            assertTrue(pdfContent.contains("Umsatzsteuer"), "PDF should contain tax label");
            assertTrue(pdfContent.contains("1,90 €"), "PDF should contain tax");
            assertTrue(pdfContent.contains("Rechnungssumme brutto"), "PDF should contain total label");
            assertTrue(pdfContent.contains("11,90 €"), "PDF should contain total price");
            assertTrue(pdfContent.contains("Thank you for your business."), "PDF should contain post-text");
        } finally {
            // Clean up resources
            document.close();
        }
    }
}
