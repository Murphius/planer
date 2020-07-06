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
import com.example.lkjhgf.helper.util.WabenTask;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.helper.futureTrip.TripListIncomplete;
import com.example.lkjhgf.activities.MainMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.future_trips);

        View view = findViewById(R.id.constraintLayout3);

        Intent intent = getIntent();
        Trip trip = (Trip) intent.getSerializableExtra(MainMenu.EXTRA_TRIP);
        MyURLParameter myURLParameter = (MyURLParameter) intent.getSerializableExtra(EXTRA_MYURLPARAMETER);

        HashMap<Fare.Type, Integer> numUserClasses = (HashMap<Fare.Type, Integer>) intent.getSerializableExtra(MainMenu.NUM_PERSONS_PER_CLASS);

        String preisstufe = UtilsString.setPreisstufenName(trip);

        ArrayList<Integer> stopIDs = new ArrayList<>();
        for(Trip.Leg leg : trip.legs){
            if(leg instanceof Trip.Public){
                Trip.Public publicLeg = (Trip.Public) leg;
                stopIDs.add(Integer.parseInt(((Trip.Public) leg).departureStop.location.id.substring(1)));
                for(Stop stop : publicLeg.intermediateStops){
                    stopIDs.add(Integer.parseInt(stop.location.id.substring(1)));
                }
                stopIDs.add(Integer.parseInt(((Trip.Public) leg).arrivalStop.location.id.substring(1)));
            }
        }
        WabenTask wabenTask = new WabenTask(MainMenu.myProvider.getPreisstufenIndex(preisstufe) < MainMenu.myProvider.getPreisstufenIndex("B"), stopIDs);
        wabenTask.execute();
        Pair<Integer, Set<Integer>> waben = new Pair<>(0, new HashSet<>());
        try {
            waben = wabenTask.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        //TODO my url parameter
        TripItem newTripItem = new TripItem(trip, false, numUserClasses, waben.first, waben.second, myURLParameter);

        new TripListIncomplete(this, view, newTripItem);
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


}
