package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.model.Cliente;
import org.hibernate.SessionFactory;

public class AuthController {

    private final SessionFactory sessionFactory;

    //WIP
    public void createCliente(String username, String password) {
        sessionFactory.inTransaction(session -> {
            Cliente cliente = new Cliente();
            session.persist(cliente);
        });
    }

    AuthController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


}
