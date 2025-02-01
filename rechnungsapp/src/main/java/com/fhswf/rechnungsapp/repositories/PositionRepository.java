package com.fhswf.rechnungsapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fhswf.rechnungsapp.models.Position;

/*
 * Da die Positionen und Rechnungen eine 1:n Beziehung haben, welche LAZY geladen wird, wird hier eine Methode benötigt, um alle Positionen einer Rechnung zu laden
 * und keine getrennte Abfrage für jede Position zu machen, was zu einer schlechten Performance führen würde.
 */

public interface PositionRepository extends JpaRepository<Position, Long> {
    
    // Lädt alle Positionen einer Rechnung
    @Query("SELECT p FROM Position p WHERE p.rechnung.id = :rechnungId")
    List<Position> findAllByRechnungId(Long rechnungId);
}
