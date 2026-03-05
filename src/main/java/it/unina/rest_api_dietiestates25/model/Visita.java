package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Visita {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne()
    private ListinoImmobile listino;


    @ManyToOne()
    private Cliente cliente;


    @ManyToOne()
    private AgenteImmobiliare agente;


    private Instant dataOra;

    @Enumerated(EnumType.STRING)
    private StatoVisita stato;  // RICHIESTA, CONFERMATA, RIFIUTATA

    private Instant creataIl;


    private String modeVisita;

    public Visita(){}

    public Visita(ListinoImmobile listino, Cliente cliente, AgenteImmobiliare agente, Instant dataOra, StatoVisita stato, String modeVisita) {
        this.listino = listino;
        this.cliente = cliente;
        this.agente = agente;
        this.dataOra = dataOra;
        this.stato = stato;
        this.creataIl = Instant.now();
        this.modeVisita = modeVisita;
    }

    public ListinoImmobile getListino() {
        return listino;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public AgenteImmobiliare getAgente() {
        return agente;
    }


    public String getModeVisita() {
        return modeVisita;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
