package com.fhswf.rechnungsapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data @Entity
public class Position {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bezeichnung;

    private int menge;

    @Enumerated(EnumType.STRING)
    private EinheitET einheit;
    
    private int nettoEinzelpreisInCent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rechnung_id", nullable = false)
    @JsonBackReference
    private Rechnung rechnung;

    public Position() {
    }

    public Position(String bezeichnung, int menge, EinheitET einheit, int nettoEinzelpreisInCent) {
        this.bezeichnung = bezeichnung;
        this.menge = menge;
        this.einheit = einheit;
        this.nettoEinzelpreisInCent = nettoEinzelpreisInCent;
    }

    public int getGesamtpreisNettoInCent() {
        return Math.round(menge * nettoEinzelpreisInCent);
    }
}
