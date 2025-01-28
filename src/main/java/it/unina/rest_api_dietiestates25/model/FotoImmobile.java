package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.awt.*;

@Entity
public class FotoImmobile {


    @Lob @NotNull
    private Image image;

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Immobile immobile;
}
