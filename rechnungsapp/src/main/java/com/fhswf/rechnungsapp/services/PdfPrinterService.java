package com.fhswf.rechnungsapp.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.fhswf.rechnungsapp.models.Position;
import com.fhswf.rechnungsapp.models.Rechnung;

import lombok.Data;

/*
 * Diese Service-Klasse ist für das Erstellen von PDF-Dokumenten zuständig. 
 * Sie verwendet die Apache PDFBox-Bibliothek, um ein PDF-Dokument zu erstellen,
 * das eine Rechnung darstellt. 
 * Dabei wird ein Rechnungsobjekt übergeben, das die notwendigen Informationen enthält.
 * 
 */

@Service @Data
public class PdfPrinterService {

    private static PDDocument document;
    private static PDPageContentStream contentStream;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
    private static float startX; // Left margin
    private static float startY; // Top of the page
    private static float rowHeight; // Space between rows
    private static PDType1Font font;
    private static PDType1Font fontBold;
    private static float fontSize;
    private static float tableFontSize;
    private static float[] columnPositions;
    private static String[] columnHeaders;
    private static float marginBottomTable;
    private static float marginBottomText;

    // Initialisiert die Standard-Attribute für die PDF-Erstellung
    private static void initAttributes() {
        startX = 50;
        startY = 700;
        rowHeight = 15;
        font = PDType1Font.HELVETICA;
        fontBold = PDType1Font.HELVETICA_BOLD;
        fontSize = 12;
        tableFontSize = 10;
        columnPositions = new float[]{50, 80, 220, 280, 330, 390, 470};
        columnHeaders = new String[]{"Pos.", "Bezeichnung", "USt", "Menge", "Einheit", "Netto/Einzel", "Gesamt netto"};
        marginBottomTable = 50;
        marginBottomText = 150;
    }
    
