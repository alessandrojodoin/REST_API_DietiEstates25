package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.model.Immobile;

import java.io.IOException;

public interface GeolocationAPI {

    public void addNearbyServiceTags(Immobile immobile) throws IOException;
}
