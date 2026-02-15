package it.unina.rest_api_dietiestates25.model;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Immobile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String tipoImmobile;

    @NotNull
    private String longitudine;

    @NotNull
    private String latitudine;

    @OneToMany(mappedBy = "immobile")
    private final Set<FotoImmobile> foto = new HashSet<FotoImmobile>() {
    };

    @NotNull
    private String via;

    @NotNull
    private String citta;

    @NotNull
    private String provincia;

    @OneToMany(mappedBy = "immobile")
    private final Set<Tag> tagDescrittivi = new HashSet<Tag>() {};


    public int getId() {return id; }
    public void setId(int id) {this.id = id; }

    public String getTipoImmobile() {return tipoImmobile; }
    //public void setTipoImmobile(String nome) {this.tipoImmobile = nome; }

    public String getLongitudine() {return longitudine; }
    public String getLatitudine() {return latitudine; }

    public Set<FotoImmobile> getFoto() {return foto; }
    //public void setFoto(Set<FotoImmobile> foto) {this.foto = foto; }


    public String getVia() {return via; }
    //public void setVia(String indirizzo) {this.via = indirizzo; }

    public String getCitta() {return citta; }
    //public void setCitta(String citta) {this.citta = citta; }

    public String getProvincia() {return provincia; }
    //public void setProvincia(String provincia) {this.provincia = provincia; }

    public Set<Tag> getTags() {return tagDescrittivi; }
    //public void setTags(Set<Tag> tagDescrittivi) {this.tagDescrittivi = tagDescrittivi; }

    public void addTag(Tag tag) {
        tagDescrittivi.add(tag);
        tag.setImmobile(this);
    }

    public void addFoto(FotoImmobile fotoImmobile) {
        foto.add(fotoImmobile);
    }


    public int getFotoNumber() {
        return foto.size();
    }


    public Immobile(){}
    public Immobile(String tipoImmobile, String latitudine, String longitudine, String indirizzo, String citta, String provincia){
        this.tipoImmobile = tipoImmobile;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.via = indirizzo;
        this.citta= citta;
        this.provincia= provincia;
    }
}
