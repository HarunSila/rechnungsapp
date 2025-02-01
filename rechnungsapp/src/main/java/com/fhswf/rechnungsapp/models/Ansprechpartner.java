package com.fhswf.rechnungsapp.models;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data @Entity
public class Ansprechpartner {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String vorname;

    private String email;

    @Enumerated(EnumType.STRING)
    private AnredeET anrede;

    @Embedded
    private AdresseT anschrift;
}
