package com.example.lkjhgf.publicTransport.query;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.TripOptions;

public class WabenTask extends AsyncTask<Integer, Void, Pair<Integer, Set<Integer>>> {

    public interface GetWaben {
        void getWabenFunction(Pair<Integer, Set<Integer>> waben);
    }

    private boolean isPreisstufeA;
    private ArrayList<Integer> stopIDs;
    private Activity activity;
    private AlertDialog dialog;
    private GetWaben getWaben;

    /**
     * Alle Angaben, die für die Anfrage benötigt werden
     *
     * @param isPreisstufeA - zur Unterscheidung, ob Waben oder Tarifgebiete gespeichert werden müssen
     * @param stopIDs       - alle Haltestellen der Verbindung
     * @param getWaben      - Liefert Funktion, die nach der Serverantwort ausgeführt werden soll
     */
    public WabenTask(boolean isPreisstufeA, ArrayList<Integer> stopIDs, Activity activity, GetWaben getWaben) {
        this.isPreisstufeA = isPreisstufeA;
        this.stopIDs = stopIDs;
        this.activity = activity;
        this.getWaben = getWaben;
    }

    /**
     * Während / vor der Ausführung, soll der Nutzer informiert werden, dass die durchquerten Waben
     * ermittelt werden <br/>
     * <p>
     * Der Vorgang kann nicht unterbrochen werden <br/>
     * Anzeigen eines Ladebalkens (sich drehender Kreis) und zugehöriger Text <br/>
     */
    @Override
    protected void onPreExecute() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(R.layout.fragment, null);
        TextView textView = mView.findViewById(R.id.textView80);
        String text = "Ermittle die durchquerten Waben...";
        textView.setText(text);
        builder.setView(mView);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    /**
     * Die durchquerten Waben ermitteln <br/>
     * <p>
     * Serveranfrage -> openvrr.de <br/>
     * Für jede Haltestelle wird über die ID die zugehörige Wabe abgefragt, bzw. bei Preisstufe
     * B und höher die Tarifgebiete. <br/>
     * Da manche Haltestellen nicht nur in einer Wabe liegen, werden diese Haltestellen übersprungen,
     * in der Annahme, dass nicht alle Haltestellen einer Fahrt in zwei oder mehr Waben liegen. <br/>
     * <p>
     * Aufruf von {@link #possibleWaben(int id)} um die Waben / Tarifgebiete zu erhalten
     */
    @Override
    protected Pair<Integer, Set<Integer>> doInBackground(Integer... integers) {
        if (isPreisstufeA) {
            return preisstufeA();
        } else {
            return rest();
        }
    }

    /**
     * Ermittelt mittels Serverabfrage die Wabe, in der die Haltestelle mit der jeweilgen ID liegt
     *
     * @param id identifiziert die Haltestelle
     * @return mögliche Waben
     */
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

    /**
     * Verfahren für die Preisstufe A <br/>
     * <p>
     * Arbeitet mit Waben und nicht mit Tarifgebieten <br/>
     * Es müssen nur Start und Endhaltestelle betrachtet werden, die weiteren Haltestellen liegen ebenfalls
     * in den jeweiligen Regionen (keine "Bögen")
     *
     * @return Startwabe und alle weiteren durchkreuzten Waben
     */
    private Pair<Integer, Set<Integer>> preisstufeA() {
        Set<Integer> crossedRegions = new HashSet<>();
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
    }

    /**
     * Ermittlung von Tarifgebieten für Preisstufe B und höher <br/>
     * <p>
     * Jede Haltestelle muss betrachtet werden <br/>
     * Ist jedoch eine Haltestelle nicht eindeutig einer Wabe zugeordnet, so wird diese Haltestelle
     * übersprungen. <br/>
     * Annahme: Keine Fahrt beinhaltet nur Haltestellen, die keiner Wabe eindeutig zugeordnet sind
     *
     * @return Startwabe und durchquerte Tarifgebiete
     */
    private Pair<Integer, Set<Integer>> rest() {
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

    /**
     * Ausführen der Hilfsfunktion und Dialog schließen
     *
     * @param result Ergebnis aller Anfragen
     */
    @Override
    protected void onPostExecute(Pair<Integer, Set<Integer>> result) {
        getWaben.getWabenFunction(result);
        dialog.dismiss();
    }
}
