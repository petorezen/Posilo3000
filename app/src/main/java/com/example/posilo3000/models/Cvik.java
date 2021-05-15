package com.example.posilo3000.models;

public class Cvik {

    private String nazov;
    private String kategoria;
    private String popis;
    private int cvikId;

    public Cvik(String nazov, String kategoria, String popis, int cvikId) {
        this.nazov = nazov;
        this.kategoria = kategoria;
        this.popis = popis;
        this.cvikId = cvikId;
    }


    public int getCvikId() {
        return cvikId;
    }

    public void setCvikId(int cvikId) {
        this.cvikId = cvikId;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }
}
