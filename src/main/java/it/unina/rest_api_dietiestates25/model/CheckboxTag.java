package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class CheckboxTag extends Tag {

    @NotNull
    private boolean valore;

    public boolean getValore() {return valore; }
    public void setValore(boolean valore) {this.valore = valore; }

}
