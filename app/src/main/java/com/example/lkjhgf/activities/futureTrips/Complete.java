package com.example.lkjhgf.activities.futureTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.helper.futureTrip.MyTrip;
import com.example.lkjhgf.helper.futureTrip.TripComplete;
import com.example.lkjhgf.activities.MainMenu;

import de.schildbach.pte.dto.Trip;

/**
 * Diese Aktivität handhabt die Übersicht über alle zukünftigen geplanten Fahrten <br/>
 * <p>
 * Wird diese Aktivität ausgeführt, nach dem der Nutzer sich für eine Fahrt entschieden hat, so wird
 * diese Fahrt mittels Intent übergeben und in {@link TripComplete} in die Liste aller Fahrten eingefügt.
 * <br/>
 * <p>
 * Wird diese Aktivität hingegen aus dem Hauptemü aus gestartet, werden in {@link TripComplete} nur die bereits
 * vorhandenen Fahrten geladen
 * <br/></p>
 * Unabhängig von der vorherigen Aktivität kommt der Nutzer zurück in das Hauptmenü {@link #onBackPressed()}
 */


public class Complete extends Activity {

    private MyTrip myTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.future_trips);

        View view = findViewById(R.id.constraintLayout3);

        Intent intent = getIntent();
        Trip trip = (Trip) intent.getSerializableExtra(MainMenu.EXTRA_TRIP);

        if (trip != null) {
            TripItem newTripItem = new TripItem(trip, true);
            myTrip = new TripComplete(this, view, newTripItem);
        } else {
            myTrip = new TripComplete(this, view, null);
        }
    }

    /**
     * Wird aufgerufen, wenn der Nutzer auf zurück klickt, wechselt die Ansicht ins Hauptmenü
     *
     * @preconditions Der Nutzer klickt auf zurück
     * @postcondtions Die Aktivität wird beendet, die Fahrten gespeichert, denn es könnte eine neue
     * Fahrt eingefügt worden sein, und der Aktivitäten Stack geleert. Anschließend kommt der Nutzer
     * zurück ins Hauptmenü.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this.getApplicationContext(), MainMenu.class);
        myTrip.saveData();
        //Sorgt dafür, dass der Aktivitäten Stack geleert wird
        finishAffinity();
        startActivity(intent);
    }
}
