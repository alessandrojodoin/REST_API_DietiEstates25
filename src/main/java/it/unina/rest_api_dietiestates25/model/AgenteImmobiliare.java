package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;


import java.util.Set;

@Entity
public class AgenteImmobiliare extends Utente {

    public AgenteImmobiliare() {}

    @NotNull
    public String agenziaImmobiliare;

    public AgenteImmobiliare(String username, String email, String nome, String cognome, String password, String numeroTelefonico, String agenziaImmobiliare){
        super(username, email, nome, cognome, password, numeroTelefonico);
        this.agenziaImmobiliare = agenziaImmobiliare;
    }


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creatore")
    private Set<ListinoImmobile> immobiliGestiti;

    public void setImmobiliGestiti(Set<ListinoImmobile> immobiliGestiti){this.immobiliGestiti= immobiliGestiti;}

    public String getUtenteTypeAsSting(){
        return "AgenteImmobiliare";
    }

}
