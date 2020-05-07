package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Complete;
import com.example.lkjhgf.activities.futureTrips.closeUp.PlanIncompleteView;
import com.example.lkjhgf.activities.multipleTrips.EditIncompleteTripFromIncompleteList;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.activities.multipleTrips.UserForm;

import java.util.ArrayList;

/**
 * Ansicht bei der Planung mehrerer Fahrten, deren Fahrscheine optimiert werden sollen
 */
public class TripIncomplete extends MyTrip {

    private int numTrip;

    /**
     * Arbeitet nur auf den in einem "Durchlauf" geplanten Fahrten
     *
     * @see MyTrip#MyTrip(Activity, View, TripItem, String Data Path)
     */
    public TripIncomplete(Activity activity, View view, TripItem tripItem) {
        super(activity, view, tripItem, MyTrip.SAVED_TRIPS);

        numTrip = activity.getIntent().getIntExtra(MainMenu.EXTRA_NUM_TRIP, 1);

        setOnClickListener();
        setRecyclerView();
    }

    /**
     * Klickt der Nutzer auf die Fahrt, wird die Detailansicht dieser Fahrt geöffnet <br/>
     * <p>
     * @see MyTrip#onItemClick(int position)
     *
     * Wenn der Nutzer in der Übersicht der aktuell geplanten Fahrten ist, muss nicht zwischen den Fahrtypen
     * unterschieden werden, da nur zu optimierende Fahrten angezeigt werden. <br/>
     * Ein zusammenlegen mit {@link TripComplete#onItemClick(int position)} ist nicht möglich,
     * da in der Klasse {@link TripComplete} das Ticket für die jeweilige Fahrt bereits bekannt ist.
     */
    @Override
    void onItemClick(int position) {
        TripItem current = tripItems.get(position);
        Intent newIntent = new Intent(activity, PlanIncompleteView.class);
        newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, current.getTrip());
        newIntent.putExtra(MainMenu.NUM_CHILDREN, current.getNumChildren());
        newIntent.putExtra(MainMenu.NUM_ADULT, current.getNumAdult());
        newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
        startNextActivity(newIntent);
    }

    /**
     * @see MyTrip#startEdit(int position)
     *
     * In der Liste finden sich nur zu optimierende Fahrten -> keine Unterscheidung nötig
     */
    @Override
    void startEdit(int position){
        TripItem current = tripItems.get(position);
        tripItems.remove(position);
        Intent newIntent = new Intent(activity, EditIncompleteTripFromIncompleteList.class);
        newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, position + 1);
        newIntent.putExtra(MainMenu.NUM_ADULT, current.getNumAdult());
        newIntent.putExtra(MainMenu.NUM_CHILDREN, current.getNumChildren());
        newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
        activity.startActivity(newIntent);
    }

    /**
     * Hinzufügen von OnClickListenern für die Buttons <br/>
     * <p>
     * addTrip -> hinzufügen einer neuen zu optimierenden Fahrt <br/>
     * abort -> entspricht zurück -> Sicherheitsabrfrage <br/>
     * calculateTickets -> weiter & keine weiteren Fahrten mehr planen; optimiereung der Fahrscheine
     */
    private void setOnClickListener() {
        addTrip.setOnClickListener(v -> {
            Intent newIntent = new Intent(activity, UserForm.class);
            newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, numTrip + 1);
            startNextActivity(newIntent);
        });

        abort.setOnClickListener(v -> activity.onBackPressed());

        //TODO Kalkulierung der Tickets
        calculateTickets.setOnClickListener(v -> {
            //Kopieren aller geplanten Fahrten
            ArrayList<TripItem> copy = new ArrayList<>(tripItems);
            for(TripItem tripItem : copy){
                tripItem.setTicket(new Integer(2));
            }
            //Liste leeren
            tripItems.clear();
            //Speichern, dass keine Fahrten geplant sind
            saveData();
            //Laden aller gespeicherten Fahrten
            dataPath = ALL_SAVED_TRIPS;
            loadData();
            for (TripItem item : copy) {
                insertTrip(item);
            }
            //TODO eigentlich anzeigen der Tickets statt aller Fahrten
            Intent newIntent = new Intent(activity.getApplicationContext(), Complete.class);
            startNextActivity(newIntent);
        });
    }
}
