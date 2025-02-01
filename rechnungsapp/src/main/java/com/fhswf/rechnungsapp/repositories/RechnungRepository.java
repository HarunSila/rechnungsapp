package com.fhswf.rechnungsapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fhswf.rechnungsapp.models.Rechnung;

/*
 * Da die Rechnungsklasse eine Beziehung zum Geschaeftspartner hat und diese LAZY geladen wird,
 * werden hier spezielle Queries definiert, die die Rechnungen inklusive der zugehoerigen Geschaeftspartner laden.
 */

public interface RechnungRepository extends JpaRepository<Rechnung, Long> {
    
    // Alle Rechnungen des Geschaeftspartners mit der ID partnerId laden
    @Query("SELECT r FROM Rechnung r JOIN FETCH r.geschaeftspartner WHERE r.geschaeftspartner.id = :partnerId")
    List<Rechnung> findAllByGeschaeftspartnerId(@Param("partnerId") Long partnerId);

    // Alle Rechnungen laden, inklusive der zugehoerigen Geschaeftspartner
    @Query("SELECT r FROM Rechnung r JOIN FETCH r.geschaeftspartner")
    List<Rechnung> findAllWithGeschaeftspartner();
}
