package it.unina.rest_api_dietiestates25;


import java.awt.*;
import java.util.List;

//@Entity
public class Immobile {

    private int id;
    private String nome;
    private String posizioneGeografica;
    private List<Image> foto;
    private String indirizzo;
    private String citta;
    private String provincia;
    private List<Tag> tagDescrittivi;


    public int getId() {return id; }
    public void setId(int id) {this.id = id; }

    public String getNome() {return nome; }
    public void setNome(String nome) {this.nome = nome; }

    public String getPosizioneGeografica() {return posizioneGeografica; }
    public void setPosizioneGeografica(String posizioneGeografica) {this.posizioneGeografica = posizioneGeografica; }

    public List<Image> getFoto() {return foto; }
    public void setFoto(List<Image> foto) {this.foto = foto; }

    public String getIndirizzo() {return indirizzo; }
    public void setIndirizzo(String indirizzo) {this.indirizzo = indirizzo; }

    public String getCitta() {return citta; }
    public void setCitta(String citta) {this.citta = citta; }

    public String getProvincia() {return provincia; }
    public void setProvincia(String provincia) {this.provincia = provincia; }

    public List<Tag> getTags() {return tagDescrittivi; }
    public void setTags(List<Tag> tagDescrittivi) {this.tagDescrittivi = tagDescrittivi; }


}
