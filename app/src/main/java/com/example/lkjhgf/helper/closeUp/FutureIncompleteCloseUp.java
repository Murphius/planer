package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.multipleTrips.EditIncompleteTripFromIncompleteList;
import com.example.lkjhgf.helper.futureTrip.MyTripList;
import com.example.lkjhgf.helper.futureTrip.TripListComplete;
import com.example.lkjhgf.helper.futureTrip.TripListIncomplete;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;

/**
 * Ansicht einer fertig geplanten "optimierten" Fahrt <br/>
 * <p>
 * Im Gegensatz zur "normalen" detaillierten Ansicht einer zu optimierenden Fahrt, kann der Nutzer
 * nicht "weiter klicken, und die Fahrt erneut hinzufügen"
 * <br/>
 *
 * @preconditions Der Nutzer klickt die zu optimierende Fahrt in der Liste geplanter Fahrten an
 * @postconditions Keine Veränderung an der Liste; der Nutzer kann nur die App schließen oder
 * zurück klicken
 */
public class FutureIncompleteCloseUp extends MultipleCloseUp {
    /**
     * @see MultipleCloseUp#MultipleCloseUp(Activity, View)
     * <p>
     * Unterschied ist der fehlende "weiter" Button <br/>
     * <p>
     * Unterschied zu {@link FutureIncompleteAllTripsCloseUp}: Aus welcher Ansicht in diese Ansicht gewechselt wird;
     * Hier: Liste der aktuell geplanten Fahrten -> optimierung
     * {@link FutureIncompleteAllTripsCloseUp}: aus der Liste ALLER geplanten Fahrten <br/>
     * <p>
     * Des weiteren wird in {@link FutureIncompleteAllTripsCloseUp} der zu verwendende Fahrschein angezeigt
     */
    public FutureIncompleteCloseUp(Activity activity,
                                   View view) {
        super(activity, view);
        buttons.button_accept.setVisibility(View.INVISIBLE);
    }

    @Override
    public void startEditing(){
        //TODO: Editieren ansicht öffnen,
        // Bei Abbruch Fahrt wieder einfügen
        //Fahrt aus der Liste der Fahrten löschen
        ArrayList<TripItem> savedTrips = TripListIncomplete.loadTripList(activity, TripListIncomplete.NEW_SAVED_TRIPS);
        savedTrips.remove(new TripItem(trip, false, null));
        MyTripList.saveTrips(savedTrips, TripListComplete.NEW_SAVED_TRIPS, activity);
        //Editieren ansicht öffnen
        Intent intent = new Intent(activity, EditIncompleteTripFromIncompleteList.class);
        intent.putExtra(MainMenu.EXTRA_TRIP, trip);
        intent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, numPersonsPerClass);
        intent.putExtra(MainMenu.EXTRA_NUM_TRIP, numTrip);
        activity.startActivity(intent);
    }
}
