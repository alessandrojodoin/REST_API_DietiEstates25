package it.unina.rest_api_dietiestates25;

public enum RisultatoOfferta {
    Accettata(1),
    Rifiutata(2),
    InRevisione(3),
    ContropropostaRicevuta(4);

    private final int tipo_offerta;
    RisultatoOfferta(int tipo_offerta) {
        this.tipo_offerta = tipo_offerta;
    }

    public int getTipo_offerta() {
        return tipo_offerta;
    }
}
