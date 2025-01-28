package it.unina.rest_api_dietiestates25.model;

public enum RisultatoOfferta {
    Accettata(1),
    Rifiutata(2),
    InRevisione(3),
    ContropropostaRicevuta(4);

    private final int tipoOfferta;
    RisultatoOfferta(int tipoOfferta) {
        this.tipoOfferta = tipoOfferta;
    }

    public int getTipoOfferta() {
        return tipoOfferta;
    }
}
