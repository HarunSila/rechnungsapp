package com.fhswf.rechnungsapp.services;


import java.util.List;

import org.springframework.stereotype.Service;

import com.fhswf.rechnungsapp.models.Geschaeftspartner;
import com.fhswf.rechnungsapp.models.Rechnung;
import com.fhswf.rechnungsapp.repositories.GeschaeftspartnerRepository;
import com.fhswf.rechnungsapp.repositories.RechnungRepository;

/*
 * Diese Service-Klasse stellt Methoden zur Verfügung, um auf die Geschaeftspartner-Repository zuzugreifen.
 * Sie enthält Methoden, um alle Geschaeftspartner zu erhalten, einen Geschaeftspartner zu erhalten, einen Geschaeftspartner zu speichern und einen Geschaeft
 * spartner zu löschen.
 * 
 * Beim Speichern und Löschen eines Geschaeftspartners werden auch die Rechnungen des Geschaeftspartners berücksichtigt.
 */

@Service
public class GeschaeftspartnerService {
    private final GeschaeftspartnerRepository geschaeftspartnerRepository;
    private final RechnungRepository rechnungRepository;

    public GeschaeftspartnerService(GeschaeftspartnerRepository geschaeftspartnerRepository, RechnungRepository rechnungRepository) {
        this.geschaeftspartnerRepository = geschaeftspartnerRepository;
        this.rechnungRepository = rechnungRepository;
    }

    public List<Geschaeftspartner> getAllGeschaeftspartner() {
        return geschaeftspartnerRepository.findAll();
    }

    public Geschaeftspartner getGeschaeftspartner(Long id) {
        return geschaeftspartnerRepository.findByIdWithRechnungen(id).orElse(null);
    }

    // Speichert einen Geschaeftspartner und berücksichtigt dabei auch die Rechnungen des Geschaeftspartners.
    // Wenn der Geschaeftspartner bereits existiert, werden die Rechnungen des Geschaeftspartners aktualisiert.
    // Wenn der Geschaeftspartner nicht existiert, wird er neu angelegt.
    public void saveGeschaeftspartner(Geschaeftspartner geschaeftspartner) {
        if(geschaeftspartner.getId() != null) {
            // Da Rechnungen Lazy geladen werden, müssen sie vor dem Löschen des Geschaeftspartners explizit geladen werden.
            List<Rechnung> rechnungen = rechnungRepository.findAllByGeschaeftspartnerId(geschaeftspartner.getId());
            geschaeftspartner.setRechnungen(rechnungen);
        }
        geschaeftspartnerRepository.save(geschaeftspartner);
    }

    // Löscht einen Geschaeftspartner und berücksichtigt dabei auch die Rechnungen des Geschaeftspartners.
    // Die Rechnungen des Geschaeftspartners werden ebenfalls gelöscht.
    public void deleteGeschaeftspartner(Long id) {
        Geschaeftspartner geschaeftspartner = geschaeftspartnerRepository.findById(id).orElse(null);
        if (geschaeftspartner != null) {
            geschaeftspartner.getRechnungen().clear();
            geschaeftspartnerRepository.delete(geschaeftspartner); 
        }
    }
}
