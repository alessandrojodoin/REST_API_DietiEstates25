package it.unina.rest_api_dietiestates25.model;

public enum RisultatoOfferta {
    Accettata(0),
    Rifiutata(1),
    InRevisione(2),
    ContropropostaRicevuta(3);

    private final int tipoOfferta;
    RisultatoOfferta(int tipoOfferta) {
        this.tipoOfferta = tipoOfferta;
    }

    public int getTipoOfferta() {return tipoOfferta;}
}
