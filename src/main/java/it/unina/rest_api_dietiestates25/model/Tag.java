package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public abstract class Tag {


    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    private Immobile immobile;

    public String getNome() {return nome; }
    public void setNome(String nome) {this.nome = nome; }

    public Tag(){}
    public Tag(String nome){
        this.nome= nome;
    }

}
