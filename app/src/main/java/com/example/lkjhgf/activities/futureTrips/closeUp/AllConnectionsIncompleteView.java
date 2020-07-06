package com.example.lkjhgf.activities.futureTrips.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.core.app.NavUtils;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Complete;
import com.example.lkjhgf.helper.closeUp.FutureIncompleteAllTripsCloseUp;
import com.example.lkjhgf.helper.futureTrip.MyTripList;
import com.example.lkjhgf.helper.ticketOverview.groupedOverview.AllTickets;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.schildbach.pte.dto.Fare;

/**
 * Alle geplanten Fahrten <br/>
 * Öffnet die Detailansicht für eine Fahrt, die bei der Optimierung berücksichtigt werden soll <br/>
 * <p>
 *
 * @preconditions Der Nutzer hat auf eine Fahrt geklickt, welche bei der Optimierung
 * berücksichtigt werden soll in der allgemeinen Fahrtenübersicht
 * <br/>
 * Diese Fahrt wird mittels Intent übergeben <br/>
 * Für diese Fahrt erhält der Nutzer nun eine Detaillierte Ansicht <br/>
 * Für jeden Schritt: Startzeit, -ort, Weg, Ankunftszeit, Zielort, Anzahl reisender Personen <br/>
 * <p>
 * Im Unterschied zur normalen Ansicht, kann der Nutzer hier nur zurück, und die Fahrt nicht erneut
 * zu den geplanten Fahrten hinzufügen
 * <br/>
 * Das Füllen der Ansicht erfolgt in der Klasse {@link FutureIncompleteAllTripsCloseUp}.
 */

public class AllConnectionsIncompleteView extends Activity {
    TripItem tripItem;
    FutureIncompleteAllTripsCloseUp closeUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detailed_view);

        RelativeLayout layout = findViewById(R.id.constraintLayout2);

        tripItem = (TripItem) getIntent().getSerializableExtra(MainMenu.EXTRA_TRIP);

        closeUp = new FutureIncompleteAllTripsCloseUp(this, layout, tripItem);
    }

    @Override
    public void onBackPressed() {
        ArrayList<TripItem> tripItems = MyTripList.loadTripList(this, MyTripList.ALL_SAVED_TRIPS);
        int indexOfTripItem = tripItems.indexOf(tripItem);
        if (indexOfTripItem > -1) {
            tripItems.get(indexOfTripItem).setTrip(closeUp.getTrip());
        }
        HashMap<Fare.Type, ArrayList<TicketToBuy>> savedTickets = AllTickets.loadTickets(this);
        for (Fare.Type type : savedTickets.keySet()) {
            ArrayList<TicketToBuy> savedTypeTickets = savedTickets.get(type);
            ArrayList<UUID> ticketUUID = tripItem.getTicketIDs(type);
            if (ticketUUID == null) {
                continue;
            }
            for (TicketToBuy ticket : savedTypeTickets) {
                if (ticketUUID.contains(ticket.getTicketID())) {
                    ArrayList<TicketToBuy.TripQuantity> tripQuantities = ticket.getTripQuantities();
                    for (TicketToBuy.TripQuantity tripQuantity : tripQuantities) {
                        if (tripQuantity.getTripItem().equals(tripItem)) {
                            tripQuantity.updateTrip(tripItem);
                            break;
                        }
                    }
                }
            }
        }
        MyTripList.saveTrips(tripItems, MyTripList.ALL_SAVED_TRIPS, this);
        AllTickets.saveData(savedTickets, this);
        Intent intent = new Intent(this, Complete.class);
        startActivity(intent);
    }

}
