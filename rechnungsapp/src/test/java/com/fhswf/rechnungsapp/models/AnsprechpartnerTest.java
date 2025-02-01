package com.fhswf.rechnungsapp.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AnsprechpartnerTest {

    @Test
    public void testConstructorAndGettersSetters() {
        Ansprechpartner ansprechpartner = new Ansprechpartner();
        
        ansprechpartner.setId(1L);
        ansprechpartner.setName("Doe");
        ansprechpartner.setVorname("John");
        ansprechpartner.setEmail("john.doe@example.com");
        ansprechpartner.setAnrede(AnredeET.HERR);
        
        AdresseT adresse = new AdresseT();
        adresse.setStrasse("Main St.");
        adresse.setNummer("123");
        adresse.setPlz("12345");
        adresse.setOrt("Springfield");
        adresse.setLand("USA");
        
        ansprechpartner.setAnschrift(adresse);
        
        assertEquals(1L, ansprechpartner.getId());
        assertEquals("Doe", ansprechpartner.getName());
        assertEquals("John", ansprechpartner.getVorname());
        assertEquals("john.doe@example.com", ansprechpartner.getEmail());
        assertEquals(AnredeET.HERR, ansprechpartner.getAnrede());
        assertEquals(adresse, ansprechpartner.getAnschrift());
    }
}
