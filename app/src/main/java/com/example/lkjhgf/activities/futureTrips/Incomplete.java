package com.example.lkjhgf.activities.futureTrips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.MyURLParameter;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.publicTransport.query.WabenTask;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.helper.futureTrip.TripListIncomplete;
import com.example.lkjhgf.activities.MainMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import de.schildbach.pte.dto.Fare;
import de.schildbach.pte.dto.Stop;
import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.form.Form.EXTRA_MYURLPARAMETER;

/**
 * Diese Aktivität handhabt die Übersicht über aller aktuell geplanten Fahrten,
 * welche bei der Optimierung berücksichtigt werden sollen<br/>
 * <p>
 * D.h. die Fahrten wurden alle in einem Schritt geplant
 * </p>
 * Wird diese Aktivität ausgeführt, nach dem der Nutzer sich für eine Fahrt entschieden hat, so wird
 * diese Fahrt mittels Intent übergeben und in {@link TripListIncomplete} in die Liste der bisher
 * geplanten Fahrten eingefügt. Zusätzlich wird die anzahl der reisenden Personen, sowie die Nummer
 * des Trips übergeben.
 * <br/>
 * <p>
 * Klickt der Nutzer auf zurück, wird die Methode {@link #onBackPressed()} ausgeführt. Der Nutzer
 * kommt nur ins Hauptmenü zurück, wenn er dies bestätigt.
 * </p>
 */
public class Incomplete extends Activity {

    private Pair<Integer, Set<Integer>> wabenResult;
    private Trip trip;
    private MyURLParameter myURLParameter;
    private HashMap<Fare.Type, Integer> numUserClasses;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.future_trips);
        view = findViewById(R.id.constraintLayout3);

        Intent intent = getIntent();
        trip = (Trip) intent.getSerializableExtra(MainMenu.EXTRA_TRIP);
        myURLParameter = (MyURLParameter) intent.getSerializableExtra(EXTRA_MYURLPARAMETER);
        numUserClasses = (HashMap<Fare.Type, Integer>) intent.getSerializableExtra(MainMenu.NUM_PERSONS_PER_CLASS);

        String preisstufe = UtilsString.setPreisstufenName(trip);

        ArrayList<Integer> stopIDs = new ArrayList<>();
        for (Trip.Leg leg : trip.legs) {
            if (leg instanceof Trip.Public) {
                Trip.Public publicLeg = (Trip.Public) leg;
                stopIDs.add(Integer.parseInt(((Trip.Public) leg).departureStop.location.id.substring(1)));
                for (Stop stop : publicLeg.intermediateStops) {
                    stopIDs.add(Integer.parseInt(stop.location.id.substring(1)));
                }
                stopIDs.add(Integer.parseInt(((Trip.Public) leg).arrivalStop.location.id.substring(1)));
            }
        }
        //Ermitteln der Durchfahrenen Waben bzw. Tarifgebiete
        new WabenTask(MainMenu.myProvider.getPreisstufenIndex(preisstufe) < MainMenu.myProvider.getPreisstufenIndex("B"), stopIDs, this, this::setWaben).execute();
    }

    /**
     * Der Nutzer plant weiter Fahrten, oder kehrt ins Hauptmenü zurück<br/>
     * <p>
     *
     * @preconditions Der Nutzer hat auf zurückgeklickt
     * @postconditions Wenn der Nutzer bestätigt, zurück ins Hauptmenü zu wollen, werden die bisher
     * geplanten Fahrten verworfen, sowie der Aktivitätsstack geleert.
     * @postconditions Wenn der Nutzer hingegen ablehnt, folgt keine weitere Handlung
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "Beim Abbrechen werden alle bisher geplanten Fahrten verworfen \n Wirklich abbrechen?");
        builder.setCancelable(false);
        builder.setPositiveButton("Ja", (dialog, which) -> {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            //leert den Aktivitätenstack
            finishAffinity();
            startActivity(intent);
        });
        builder.setNegativeButton("Nein", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Setzt für das neu hinzugefügte TripItem die benötigten Waben bzw Tarifgebiete und füllt anschließend die Ansicht
     *
     * @param result benötigte Waben bzw. Tarifgebiete
     */
    private void setWaben(Pair<Integer, Set<Integer>> result) {
        wabenResult = result;
        TripItem newTripItem = new TripItem(trip, false, numUserClasses, wabenResult.first, wabenResult.second, myURLParameter);
        new TripListIncomplete(this, view, newTripItem);
    }
}
