package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.*;

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



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "riepilogo")
    private Set<OffertaUtente> offerteFatte;

    public RiepilogoAttivita() {}
    public RiepilogoAttivita(Cliente cliente) {
        this.cliente = cliente;
        this.id = cliente.getId();
    }
    public int getId() {
        return id;
    }

   public void addVisualizzazione(VisualizzazioneImmobile view){
        this.immobiliVisualizzati.add(view);
   }

   public VisualizzazioneImmobile findVisualizzazione(ListinoImmobile listino){

        for(VisualizzazioneImmobile  visualizzazione : immobiliVisualizzati){
            if (visualizzazione.getImmobile().getId() == listino.getId()){
                return visualizzazione;
            }
        }

        return null;
   }

}
