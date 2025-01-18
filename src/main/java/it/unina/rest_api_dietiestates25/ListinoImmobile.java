package it.unina.rest_api_dietiestates25;

import java.awt.*;
import java.util.ArrayList;

public class ListinoImmobile {
    private Immobile immobile;
    private int numeroVisualizzazioni;
    private String tipologia_contratto;
    private int speseCondominiali;
    private ArrayList<Offerta> offerte;
    private int prezzo;
    //private ArrayList<> prenotazioni;
    private AgenteImmobiliare agenteImmobiliare;
    private boolean venduto;

    public Immobile getImmobile() {return immobile; }
    public void setImmobile(Immobile immobile) {this.immobile = immobile; }

    public int getNumeroVisualizzazioni() {return numeroVisualizzazioni; }
    public void setNumeroVisualizzazioni(int numeroVisualizzazioni) {this.numeroVisualizzazioni = numeroVisualizzazioni; }

    public String getTipologia_contratto() {return tipologia_contratto; }
    public void setId(int id) {this.tipologia_contratto = tipologia_contratto; }

    public int getSpeseCondominiali() {return speseCondominiali; }
    public void setSpeseCondominiali(int speseCondominiali) {this.speseCondominiali = speseCondominiali; }

    public ArrayList<Offerta> getOfferte() {return offerte; }
    public void setOfferte(ArrayList<Offerta> offerte) {this.offerte = offerte; }

    public int getPrezzo() {return prezzo; }
    public void setPrezzo(int prezzo) {this.prezzo = prezzo; }

    public AgenteImmobiliare getAgenteImmobiliare() {return agenteImmobiliare; }
    public void setAgenteImmobiliare(AgenteImmobiliare agenteImmobiliare) {this.agenteImmobiliare = agenteImmobiliare; }


}
