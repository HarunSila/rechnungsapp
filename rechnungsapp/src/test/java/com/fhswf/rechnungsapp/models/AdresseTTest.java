package com.fhswf.rechnungsapp.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AdresseTTest {
    
    @Test
    public void testConstructorAndGettersSetters() {
        AdresseT adresse = new AdresseT();
        
        adresse.setStrasse("Main St.");
        adresse.setNummer("123");
        adresse.setPlz("12345");
        adresse.setOrt("Springfield");
        adresse.setLand("USA");
        
        assertEquals("Main St.", adresse.getStrasse());
        assertEquals("123", adresse.getNummer());
        assertEquals("12345", adresse.getPlz());
        assertEquals("Springfield", adresse.getOrt());
        assertEquals("USA", adresse.getLand());
    }
}
