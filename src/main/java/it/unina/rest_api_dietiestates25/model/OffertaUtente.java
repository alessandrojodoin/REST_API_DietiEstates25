package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Entity
public class OffertaUtente extends Offerta {


    @ManyToOne()
    private RiepilogoAttivita riepilogo;

    private int cifraContropropostaInCentesimi;


    public OffertaUtente(){}
    public OffertaUtente(ListinoImmobile listino, RiepilogoAttivita riepilogo, String emailOfferente, String nome,
                         String cognome, String telefono, RisultatoOfferta risultatoOfferta, int cifraInCentesimi,
                         int cifraContropropostaInCentesimi){
        super(listino, emailOfferente, nome, cognome, telefono, risultatoOfferta, cifraInCentesimi);
        this.riepilogo= riepilogo;
        this.cifraContropropostaInCentesimi= cifraContropropostaInCentesimi;
    }



    public RiepilogoAttivita getRiepilogo() {
        return riepilogo;
    }


    public int getCifraContropropostaInCentesimi() {
        return cifraContropropostaInCentesimi;
    }



    public void setCifraContropropostaInCentesimi(int cifraContropropostaInCentesimi) {
        this.cifraContropropostaInCentesimi = cifraContropropostaInCentesimi;
    }



    public void setRiepilogo(RiepilogoAttivita riepilogo) {
        this.riepilogo = riepilogo;
    }


}
