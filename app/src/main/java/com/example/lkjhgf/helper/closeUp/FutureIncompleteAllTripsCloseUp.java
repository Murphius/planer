package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.view.View;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

public class FutureIncompleteAllTripsCloseUp extends MultipleCloseUp {
    /**
     * @see MultipleCloseUp#MultipleCloseUp(Activity, View)
     * <p>
     * Unterschiede sind das Anzeigen von Fahrscheinen sowie die unterschiedliche Funktion
     * beim zurückklicken und das es keinen "weiter" Button gibt; <br/>
     * Unterschied zu {@link FutureIncompleteCloseUp}: Aus welcher Ansicht in diese Ansicht gewechselt wird;
     * Hier: aus der Liste ALLER geplanten Fahrten <br/>
     * {@link FutureIncompleteCloseUp}: Liste der aktuell geplanten Fahrten, welche anschließend optimiert werden sollen
     */
    public FutureIncompleteAllTripsCloseUp(Activity activity, View view, TripItem tripItem) {
        super(tripItem, activity, view);
        //Anpassen des Layouts
        textViewClass.ticketView.setVisibility(View.VISIBLE);
        textViewClass.useTicket.setVisibility(View.VISIBLE);
        buttons.button_accept.setVisibility(View.INVISIBLE);
    }

}
