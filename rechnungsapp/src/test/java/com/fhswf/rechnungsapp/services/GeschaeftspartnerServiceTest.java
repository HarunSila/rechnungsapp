package com.fhswf.rechnungsapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fhswf.rechnungsapp.models.AdresseT;
import com.fhswf.rechnungsapp.models.Geschaeftspartner;
import com.fhswf.rechnungsapp.models.Rechnung;
import com.fhswf.rechnungsapp.repositories.GeschaeftspartnerRepository;
import com.fhswf.rechnungsapp.repositories.RechnungRepository;

public class GeschaeftspartnerServiceTest {
    
    @Mock
    private GeschaeftspartnerRepository geschaeftspartnerRepository;

    @Mock
    private RechnungRepository rechnungRepository;

    @InjectMocks
    private GeschaeftspartnerService geschaeftspartnerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Geschaeftspartner createGeschaeftspartner(Long id, String name, String beschreibung) {
        Geschaeftspartner geschaeftspartner = new Geschaeftspartner(
            name,
            beschreibung,
            new AdresseT("Teststraße", "1", "12345", "Teststadt", "Testland")
        );
        geschaeftspartner.setId(id);
        return geschaeftspartner;
    }

    @Test
    void testGetAllGeschaeftspartner() {
        Geschaeftspartner gp1 = createGeschaeftspartner(1L, "Test1", "Test1");
        Geschaeftspartner gp2 = createGeschaeftspartner(2L, "Test2", "Test2");

        when(geschaeftspartnerRepository.findAll()).thenReturn(List.of(gp1, gp2));

        List<Geschaeftspartner> result = geschaeftspartnerService.getAllGeschaeftspartner();

        assertEquals(2, result.size());
        assertEquals("Test1", result.get(0).getName());
        assertEquals("Test1", result.get(0).getBeschreibung());
        assertEquals("Test2", result.get(1).getName());
        assertEquals("Test2", result.get(1).getBeschreibung());
        verify(geschaeftspartnerRepository, times(1)).findAll();
    }

    @Test
    void testGetGeschaeftspartner_Found() {
        Geschaeftspartner gp = createGeschaeftspartner(1L, "Test1", "Test1");

        when(geschaeftspartnerRepository.findByIdWithRechnungen(1L)).thenReturn(Optional.of(gp));

        Geschaeftspartner result = geschaeftspartnerService.getGeschaeftspartner(1L);

        assertNotNull(result);
        assertEquals("Test1", result.getName());
        assertEquals("Test1", result.getBeschreibung());
        verify(geschaeftspartnerRepository, times(1)).findByIdWithRechnungen(1L);
    }

    @Test
    void testGetGeschaeftspartner_NotFound() {
        when(geschaeftspartnerRepository.findByIdWithRechnungen(1L)).thenReturn(Optional.empty());

        Geschaeftspartner result = geschaeftspartnerService.getGeschaeftspartner(1L);

        assertNull(result);
        verify(geschaeftspartnerRepository, times(1)).findByIdWithRechnungen(1L);
    }

    @Test
    void testSaveGeschaeftspartner_NewGeschaeftspartner() {
        Geschaeftspartner gp = createGeschaeftspartner(null, "Test1", "Test1");

        geschaeftspartnerService.saveGeschaeftspartner(gp);

        verify(rechnungRepository, never()).findAllByGeschaeftspartnerId(anyLong());
        verify(geschaeftspartnerRepository, times(1)).save(gp);
    }

    @Test
    void testSaveGeschaeftspartner_ExistingGeschaeftspartner() {
        Geschaeftspartner gp = createGeschaeftspartner(1L, "Test1", "Test1");
        Rechnung rechnung1 = new Rechnung();
        Rechnung rechnung2 = new Rechnung();

        when(rechnungRepository.findAllByGeschaeftspartnerId(1L)).thenReturn(List.of(rechnung1, rechnung2));

        geschaeftspartnerService.saveGeschaeftspartner(gp);

        assertEquals(2, gp.getRechnungen().size());
        verify(rechnungRepository, times(1)).findAllByGeschaeftspartnerId(1L);
        verify(geschaeftspartnerRepository, times(1)).save(gp);
    }

    @Test
    void testDeleteGeschaeftspartner_Found() {
        Geschaeftspartner gp = createGeschaeftspartner(1L, "Test1", "Test1");
        List<Rechnung> rechnungen = new ArrayList<>(List.of(new Rechnung(), new Rechnung()));
        gp.setRechnungen(rechnungen);

        when(geschaeftspartnerRepository.findById(1L)).thenReturn(Optional.of(gp));

        geschaeftspartnerService.deleteGeschaeftspartner(1L);

        assertTrue(gp.getRechnungen().isEmpty());
        verify(geschaeftspartnerRepository, times(1)).delete(gp);
    }

    @Test
    void testDeleteGeschaeftspartner_NotFound() {
        when(geschaeftspartnerRepository.findById(1L)).thenReturn(Optional.empty());

        geschaeftspartnerService.deleteGeschaeftspartner(1L);

        verify(geschaeftspartnerRepository, never()).delete(any());
    }
}
