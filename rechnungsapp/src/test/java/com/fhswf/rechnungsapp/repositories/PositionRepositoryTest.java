package com.fhswf.rechnungsapp.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fhswf.rechnungsapp.models.Geschaeftspartner;
import com.fhswf.rechnungsapp.models.Position;
import com.fhswf.rechnungsapp.models.Rechnung;

@DataJpaTest
public class PositionRepositoryTest {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private RechnungRepository rechnungRepository;

    @Autowired
    private GeschaeftspartnerRepository geschaeftspartnerRepository;

    @Test
    void testFindAllByRechnungId() {
        Rechnung rechnung = new Rechnung();
        rechnung.setNummer("Test_1234");

        Geschaeftspartner geschaeftspartner = new Geschaeftspartner();
        rechnung.setGeschaeftspartner(geschaeftspartner);

        Position position1 = new Position();
        position1.setBezeichnung("TEST_PROD_1");
        position1.setRechnung(rechnung);

        Position position2 = new Position();
        position2.setBezeichnung("TEST_PROD_2");
        position2.setRechnung(rechnung);

        rechnung.setPositionen(List.of(position1, position2));

        geschaeftspartner = geschaeftspartnerRepository.save(geschaeftspartner);
        assertThat(geschaeftspartner).isNotNull();

        rechnung = rechnungRepository.save(rechnung);
        assertThat(rechnung).isNotNull();

        List<Position> result = positionRepository.findAllByRechnungId(rechnung.getId());

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }
}
