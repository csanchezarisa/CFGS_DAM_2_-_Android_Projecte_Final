package com.example.buidemsl.util.objects.models;

public class Zona {
    private long id;
    private String descripcio;

    public Zona(long id, String descripcio) {
        this.id = id;
        this.descripcio = descripcio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }
}
