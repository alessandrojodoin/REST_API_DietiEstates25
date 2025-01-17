package it.unina.rest_api_dietiestates25;


import java.awt.*;
import java.util.ArrayList;

public class Immobile {

    private int id;
    private String nome;
    private String posizione_geografica;
    private ArrayList<Image> foto;
    private String indirizzo;
    private String citta;
    private String provincia;
    //private ArrayList<Tag> tag_descrittivi;



    public int getId() {return id; }
    public void setId(int id) {this.id = id; }

    public String getNome() {return nome; }
    public void setNome(String nome) {this.nome = nome; }

    public String getPosizione_geografica() {return posizione_geografica; }
    public void setPosizione_geografica(String posizione_geografica) {this.posizione_geografica = posizione_geografica; }

    public ArrayList<Image> getFoto() {return foto; }
    public void setFoto(ArrayList<Image> foto) {this.foto = foto; }

    public String getIndirizzo() {return indirizzo; }
    public void setIndirizzo(String indirizzo) {this.indirizzo = indirizzo; }

    public String getCitta() {return citta; }
    public void setCitta(String citta) {this.citta = citta; }

    public String getProvincia() {return provincia; }
    public void setProvincia(String provincia) {this.provincia = provincia; }



}
