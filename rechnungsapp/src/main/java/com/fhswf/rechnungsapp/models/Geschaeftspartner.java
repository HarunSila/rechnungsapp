package com.fhswf.rechnungsapp.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data @Entity
public class Geschaeftspartner {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String beschreibung;

    @Embedded
    private AdresseT anschrift;

    @OneToMany(mappedBy = "geschaeftspartner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Rechnung> rechnungen;

    public Geschaeftspartner() {
    }

    public Geschaeftspartner(String name, String beschreibung, AdresseT anschrift) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.anschrift = anschrift;
    }
}
