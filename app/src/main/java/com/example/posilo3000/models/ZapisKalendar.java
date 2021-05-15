package com.example.posilo3000.models;

import java.util.List;

public class ZapisKalendar {

    private String den;
    private String cas;
    private Cvik cvik;

    public ZapisKalendar(String den, String cas, Cvik cvik) {
        this.den = den;
        this.cas = cas;
        this.cvik = cvik;
    }

    public String getDen() {
        return den;
    }

    public void setDen(String den) {
        this.den = den;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public Cvik getCvik() {
        return cvik;
    }

    public void setCvik(Cvik cvik) {
        this.cvik = cvik;
    }
}
