package com.example.buidemsl.util.objects.models;

import androidx.annotation.Nullable;

import com.example.buidemsl.util.objects.Date;

public class Maquina {
    private long id;
    private String adreca;
    private String codiPostal;
    private String poblacio;
    private String numeroSerie;
    private Date ultimaRevisio;
    private Client client;
    private Zona zona;
    private Tipus tipus;

    public Maquina(long id, String adreca, String codiPostal, String poblacio, String numeroSerie, @Nullable Date ultimaRevisio, @Nullable Client client, @Nullable Zona zona, @Nullable Tipus tipus) {
        this.id = id;
        this.adreca = adreca;
        this.codiPostal = codiPostal;
        this.poblacio = poblacio;
        this.numeroSerie = numeroSerie;
        if (ultimaRevisio != null)
            this.ultimaRevisio = ultimaRevisio;
        if (client != null)
            this.client = client;
        if (zona != null)
            this.zona = zona;
        if (tipus != null)
            this.tipus = tipus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAdreca() {
        return adreca;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

    public String getCodiPostal() {
        return codiPostal;
    }

    public void setCodiPostal(String codiPostal) {
        this.codiPostal = codiPostal;
    }

    public String getPoblacio() {
        return poblacio;
    }

    public void setPoblacio(String poblacio) {
        this.poblacio = poblacio;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Date getUltimaRevisio() {
        return ultimaRevisio;
    }

    public void setUltimaRevisio(Date ultimaRevisio) {
        this.ultimaRevisio = ultimaRevisio;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Tipus getTipus() {
        return tipus;
    }

    public void setTipus(Tipus tipus) {
        this.tipus = tipus;
    }

    public String getFullDirection() {
        return this.adreca + ", " + this.poblacio + ", " + this.codiPostal;
    }
}
