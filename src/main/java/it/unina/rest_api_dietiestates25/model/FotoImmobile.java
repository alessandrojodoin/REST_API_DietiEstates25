package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
public class FotoImmobile {


    @NotNull
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Immobile immobile;

    public FotoImmobile() {
    }



    public FotoImmobile(byte[] image, Immobile immobile) {
        this.image = image;
        this.immobile = immobile;
    }

    public int getId() {return id;}

    public byte[] getImage() {
        return image;
    }

    public Immobile getImmobile() {
        return immobile;
    }

}
