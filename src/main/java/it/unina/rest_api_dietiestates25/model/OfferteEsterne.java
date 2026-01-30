package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Entity
public class OfferteEsterne {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ListinoImmobile listino;

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

    @NotNull
    private Instant istanteCreazione;

    public OfferteEsterne(){}

    public OfferteEsterne(ListinoImmobile listino, String emailOfferente, String nome,
                   String cognome, String telefono, RisultatoOfferta risultatoOfferta, int cifraInCentesimi){
        this.listino= listino;
        this.emailOfferente= emailOfferente;
        this.nome= nome;
        this.cognome= cognome;
        this.telefono= telefono;
        this.risultatoOfferta= risultatoOfferta;
        this.cifraInCentesimi= cifraInCentesimi;
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


    public int getCifraInCentesimi() {
        return cifraInCentesimi;
    }


    public Instant getIstanteCreazione() {
        return istanteCreazione;
    }

    public ListinoImmobile getListino(){return listino;}
    public void setListino(ListinoImmobile listino){this.listino= listino;}


    public RisultatoOfferta getRisultatoOfferta(){return risultatoOfferta;}
    public void setRisultatoOfferta(RisultatoOfferta risultatoOfferta){this.risultatoOfferta= risultatoOfferta;}


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

}
