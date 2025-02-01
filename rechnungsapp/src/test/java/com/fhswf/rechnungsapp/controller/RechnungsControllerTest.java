package com.fhswf.rechnungsapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhswf.rechnungsapp.models.Ansprechpartner;
import com.fhswf.rechnungsapp.models.Geschaeftspartner;
import com.fhswf.rechnungsapp.models.Position;
import com.fhswf.rechnungsapp.models.Rechnung;
import com.fhswf.rechnungsapp.models.Umsatzsteuer;
import com.fhswf.rechnungsapp.services.RechnungService;

public class RechnungsControllerTest {
    
    private MockMvc mockMvc;

    @Mock
    private RechnungService rechnungService;

    @InjectMocks
    private RechnungController rechnungController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rechnungController).build();
    }

    private Rechnung createRechnung() {
        Umsatzsteuer mockUmsatzsteuer = Mockito.mock(Umsatzsteuer.class);
        Geschaeftspartner mockGeschaeftspartner = Mockito.mock(Geschaeftspartner.class);
        Ansprechpartner mockAnsprechpartner = Mockito.mock(Ansprechpartner.class);

        Position mockPosition = Mockito.mock(Position.class);
        List<Position> mockPositionen = new ArrayList<>();
        mockPositionen.add(mockPosition);

        Date leistungVon = new Date();
        Date leistungBis = new Date();
        
        Rechnung rechnung = new Rechnung(
            "R001", 
            "Invoice 1", 
            true, 
            "Pre-text", 
            "Post-text", 
            leistungVon, 
            leistungBis, 
            "B123", 
            mockUmsatzsteuer, 
            mockPositionen, 
            mockGeschaeftspartner, 
            mockAnsprechpartner
        );
        rechnung.setId(123L);
        return rechnung;
    }

    @Test
    void testPostRechnung() throws Exception {
        Rechnung rechnung = createRechnung();
        when(rechnungService.saveRechnung(any(Rechnung.class))).thenReturn(rechnung);

        mockMvc.perform(post("/api/rechnung")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(rechnung)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(123))
            .andExpect(jsonPath("$.nummer").value("R001"))
            .andExpect(jsonPath("$.bezeichnung").value("Invoice 1"))
            .andExpect(jsonPath("$.perMail").value(true))
            .andExpect(jsonPath("$.preText").value("Pre-text"))
            .andExpect(jsonPath("$.postText").value("Post-text"))
            .andExpect(jsonPath("$.leistungVon").exists())
            .andExpect(jsonPath("$.leistungBis").exists())
            .andExpect(jsonPath("$.bestellNummer").value("B123"))
            .andExpect(jsonPath("$.umsatzsteuer").isNotEmpty())
            .andExpect(jsonPath("$.positionen").isNotEmpty())
            .andExpect(jsonPath("$.ansprechpartner").isNotEmpty());
    }

    @Test
    void testPostRechnungBadRequest() throws Exception {
        Rechnung rechnung = createRechnung();
        when(rechnungService.saveRechnung(any(Rechnung.class))).thenThrow(new Exception());

        mockMvc.perform(post("/api/rechnung")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(rechnung)))
            .andExpect(status().isBadRequest());
    }
}
