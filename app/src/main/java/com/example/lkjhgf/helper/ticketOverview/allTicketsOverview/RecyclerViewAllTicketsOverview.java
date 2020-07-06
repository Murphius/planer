package com.example.lkjhgf.helper.ticketOverview.allTicketsOverview;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.tickets.allTicketsView.AllTicketAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import de.schildbach.pte.dto.Fare;

public class RecyclerViewAllTicketsOverview {

    private Activity activity;

    private RecyclerView recyclerView;
    /**
     * Managed die Klicks und das Anzeigen der Items
     */
    private AllTicketAdapter adapter;

    /**
     * Für das Anzeigen aller gespeicherter Tickets, ohne die zugehörigen Fahrten anzuzeigen. <br/>
     *
     * Kein Zusammenfassen der Fahrscheine.
     * Das Füllen der Ansicht erfolgt in {@link AllTicketAdapter}
     * @param activity - Zum Laden der Tickets
     * @param view - Ansicht die gefüllt werden soll.
     */
    public RecyclerViewAllTicketsOverview(Activity activity, View view){
        this.activity = activity;
        recyclerView = view.findViewById(R.id.recyclerView9);
        TextView fullpriceHolder = view.findViewById(R.id.textView107);

        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketsToBuy = AllTickets.loadTickets(activity, 24*60*60*1000);
        if(allTicketsToBuy == null){
            allTicketsToBuy = new HashMap<>();
        }

        ArrayList<TicketToBuy> ticketItems = new ArrayList<>();

        int fullPrice = AllTickets.getFullPrice();

        for(Fare.Type type : allTicketsToBuy.keySet()){
            if(allTicketsToBuy.get(type) != null){
                ticketItems.addAll(allTicketsToBuy.get(type));
            }
        }

        fullpriceHolder.setText(UtilsString.centToString(fullPrice));
        adapter = new AllTicketAdapter(activity, ticketItems);
        buildRecyclerView();
    }

    /**
     * Initialisierung des Recyclerviews
     */
    private void buildRecyclerView(){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
