package it.unina.rest_api_dietiestates25;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class VisualizzazioneImmobile {

    @Id
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Immobile immobile;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    @NotNull
    private LocalDateTime istanteVisualizzazione;
}
