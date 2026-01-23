package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class VisualizzazioneImmobile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ListinoImmobile immobile;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    @NotNull
    private Instant istanteVisualizzazione;



    public VisualizzazioneImmobile(){}
    public VisualizzazioneImmobile(ListinoImmobile immobile, Cliente cliente){
        this.immobile= immobile;
        this.cliente= cliente;
        this.istanteVisualizzazione= Instant.now();
    }

}


