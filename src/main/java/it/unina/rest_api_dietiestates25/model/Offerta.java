package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
public class Offerta {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ListinoImmobile listino;

    @ManyToOne()
    private RiepilogoAttivita riepilogo;

    @NotNull
    private String emailOfferente;

    @NotNull
    String nome;

    @NotNull
    String cognome;

    @NotNull
    String telefono;

    @NotNull
    private RisultatoOfferta risultatoOfferta;

    private int cifraInCentesimi;

    private int cifraContropropostaInCentesimi;

    @NotNull
    private Instant istanteCreazione;


    public Offerta(){}
    public Offerta(ListinoImmobile listino, RiepilogoAttivita riepilogo, String emailOfferente, String nome,
                   String cognome, String telefono, RisultatoOfferta risultatoOfferta, int cifraInCentesimi,
                   int cifraContropropostaInCentesimi){
        this.listino= listino;
        this.riepilogo= riepilogo;
        this.emailOfferente= emailOfferente;
        this.nome= nome;
        this.cognome= cognome;
        this.telefono= telefono;
        this.risultatoOfferta= risultatoOfferta;
        this.cifraInCentesimi= cifraInCentesimi;
        this.cifraContropropostaInCentesimi= cifraContropropostaInCentesimi;
        this.istanteCreazione= Instant.now();
    }

    public int getId() {
        return id;
    }
    public String getEmailOfferente() {
        return emailOfferente;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getTelefono() {
        return telefono;
    }
    public RiepilogoAttivita getRiepilogo() {
        return riepilogo;
    }

    public int getCifraInCentesimi() {
        return cifraInCentesimi;
    }

    public int getCifraContropropostaInCentesimi() {
        return cifraContropropostaInCentesimi;
    }

    public Instant getIstanteCreazione() {
        return istanteCreazione;
    }

    public ListinoImmobile getListino(){return listino;}
    public void setListino(ListinoImmobile listino){this.listino= listino;}


    public RisultatoOfferta getRisultatoOfferta(){return risultatoOfferta;}
    public void setRisultatoOfferta(RisultatoOfferta risultatoOfferta){this.risultatoOfferta= risultatoOfferta;}

    public void setCifraContropropostaInCentesimi(int cifraContropropostaInCentesimi) {
        this.cifraContropropostaInCentesimi = cifraContropropostaInCentesimi;
    }

    public void setEmailOfferente(String emailOfferente) {
        this.emailOfferente = emailOfferente;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCifraInCentesimi(int cifraInCentesimi) {
        this.cifraInCentesimi = cifraInCentesimi;
    }

    public void setIstanteCreazione(Instant istanteCreazione) {
        this.istanteCreazione = istanteCreazione;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRiepilogo(RiepilogoAttivita riepilogo) {
        this.riepilogo = riepilogo;
    }


}
