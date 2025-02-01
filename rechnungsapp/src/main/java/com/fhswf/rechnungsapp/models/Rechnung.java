package com.fhswf.rechnungsapp.models;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fhswf.rechnungsapp.services.PdfPrinterService;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

/*
 * Die Klasse Rechnung stellt eine Rechnung dar. Sie enthält alle relevanten Informationen, die auf einer Rechnung stehen müssen.
 * 
 * Außerdem enthält sie Methoden, um die Gesamtsumme, die Umsatzsteuer und die Gesamtsumme inklusive Umsatzsteuer zu berechnen.
 * 
 * Die Methode printPDF() erstellt ein PDF-Dokument aus der Rechnung.
 */

@Data @Entity
public class Rechnung {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nummer;

    private String bezeichnung;

    private boolean perMail;

    @Column(length = 5000)
    private String preText;

    @Column(length = 5000)
    private String postText;

    @Temporal(TemporalType.DATE)
    private Date leistungVon;

    @Temporal(TemporalType.DATE)
    private Date leistungBis;

    private String bestellNummer;

    private int gesamtNettoInCent;

    private int umsatzSteuerInCent;

    private int gesamtBruttoInCent;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] generierteRechnung;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "umsatzsteuer_id", referencedColumnName = "id")
    private Umsatzsteuer umsatzsteuer;

    @OneToMany(mappedBy = "rechnung", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Position> positionen;

    @ManyToOne
    @JoinColumn(name = "geschaeftspartner_id", nullable = false)
    @JsonBackReference
    private Geschaeftspartner geschaeftspartner;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ansprechpartner_id", referencedColumnName = "id")
    private Ansprechpartner ansprechpartner;

    public Rechnung() {

    }

    public Rechnung(String nummer, String bezeichnung, boolean perMail, String preText, String postText, Date leistungVon, 
                    Date leistungBis, String bestellNummer, Umsatzsteuer umsatzsteuer, List<Position> positionen, Geschaeftspartner geschaeftspartner, Ansprechpartner ansprechpartner) {
        this.nummer = nummer;
        this.bezeichnung = bezeichnung;
        this.perMail = perMail;
        this.preText = preText;
        this.postText = postText;
        this.leistungVon = leistungVon;
        this.leistungBis = leistungBis;
        this.bestellNummer = bestellNummer;
        this.umsatzsteuer = umsatzsteuer;
        this.positionen = positionen;
        this.geschaeftspartner = geschaeftspartner;
        if(ansprechpartner != null) {
            this.ansprechpartner = ansprechpartner;
        }

        this.gesamtNettoInCent = calculateGesamtNettoInCent();
        this.umsatzSteuerInCent = calculateUmsatzSteuerInCent();
        this.gesamtBruttoInCent = calculateGesamtBruttoInCent();
    }

    // Kalkuliert die Gesamtsumme der Rechnung in Cent
    int calculateGesamtNettoInCent() {
        return positionen.stream().mapToInt(Position::getGesamtpreisNettoInCent).sum();
    }

    // Kalkuliert die Umsatzsteuer der Rechnung in Cent
    int calculateUmsatzSteuerInCent() {
        if (umsatzsteuer != null && umsatzsteuer.getSatz() != null) {
            return Math.round(gesamtNettoInCent * (umsatzsteuer.getSatz() / 100));
        }
        return 0;
    }

    // Kalkuliert die Gesamtsumme der Rechnung inklusive Umsatzsteuer in Cent
    int calculateGesamtBruttoInCent() {
        return gesamtNettoInCent + umsatzSteuerInCent;
    }

    // Erstellt ein PDF-Dokument aus der Rechnung. 
    // Dazu wird die Methode createDocument() aus der Klasse PdfPrinterService aufgerufen.
    // Diese Methode kann eine IOException werfen, wenn das PDF nicht erstellt werden kann.
    public void printPDF() throws IOException {
        try  {
            PdfPrinterService.createDocument(this);
        } catch (Exception e) {
            System.err.println("Error while creating PDF: " + e.getMessage());
            throw new IOException("Error while creating PDF", e);
        }
    }    
}