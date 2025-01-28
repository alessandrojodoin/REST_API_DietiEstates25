package it.unina.rest_api_dietiestates25.model;

import it.unina.rest_api_dietiestates25.Offerta;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class ListinoImmobile {

    @Id
    private int id;

    @OneToOne(optional = false)
    @MapsId
    private Immobile immobile;

    private int numeroVisualizzazioni;

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

    private boolean venduto;

    private LocalDateTime istanteCreazione;

    public Immobile getImmobile() {return immobile; }
    public void setImmobile(Immobile immobile) {this.immobile = immobile; }

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


}
