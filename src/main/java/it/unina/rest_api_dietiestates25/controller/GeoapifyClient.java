package it.unina.rest_api_dietiestates25.controller;



import it.unina.rest_api_dietiestates25.model.Immobile;
import jakarta.json.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.StringReader;

public class GeoapifyClient implements GeolocationAPI {


    public void addNearbyServiceTags(Immobile immobile) throws IOException {

        final int radiusMeters = 1000;

        JsonObject responseJson = getNearbyServices(radiusMeters, immobile.getLatitudine(), immobile.getLongitudine());


        //Ricerca dei luoghi presenti nella risposta dell'API
        boolean isPublicTransportPresent = false;
        boolean isSchoolPresent = false;
        boolean isParkPresent = false;

        JsonArray features = responseJson.getJsonArray("features");
        for(JsonValue place : features) {
            JsonArray categories = place.asJsonObject().getJsonObject("properties").getJsonArray("categories");
                    for(JsonValue category : categories) {

                        if (((JsonString) category).getString().equals("public_transport"))
                            isPublicTransportPresent = true;

                        if (((JsonString) category).getString().equals("education.school"))
                            isSchoolPresent = true;

                        if (((JsonString) category).getString().equals("leisure.park"))
                            isParkPresent = true;

                    }
        }

        
        //Aggiunta dei tag all'immobile
        ImmobileController immobileController = new ImmobileController();

        if(isPublicTransportPresent) {
            immobileController.aggiungiCheckboxTag("Vicino a una fermata di trasporto pubblico", true, immobile);
        }

        if(isSchoolPresent) {
            immobileController.aggiungiCheckboxTag("Vicino a una scuola", true, immobile);
        }

        if(isParkPresent) {
            immobileController.aggiungiCheckboxTag("Vicino a un parco pubblico", true, immobile);
        }
        

        //...




    }

    private JsonObject getNearbyServices(int radiusMeters, String latitude, String longitude) throws IOException {

        String url = "https://api.geoapify.com/v2/places?categories=public_transport,education.school,leisure.park&filter=circle:%s,%s,%d&bias=proximity:%s,%s&limit=20&apiKey=%s";
        url = String.format(url, longitude, latitude, radiusMeters, longitude, latitude, System.getenv("GEOAPIFY_API_KEY"));


        //Richiesta http che viene mandata all'endpoint di Geoapify
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();



        //Conversione del corpo della risposta in JsonObject
        JsonReader jsonReader = Json.createReader(new StringReader(response.body().string()));
        JsonObject responseJson = jsonReader.readObject();
        jsonReader.close();
        response.close();

        return responseJson;

    }

    public static void main(String[] args) throws IOException {
        GeoapifyClient client = new GeoapifyClient();
        ImmobileController immobileController = new ImmobileController();
        Immobile immobile = immobileController.createImmobile("a", "40.880303259538664", "14.271733626162813", "a", "a", "a");

        client.addNearbyServiceTags(immobile);
    }



}
