package com.fhswf.rechnungsapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fhswf.rechnungsapp.models.Geschaeftspartner;
import com.fhswf.rechnungsapp.services.GeschaeftspartnerService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

/*
 * Controller für Geschaeftspartner mit den REST-Methoden GET, POST, PUT und DELETE
 * 
 * @RestController: Klasse ist ein Controller für REST-Requests und gibt JSON-Objekte zurück.
 * 
 * @RequestMapping: Mapping für die URL, die die Methoden des Controllers aufruft. 
 */

@RestController @RequestMapping("/api")
public class GeschaeftspartnerController {

    private final GeschaeftspartnerService geschaeftspartnerService;

    public GeschaeftspartnerController(GeschaeftspartnerService geschaeftspartnerService) {
        this.geschaeftspartnerService = geschaeftspartnerService;
    }

    @GetMapping("/geschaeftspartner")
    public List<Geschaeftspartner> getAllGeschaeftspartner() {
        return geschaeftspartnerService.getAllGeschaeftspartner();
    }

    @GetMapping("/geschaeftspartner/{id}")
    public Geschaeftspartner getGeschaeftspartner(@PathVariable Long id) {
        return geschaeftspartnerService.getGeschaeftspartner(id);
    }
    

    @PostMapping("/geschaeftspartner")
    public ResponseEntity<Void> postGeschaeftspartner(@RequestBody Geschaeftspartner geschaeftspartner) {
        this.geschaeftspartnerService.saveGeschaeftspartner(geschaeftspartner);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/geschaeftspartner/{id}")
    public ResponseEntity<Void> putGeschaeftspartner(@PathVariable Long id ,@RequestBody Geschaeftspartner geschaeftspartner) {
        this.geschaeftspartnerService.saveGeschaeftspartner(geschaeftspartner);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/geschaeftspartner/{id}")
    public ResponseEntity<Void> deleteGeschaeftspartner(@PathVariable Long id) {
        this.geschaeftspartnerService.deleteGeschaeftspartner(id);
        return ResponseEntity.ok().build();
    }
}
