package it.unina.rest_api_dietiestates25.controller;

public class GeolocationAPIFactory {

    private GeolocationAPIFactory() {}
    public static GeolocationAPI getGeolocationAPI() {
        return new GeoapifyClient();
    }
}
