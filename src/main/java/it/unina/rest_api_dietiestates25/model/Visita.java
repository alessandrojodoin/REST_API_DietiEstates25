package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Visita {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int immobileId;
    private int clienteId;
    private int agenteId;

    private LocalDateTime dataOra;

    @Enumerated(EnumType.STRING)
    private StatoVisita stato;  // RICHIESTA, CONFERMATA, RIFIUTATA

    private LocalDateTime creataIl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImmobileId() {
        return immobileId;
    }

    public void setImmobileId(int immobileId) {
        this.immobileId = immobileId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getAgenteId() {
        return agenteId;
    }

    public void setAgenteId(int agenteId) {
        this.agenteId = agenteId;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public StatoVisita getStato() {
        return stato;
    }

    public void setStato(StatoVisita stato) {
        this.stato = stato;
    }

    public LocalDateTime getCreataIl() {
        return creataIl;
    }

    public void setCreataIl(LocalDateTime creataIl) {
        this.creataIl = creataIl;
    }
}
