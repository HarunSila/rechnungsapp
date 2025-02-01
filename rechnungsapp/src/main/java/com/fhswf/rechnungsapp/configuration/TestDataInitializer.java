package com.fhswf.rechnungsapp.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fhswf.rechnungsapp.models.AdresseT;
import com.fhswf.rechnungsapp.models.AnredeET;
import com.fhswf.rechnungsapp.models.Ansprechpartner;
import com.fhswf.rechnungsapp.models.EinheitET;
import com.fhswf.rechnungsapp.models.Geschaeftspartner;
import com.fhswf.rechnungsapp.models.Position;
import com.fhswf.rechnungsapp.models.Rechnung;
import com.fhswf.rechnungsapp.models.Umsatzsteuer;
import com.fhswf.rechnungsapp.repositories.GeschaeftspartnerRepository;
import com.fhswf.rechnungsapp.repositories.RechnungRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*
 * Diese Klasse initialisiert Testdaten für die Geschäftspartner und Rechnungen.
 * Dadurch können die Anwendung und die Datenbank mit Beispieldaten gefüllt werden, um die Funktionalität zu testen und zu demonstrieren.
 * 
 * Die Initialisierung erfolgt über die Methode initGeschaeftspartnerData, die eine Liste von Geschäftspartnern erstellt und in die Datenbank speichert.
 * 
 * Der Prozedur wird ein CommandLineRunner-Bean zugeordnet, das beim Start der Anwendung ausgeführt wird.
 */

@Configuration
public class TestDataInitializer {

    // Beim Start der Anwendung werden Testdaten für Geschäftspartner und Rechnungen initialisiert
    // und in die Datenbank gespeichert.
    @Bean
    CommandLineRunner initGeschaeftspartnerData(GeschaeftspartnerRepository repository, RechnungRepository rechnungRepository) {
        return args -> {
            List<Geschaeftspartner> geschaeftspartnerList = createGeschaeftspartnerList();
            repository.saveAll(geschaeftspartnerList);

            Geschaeftspartner geschaeftspartner = repository.findById(1L).orElse(null);
            List<Rechnung> rechnungen = createRechnungenList(geschaeftspartner);
            rechnungRepository.saveAll(rechnungen);
        };
    }

    // Erstellt eine Liste von Geschäftspartnern mit Beispieldaten.
    private List<Geschaeftspartner> createGeschaeftspartnerList() {
        return List.of(
            new Geschaeftspartner(
                "Müller GmbH", "Verlässlicher Lieferant für Büromaterialien.",
                new AdresseT("Hauptstraße", "12", "10115", "Berlin", "Deutschland")
            ),
            new Geschaeftspartner(
                "Schneider & Co. KG", "Langjähriger Partner im Bereich Maschinenbau.",
                new AdresseT("Industrieweg", "34", "50667", "Köln", "Deutschland")
            ),
            new Geschaeftspartner(
                "Autohaus Meier AG", "Spezialist für Firmenwagen und Fuhrparkmanagement.",
                new AdresseT("Werkstraße", "8", "80331", "München", "Deutschland")
            ),
            new Geschaeftspartner(
                "KreativDesign", "Agentur für Marketing und Grafikdesign.",
                new AdresseT("Kreativweg", "22", "20457", "Hamburg", "Deutschland")
            ),
            new Geschaeftspartner(
                "TechSolutions GmbH", "IT-Dienstleister für Softwareentwicklung und Beratung.",
                new AdresseT("Technologiepark", "7", "44137", "Dortmund", "Deutschland")
            ),
            new Geschaeftspartner(
                "Lindner Bauunternehmen", "Bauunternehmer mit Fokus auf nachhaltiges Bauen.",
                new AdresseT("Bauhofstraße", "11", "40210", "Düsseldorf", "Deutschland")
            ),
            new Geschaeftspartner(
                "Weber Handelshaus", "Großhändler für Konsumgüter.",
                new AdresseT("Marktplatz", "5", "23552", "Lübeck", "Deutschland")
            ),
            new Geschaeftspartner(
                "Dr. Krüger Consulting", "Beratungshaus für Unternehmensstrategie und Management.",
                new AdresseT("Beraterweg", "19", "04109", "Leipzig", "Deutschland")
            ),
            new Geschaeftspartner(
                "Schmidt Logistik", "Dienstleister für Transport und Logistiklösungen.",
                new AdresseT("Logistikstraße", "4", "89073", "Ulm", "Deutschland")
            ),
            new Geschaeftspartner(
                "Fischer Anlagenbau", "Hersteller von Spezialanlagen für die Industrie.",
                new AdresseT("Anlagenring", "29", "65185", "Wiesbaden", "Deutschland")
            ),
            new Geschaeftspartner(
                "Gruber Verlag", "Verlagshaus mit Fokus auf technische Literatur.",
                new AdresseT("Buchstraße", "10", "93047", "Regensburg", "Deutschland")
            ),
            new Geschaeftspartner(
                "Scholz Elektrotechnik", "Experte für Elektrolösungen in Wohn- und Gewerbegebäuden.",
                new AdresseT("Elektrostraße", "3", "60311", "Frankfurt am Main", "Deutschland")
            ),
            new Geschaeftspartner(
                "Hoffmann Immobilien", "Maklerbüro für Wohn- und Gewerbeimmobilien.",
                new AdresseT("Immobilienweg", "17", "30159", "Hannover", "Deutschland")
            ),
            new Geschaeftspartner(
                "Becker Metallbau", "Spezialist für Metallkonstruktionen und -bearbeitung.",
                new AdresseT("Metallring", "13", "97070", "Würzburg", "Deutschland")
            ),
            new Geschaeftspartner(
                "Schwarz Biotechnologie", "Innovatives Unternehmen in der Biotechnologiebranche.",
                new AdresseT("BioPark", "1", "93053", "Regensburg", "Deutschland")
            )
        );
    }

