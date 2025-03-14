package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;

@Entity
public class IntegerTag extends Tag {

    @NotNull
    private int valore;

    public int getValore() {return valore; }
    public void setValore(int valore) {this.valore = valore; }

    public IntegerTag(){}
    public IntegerTag(String nome, int valore){
        super(nome);
        this.valore= valore;

    }

}
