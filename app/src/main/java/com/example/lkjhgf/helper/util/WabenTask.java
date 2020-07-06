package com.example.lkjhgf.helper.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Pair;

import com.example.lkjhgf.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class WabenTask extends AsyncTask<Integer, Void, Pair<Integer, Set<Integer>>> {

    public interface GetWaben{
        void getWabenFunction(Pair<Integer, Set<Integer>> waben);
    }

    private boolean isPreisstufeA;
    private ArrayList<Integer> stopIDs;
    private Activity activity;
    private AlertDialog dialog;
    private GetWaben getWaben;

    public WabenTask(boolean isPreisstufeA, ArrayList<Integer> stopIDs, Activity activity, GetWaben getWaben) {
        this.isPreisstufeA = isPreisstufeA;
        this.stopIDs = stopIDs;
        this.activity = activity;
        this.getWaben = getWaben;
    }

    @Override
    protected void onPreExecute() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(R.layout.fragment);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    protected Pair<Integer, Set<Integer>> doInBackground(Integer... integers) {
        Set<Integer> crossedRegions = new HashSet<>();
        if (isPreisstufeA) {
            int startIndex = 0;
            int endIndex = stopIDs.size() - 1;
            int startStop = stopIDs.get(startIndex);
            int destinationStop = stopIDs.get(endIndex);
            String[] startResult, destinationResult;
            startResult = possibleWaben(startStop);
            destinationResult = possibleWaben(destinationStop);
            while (startResult.length != 1 && startIndex + 1 < stopIDs.size()) {
                startIndex++;
                startStop = stopIDs.get(startIndex);
                startResult = possibleWaben(startStop);
            }
            while (destinationResult.length != 1 && endIndex > 0) {
                endIndex--;
                destinationStop = stopIDs.get(endIndex);
                destinationResult = possibleWaben(destinationStop);
            }
            int startWabe = Integer.parseInt(startResult[0]);
            int destinationWabe = Integer.parseInt(destinationResult[0]);
            crossedRegions.add(startWabe);
            crossedRegions.add(destinationWabe);
            return new Pair<>(startWabe, crossedRegions);
        } else {
            Set<Integer> crossedFarezones = new HashSet<>();
            int startIndex = 0;
            int startStop = stopIDs.get(startIndex);
            String[] farezones = possibleWaben(startStop);
            while (farezones.length != 1 && startIndex < stopIDs.size()) {
                startIndex++;
                farezones = possibleWaben(startStop);
            }
            int startWabe = Integer.parseInt(farezones[0]);
            crossedFarezones.add(startWabe);

            while (startIndex + 1 < stopIDs.size()) {
                startIndex++;
                farezones = possibleWaben(stopIDs.get(startIndex));
                if (farezones.length == 1) {
                    int zws = Integer.parseInt(farezones[0]);
                    crossedFarezones.add(zws);
                }
            }
            return new Pair<>(startWabe, crossedFarezones);
        }
    }

    private String[] possibleWaben(int id) {
        try {
            URL url = new URL("https://openvrr.de/api/3/action/datastore_search?resource_id=fcc69f43-15c6-4e62-bf2c-b07d7d370603&limit=5&q={\"Nummer\":\"" + id + "\"}");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            // optional default is GET
            //add request header
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //Read JSON response and print
            JSONObject myResponse = new JSONObject(response.toString());
            Object object = myResponse.getJSONObject("result").getJSONArray("records").getJSONObject(0).get("Tarifzone");
            return object.toString().split("\n");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return new String[]{};
    }

    @Override
    protected void onPostExecute(Pair<Integer, Set<Integer>>result) {
        getWaben.getWabenFunction(result);
        dialog.dismiss();
    }
}
