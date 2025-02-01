package com.fhswf.rechnungsapp.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fhswf.rechnungsapp.models.Geschaeftspartner;
import com.fhswf.rechnungsapp.models.Rechnung;

import java.util.List;

@DataJpaTest
public class RechnungRepositoryTest {

    @Autowired
    private RechnungRepository rechnungRepository;

    @Autowired
    private GeschaeftspartnerRepository geschaeftspartnerRepository;

    @Test
    void testFindAllByGeschaeftspartnerId() {
        Geschaeftspartner geschaeftspartner = new Geschaeftspartner();
        geschaeftspartner.setName("Partner 1");
        geschaeftspartner = geschaeftspartnerRepository.save(geschaeftspartner);
        
        Rechnung rechnung1 = new Rechnung();
        rechnung1.setGeschaeftspartner(geschaeftspartner);
        rechnungRepository.save(rechnung1);
        
        Rechnung rechnung2 = new Rechnung();
        rechnung2.setGeschaeftspartner(geschaeftspartner);
        rechnungRepository.save(rechnung2);

        List<Rechnung> result = rechnungRepository.findAllByGeschaeftspartnerId(geschaeftspartner.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    void testFindAllWithGeschaeftspartner() {
        Geschaeftspartner geschaeftspartner = new Geschaeftspartner();
        geschaeftspartner.setName("Partner 1");
        geschaeftspartner = geschaeftspartnerRepository.save(geschaeftspartner);
        
        Rechnung rechnung1 = new Rechnung();
        rechnung1.setGeschaeftspartner(geschaeftspartner);
        rechnungRepository.save(rechnung1);
        
        Rechnung rechnung2 = new Rechnung();
        rechnung2.setGeschaeftspartner(geschaeftspartner);
        rechnungRepository.save(rechnung2);

        List<Rechnung> result = rechnungRepository.findAllWithGeschaeftspartner();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getGeschaeftspartner()).isNotNull();
        assertThat(result.get(1).getGeschaeftspartner()).isNotNull();
    }
}

