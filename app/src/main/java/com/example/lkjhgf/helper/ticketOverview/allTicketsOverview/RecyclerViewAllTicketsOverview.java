package com.example.lkjhgf.helper.ticketOverview.allTicketsOverview;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.ticketOverview.groupedOverview.AllTickets;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.tickets.allTicketsView.AllTicketAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import de.schildbach.pte.dto.Fare;

public class RecyclerViewAllTicketsOverview {

    private Activity activity;

    /**
     * Liste der Anzuzeigenden Elemente
     */
    private ArrayList<TicketToBuy> ticketItems;

    private RecyclerView recyclerView;
    private TextView fullpriceHolder;
    /**
     * Managed die Klicks und das Anzeigen der Items
     */
    private AllTicketAdapter adapter;

    public RecyclerViewAllTicketsOverview(Activity activity, View view){
        this.activity = activity;
        recyclerView = view.findViewById(R.id.recyclerView9);
        fullpriceHolder = view.findViewById(R.id.textView107);

        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketsToBuy = AllTickets.loadTickets(activity);
        ticketItems = new ArrayList<>();
        int fullPrice = AllTickets.getFullPrice();
        for(Fare.Type type : allTicketsToBuy.keySet()){
            ticketItems.addAll(allTicketsToBuy.get(type));
        }
        fullpriceHolder.setText(UtilsString.centToString(fullPrice));
        adapter = new AllTicketAdapter(activity, ticketItems);
        buildRecyclerView();
    }

    private void buildRecyclerView(){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
