package com.fhswf.rechnungsapp.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EinheitETTest {
    @Test
    public void testEnumValues() {
        // Test if the enum values are correctly defined
        assertEquals(3, EinheitET.values().length);  // Should be 3 values (STUNDE, PT, STUECK)
        assertTrue(EinheitET.STUNDE instanceof EinheitET);
        assertTrue(EinheitET.PT instanceof EinheitET);
        assertTrue(EinheitET.STUECK instanceof EinheitET);
    }

    @Test
    public void testEnumValueOf() {
        // Test if valueOf correctly retrieves an enum based on its name
        assertEquals(EinheitET.STUNDE, EinheitET.valueOf("STUNDE"));
        assertEquals(EinheitET.PT, EinheitET.valueOf("PT"));
        assertEquals(EinheitET.STUECK, EinheitET.valueOf("STUECK"));
    }

    @Test
    public void testEnumComparison() {
        // Test equality comparison
        assertEquals(EinheitET.STUNDE, EinheitET.STUNDE);
        assertNotEquals(EinheitET.STUNDE, EinheitET.PT);
    }

    @Test
    public void testEnumOrdinality() {
        // Test ordinal value (index of enum value)
        assertEquals(0, EinheitET.STUNDE.ordinal());    // STUNDE is the first value, so its ordinal is 0
        assertEquals(1, EinheitET.PT.ordinal());        // PT is the second value, so its ordinal is 1
        assertEquals(2, EinheitET.STUECK.ordinal());    // STUECK is the third value, so its ordinal is 2
    }

    @Test
    public void testEnumName() {
        // Test that name() method returns the correct name of the enum constant
        assertEquals("STUNDE", EinheitET.STUNDE.name());
        assertEquals("PT", EinheitET.PT.name());
        assertEquals("STUECK", EinheitET.STUECK.name());
    }
}
