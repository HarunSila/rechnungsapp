package com.fhswf.rechnungsapp.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import com.fhswf.rechnungsapp.models.Ansprechpartner;
import com.fhswf.rechnungsapp.models.Geschaeftspartner;
import com.fhswf.rechnungsapp.models.Position;
import com.fhswf.rechnungsapp.models.Rechnung;
import com.fhswf.rechnungsapp.repositories.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class RechnungServiceTest {
    
    @Mock
    private RechnungRepository rechnungRepository;

    @Mock
    private GeschaeftspartnerRepository geschaeftspartnerRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private RechnungService rechnungService;

    private Rechnung mockRechnung;
    private Geschaeftspartner geschaeftspartner;
    private Position position;
    private Ansprechpartner ansprechpartner;

    // Crate and mock test data for test cases
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        geschaeftspartner = new Geschaeftspartner();
        geschaeftspartner.setId(0L); // New partner (should be saved)

        position = new Position();

        ansprechpartner = new Ansprechpartner();
        ansprechpartner.setEmail("test@example.com");

        mockRechnung = mock(Rechnung.class);

        when(mockRechnung.getGeschaeftspartner()).thenReturn(geschaeftspartner);
        when(mockRechnung.getPositionen()).thenReturn(List.of(position));
        when(mockRechnung.getAnsprechpartner()).thenReturn(ansprechpartner);
        when(mockRechnung.isPerMail()).thenReturn(false);
        when(mockRechnung.getNummer()).thenReturn("12345");
        when(mockRechnung.getBestellNummer()).thenReturn("98765");
        when(mockRechnung.getGenerierteRechnung()).thenReturn(new byte[]{1, 2, 3}); // Fake PDF
    }

    @Test
    void testSaveRechnung_NewGeschaeftspartner_ShouldSaveIt() throws Exception {
        Rechnung savedRechnung = rechnungService.saveRechnung(mockRechnung);

        verify(geschaeftspartnerRepository).save(geschaeftspartner);
        verify(rechnungRepository).save(mockRechnung);
        verify(positionRepository).saveAll(anyList());
        verify(mockRechnung).printPDF();
        assertNotNull(savedRechnung);
    }

    @Test
    void testSaveRechnung_ExistingGeschaeftspartner_ShouldNotSaveIt() throws Exception {
        geschaeftspartner.setId(10L); // Existing partner
        when(mockRechnung.getGeschaeftspartner()).thenReturn(geschaeftspartner);

        rechnungService.saveRechnung(mockRechnung);

        // Should not save existing partner
        verify(geschaeftspartnerRepository, never()).save(any());
    }

    @Test
    void testSaveRechnung_WithEmail_ShouldSendMail() throws Exception {
        when(mockRechnung.isPerMail()).thenReturn(true);

        MimeMessage mockMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mockMessage);

        rechnungService.saveRechnung(mockRechnung);

        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(emailSender).send(messageCaptor.capture());

        MimeMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage);
    }

    @Test
    void testSendRechnungViaMail_NoRecipient_ShouldNotSendEmail() throws MessagingException {
        when(mockRechnung.getAnsprechpartner()).thenReturn(null);

        rechnungService.sendRechnungViaMail(mockRechnung);

        verify(emailSender, never()).send(any(MimeMessage.class));
    }
}
