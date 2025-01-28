package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Utente {

    @Id
    private int id;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String nome;

    @NotNull
    private String cognome;

    @NotNull
    private String hashedPassword;

    @NotNull
    private String numeroTelefonico;

    public int getId() {return id; }
    public void setId(int id) {this.id = id; }

    public String getUsername() {return username; }
    public void setUsername(String username) {this.username = username; }

    public String getEmail() {return email; }
    public void setEmail(String email) {this.email = email; }

    public String getNome() {return nome; }
    public void setNome(String nome) {this.nome = nome; }

    public String getCognome() {return cognome; }
    public void setCognome(String cognome) {this.cognome = cognome; }

    public String getHashedPassword() {return hashedPassword; }
    public void setHashedPassword(String hashedPassword) {this.hashedPassword = hashedPassword; }

}
