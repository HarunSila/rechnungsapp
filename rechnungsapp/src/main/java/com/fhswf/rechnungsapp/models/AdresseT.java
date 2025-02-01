package com.fhswf.rechnungsapp.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data @Embeddable
public class AdresseT {
    private String strasse;
    private String nummer;
    private String plz;
    private String ort;
    private String land;

    public AdresseT() {
    }

    public AdresseT(String strasse, String nummer, String plz, String ort, String land) {
        this.strasse = strasse;
        this.nummer = nummer;
        this.plz = plz;
        this.ort = ort;
        this.land = land;
    }
}
