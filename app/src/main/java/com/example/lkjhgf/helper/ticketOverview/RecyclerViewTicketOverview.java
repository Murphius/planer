package com.example.lkjhgf.helper.ticketOverview;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.tickets.TicketAdapter;
import com.example.lkjhgf.recyclerView.tickets.TicketItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.schildbach.pte.dto.Fare;

/**
 * Handhabung des RecyclerViews, in dem die Tickets angezeigt werden
 */
class RecyclerViewTicketOverview {

    private Activity activity;

    /**
     * Liste der anzuzeigenden Elemente
     */
    private ArrayList<TicketItem> ticketItems;

    private RecyclerView recyclerView;
    /**
     * Managed die Klicks und das Anzeigen der Liste an TicketItems
     */
    private TicketAdapter adapter;

    /**
     * Erzeugt aus den gespeicherten Fahrten ({@link TicketToBuy}) eine Liste mit Objekten der
     * Klasse {@link TicketItem}, welche im Adapter verarbeitet werden um angezeigt zu werden
     *
     * @param activity   für Farben... benötigt
     * @param view       Layout
     * @param allTickets enthält alle gespeicherten Fahrten
     */
    RecyclerViewTicketOverview(Activity activity, View view, AllTickets allTickets) {
        this.activity = activity;

        recyclerView = view.findViewById(R.id.recyclerView7);

        //gespeicherte Tickets holen
        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketsToBuy = allTickets.getTickets();
        ArrayList<TicketToBuy> ticketsToBuy = new ArrayList<>();
        for (Fare.Type type : allTicketsToBuy.keySet()) {
            ticketsToBuy.addAll(allTicketsToBuy.get(type));
        }

        //Gesamte Ticketliste
        ArrayList<TicketToBuy> tickets = new ArrayList<>();
        //jeweilige Häufigkeit
        ArrayList<Integer> quantity = new ArrayList<>();
        //Liste mit den Items zum anzeigen
        ticketItems = new ArrayList<>();

        //Umwandlung TicketToBuy -> TicketItem
        if (!ticketsToBuy.isEmpty()) {
            Iterator<TicketToBuy> it = ticketsToBuy.iterator();
            TicketToBuy current = it.next();
            tickets.add(current);
            quantity.add(1);
            while (it.hasNext()) {
                TicketToBuy next = it.next();
                if (current.equals(next)) {
                    tickets.get(tickets.size() - 1).getTripList().addAll(next.getTripList());
                    quantity.set(quantity.size() - 1, quantity.get(quantity.size() - 1) + 1);
                } else {
                    tickets.add(next);
                    quantity.add(1);
                }
                current = next;
            }

            ticketItems = new ArrayList<>();
            for (int i = 0; i < quantity.size(); i++) {
                tickets.get(i).getTripList().sort((o1, o2) -> {
                    if (o1.getTrip().getFirstDepartureTime().after(o2.getTrip().getFirstDepartureTime())) {
                        return -1;
                    } else if (o1.getTrip().getFirstDepartureTime().getTime() == o2.getTrip().getFirstDepartureTime().getTime()) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
                ticketItems.add(new TicketItem(tickets.get(i), quantity.get(i)));
            }
        }

        adapter = new TicketAdapter(activity, ticketItems);
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


}
