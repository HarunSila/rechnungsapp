package com.fhswf.rechnungsapp.models;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RechnungTest {

    private Rechnung rechnung;
    private Date vonDate;
    private Date bisDate;
    private String preText;
    private String postText;
    private Umsatzsteuer umsatzsteuer;
    private List<Position> positionen;
    private Geschaeftspartner geschaeftspartner;
    private Ansprechpartner ansprechpartner;

    // create TestRechnung for each test case
    @BeforeEach
    public void setUp() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        vonDate = formatter.parse("12.04.2023");
        bisDate = formatter.parse("16.04.2023");

        preText = "Sehr geehrter Herr Mustermann,\nsehr geehrte Damen und Herren,\n\ngemäß Ihrer Bestellung 233003283 vom 12.04.2023 berechnen wir wie folgt:";
        postText = "Bitte überweisen Sie den Gesamtbetrag auf das unten agegebene Konto.\n\nMit freundlichen Grüßen,\nIhr Flansch-Team\nMuster GmbH";

        umsatzsteuer = new Umsatzsteuer();
        umsatzsteuer.setSatz(19f);

        positionen = List.of(
            new Position("Flansch-Beratung XXL", 1000, EinheitET.STUECK ,10),
            new Position("Flansch-Beratung XL", 2000, EinheitET.STUECK ,8)
        );

        geschaeftspartner = new Geschaeftspartner();
        ansprechpartner = new Ansprechpartner();

        rechnung = new Rechnung(
                "12345",
                "Flansch-Beratung",
                true,
                preText,
                postText,
                vonDate,
                bisDate,
                "98765",
                umsatzsteuer,
                positionen,
                geschaeftspartner,
                ansprechpartner
        );
    }

    @Test
    public void testConstructorAndGettersSetters() {
        assertEquals("12345", rechnung.getNummer());
        assertEquals("Flansch-Beratung", rechnung.getBezeichnung());
        assertTrue(rechnung.isPerMail());
        assertEquals(preText, rechnung.getPreText());
        assertEquals(postText, rechnung.getPostText());
        assertEquals(vonDate, rechnung.getLeistungVon());
        assertEquals(bisDate, rechnung.getLeistungBis());
        assertEquals("98765", rechnung.getBestellNummer());
        assertEquals(umsatzsteuer, rechnung.getUmsatzsteuer());
        assertEquals(positionen, rechnung.getPositionen());
        assertEquals(geschaeftspartner, rechnung.getGeschaeftspartner());
        assertEquals(ansprechpartner, rechnung.getAnsprechpartner());
    }

    @Test
    public void testCalculateGesamtNettoInCent() {        
        int expectedGesamtNettoInCent = 1000 * 10 + 2000 * 8;
        assertEquals(expectedGesamtNettoInCent, rechnung.calculateGesamtNettoInCent());
    }

    @Test
    public void testCalculateUmsatzSteuerInCent() {
        int gesamtNettoInCent = rechnung.calculateGesamtNettoInCent();
        rechnung.setGesamtNettoInCent(gesamtNettoInCent);
        
        int expectedUmsatzSteuerInCent = Math.round(gesamtNettoInCent * (19f / 100));
        assertEquals(expectedUmsatzSteuerInCent, rechnung.calculateUmsatzSteuerInCent());
    }

    @Test
    public void testCalculateGesamtBruttoInCent() {
        int gesamtNettoInCent = rechnung.calculateGesamtNettoInCent();
        int umsatzSteuerInCent = rechnung.calculateUmsatzSteuerInCent();

        rechnung.setGesamtNettoInCent(gesamtNettoInCent);
        rechnung.setUmsatzSteuerInCent(umsatzSteuerInCent);
        
        int expectedGesamtBruttoInCent = gesamtNettoInCent + umsatzSteuerInCent;
        assertEquals(expectedGesamtBruttoInCent, rechnung.calculateGesamtBruttoInCent());
    }

    @Test
    public void testGenerateAndSavePDF() throws IOException {
        String outputFilePath = "RechnungUnitTestOutput.pdf";
        rechnung.printPDF();
        PDDocument document = null;

        try {
            document = PDDocument.load(rechnung.getGenerierteRechnung());
            assertNotNull(document, "The generated PDF document should not be null.");

            File pdfFile = new File(outputFilePath);
            try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
                document.save(outputStream);
            } catch (IOException e) {
                fail("Failed to save the generated PDF: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Failed to load the generated PDF: " + e.getMessage());
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
