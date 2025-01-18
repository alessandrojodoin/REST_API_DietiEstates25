package it.unina.rest_api_dietiestates25;

public class Offerta {
    private Immobile immobile;
    private Utente utente;
    private RisultatoOfferta risultatoOfferta;

    public Immobile getImmobile(){return immobile;}
    public void setImmobile(Immobile immobile){this.immobile= immobile;}

    public Utente getUtente(){return utente;}
    public void setUtente(Utente utente){this.utente= utente;}

    public RisultatoOfferta getRisultatoOfferta(){return risultatoOfferta;}
    public void setRisultatoOfferta(RisultatoOfferta risultatoOfferta){this.risultatoOfferta= risultatoOfferta;}

}
