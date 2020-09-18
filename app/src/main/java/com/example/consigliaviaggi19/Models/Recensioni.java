package com.example.consigliaviaggi19.Models;

public class Recensioni {
    private String testo;
    private String idAutore;
    private float voto;
    private String struttura;

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getIdAutore() {
        return idAutore;
    }

    public void setIdAutore(String idAutore) {
        this.idAutore = idAutore;
    }

    public String getStruttura() {
        return struttura;
    }

    public void setStruttura(String struttura) {
        this.struttura = struttura;
    }

    public float getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public Recensioni(){}

    public Recensioni(String testo,String Autore,String recensita,float valutazione){
       this.testo = testo;
       idAutore = Autore;
        struttura = recensita;
       voto = valutazione;
    }

}
