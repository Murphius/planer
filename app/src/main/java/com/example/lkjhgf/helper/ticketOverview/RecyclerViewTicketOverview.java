package com.example.lkjhgf.helper.ticketOverview.tickets;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketInformationHolder;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.recyclerView.tickets.TicketItem;

import java.util.ArrayList;

public class RecyclerViewTicketOverview {

    private Activity activity;
    private View view;
    private AllTickets allTickets;

    private ArrayList<TicketItem> ticketItems;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    //TODO Adapter spezifizieren
    private TicketAdapter adapter;

    public RecyclerViewTicketOverview(Activity activity, View view, AllTickets allTickets) {
        this.activity = activity;
        this.view = view;
        this.allTickets = allTickets;

        recyclerView = view.findViewById(R.id.recyclerView7);

        ArrayList<TicketInformationHolder> ticketInformationHolders = allTickets.getTickets();
        ArrayList<Ticket> tickets = new ArrayList<>();
        ArrayList<String> preisstufe = new ArrayList<>();
        ArrayList<Integer> quantity = new ArrayList<>();
        ArrayList<ArrayList<TripItem>> tripItems = new ArrayList<>();

        tickets.add(ticketInformationHolders.get(0).getTicket());
        preisstufe.add(ticketInformationHolders.get(0).getPreisstufe());
        quantity.add(1);
        tripItems.add(ticketInformationHolders.get(0).getTripList());

        for(int i = 1; i <ticketInformationHolders.size(); i++){
            boolean contains = false;
            Ticket currentTicket = ticketInformationHolders.get(i).getTicket();
            String currentPreisstufe = ticketInformationHolders.get(i).getPreisstufe();
            for(int j = 0; j < tickets.size() && !contains; j++){
                if(currentTicket.equals(tickets.get(j))){
                    if(currentPreisstufe.equals(preisstufe.get(j))){
                        quantity.set(j, quantity.get(j) + 1);
                        tripItems.get(j).addAll(ticketInformationHolders.get(i).getTripList());
                        contains = true;
                    }else{
                        tickets.add(currentTicket);
                        preisstufe.add(currentPreisstufe);
                        quantity.add(1);
                        tripItems.add(ticketInformationHolders.get(i).getTripList());
                        contains = true;
                    }
                }
            }
            if(!contains){
                tickets.add(currentTicket);
                preisstufe.add(currentPreisstufe);
                quantity.add(1);
                tripItems.add(ticketInformationHolders.get(i).getTripList());
            }
        }





        buildRecyclerView();
    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //TODO Detailansicht öffnen
            }

            @Override
            public void showDetails(int position) {
                ticketItems.get(position).setShowDetails();
                adapter.notifyDataSetChanged();
            }
        });
    }


}
