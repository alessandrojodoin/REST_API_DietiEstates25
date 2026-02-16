package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class StringTag extends Tag {

    @NotNull
    private String valore;

    @Override
    public String getValore() {return valore; }

    @Override
    public String getValueType() { return "String";}

    public StringTag(){}

    public StringTag(String nome, String valore){
        super(nome);
        this.valore= valore;
    }

}
