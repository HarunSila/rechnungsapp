package com.fhswf.rechnungsapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhswf.rechnungsapp.models.Geschaeftspartner;
import com.fhswf.rechnungsapp.services.GeschaeftspartnerService;

public class GeschaeftspartnerControllerTest {
    
    private MockMvc mockMvc;

    @Mock
    private GeschaeftspartnerService geschaeftspartnerService;

    @InjectMocks
    private GeschaeftspartnerController geschaeftspartnerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(geschaeftspartnerController).build();
    }

    private Geschaeftspartner createGeschaeftspartner(Long id, String name, String beschreibung) {
        Geschaeftspartner geschaeftspartner = new Geschaeftspartner();
        geschaeftspartner.setId(id);
        geschaeftspartner.setName(name);
        geschaeftspartner.setBeschreibung(beschreibung);
        return geschaeftspartner;
    }

    @Test
    void testGetAllGeschaeftspartner() throws Exception {
        List<Geschaeftspartner> geschaeftspartnerList =  Arrays.asList(
          createGeschaeftspartner(123L, "Acme Corp.", "This is a description of Acme Corp."),
            createGeschaeftspartner(456L, "Globex Corp.", "This is a description of Globex Corp.")
        );

        when(geschaeftspartnerService.getAllGeschaeftspartner()).thenReturn(geschaeftspartnerList);

        mockMvc.perform(get("/api/geschaeftspartner"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getGeschaeftspartner() throws Exception {
        Geschaeftspartner geschaeftspartner = createGeschaeftspartner(123L, "Acme Corp.", "This is a description of Acme Corp.");

        when(geschaeftspartnerService.getGeschaeftspartner(123L)).thenReturn(geschaeftspartner);

        mockMvc.perform(get("/api/geschaeftspartner/123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(123))
            .andExpect(jsonPath("$.name").value("Acme Corp."))
            .andExpect(jsonPath("$.beschreibung").value("This is a description of Acme Corp."));
    }

    @Test
    void testPostGeschaeftspartner() throws Exception {
        Geschaeftspartner geschaeftspartner = createGeschaeftspartner(123L, "Acme Corp.", "This is a description of Acme Corp.");

        doNothing().when(geschaeftspartnerService).saveGeschaeftspartner(any(Geschaeftspartner.class));

        mockMvc.perform(post("/api/geschaeftspartner")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(geschaeftspartner)))
                .andExpect(status().isOk());
    }

    @Test
    void testPutGeschaeftspartner() throws Exception {
        Geschaeftspartner geschaeftspartner = createGeschaeftspartner(123L, "Acme Corp.", "This is a description of Acme Corp.");
        doNothing().when(geschaeftspartnerService).saveGeschaeftspartner(any(Geschaeftspartner.class));

        mockMvc.perform(put("/api/geschaeftspartner/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(geschaeftspartner)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteGeschaeftspartner() throws Exception {
        doNothing().when(geschaeftspartnerService).deleteGeschaeftspartner(123L);

        mockMvc.perform(delete("/api/geschaeftspartner/123"))
                .andExpect(status().isOk());
    }
}
