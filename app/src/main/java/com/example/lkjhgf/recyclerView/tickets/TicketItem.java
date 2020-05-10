package com.example.lkjhgf.recyclerView.tickets;

import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Iterator;

public class TicketItem  implements Comparable<TicketItem>{

    private boolean showDetails;
    private TicketToBuy ticket;
    private int quantity;


    public TicketItem (TicketToBuy ticket, int quantity){
        this.ticket = ticket;
        this.quantity = quantity;
        removeDuplicatedTrips();
    }

    private void removeDuplicatedTrips(){
        Iterator<TripItem> it = ticket.getTripList().iterator();
        TripItem current = it.next();
        while (it.hasNext()){
            TripItem next = it.next();
            if(current.equals(next)){
                it.remove();
            }else{
                current = next;
            }
        }
    }

    public void setShowDetails() {
        showDetails = !showDetails;
    }

    public boolean getShowDetails(){
        return showDetails;
    }

    public String getPreisstufe() {
        return ticket.getPreisstufe();
    }

    public Ticket getTicket() {
        return ticket.getTicket();
    }

    public int getQuantity(){
        return quantity;
    }

    public ArrayList<TripItem> getTripItems(){
        return ticket.getTripList();
    }

    @Override
    public int compareTo(TicketItem o){
        return ticket.compareTo(o.ticket);
    }

}
