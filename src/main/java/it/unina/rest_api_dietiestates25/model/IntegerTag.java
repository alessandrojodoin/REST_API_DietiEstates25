package it.unina.rest_api_dietiestates25.model;

import it.unina.rest_api_dietiestates25.Tag;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class IntegerTag extends Tag {

    @NotNull
    private int valore;

    public int getValore() {return valore; }
    public void setValore(int valore) {this.valore = valore; }

}
