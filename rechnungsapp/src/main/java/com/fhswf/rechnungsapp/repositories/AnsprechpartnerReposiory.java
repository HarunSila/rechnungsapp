package com.fhswf.rechnungsapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fhswf.rechnungsapp.models.Ansprechpartner;

public interface AnsprechpartnerReposiory extends JpaRepository<Ansprechpartner, Long> {
    
}
