package it.unina.rest_api_dietiestates25.model;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Immobile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String nome;

    //@NotNull
    //private posizioneGeografica

    @NotNull
    private String posizioneGeografica;

    //@OneToMany(mappedBy = "immobile")
    //private Set<FotoImmobile> foto;

    @NotNull
    private String indirizzo;

    @NotNull
    private String citta;

    @NotNull
    private String provincia;

    @OneToMany(mappedBy = "immobile")
    private Set<Tag> tagDescrittivi;


    public int getId() {return id; }
    public void setId(int id) {this.id = id; }

    public String getNome() {return nome; }
    public void setNome(String nome) {this.nome = nome; }

    public String getPosizioneGeografica() {return posizioneGeografica; }
    public void setPosizioneGeografica(String posizioneGeografica) {this.posizioneGeografica = posizioneGeografica; }

   // public Set<FotoImmobile> getFoto() {return foto; }
    //public void setFoto(Set<FotoImmobile> foto) {this.foto = foto; }

    public String getIndirizzo() {return indirizzo; }
    public void setIndirizzo(String indirizzo) {this.indirizzo = indirizzo; }

    public String getCitta() {return citta; }
    public void setCitta(String citta) {this.citta = citta; }

    public String getProvincia() {return provincia; }
    public void setProvincia(String provincia) {this.provincia = provincia; }

    public Set<Tag> getTags() {return tagDescrittivi; }
    public void setTags(Set<Tag> tagDescrittivi) {this.tagDescrittivi = tagDescrittivi; }


    public Immobile(){}
    public Immobile(String nome,String posizioneGeografica,String indirizzo, String citta, String provincia){
        this.nome= nome;
        this.posizioneGeografica= posizioneGeografica;
        this.indirizzo= indirizzo;
        this.citta= citta;
        this.provincia= provincia;
    }
}
