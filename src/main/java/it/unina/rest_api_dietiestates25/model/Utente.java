package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.NaturalId;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;



@Entity
public class Utente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @NaturalId
    private String username;

    @NotNull
    @NaturalId
    private String email;

    @NotNull
    private String nome;

    @NotNull
    private String cognome;

    @NotNull
    private String hashedPassword;

    @NotNull
    private String numeroTelefonico;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setPassword(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            this.hashedPassword = Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Password Hashing Failed");
        }
    }

    public boolean verifyPassword(String password) {
        boolean isPasswordCorrect = false;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            isPasswordCorrect = hashedPassword.equals(Base64.getEncoder().encodeToString(hash));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Password Hashing Failed");
        }
        return isPasswordCorrect;
    }


    public String getNumeroTelefonico() {
        return numeroTelefonico;
    }

    public void setNumeroTelefonico(String numeroTelefonico) {
        this.numeroTelefonico = numeroTelefonico;
    }

    public String getUtenteTypeAsSting(){
        return "Utente";
    }

    public Utente() {
    }

    public Utente(String username, String email, String nome, String cognome, String password, String numeroTelefonico) {

        setUsername(username);
        setEmail(email);
        setNome(nome);
        setCognome(cognome);
        setPassword(password);
        setNumeroTelefonico(numeroTelefonico);


    }


    public String getAgenziaImmobiliare() {
        return "";
    }

}



