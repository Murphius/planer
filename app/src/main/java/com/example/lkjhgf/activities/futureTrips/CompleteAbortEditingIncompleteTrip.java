package com.example.lkjhgf.activities.futureTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.MyURLParameter;
import com.example.lkjhgf.helper.futureTrip.MyTripList;
import com.example.lkjhgf.helper.futureTrip.TripListComplete;
import com.example.lkjhgf.helper.ticketOverview.groupedOverview.AllTickets;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.helper.util.WabenTask;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import de.schildbach.pte.dto.Fare;
import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.form.Form.EXTRA_MYURLPARAMETER;
import static com.example.lkjhgf.helper.futureTrip.MyTripList.ALL_SAVED_TRIPS;

/**
 * Editieren einer bereits optimierten Fahrt abgebrochen <br/>
 *
 * @preconditions Der Nutzer hat in der Übersicht aller Fahrten {@link Complete} bei einer zu optimierenden
 * Fahrt editieren geklickt und dies abgebrochen
 * @postconditions Die gleiche Fahrt wird wieder in die Liste der zukünftigen Fahrten eingefügt und erhält
 * wieder die gleichen Fahrscheine
 */
public class CompleteAbortEditingIncompleteTrip extends Activity {

    private MyTripList myTripList;
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

        numUserClasses = (HashMap<Fare.Type, Integer>) intent.getSerializableExtra(MainMenu.NUM_PERSONS_PER_CLASS);
        myURLParameter = (MyURLParameter) intent.getSerializableExtra(EXTRA_MYURLPARAMETER);

        String preisstufe = UtilsString.setPreisstufenName(trip);

        ArrayList<Integer> stopIDs = new ArrayList<>();
        for (Trip.Leg leg : trip.legs) {
            if (leg instanceof Trip.Public) {
                stopIDs.add(Integer.parseInt(((Trip.Public) leg).departureStop.location.id.substring(1)));
                stopIDs.add(Integer.parseInt(((Trip.Public) leg).arrivalStop.location.id.substring(1)));
            }
        }

        //Benötigte Waben ermitteln, Fahrt in die Liste zukünftiger Fahrten einfügen, Ansicht füllen
        new WabenTask(MainMenu.myProvider.getPreisstufenIndex(preisstufe) < MainMenu.myProvider.getPreisstufenIndex("B"), stopIDs, this, this::setWaben).execute();
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

    /**
     * Setzt für das wieder eingefügte Item die benötigten Waben / Tarifgebiete und fügt die Fahrt
     * wieder in die Liste zukünftiger Fahrten ein und speichert dies
     *
     * @param result benötigte Waben bzw. Tarifgebiete
     */
    private void setWaben(Pair<Integer, Set<Integer>> result) {
        wabenResult = result;
        TripItem newTripItem = new TripItem(trip, false, numUserClasses, wabenResult.first, wabenResult.second, myURLParameter);
        ArrayList<TripItem> list = MyTripList.loadTripList(this, ALL_SAVED_TRIPS);
        list.add(newTripItem);
        HashMap<Fare.Type, ArrayList<TicketToBuy>> newTicketList = MainMenu.myProvider.optimise(list, AllTickets.loadTickets(this), this);
        AllTickets.saveData(newTicketList, this);
        myTripList = new TripListComplete(this, view, newTripItem);
    }

}
