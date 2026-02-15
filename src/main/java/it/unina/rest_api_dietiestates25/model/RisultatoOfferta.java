package it.unina.rest_api_dietiestates25.model;

public enum RisultatoOfferta {
    ACCETTATA(0),
    RIFIUTATA(1),
    IN_REVISIONE(2),
    CONTROPROPOSTA_RICEVUTA(3);

    private final int tipoOfferta;
    RisultatoOfferta(int tipoOfferta) {
        this.tipoOfferta = tipoOfferta;
    }

    public int getTipoOfferta() {return tipoOfferta;}
}
