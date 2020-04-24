package com.example.lkjhgf.activities.futureTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.futureTrip.MyTrip;
import com.example.lkjhgf.helper.futureTrip.TripComplete;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import de.schildbach.pte.dto.Trip;

public class CompleteAbortEditingIncompleteTrip extends Activity {

    private MyTrip myTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.future_trips);

        View view = findViewById(R.id.constraintLayout3);

        Intent intent = getIntent();
        Trip trip = (Trip) intent.getSerializableExtra(MainMenu.EXTRA_TRIP);
        int numAdult = intent.getIntExtra(MainMenu.NUM_ADULT, 0);
        int numChildren = intent.getIntExtra(MainMenu.NUM_CHILDREN, 0);

        TripItem newTripItem = new TripItem(trip, false, numAdult, numChildren);

        myTrip = new TripComplete(this, view, newTripItem);
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
