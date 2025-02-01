package com.fhswf.rechnungsapp.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fhswf.rechnungsapp.models.Geschaeftspartner;
import com.fhswf.rechnungsapp.models.Rechnung;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class GeschaeftspartnerRepositoryTest {

    @Autowired
    private GeschaeftspartnerRepository geschaeftspartnerRepository;

    @Test
    void testFindByIdWithRechnungen() {
        Geschaeftspartner geschaeftspartner = new Geschaeftspartner();
        geschaeftspartner.setName("Partner 1");
        
        Rechnung rechnung1 = new Rechnung();
        rechnung1.setNummer("Test_1234");
        rechnung1.setGeschaeftspartner(geschaeftspartner);

        Rechnung rechnung2 = new Rechnung();
        rechnung2.setNummer("Test_4567");
        rechnung2.setGeschaeftspartner(geschaeftspartner);

        geschaeftspartner.setRechnungen(List.of(rechnung1, rechnung2));

        geschaeftspartner = geschaeftspartnerRepository.save(geschaeftspartner);

        Geschaeftspartner result = geschaeftspartnerRepository.findByIdWithRechnungen(geschaeftspartner.getId()).orElse(null);

        assertThat(geschaeftspartner.getId()).isNotNull();
        assertThat(result).isNotNull();
        assertThat(result.getRechnungen()).isNotNull();
        assertThat(result.getRechnungen()).hasSize(2);
    }

    @Test
    void testFindByIdWithNoRechnungen() {
        Geschaeftspartner geschaeftspartner = new Geschaeftspartner();
        geschaeftspartner.setName("Partner 2");
        geschaeftspartner = geschaeftspartnerRepository.save(geschaeftspartner);

        Optional<Geschaeftspartner> result = geschaeftspartnerRepository.findByIdWithRechnungen(geschaeftspartner.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getRechnungen()).isNull();
    }
}

