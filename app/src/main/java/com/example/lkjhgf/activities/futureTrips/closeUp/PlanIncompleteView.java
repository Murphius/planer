package com.example.lkjhgf.activities.futureTrips.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.futureTrips.IncompleteAfterRefresh;
import com.example.lkjhgf.helper.closeUp.FutureIncompleteCloseUp;
import com.example.lkjhgf.helper.futureTrip.MyTripList;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;

import de.schildbach.pte.dto.Trip;

/**
 * Übersicht der aktuell geplanten Fahrten <br/>
 * Öffnet die Detailansicht für einer einzelnen zu optimierenden Fahrt<br/>
 * <p>
 * @preconditions Der Nutzer hat auf eine Fahrt geklickt, welche bei der Optimierung
 * berücksichtigt werden soll in der Liste der Fahrten, welche er aktuell plant
 * <br/>
 * Diese Fahrt wird mittels Intent übergeben <br/>
 * Für diese Fahrt erhält der Nutzer nun eine Detaillierte Ansicht <br/>
 * Für jeden Schritt: Startzeit, -ort, Weg, Ankunftszeit, Zielort, Anzahl reisender Personen <br/>
 *
 * Im Unterschied zur normalen Ansicht, kann der Nutzer hier nur zurück, und die Fahrt nicht erneut
 * zu den geplanten Fahrten hinzufügen
 * <br/>
 * Das Füllen der Ansicht erfolgt in der Klasse {@link FutureIncompleteCloseUp}.
 */
public class PlanIncompleteView extends Activity {

    FutureIncompleteCloseUp closeUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detailed_view);

       closeUp =  new FutureIncompleteCloseUp(this, findViewById(R.id.constraintLayout2));
    }

    @Override
    public void onBackPressed(){
        Trip trip = closeUp.getTrip();
        ArrayList<TripItem> tripItems = MyTripList.loadTripList(this, MyTripList.NEW_SAVED_TRIPS);
        int indexOfTrip = tripItems.indexOf(new TripItem(trip, false, null));
        if(indexOfTrip > -1){
            tripItems.get(indexOfTrip).setTrip(trip);
        }
        MyTripList.saveTrips(tripItems, MyTripList.NEW_SAVED_TRIPS, this);
        Intent intent = new Intent(this, IncompleteAfterRefresh.class);
        startActivity(intent);
    }
}
