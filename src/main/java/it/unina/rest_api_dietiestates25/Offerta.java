package it.unina.rest_api_dietiestates25;

import jakarta.persistence.Entity;

@Entity
public class Offerta {
    private ListinoImmobile listino;
    private Utente utente;
    private RisultatoOfferta risultatoOfferta;

    public ListinoImmobile getListino(){return listino;}
    public void setListino(ListinoImmobile listino){this.listino= listino;}

    public Utente getUtente(){return utente;}
    public void setUtente(Utente utente){this.utente= utente;}

    public RisultatoOfferta getRisultatoOfferta(){return risultatoOfferta;}
    public void setRisultatoOfferta(RisultatoOfferta risultatoOfferta){this.risultatoOfferta= risultatoOfferta;}

}
