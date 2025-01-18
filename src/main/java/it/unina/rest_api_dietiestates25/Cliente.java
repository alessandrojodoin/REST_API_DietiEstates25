package it.unina.rest_api_dietiestates25;

public class Cliente extends Utente{
    private RiepilogoAttivita riepilogo;

    public RiepilogoAttivita getRiepilogo(){return riepilogo; }
    public void setRiepilogo(RiepilogoAttivita riepilogo){this.riepilogo= riepilogo; }
}
