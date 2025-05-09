package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class FloatTag extends Tag {

    @NotNull
    private float valore;

    public String getValueType() { return "Float";}

    public Float getValore() {return valore; }
    public void setValore(float valore) {this.valore = valore; }

    public FloatTag(){}

    public FloatTag(String nome, float valore){
        super(nome);
        this.valore= valore;
    }

}
