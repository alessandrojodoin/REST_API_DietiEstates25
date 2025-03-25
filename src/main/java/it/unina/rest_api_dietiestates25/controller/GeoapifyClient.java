package it.unina.rest_api_dietiestates25.controller;



import it.unina.rest_api_dietiestates25.model.Immobile;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.StringReader;

public class GeoapifyClient {


    public void addNearbyServiceTags(Immobile immobile) throws IOException {

        String url = "https://api.geoapify.com/v2/places?categories=public_transport.bus,education.school,leisure.park&filter=circle:%s,%s,300&bias=proximity:%s,%s&limit=20&apiKey=%s";
        url = String.format(url, immobile.getLongitudine(), immobile.getLatitudine(), immobile.getLongitudine(), immobile.getLatitudine(), System.getenv("GEOAPIFY_API_KEY"));


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        //System.out.println(response.body().string());

        JsonReader jsonReader = Json.createReader(new StringReader(response.body().string()));
        JsonObject responseJson = jsonReader.readObject();
        jsonReader.close();


        responseJson.getJsonObject("features");
                /*.forEach(
                (name, location) -> location.asJsonObject().getJsonObject("categories")
                        .forEach( (categoryNumber, categoryType) -> {
                            if(categoryType.toString().equals("public_transport")) System.out.println("Fermata trasporto publico presente");
                        })
        );*/

        response.close();




    }

    public static void main(String[] args) throws IOException {
        GeoapifyClient client = new GeoapifyClient();
        Immobile immobile = new Immobile("a", "40.880303259538664", "14.271733626162813", "a", "a", "a");
        client.addNearbyServiceTags(immobile);
    }



}