    // Erstellt eine Liste von Rechnungen für einen Geschäftspartner.
    // Die Rechnungen enthalten Beispieldaten für Ansprechpartner, Umsatzsteuer, Positionen und Beträge.
    // Die Rechnungen werden mit den Geschäftspartnern verknüpft.
    private List<Rechnung> createRechnungenList(Geschaeftspartner geschaeftspartner) {
        List<Rechnung> rechnungen = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Ansprechpartner ansprechpartner = new Ansprechpartner();
            ansprechpartner.setId(null);
            ansprechpartner.setName("Meier");
            ansprechpartner.setVorname("Hans");
            ansprechpartner.setEmail("hans.meier@mueller.de");
            ansprechpartner.setAnrede(AnredeET.HERR);
            ansprechpartner.setAnschrift(new AdresseT("Nebenstraße", "5", "10115", "Berlin", "Deutschland"));

            Umsatzsteuer umsatzsteuer = new Umsatzsteuer();
            umsatzsteuer.setName("Standard-Steuersatz");
            umsatzsteuer.setSatz(19.0f);

            Rechnung rechnung = new Rechnung();
            rechnung.setId(null);
            rechnung.setNummer("R-" + (1000 + i));
            rechnung.setBezeichnung("Rechnung Nr. " + (1000 + i));
            rechnung.setPerMail(false);
            rechnung.setPreText("Vielen Dank für Ihre Bestellung! Bitte überweisen Sie den Betrag innerhalb von 14 Tagen.");
            rechnung.setPostText("Mit freundlichen Grüßen,\nIhr Team der Müller GmbH");
            rechnung.setLeistungVon(new Date());
            rechnung.setLeistungBis(new Date()); 
            rechnung.setBestellNummer("B-" + (2000 + i));
            rechnung.setGesamtNettoInCent(50000 + i * 1000); // Example net amount
            rechnung.setUmsatzSteuerInCent(Math.round((50000 + i * 1000) * 0.19f));
            rechnung.setGesamtBruttoInCent(rechnung.getGesamtNettoInCent() + rechnung.getUmsatzSteuerInCent());
            rechnung.setUmsatzsteuer(umsatzsteuer);
            rechnung.setGeschaeftspartner(geschaeftspartner);
            rechnung.setAnsprechpartner(ansprechpartner);

            // Create Positions
            List<Position> positions = Arrays.asList(
                createPosition("Bürostuhl", 2, EinheitET.STUECK, 15000, rechnung),
                createPosition("Schreibtisch", 1, EinheitET.STUECK, 35000, rechnung)
            );
            rechnung.setPositionen(positions);
            rechnungen.add(rechnung);
        }
        return rechnungen;
    }

    private static Position createPosition(String bezeichnung, int menge, EinheitET einheit, int nettoEinzelpreisInCent, Rechnung rechnung) {
        Position position = new Position();
        position.setBezeichnung(bezeichnung);
        position.setMenge(menge);
        position.setEinheit(einheit);
        position.setNettoEinzelpreisInCent(nettoEinzelpreisInCent);
        position.setRechnung(rechnung);
        return position;
    }
}
