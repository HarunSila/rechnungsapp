package com.fhswf.rechnungsapp.services;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fhswf.rechnungsapp.models.Geschaeftspartner;
import com.fhswf.rechnungsapp.models.Position;
import com.fhswf.rechnungsapp.models.Rechnung;
import com.fhswf.rechnungsapp.repositories.GeschaeftspartnerRepository;
import com.fhswf.rechnungsapp.repositories.PositionRepository;
import com.fhswf.rechnungsapp.repositories.RechnungRepository;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.transaction.Transactional;

/*
 * Diese Service-Klasse stellt Methoden zur Verfügung, um Rechnungen zu speichern und zu versenden.
 * 
 */

@Service
public class RechnungService {
    
    private final RechnungRepository rechnungRepository;
    private final GeschaeftspartnerRepository geschaeftspartnerRepository;
    private final PositionRepository positionRepository;
    private final JavaMailSender emailSender;

    public RechnungService(
                            RechnungRepository rechnungRepository, 
                            GeschaeftspartnerRepository geschaeftspartnerRepository,
                            PositionRepository positionRepository,
                            JavaMailSender emailSender
                        ) {
        this.rechnungRepository = rechnungRepository;
        this.geschaeftspartnerRepository = geschaeftspartnerRepository;
        this.positionRepository = positionRepository;
        this.emailSender = emailSender;
    }

    // Da die Methode mehrere Datenbankoperationen durchführt, wird sie in einer Transaktion ausgeführt, 
    // sodass alle Operationen entweder erfolgreich oder fehlerhaft sind. Es stellt sicher, dass die Datenbank
    // in einem konsistenten Zustand bleibt.
    @Transactional
    public Rechnung saveRechnung(Rechnung rechnung) throws Exception { 
        Geschaeftspartner geschaeftspartner = rechnung.getGeschaeftspartner();

        // Wenn der Geschäftspartner noch nicht in der Datenbank gespeichert ist, wird er gespeichert.
        if (geschaeftspartner.getId() == 0) {
            geschaeftspartner.setId(null);
            geschaeftspartnerRepository.save(geschaeftspartner);
        }

        // Die Rechnung wird erstellt und in der Datenbank gespeichert.
        rechnung.printPDF();
        rechnungRepository.save(rechnung);

        // Die Positionen der Rechnung werden in der Datenbank gespeichert.
        List<Position> positionen = rechnung.getPositionen();
        for (Position p : positionen) {
            p.setRechnung(rechnung); 
        }
        this.positionRepository.saveAll(positionen);

        // Wenn die Rechnung per Mail versendet werden soll, wird sie an die Mailadresse des Ansprechpartners gesendet.
        if (rechnung.isPerMail()) {
            sendRechnungViaMail(rechnung);
        }
    
        return rechnung;
    }

    // Diese Methode sendet eine Rechnung per Mail an den Ansprechpartner des Geschäftspartners.
    public void sendRechnungViaMail(Rechnung rechnung) throws MessagingException {
        // Wenn der Ansprechpartner oder dessen Mailadresse nicht vorhanden sind, wird die Mail nicht versendet.
        if(rechnung.getAnsprechpartner() == null || rechnung.getAnsprechpartner().getEmail() == null || rechnung.getAnsprechpartner().getEmail().isEmpty()) {
            return;
        }
        // Die Mail wird mit dem PDF der Rechnung als Anhang versendet.
        String documentName = "Rechnung_" + rechnung.getNummer() + ".pdf";
        String recipent = rechnung.getAnsprechpartner().getEmail();
        String subject = "Rechnung " + rechnung.getNummer() + " zu Ihrer Bestellung " + rechnung.getBestellNummer();
        String text = "Sehr geehrte Damen und Herren,\n\nanbei erhalten Sie die Rechnung zu Ihrer Bestellung " + rechnung.getBestellNummer() + ".\n\n" + rechnung.getPostText();

        byte[] pdfBytes = rechnung.getGenerierteRechnung();

        // Die Mail wird mit dem PDF als Anhang versendet.
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipent);
        helper.setSubject(subject);
        helper.setText(text);

        DataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
        helper.addAttachment(documentName, dataSource);

        emailSender.send(message);
    }
}
