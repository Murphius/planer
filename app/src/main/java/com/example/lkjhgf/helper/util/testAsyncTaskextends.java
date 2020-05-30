package com.example.lkjhgf.helper.util;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class testAsyncTaskextends extends AsyncTask<Integer, Void, ArrayList<Integer>> {
    @Override
    protected ArrayList<Integer> doInBackground(Integer... haltestellen) {
        URL url;
        HttpURLConnection connection;
        ArrayList<Integer> result = new ArrayList<>();
        for (int haltestelle: haltestellen ) {
            try {
                url = new URL("https://openvrr.de/api/3/action/datastore_search?resource_id=fcc69f43-15c6-4e62-bf2c-b07d7d370603&limit=5&q={\"Nummer\":\"" + haltestelle + "\"}");

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int code = connection.getResponseCode();
                // optional default is GET
                //add request header
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + code);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print in String
                System.out.println(response.toString());

                //Read JSON response and print
                JSONObject myResponse = new JSONObject(response.toString());
                System.out.println("result after Reading JSON Response");
                System.out.println("origin- " + myResponse.getJSONObject("result").getJSONArray("records").getJSONObject(0).getInt("Tarifzone"));
                result.add(myResponse.getJSONObject("result").getJSONArray("records").getJSONObject(0).getInt("Tarifzone"));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
