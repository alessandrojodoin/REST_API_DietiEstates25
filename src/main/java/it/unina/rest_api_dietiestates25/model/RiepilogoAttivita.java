package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Set;

@Entity
public class RiepilogoAttivita {

    public void setId(int id) {
        this.id = id;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(optional = false, mappedBy = "riepilogo")
    //@MapsId
    private Cliente cliente;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<VisualizzazioneImmobile> immobiliVisualizzati;

    //private visite_prenotate

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "riepilogo")
    private Set<Offerta> offerteFatte;


    public Set<VisualizzazioneImmobile> getImmobiliVisualizzati() {return immobiliVisualizzati; }
    public void setImmobiliVisualizzati(Set<VisualizzazioneImmobile> immobiliVisualizzati) {this.immobiliVisualizzati = immobiliVisualizzati; }

    public Set<Offerta> getOfferteFatte() {return offerteFatte; }
    public void setOfferteFatte(Set<Offerta> offerteFatte) {this.offerteFatte = offerteFatte; }

    public RiepilogoAttivita() {}
    public RiepilogoAttivita(Cliente cliente) {
        this.cliente = cliente;
        this.id = cliente.getId();
    }
    public int getId() {
        return id;
    }

}
