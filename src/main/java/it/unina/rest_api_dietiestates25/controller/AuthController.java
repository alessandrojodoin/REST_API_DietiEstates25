package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.model.Cliente;
import org.hibernate.SessionFactory;

public class AuthController {

    private final SessionFactory sessionFactory;

    //WIP
    public void createCliente(String username, String email, String nome, String cognome, String password, String numeroTelefonico) {
        sessionFactory.inTransaction(session -> {
            Cliente cliente = new Cliente(username, email, nome, cognome, password, numeroTelefonico);
            session.persist(cliente);
        });
    }

    public AuthController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


}
