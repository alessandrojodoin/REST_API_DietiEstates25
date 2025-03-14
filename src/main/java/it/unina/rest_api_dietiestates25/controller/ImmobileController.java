package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import org.hibernate.SessionFactory;

public class ImmobileController {

    private final SessionFactory sessionFactory = Database.getInstance().getSessionFactory();



}
