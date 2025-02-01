package com.fhswf.rechnungsapp.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PositionTest {

    private Position position;
    private Rechnung mockRechnung;
    private String bezeichnung;
    private EinheitET einheit;
    private int menge;
    private int nettoEinzelpreisInCent;

    @BeforeEach
    void setUp() {
        position = new Position();

        bezeichnung = "Product A";
        position.setBezeichnung(bezeichnung);

        einheit = EinheitET.STUECK;
        position.setEinheit(einheit);

        mockRechnung = Mockito.mock(Rechnung.class);
        position.setRechnung(mockRechnung);

        menge = 10;
        position.setMenge(menge);

        nettoEinzelpreisInCent = 100;
        position.setNettoEinzelpreisInCent(nettoEinzelpreisInCent);
    }
    
    @Test
    public void testConstructor() {
        Position position = new Position(bezeichnung, menge, einheit, nettoEinzelpreisInCent);
        assertEquals(bezeichnung, position.getBezeichnung());
        assertEquals(menge, position.getMenge());
        assertEquals(einheit, position.getEinheit());
        assertEquals(nettoEinzelpreisInCent, position.getNettoEinzelpreisInCent());
    }

    @Test
    public void testGettersSetters() {
        assertEquals(bezeichnung, position.getBezeichnung());
        assertEquals(menge, position.getMenge());
        assertEquals(einheit, position.getEinheit());
        assertEquals(mockRechnung, position.getRechnung());
    }

    @Test
    public void testGetGesamtpreisNettoInCent() {
        int expectedGesamtpreisNetto = Math.round(menge * nettoEinzelpreisInCent);

        assertEquals(expectedGesamtpreisNetto, position.getGesamtpreisNettoInCent());
    }
}
