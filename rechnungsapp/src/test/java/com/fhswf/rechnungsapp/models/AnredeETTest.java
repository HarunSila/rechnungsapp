package com.fhswf.rechnungsapp.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AnredeETTest {
    
     @Test
    public void testEnumValues() {
        // Test if the enum values are correctly defined
        assertEquals(3, AnredeET.values().length);  // Should be 3 values (HERR, FRAU, DIVERS)
        assertTrue(AnredeET.HERR instanceof AnredeET);
        assertTrue(AnredeET.FRAU instanceof AnredeET);
        assertTrue(AnredeET.DIVERS instanceof AnredeET);
    }

    @Test
    public void testEnumValueOf() {
        // Test if valueOf correctly retrieves an enum based on its name
        assertEquals(AnredeET.HERR, AnredeET.valueOf("HERR"));
        assertEquals(AnredeET.FRAU, AnredeET.valueOf("FRAU"));
        assertEquals(AnredeET.DIVERS, AnredeET.valueOf("DIVERS"));
    }

     @Test
    public void testEnumComparison() {
        // Test equality comparison
        assertEquals(AnredeET.HERR, AnredeET.HERR);
        assertNotEquals(AnredeET.HERR, AnredeET.FRAU);
    }

    @Test
    public void testEnumOrdinality() {
        // Test ordinal value (index of enum value)
        assertEquals(0, AnredeET.HERR.ordinal());    // HERR is the first value, so its ordinal is 0
        assertEquals(1, AnredeET.FRAU.ordinal());    // FRAU is the second value, so its ordinal is 1
        assertEquals(2, AnredeET.DIVERS.ordinal());  // DIVERS is the third value, so its ordinal is 2
    }

    @Test
    public void testEnumName() {
        // Test that name() method returns the correct name of the enum constant
        assertEquals("HERR", AnredeET.HERR.name());
        assertEquals("FRAU", AnredeET.FRAU.name());
        assertEquals("DIVERS", AnredeET.DIVERS.name());
    }
}
