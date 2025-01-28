package it.unina.rest_api_dietiestates25;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Set;

@Entity
public class RiepilogoAttivita {

    @Id
    private int id;

    @OneToOne(optional = false, mappedBy = "riepilogo")
    @MapsId
    private Cliente cliente;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<VisualizzazioneImmobile> immobiliVisualizzati;

    //private visite_prenotate

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Offerta> offerteFatte;


    public Set<VisualizzazioneImmobile> getImmobiliVisualizzati() {return immobiliVisualizzati; }
    public void setImmobiliVisualizzati(Set<VisualizzazioneImmobile> immobiliVisualizzati) {this.immobiliVisualizzati = immobiliVisualizzati; }

    public Set<Offerta> getOfferteFatte() {return offerteFatte; }
    public void setOfferteFatte(Set<Offerta> offerteFatte) {this.offerteFatte = offerteFatte; }

}
