package com.example.lkjhgf.activites.futureTrips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.helper.futureTrip.TripIncomplete;
import com.example.lkjhgf.activites.MainMenu;

import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.closeUp.CloseUp.EXTRA_TRIP;
import static com.example.lkjhgf.helper.closeUp.MultipleCloseUp.EXTRA_NUM_ADULT;
import static com.example.lkjhgf.helper.closeUp.MultipleCloseUp.EXTRA_NUM_CHILDREN;

/**
 * Diese Aktivität handhabt die Übersicht über aller aktuell geplanten Fahrten,
 * welche bei der Optimierung berücksichtigt werden sollen<br/>
 * <p>
 * D.h. die Fahrten wurden alle in einem Schritt geplant
 * </p>
 * Wird diese Aktivität ausgeführt, nach dem der Nutzer sich für eine Fahrt entschieden hat, so wird
 * diese Fahrt mittels Intent übergeben und in {@link TripIncomplete} in die Liste der bisher
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
        Trip trip = (Trip) intent.getSerializableExtra(EXTRA_TRIP);
        int numAdult = intent.getIntExtra(EXTRA_NUM_ADULT, 0);
        int numChildren = intent.getIntExtra(EXTRA_NUM_CHILDREN, 0);

        TripItem newTripItem = new TripItem(trip, false, numAdult, numChildren);

        new TripIncomplete(this, view, newTripItem);
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
