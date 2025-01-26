package it.unina.rest_api_dietiestates25;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class Cliente extends Utente{

    @OneToOne(optional = false)
    private RiepilogoAttivita riepilogo;

    public RiepilogoAttivita getRiepilogo(){return riepilogo; }
    public void setRiepilogo(RiepilogoAttivita riepilogo){this.riepilogo= riepilogo; }
}
