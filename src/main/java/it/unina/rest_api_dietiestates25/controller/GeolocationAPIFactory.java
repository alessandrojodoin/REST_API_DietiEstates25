package it.unina.rest_api_dietiestates25.controller;

public class GeolocationAPIFactory {

    public static GeolocationAPI getGeolocationAPI() {
        return new GeoapifyClient();
    }
}
