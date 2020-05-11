package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.closeUp.AllConnectionsIncompleteView;
import com.example.lkjhgf.activities.futureTrips.closeUp.AllTripsCompleteView;
import com.example.lkjhgf.activities.multipleTrips.EditIncompleteTripFromCompleteList;
import com.example.lkjhgf.activities.singleTrip.EditTrip;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.activities.singleTrip.UserForm;

import de.schildbach.pte.dto.Fare;

/**
 * Ansicht nach der Planung einer Fahrt, die nicht Optimiert werden muss <br/>
 * Ansicht, wenn aus dem Hauptmenü aufgerufen
 */
public class TripListComplete extends MyTripList {

    /**
     * Layout der Ansicht modifizieren <br/>
     * <p>
     * In dieser Ansicht können keine Fahrscheine berechnet werden -> dieser Button ist für
     * den Nutzer nicht sichrbar <br/>
     * Auch  kann der Nutzer nicht abbrechen, sondern nur zurück ins Hauptmenü <br/>
     *
     * @see MyTripList#MyTripList(Activity activity, View view, TripItem tripItem, String DataPath)
     */
    public TripListComplete(Activity activity, View view, TripItem tripItem) {
        super(activity, view, tripItem, MyTripList.ALL_SAVED_TRIPS);

        abort.setVisibility(View.GONE);
        calculateTickets.setVisibility(View.GONE);

        setOnClickListener();
        setRecyclerView();
    }

    /**
     * Klickt der Nutzer auf die Fahrt, wird die Detailansicht dieser Fahrt geöffnet <br/>
     * <p>
     * @see MyTripList#onItemClick(int position)
     *
     * Wenn der Nutzer in der Übersicht aller gespeicherten Fahrten ist, muss zwischen den Fahrtypen
     * unterschieden werden, da davon abhängig das Layout anders gestaltet werden muss. <br/>
     * Ein zusammenlegen mit {@link TripListIncomplete#onItemClick(int position)} ist nicht möglich,
     * da in der Klasse {@link TripListIncomplete} das Ticket für die jeweilige Fahrt noch nicht bekannt ist.
     */
    @Override
    void onItemClick(int position) {
        TripItem current = tripItems.get(position);
        Intent newIntent;
        //Abhängig von der Fahrt wird eine andere Aktivität gestartet
        if (current.isComplete()) {
            newIntent = new Intent(activity.getApplicationContext(), AllTripsCompleteView.class);
        } else {
            //TODO #Trip??
            newIntent = new Intent(activity.getApplicationContext(), AllConnectionsIncompleteView.class);
            //#Fahrt und #reisende Personen ebenfalls übergeben
            //TODO erweitern für weitere Personenklassen
            newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, position);
            newIntent.putExtra(MainMenu.NUM_ADULT, current.getNumUserClass(Fare.Type.ADULT));
            newIntent.putExtra(MainMenu.NUM_ADULT, current.getNumUserClass(Fare.Type.CHILD));
        }
        newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
        startNextActivity(newIntent);
    }

    /**
     * @see MyTripList#startEdit(int position)
     */
    @Override
    void startEdit(int position) {
        TripItem current = tripItems.get(position);

        tripItems.remove(position);
        adapter.notifyItemRemoved(position);
        Intent newIntent;

        if (current.isComplete()) {
            newIntent = new Intent(activity.getApplicationContext(), EditTrip.class);
        } else {
            newIntent = new Intent(activity.getApplicationContext(),
                    EditIncompleteTripFromCompleteList.class);
            //TODO erweitern für weitere Personenklassen
            newIntent.putExtra(MainMenu.NUM_ADULT, current.getNumUserClass(Fare.Type.ADULT));
            newIntent.putExtra(MainMenu.NUM_ADULT, current.getNumUserClass(Fare.Type.CHILD));
            newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, position + 1);
        }
        newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
        startNextActivity(newIntent);
    }

    /**
     * Neue Fahrt planen, die nicht in bei der Optimierung berücksichtigt wird <br/>
     *
     * @preconditions der Nutzer hat den Button Plus gedrückt
     * @postconditions Öffnen des Formulars für eine neue Fahrt, welche nicht optimiert wird
     */
    private void setOnClickListener() {
        addTrip.setOnClickListener(v -> {
            Intent newIntent = new Intent(activity, UserForm.class);
            super.startNextActivity(newIntent);
        });
    }
}
