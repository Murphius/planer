package com.example.lkjhgf.activities.futureTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.futureTrip.MyTripList;
import com.example.lkjhgf.helper.futureTrip.TripListComplete;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.optimisation.OptimisationUtil;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.HashMap;

import de.schildbach.pte.dto.Fare;
import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.futureTrip.MyTripList.ALL_SAVED_TRIPS;

public class CompleteAbortEditingIncompleteTrip extends Activity {

    private MyTripList myTripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.future_trips);

        View view = findViewById(R.id.constraintLayout3);

        Intent intent = getIntent();
        Trip trip = (Trip) intent.getSerializableExtra(MainMenu.EXTRA_TRIP);

        HashMap<Fare.Type, Integer> numUserClass = (HashMap<Fare.Type, Integer>) intent.getSerializableExtra(MainMenu.NUM_PERSONS_PER_CLASS);

        TripItem newTripItem = new TripItem(trip, false, numUserClass);
        ArrayList<TripItem> list = MyTripList.loadTripList(this, ALL_SAVED_TRIPS);
        list.add(newTripItem);
        HashMap<Fare.Type, ArrayList<TicketToBuy>> newTicketList = MainMenu.myProvider.optimise(list, AllTickets.loadTickets(this), this);
        AllTickets.saveData(newTicketList, this);
        myTripList = new TripListComplete(this, view, newTripItem);
    }

    /**
     * Wird aufgerufen, wenn der Nutzer auf zurück klickt, wechselt die Ansicht ins Hauptmenü
     *
     * @preconditions Der Nutzer klickt auf zurück
     * @postconditions Die Aktivität wird beendet, die Fahrten gespeichert, denn es könnte eine neue
     * Fahrt eingefügt worden sein, und der Aktivitäten Stack geleert. Anschließend kommt der Nutzer
     * zurück ins Hauptmenü.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this.getApplicationContext(), MainMenu.class);
        myTripList.saveData(ALL_SAVED_TRIPS);
        //Sorgt dafür, dass der Aktivitäten Stack geleert wird
        finishAffinity();
        startActivity(intent);
    }

}
