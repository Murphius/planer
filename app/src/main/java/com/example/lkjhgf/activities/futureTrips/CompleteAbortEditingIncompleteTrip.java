package com.example.lkjhgf.activities.futureTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.futureTrip.MyTripList;
import com.example.lkjhgf.helper.futureTrip.TripListComplete;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.helper.util.WabenTask;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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

        String preisstufe = UtilsString.setPreisstufenName(trip);

        ArrayList<Integer> stopIDs = new ArrayList<>();
        for(Trip.Leg leg : trip.legs){
            if(leg instanceof Trip.Public){
                stopIDs.add(Integer.parseInt(((Trip.Public) leg).departureStop.location.id.substring(1)));
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

        TripItem newTripItem = new TripItem(trip, false, numUserClass, waben.first, waben.second);

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
