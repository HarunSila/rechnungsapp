package com.fhswf.rechnungsapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fhswf.rechnungsapp.models.Umsatzsteuer;

public interface UmsatzsteuerRepository extends JpaRepository<Umsatzsteuer, Long> {
    
}
