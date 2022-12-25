package com.merive.pressonemilliontimes.api;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class API {

    static final String BASE_URL = "https://api.merive.vercel.app/pressonemilliontimes/";

    public String get() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(BASE_URL).openConnection();

        httpURLConnection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        if (httpURLConnection.getResponseCode() == 200) {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) response.append(inputLine);
            bufferedReader.close();
        }
        return response.toString();
    }
}
