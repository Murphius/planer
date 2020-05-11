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

public class RecyclerViewTicketOverview {

    private Activity activity;
    private View view;
    private AllTickets allTickets;

    private ArrayList<TicketItem> ticketItems;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TicketAdapter adapter;

    public RecyclerViewTicketOverview(Activity activity, View view, AllTickets allTickets) {
        this.activity = activity;
        this.view = view;
        this.allTickets = allTickets;

        recyclerView = view.findViewById(R.id.recyclerView7);

        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketsToBuy = allTickets.getTickets();
        ArrayList<TicketToBuy> ticketsToBuy = new ArrayList<>();
        for(Fare.Type type : allTicketsToBuy.keySet()){
            ticketsToBuy.addAll(allTicketsToBuy.get(type));
        }
        ArrayList<TicketToBuy> tickets = new ArrayList<>();
        ArrayList<Integer> quantity = new ArrayList<>();
        ticketItems = new ArrayList<>();

        if(! ticketsToBuy.isEmpty()){
            Iterator<TicketToBuy> it = ticketsToBuy.iterator();
            TicketToBuy current = it.next();
            tickets.add(current);
            quantity.add(1);
            while (it.hasNext()){
                TicketToBuy next = it.next();
                if(current.equals(next)){
                    tickets.get(tickets.size()-1).getTripList().addAll(next.getTripList());
                    quantity.set(quantity.size()-1, quantity.get(quantity.size()-1)+1);
                }else{
                    tickets.add(next);
                    quantity.add(1);
                }
                current = next;
            }

            ticketItems = new ArrayList<>();
            for (int i = 0; i < quantity.size(); i++) {
                tickets.get(i).getTripList().sort((o1, o2) -> {
                    if(o1.getTrip().getFirstDepartureTime().before(o2.getTrip().getFirstDepartureTime())){
                        return -1;
                    }else if(o1.getTrip().getFirstDepartureTime().getTime() == o2.getTrip().getFirstDepartureTime().getTime()){
                        return 0;
                    }else{
                        return 1;
                    }
                });
                ticketItems.add(new TicketItem(tickets.get(i), quantity.get(i)));
            }
        }

        adapter = new TicketAdapter(activity, ticketItems);
        buildRecyclerView();
    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            ticketItems.get(position).setShowDetails();
            adapter.notifyDataSetChanged();
        });
    }


}
