package com.example.buidemsl.util.objects.models;

public class Tipus {
    private long id;
    private String descripcio;
    private String color;

    public Tipus(long id, String descripcio, String color) {
        this.id = id;
        this.descripcio = descripcio;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
