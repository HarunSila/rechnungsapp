package com.fhswf.rechnungsapp.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UmsatzsteuerTest {
    
    @Test
    public void testConstructorAndGettersSetters() {
        Umsatzsteuer umsatzsteuer = new Umsatzsteuer();
        
        umsatzsteuer.setName("MwSt 19%");
        umsatzsteuer.setSatz(19.0f);
        
        assertEquals("MwSt 19%", umsatzsteuer.getName());
        assertEquals(19.0f, umsatzsteuer.getSatz());
    }

    @Test
    public void testSettersAndGetters() {
        Umsatzsteuer umsatzsteuer = new Umsatzsteuer();
        
        umsatzsteuer.setName("MwSt 7%");
        umsatzsteuer.setSatz(7.0f);
        
        assertEquals("MwSt 7%", umsatzsteuer.getName());
        assertEquals(7.0f, umsatzsteuer.getSatz());
        
        umsatzsteuer.setName("MwSt 19%");
        umsatzsteuer.setSatz(19.0f);
        
        assertEquals("MwSt 19%", umsatzsteuer.getName());
        assertEquals(19.0f, umsatzsteuer.getSatz());
    }
}
