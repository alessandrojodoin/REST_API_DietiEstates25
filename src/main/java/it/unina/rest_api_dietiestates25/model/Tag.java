package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Tag {


    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    private Immobile immobile;

    public String getNome() {return nome; }
    public void setNome(String nome) {this.nome = nome; }

    public void setImmobile(Immobile immobile) {this.immobile = immobile; }

    public Object getValore() { return null; }

    public String getValueType() { return "NoValue";}

    public Tag(){}
    public Tag(String nome){
        this.nome= nome;
    }

}
