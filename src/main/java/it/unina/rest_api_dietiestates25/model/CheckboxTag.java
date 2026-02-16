package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class CheckboxTag extends Tag {

    @NotNull
    private boolean valore;

    @Override
    public String getValueType() { return "Boolean";}

    @Override
    public Boolean getValore() {return valore; }


    public CheckboxTag(){}
    public CheckboxTag(String nome, boolean valore){
        super(nome);
        this.valore= valore;
    }

}
