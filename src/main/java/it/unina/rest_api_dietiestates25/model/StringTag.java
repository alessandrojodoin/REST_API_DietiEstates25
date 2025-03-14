package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class StringTag extends Tag {

    @NotNull
    private String valore;

    public String getValore() {return valore; }
    public void setValore(String valore) {this.valore = valore; }

    public StringTag(){}

    public StringTag(String nome, String valore){
        super(nome);
        this.valore= valore;
    }

}
