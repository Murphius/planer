package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.activites.singleTrip.UserForm;

/**
 * Ansicht nach der Planung einer Fahrt, die nicht Optimiert werden muss <br/>
 * Ansicht, wenn aus dem Hauptmenü aufgerufen
 */
public class TripComplete extends MyTrip {

    /**
     * Layout der Ansicht modifizieren <br/>
     * <p>
     * In dieser Ansicht können keine Fahrscheine berechnet werden -> dieser Button ist für
     * den Nutzer nicht sichrbar <br/>
     * Auch  kann der Nutzer nicht abbrechen, sondern nur zurück ins Hauptmenü <br/>
     *
     * @see MyTrip#MyTrip(Activity activity, View view, TripItem tripItem, String DataPath)
     */
    public TripComplete(Activity activity, View view, TripItem tripItem) {
        super(activity, view, tripItem, MyTrip.ALL_SAVED_TRIPS);

        abort.setVisibility(View.GONE);
        calculateTickets.setVisibility(View.GONE);

        setOnClickListener();
        setRecyclerView();
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
