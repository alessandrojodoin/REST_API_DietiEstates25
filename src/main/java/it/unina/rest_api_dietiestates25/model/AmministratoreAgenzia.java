package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;

@Entity
public class AmministratoreAgenzia extends Utente {
        public AmministratoreAgenzia(){}

    public AmministratoreAgenzia(String username, String email, String nome, String cognome, String password, String numeroTelefonico){
            super(username, email, nome, cognome, password, numeroTelefonico);
    }


    public String getUtenteTypeAsSting(){
        return "AmministratoreAgenzia";
    }
}
