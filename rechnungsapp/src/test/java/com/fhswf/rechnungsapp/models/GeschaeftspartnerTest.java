package com.fhswf.rechnungsapp.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GeschaeftspartnerTest {

    Geschaeftspartner geschaeftspartner;
    AdresseT adresse;
    Rechnung rechnung1;
    Rechnung rechnung2;

    // create TestGeshaeftspartner for each test case
    @BeforeEach
    void setUp() {
        geschaeftspartner = new Geschaeftspartner();

        geschaeftspartner.setId(123L);
        geschaeftspartner.setName("Acme Corp.");
        geschaeftspartner.setBeschreibung("This is a description of Acme Corp.");
        
        adresse = new AdresseT();
        adresse.setStrasse("Main St.");
        adresse.setNummer("100");
        adresse.setPlz("98765");
        adresse.setOrt("Berlin");
        adresse.setLand("Germany");
        
        geschaeftspartner.setAnschrift(adresse);

        rechnung1 = new Rechnung();
        rechnung1.setId(1001L);
        rechnung1.setGesamtBruttoInCent(10050);
        
        rechnung2 = new Rechnung();
        rechnung2.setId(1002L);
        rechnung2.setGesamtBruttoInCent(20075);
        
        geschaeftspartner.setRechnungen(Arrays.asList(rechnung1, rechnung2));
    }
    
    @Test
    public void testConstructorAndGettersSetters() {
        assertEquals(123L, geschaeftspartner.getId());
        assertEquals("Acme Corp.", geschaeftspartner.getName());
        assertEquals("This is a description of Acme Corp.", geschaeftspartner.getBeschreibung());
        assertEquals(adresse, geschaeftspartner.getAnschrift());
    }

    @Test
    public void testRechnungenList() {
        assertNotNull(geschaeftspartner.getRechnungen());
        assertEquals(2, geschaeftspartner.getRechnungen().size());
        assertTrue(geschaeftspartner.getRechnungen().contains(rechnung1));
        assertTrue(geschaeftspartner.getRechnungen().contains(rechnung2));
    }
}
