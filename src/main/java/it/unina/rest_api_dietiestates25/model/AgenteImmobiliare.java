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

    private boolean googleLinked = false;
    private String googleEmail;
    private String googleRefreshToken;


    public AgenteImmobiliare(String username, String email, String nome, String cognome, String password, String numeroTelefonico, String agenziaImmobiliare){
        super(username, email, nome, cognome, password, numeroTelefonico);
        this.agenziaImmobiliare = agenziaImmobiliare;
    }


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creatore")
    private Set<ListinoImmobile> immobiliGestiti;

    public void setImmobiliGestiti(Set<ListinoImmobile> immobiliGestiti){this.immobiliGestiti= immobiliGestiti;}

    @Override
    public String getUtenteTypeAsSting(){
        return "AgenteImmobiliare";
    }

    @Override
    public String getAgenziaImmobiliare() {
        return agenziaImmobiliare;
    }

    public boolean isGoogleLinked() {
        return googleLinked;
    }

    public void setGoogleLinked(boolean googleLinked) {
        this.googleLinked = googleLinked;
    }

    public String getGoogleEmail() {
        return googleEmail;
    }

    public void setGoogleEmail(String googleEmail) {
        this.googleEmail = googleEmail;
    }

    public String getGoogleRefreshToken() {
        return googleRefreshToken;
    }

    public void setGoogleRefreshToken(String googleRefreshToken) {
        this.googleRefreshToken = googleRefreshToken;
    }

}
