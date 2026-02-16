package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class AccountSupporto extends Utente {
        public AccountSupporto(){}

    @NotNull
    public String agenziaImmobiliare;

    public AccountSupporto(String username, String email, String nome, String cognome, String password, String numeroTelefonico, String agenziaImmobiliare){
            super(username, email, nome, cognome, password, numeroTelefonico);
            this.agenziaImmobiliare = agenziaImmobiliare;
    }


    @Override
    public String getUtenteTypeAsSting(){
        return "AccountSupporto";
    }

    @Override
    public String getAgenziaImmobiliare() {
        return agenziaImmobiliare;
    }

}