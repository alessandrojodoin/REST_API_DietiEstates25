package it.unina.rest_api_dietiestates25;

import java.awt.*;
import java.util.ArrayList;

public class RiepilogoAttivita {
    private ArrayList<Immobile> immobili_visualizzati;
    //private visite_prenotate
    private ArrayList<Offerta> offerte_fatte;


    public ArrayList<Immobile> getImmobili_visualizzati() {return immobili_visualizzati; }
    public void setImmobili_visualizzati(ArrayList<Immobile> immobili_visualizzati) {this.immobili_visualizzati = immobili_visualizzati; }

    public ArrayList<Offerta> getOfferte_fatte() {return offerte_fatte; }
    public void setOfferte_fatte(ArrayList<Offerta> offerte_fatte) {this.offerte_fatte = offerte_fatte; }

}
