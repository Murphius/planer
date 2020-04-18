package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activites.futureTrips.Complete;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.helper.closeUp.MultipleCloseUp;
import com.example.lkjhgf.activites.multipleTrips.CopyTrip;
import com.example.lkjhgf.activites.multipleTrips.UserForm;

import java.util.ArrayList;

/**
 * Ansicht bei der Planung mehrerer Fahrten, deren Fahrscheine optimiert werden sollen
 */
public class TripIncomplete extends MyTrip {

    private int numTrip;

    /**
     * Arbeitet nur auf den in einem "Durchlauf" geplanten Fahrten
     * @see MyTrip#MyTrip(Activity, View, TripItem, String Data Path)
     */
    public TripIncomplete(Activity activity, View view, TripItem tripItem) {
        super(activity, view, tripItem, MyTrip.SAVED_TRIPS);

        numTrip = activity.getIntent().getIntExtra(MultipleCloseUp.EXTRA_NUM_TRIP, 1);

        setOnClickListener();
        setRecyclerView();
    }

    /**
     * Hinzufügen von OnClickListenern für die Buttons <br/>
     *
     * addTrip -> hinzufügen einer neuen zu optimierenden Fahrt <br/>
     * abort -> entspricht zurück -> Sicherheitsabrfrage <br/>
     * calculateTickets -> weiter & keine weiteren Fahrten mehr planen; optimiereung der Fahrscheine
     */
    private void setOnClickListener() {
        addTrip.setOnClickListener(v -> {
            Intent newIntent = new Intent(activity, UserForm.class);
            newIntent.putExtra(EXTRA_NUM_TRIP, numTrip + 1);
            startNextActivity(newIntent);
        });

        abort.setOnClickListener(v -> activity.onBackPressed());

        //TODO Kalkulierung der Tickets
        calculateTickets.setOnClickListener(v -> {
            ArrayList<TripItem> copy = new ArrayList<>(tripItems);
            dataPath = ALL_SAVED_TRIPS;
            tripItems.clear();
            loadData();
            for(TripItem item : copy){
                insertTrip(item);
            }
            //TODO eigentlich anzeigen der Tickets statt aller Fahrten
            Intent newIntent = new Intent(activity.getApplicationContext(), Complete.class);
            startNextActivity(newIntent);
        });
    }
}
