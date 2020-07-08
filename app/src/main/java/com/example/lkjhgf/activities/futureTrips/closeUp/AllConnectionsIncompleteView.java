package com.example.lkjhgf.activities.futureTrips.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Complete;
import com.example.lkjhgf.helper.closeUp.FutureIncompleteAllTripsCloseUp;
import com.example.lkjhgf.helper.futureTrip.MyTripList;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TripQuantity;
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
    /**
     * Zu betrachtende Fahrt
     */
    private TripItem tripItem;
    /**
     * Aufrufende Klasse - in diese soll anschließend wieder zurück gekehrt werden
     */
    private Class parentClass;

    FutureIncompleteAllTripsCloseUp closeUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detailed_view);

        RelativeLayout layout = findViewById(R.id.constraintLayout2);

        tripItem = (TripItem) getIntent().getSerializableExtra(MainMenu.EXTRA_TRIP);
        parentClass = (Class) getIntent().getSerializableExtra(MainMenu.EXTRA_CLASS);

        closeUp = new FutureIncompleteAllTripsCloseUp(this, layout, tripItem);
    }

    /**
     * Da der Nutzer in dieser Ansicht die Möglichkeit hat, die Fahrt zu aktualisieren,
     * müssen die eventuell dadurch entstandenen Änderungen in der Liste der Fahrten und
     * in der Liste der Tickets gespeichert werden
     *
     * @preconditions Der Nutzer hat auf die Fahrt geklickt, entweder in der Liste aller
     * Fahrten ({@link Complete}) oder in der Fahrscheinübersicht ({@link com.example.lkjhgf.activities.ticketOverview.TicketOverviewGroupedTickets})
     * @postconditions Veränderungen in der Fahrt, zB Gleiswechsel oder Verspätungen sind sowohl in
     * den zugeordneten Tickets, als auch in der Liste der gespeicherten Fahrten ersichtlich
     */
    @Override
    public void onBackPressed() {
        //Laden der gespeicherten Fahrten
        ArrayList<TripItem> tripItems = MyTripList.loadTripList(this, MyTripList.ALL_SAVED_TRIPS);
        int indexOfTripItem = tripItems.indexOf(tripItem);
        //Anpassen des Ticketindexes
        if (indexOfTripItem > -1) {
            tripItems.get(indexOfTripItem).setTrip(closeUp.getTrip());
        }
        //Aktualisieren der Fahrt des TripItems
        tripItem.setTrip(closeUp.getTrip());
        //Laden der Tickets
        HashMap<Fare.Type, ArrayList<TicketToBuy>> savedTickets = AllTickets.loadTickets(this);
        for (Fare.Type type : savedTickets.keySet()) {
            ArrayList<TicketToBuy> savedTypeTickets = savedTickets.get(type);
            ArrayList<UUID> ticketUUID = tripItem.getTicketIDs(type);
            if (ticketUUID == null || ticketUUID.isEmpty()) {
                continue;
            }
            //Prüfen, ob das jeweilige Ticket dieser Fahrt zugeordnet ist
            for (TicketToBuy ticket : savedTypeTickets) {
                if (ticketUUID.contains(ticket.getTicketID())) {
                    //Falls ja, die entsprechende Fahrt aktualisieren
                    ArrayList<TripQuantity> tripQuantities = ticket.getTripQuantities();
                    for (TripQuantity tripQuantity : tripQuantities) {
                        if (tripQuantity.getTripItem().equals(tripItem)) {
                            tripQuantity.updateTrip(tripItem);
                            break;
                        }
                    }
                }
            }
        }
        //Speichern von Fahrten und Tickets
        MyTripList.saveTrips(tripItems, MyTripList.ALL_SAVED_TRIPS, this);
        AllTickets.saveData(savedTickets, this);
        //Aufrufende Aktivität starten
        Intent intent = new Intent(this, parentClass);
        startActivity(intent);
    }

}