    // Diese Methode ist der Einstiegspunkt für die Erstellung eines PDF-Dokuments.
    // Sie erstellt ein neues PDF-Dokument und fügt die notwendigen Inhalte hinzu.
    // Dabei deligiert sie die Erstellung der einzelnen Bestandteile an spezielle Methoden.
    // Das fertige PDF-Dokument wird in einem ByteArrayOutputStream gespeichert, um diesen in der Rechnung zu speichern.
    // Die Methode ist auh für die abschließende Bereinigung zuständig.
    public static PDDocument createDocument(Rechnung rechnung) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        initAttributes();
        try {
            document = new PDDocument();
            addPage();
            configureContentStream(rowHeight, startX, startY, fontBold, fontSize);
    
            // Title
            contentStream.showText("Rechnung"); 
            addNewLine();
    
            // Rechnungsnummer, Bestellnummer, Belegdatum
            setFont(font, fontSize);
            contentStream.showText("Rechnungsnummer: " + rechnung.getNummer());
            addNewLine();
            contentStream.showText("Bestellnummer: " + rechnung.getBestellNummer());
            addNewLine();
    
            // Format today's date
            String todayDate = formatter.format(new Date());
            contentStream.showText("Belegdatum: " + todayDate);
            addNewLine();
            addNewLine();
    
            // Pretext
            parseText(rechnung.getPreText());
            addNewLine();

            // Positionen
            parsePositionTable(rechnung);
    
            // Leistungszeitraum
            setFont(font, fontSize);
            checkPageBreak(marginBottomTable);
            contentStream.showText("Leistungszeitraum: " + formatter.format(rechnung.getLeistungVon()) + " bis " + formatter.format(rechnung.getLeistungBis()));
            parseSeperator(font, fontSize);
    
            // Summe netto
            double gesamtNettoInEuro = rechnung.getGesamtNettoInCent() / 100.0;
            checkPageBreak(marginBottomTable);
            contentStream.showText("Summe netto");
            contentStream.newLineAtOffset(columnPositions[6] - startX, 0);
            contentStream.showText(String.format("%.2f", gesamtNettoInEuro) + " €");
            contentStream.newLineAtOffset(-columnPositions[6] + startX, 0);
            parseSeperator(font, fontSize);
    
            // Umsatzsteuer
            checkPageBreak(marginBottomTable);
            rechnung.setUmsatzSteuerInCent(Math.round(rechnung.getGesamtNettoInCent() * (rechnung.getUmsatzsteuer().getSatz() / 100)));
            double umsatzSteuerInEuro = rechnung.getUmsatzSteuerInCent() / 100.0;
            contentStream.showText("Umsatzsteuer");
            contentStream.newLineAtOffset(columnPositions[6] - startX, 0);
            contentStream.showText(String.format("%.2f", umsatzSteuerInEuro) + " €");
            contentStream.newLineAtOffset(-columnPositions[6] + startX, 0);
            parseSeperator(font, fontSize);
    
            // Rechnungssumme brutto
            checkPageBreak(marginBottomTable);
            rechnung.setGesamtBruttoInCent(rechnung.getGesamtNettoInCent() + rechnung.getUmsatzSteuerInCent());
            double gesamtBruttoInEuro = rechnung.getGesamtBruttoInCent() / 100.0;
            contentStream.showText("Rechnungssumme brutto");
            contentStream.newLineAtOffset(columnPositions[6] - startX, 0);
            contentStream.showText(String.format("%.2f", gesamtBruttoInEuro) + " €");
            contentStream.newLineAtOffset(-columnPositions[6] + startX, 0);
            parseSeperator(font, fontSize);
            addNewLine();
    
            // Footer text
            checkPageBreak(marginBottomText);
            parseText(rechnung.getPostText());
    
            // Schließe ContentStream und speichere das Dokument
            contentStream.endText();
            contentStream.close();

            document.save(byteArrayOutputStream);
            rechnung.setGenerierteRechnung(byteArrayOutputStream.toByteArray());
            return document;
        } finally {
            // Bereinige Ressourcen
            document.close();
            byteArrayOutputStream.close();
        }
    }

    // Erstellt eine neue Seite und initialisiert den ContentStream
    private static void addPage() throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);
        contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
    }

    // Konfiguriert den ContentStream für die Erstellung von Text mit der angegebenen Schriftart und Schriftgröße
    // und setzt den Textcursor an die angegebene Position auf der Seite
    private static void configureContentStream(float leading, float newLineAtXOffset, float newLineAtYOffset, PDType1Font font, float fontSize) throws IOException {
        contentStream.setLeading(leading);
        contentStream.newLineAtOffset(newLineAtXOffset, newLineAtYOffset);
        setFont(font, fontSize);
    }

    // Setzt die Schriftart und Schriftgröße für den ContentStream auf die angegebenen Werte
    private static void setFont(PDType1Font font, float fontSize) throws IOException {
        contentStream.setFont(font, fontSize);
    }

    // Parst den übergebenen Text und fügt ihn dem PDF-Dokument hinzu
    // Dabei wird der Text anhand von Zeilenumbrüchen in einzelne Zeilen aufgeteilt
    private static void parseText(String text) throws IOException {
        String[] textLines = text.split("\n");
        for (String line : textLines) {
            contentStream.showText(line);
            addNewLine();
        }
    }

    // Parst die Positionen der Rechnung und fügt sie als Tabelle dem PDF-Dokument hinzu
    private static void parsePositionTable(Rechnung rechnung) throws IOException {
        int positionNumber = 1;
        int gesamtNettoInCent = 0;

        // Table header
        setFont(fontBold, fontSize);
        parsePositionLine(columnHeaders);
        parseSeperator(font, tableFontSize);

        // Add table rows for each Position
        for (Position position : rechnung.getPositionen()) {
            checkPageBreak(marginBottomTable);
            gesamtNettoInCent += position.getGesamtpreisNettoInCent();
            double netPrice = position.getNettoEinzelpreisInCent() / 100.0;
            double totalPrice = position.getGesamtpreisNettoInCent() / 100.0;

            // Fügt eine Zeile in der Tabelle hinzu
            parsePositionLine(
                String.valueOf(positionNumber++), 
                position.getBezeichnung(), 
                rechnung.getUmsatzsteuer().getSatz().toString() + "%", 
                String.valueOf(position.getMenge()), 
                position.getEinheit().toString(), 
                String.format("%.2f", netPrice) + " €", 
                String.format("%.2f", totalPrice) + " €");
            parseSeperator(font, tableFontSize); // Fügt eine Trennlinie hinzu
        }
        rechnung.setGesamtNettoInCent(gesamtNettoInCent);
    }

    // Fügt eine Zeile in der Tabelle hinzu und setzt die Positionen der einzelnen Spalten
    // anhand der übergebenen Inhalte (Strings)
    private static void parsePositionLine(String... content) throws IOException {
        contentStream.newLineAtOffset(columnPositions[0] - startX, 0);
        contentStream.showText(content[0]);

        contentStream.newLineAtOffset(columnPositions[1] - columnPositions[0], 0);
        contentStream.showText(content[1]);

        contentStream.newLineAtOffset(columnPositions[2] - columnPositions[1], 0);
        contentStream.showText(content[2]);

        contentStream.newLineAtOffset(columnPositions[3] - columnPositions[2], 0);
        contentStream.showText(content[3]);

        contentStream.newLineAtOffset(columnPositions[4] - columnPositions[3], 0);
        contentStream.showText(content[4]);

        contentStream.newLineAtOffset(columnPositions[5] - columnPositions[4], 0);
        contentStream.showText(content[5]);

        contentStream.newLineAtOffset(columnPositions[6] - columnPositions[5], 0);
        contentStream.showText(content[6]);

        contentStream.newLineAtOffset(-columnPositions[6] + startX, 0);    
    }
    
    // Fügt eine Trennlinie in das PDF-Dokument ein und setzt die Schriftart und Schriftgröße zurück.
    // Dadurch ist die Schriftart und Schriftgröße der Trennlinie konsistent mit dem Rest des Dokuments.
    private static void parseSeperator(PDType1Font font, float fontSize) throws IOException {
        String separator = "-------------------------------------------------------------------------------------------------------------------------------";
        setFont(PDType1Font.HELVETICA, 12);
        addNewLine();
        contentStream.showText(separator);
        addNewLine();
        setFont(font, fontSize);
    }

    // Überprüft, ob ein Seitenumbruch notwendig ist und fügt ggf. eine neue Seite hinzu
    private static void checkPageBreak(float marginBottom) throws IOException {
        // Wenn der Text am unteren Rand der Seite angekommen ist, füge eine neue Seite hinzu
        if (startY <= marginBottom) {
            contentStream.endText();
            contentStream.close();
            addPage();
            startY = 700; // Reset to top of the new page
            configureContentStream(rowHeight, startX, startY, font, fontSize);
        }
    }

    // Fügt eine neue Zeile in das PDF-Dokument ein und aktualisiert die Y-Position.
    // Die Position wird verwendet, um den Textcursor an die richtige Stelle zu setzen und
    // sicherzustellen, dass der Text nicht über den unteren Rand der Seite hinausgeht.
    private static void addNewLine() throws IOException {
        contentStream.newLine();
        startY -= rowHeight;
    }
}
