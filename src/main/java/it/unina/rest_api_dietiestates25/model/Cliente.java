package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class Cliente extends Utente {

    @OneToOne(optional = false, cascade = {CascadeType.ALL})
    private RiepilogoAttivita riepilogo;

    public RiepilogoAttivita getRiepilogo(){return riepilogo; }
    public void setRiepilogo(RiepilogoAttivita riepilogo){this.riepilogo= riepilogo; }


    public Cliente(){}

    public Cliente(String username, String email, String nome, String cognome, String password, String numeroTelefonico){
        super(username, email, nome, cognome, password, numeroTelefonico);
        setRiepilogo(new RiepilogoAttivita(this));
    }
}
