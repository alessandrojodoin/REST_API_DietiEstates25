package it.unina.rest_api_dietiestates25;

public class Utente {

    private int id;
    private String username;
    private String email;
    private String nome;
    private String cognome;
    private String hashed_password;

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

    public String getHashed_password() {return hashed_password; }
    public void setHashed_password(String hashed_password) {this.hashed_password = hashed_password; }

}
