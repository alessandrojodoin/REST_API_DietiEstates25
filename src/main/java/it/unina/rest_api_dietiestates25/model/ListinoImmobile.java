package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class ListinoImmobile {

    @Id
    private int id;

    @OneToOne(optional = false)
    @MapsId
    private Immobile immobile;

    private int numeroVisualizzazioni=0;

    @NotNull
    private String tipologiaContratto;

    @NotNull
    private int speseCondominiali;

    @OneToMany(mappedBy = "listino")
    private Set<Offerta> offerte;

    private int prezzo;

    //private ArrayList<> prenotazioni;
    @ManyToOne(fetch = FetchType.LAZY)
    private AgenteImmobiliare creatore;

    private boolean isVenduto = false;



    private Instant istanteCreazione;

    public Immobile getImmobile() {return immobile; }
    public void setImmobile(Immobile immobile) {this.immobile = immobile; }

    public int getId() {return id;}

    public int getNumeroVisualizzazioni() {return numeroVisualizzazioni; }
    public void setNumeroVisualizzazioni(int numeroVisualizzazioni) {this.numeroVisualizzazioni = numeroVisualizzazioni; }

    public String getTipologiaContratto() {return tipologiaContratto; }
    public void setTipologiaContratto(String tipologiaContratto) {this.tipologiaContratto = tipologiaContratto; }

    public int getSpeseCondominiali() {return speseCondominiali; }
    public void setSpeseCondominiali(int speseCondominiali) {this.speseCondominiali = speseCondominiali; }

    public Set<Offerta> getOfferte() {return offerte; }
    public void setOfferte(Set<Offerta> offerte) {this.offerte = offerte; }

    public int getPrezzo() {return prezzo; }
    public void setPrezzo(int prezzo) {this.prezzo = prezzo; }

    public AgenteImmobiliare getCreatore() {return creatore; }
    public void setCreatore(AgenteImmobiliare creatore) {this.creatore = creatore; }

    public ListinoImmobile(){}

    public ListinoImmobile(Immobile immobile, String tipologiaContratto, int speseCondominiali, int prezzo,
                           AgenteImmobiliare creatore){
        this.immobile= immobile;
        this.tipologiaContratto= tipologiaContratto;
        this.speseCondominiali= speseCondominiali;
        this.prezzo= prezzo;
        this.creatore= creatore;
        this.istanteCreazione= Instant.now();
    }

    public boolean isVenduto() {return isVenduto;}
    public void setVenduto(boolean isVenduto) {this.isVenduto = isVenduto;}


    public Instant getIstanteCreazione() {
        return istanteCreazione;
    }
}
