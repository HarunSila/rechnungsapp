package com.fhswf.rechnungsapp.controller;

import org.springframework.web.bind.annotation.RestController;

import com.fhswf.rechnungsapp.models.Rechnung;
import com.fhswf.rechnungsapp.services.RechnungService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Controller-Klasse für die Rechnung mit POST-Methode zum Speichern einer Rechnung.
 * 
 * @RestController: Controller-Klasse für RESTful-Service
 * 
 * @RequestMapping: Mapping der URL für den RESTful-Service
 * 
 */

@RestController @RequestMapping("/api")
public class RechnungController {
    private final RechnungService rechnungService;

    public RechnungController(RechnungService rechnungService) {
        this.rechnungService = rechnungService;
    }

    @PostMapping("/rechnung")
    public ResponseEntity<Rechnung> postRechnung(@RequestBody Rechnung rechnung) {
        try {
            Rechnung entity = rechnungService.saveRechnung(rechnung);
            return ResponseEntity.ok(entity);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.badRequest().build();
        }
    }
    
}
