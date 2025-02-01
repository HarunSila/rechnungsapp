package com.fhswf.rechnungsapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fhswf.rechnungsapp.models.Geschaeftspartner;

/*
 * Da Geschaeftspartner und Rechnungen in einer 1:n Beziehung stehen, welche Lazuy geladen wird, 
 * wird hier eine spezielle Query benötigt, um die Rechnungen bei Bedarf mitzuladen.
 */

public interface GeschaeftspartnerRepository extends JpaRepository<Geschaeftspartner, Long> {
    
    // Lädt einen Geschäftspartner mit seinen Rechnungen anhand der ID aus der Datenbank
    @Query("SELECT g FROM Geschaeftspartner g LEFT JOIN FETCH g.rechnungen WHERE g.id = :id")
    Optional<Geschaeftspartner> findByIdWithRechnungen(@Param("id") Long id);

}
