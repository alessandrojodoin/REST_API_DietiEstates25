package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class IntegerTag extends Tag {

    @NotNull
    private int valore;

    @Override
    public String getValueType() { return "Integer";}

    @Override
    public Integer getValore() {return valore; }


    public IntegerTag(){}
    public IntegerTag(String nome, int valore){
        super(nome);
        this.valore= valore;

    }

}
