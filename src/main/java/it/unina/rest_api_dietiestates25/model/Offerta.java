package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Offerta {

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

    private int cifraContropropostaInCentesimi;

    @NotNull
    private LocalDateTime istanteCreazione;

    public ListinoImmobile getListino(){return listino;}
    public void setListino(ListinoImmobile listino){this.listino= listino;}

    public Utente getUtente(){return utente;}
    public void setUtente(Utente utente){this.utente= utente;}

    public RisultatoOfferta getRisultatoOfferta(){return risultatoOfferta;}
    public void setRisultatoOfferta(RisultatoOfferta risultatoOfferta){this.risultatoOfferta= risultatoOfferta;}

}
