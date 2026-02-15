package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class CheckboxTag extends Tag {

    @NotNull
    private boolean valore;

    public String getValueType() { return "Boolean";}

    public Boolean getValore() {return valore; }
    //public void setValore(boolean valore) {this.valore = valore; }

    public CheckboxTag(){}
    public CheckboxTag(String nome, boolean valore){
        super(nome);
        this.valore= valore;
    }

}
