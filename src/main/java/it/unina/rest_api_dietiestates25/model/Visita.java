package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Visita {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int immobileId;
    private int clienteId;
    private int agenteId;

    private Instant dataOra;

    @Enumerated(EnumType.STRING)
    private StatoVisita stato;  // RICHIESTA, CONFERMATA, RIFIUTATA

    private Instant creataIl;


    public Visita(){}

    public Visita(int immobileId, int clienteId, int agenteId, Instant dataOra, StatoVisita stato, String modeVisita) {
        this.immobileId = immobileId;
        this.clienteId = clienteId;
        this.agenteId = agenteId;
        this.dataOra = dataOra;
        this.stato = stato;
        this.creataIl = Instant.now();
        this.modeVisita = modeVisita;
    }


    private String modeVisita;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImmobileId() {
        return immobileId;
    }


    public int getClienteId() {
        return clienteId;
    }


    public int getAgenteId() {
        return agenteId;
    }


    public Instant getDataOra() {
        return dataOra;
    }


    public StatoVisita getStato() {
        return stato;
    }

    public void setStato(StatoVisita stato) {
        this.stato = stato;
    }

    public Instant getCreataIl() {
        return creataIl;
    }

}
