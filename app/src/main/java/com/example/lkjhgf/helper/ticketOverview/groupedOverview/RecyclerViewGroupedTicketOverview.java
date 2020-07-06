package com.example.lkjhgf.helper.ticketOverview.groupedOverview;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.helper.util.TripQuantitiesTimeComparator;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.tickets.groupedTicketView.GroupedTicketAdapter;
import com.example.lkjhgf.recyclerView.tickets.groupedTicketView.TicketItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import de.schildbach.pte.dto.Fare;

/**
 * Handhabung des RecyclerViews, in dem die Tickets angezeigt werden
 */
public class RecyclerViewGroupedTicketOverview {

    private Activity activity;

    /**
     * Liste der anzuzeigenden Elemente
     */
    private ArrayList<TicketItem> ticketItems;

    private RecyclerView recyclerView;
    /**
     * Managed die Klicks und das Anzeigen der Liste an TicketItems
     */
    private GroupedTicketAdapter adapter;

    /**
     * Erzeugt aus den gespeicherten Fahrten ({@link TicketToBuy}) eine Liste mit Objekten der
     * Klasse {@link TicketItem}, welche im Adapter verarbeitet werden um angezeigt zu werden
     *
     * @param activity   für Farben... benötigt
     * @param view       Layout
     * @param allTickets enthält alle gespeicherten Fahrten
     */
    public RecyclerViewGroupedTicketOverview(Activity activity, View view, AllTickets allTickets) {
        this.activity = activity;

        recyclerView = view.findViewById(R.id.recyclerView7);
        ticketItems = new ArrayList<>();

        //gespeicherte Tickets holen
        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketsToBuy = allTickets.getTickets();
        ArrayList<TicketToBuy> ticketsToBuy = new ArrayList<>();
        for (Fare.Type type : allTicketsToBuy.keySet()) {
            if (allTicketsToBuy.get(type) != null) {
                ticketsToBuy.addAll(allTicketsToBuy.get(type));
            }
        }

        if (!ticketsToBuy.isEmpty()) {
            sumUpTicketToBuyList(ticketsToBuy);
        }

        adapter = new GroupedTicketAdapter(activity, ticketItems);
        buildRecyclerView();
    }

    /**
     * Initialiserung des RecyclerViews
     */
    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            ticketItems.get(position).setShowDetails();
            adapter.notifyDataSetChanged();
        });
    }

    /**
     * Umwandlung TicketToBuy -> TicketItem <br/>
     * <p>
     * Fasst Tickets wenn möglich zusammen und wandelt die zusammengefassten
     * anschließend in TicketItems um
     *
     * @param ticketsToBuy Fahrten die zusammengefasst werden sollen
     */
    private void sumUpTicketToBuyList(ArrayList<TicketToBuy> ticketsToBuy) {
        //Gesamte Ticketliste
        ArrayList<TicketToBuy> tickets = new ArrayList<>();
        //jeweilige Häufigkeit
        ArrayList<Integer> quantity = new ArrayList<>();
        //jeweilige freie Fahrten
        ArrayList<Integer> freeTrips = new ArrayList<>();

        Iterator<TicketToBuy> it = ticketsToBuy.iterator();
        TicketToBuy current = it.next();
        tickets.add(current);
        freeTrips.add(current.getFreeTrips());
        quantity.add(1);
        while (it.hasNext()) {
            TicketToBuy next = it.next();
            if (current.getTicket() instanceof NumTicket) {
                if (current.equals(next)) {
                    tickets.get(tickets.size() - 1).addTripItems(next.getTripList());
                    quantity.set(quantity.size() - 1, quantity.get(quantity.size() - 1) + 1);
                    freeTrips.set(freeTrips.size() - 1, freeTrips.get(freeTrips.size() - 1) + next.getFreeTrips());
                }
            } else {
                if (next.getValidFarezones().equals(current.getValidFarezones())
                        && current.getFirstDepartureTime().equals(next.getFirstDepartureTime())
                        && current.getLastArrivalTime().equals(next.getLastArrivalTime())) {
                    tickets.get(tickets.size() - 1).getTripList().addAll(next.getTripList());
                    quantity.set(quantity.size() - 1, quantity.get(quantity.size() - 1) + 1);
                    freeTrips.set(freeTrips.size() - 1, freeTrips.get(freeTrips.size() - 1) + next.getFreeTrips());
                } else {
                    tickets.add(next);
                    quantity.add(1);
                    freeTrips.add(next.getFreeTrips());
                }
            }
            current = next;
        }
        for (int i = 0; i < quantity.size(); i++) {
            Collections.sort(tickets.get(i).getTripQuantities(), new TripQuantitiesTimeComparator());
            ticketItems.add(new TicketItem(tickets.get(i), quantity.get(i), freeTrips.get(i)));
        }
    }


}
