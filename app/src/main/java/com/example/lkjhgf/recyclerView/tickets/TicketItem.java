package com.example.lkjhgf.recyclerView.tickets;

import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.VRRpreisstufenComparator;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Iterator;

public class TicketItem implements Comparable<TicketItem>{

    private boolean showDetails;
    private Ticket ticket;
    private String preisstufe;
    private int quantity;
    private ArrayList<TripItem> tripItems;


    public TicketItem(Ticket ticket, String preisstufe, int quantity, ArrayList<TripItem> tripItems){
        this.ticket = ticket;
        this.preisstufe = preisstufe;
        this.quantity = quantity;
        this.tripItems = tripItems;
        removeDuplicatedTrips();
    }

    private void removeDuplicatedTrips(){
        Iterator<TripItem> it = tripItems.iterator();
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
        return preisstufe;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public int getQuantity(){
        return quantity;
    }

    public ArrayList<TripItem> getTripItems(){
        return tripItems;
    }

    @Override
    public int compareTo(TicketItem o) {
        if(ticket.equals(o.ticket)){
            return VRRpreisstufenComparator.getPreisstufenIndex(preisstufe) - VRRpreisstufenComparator.getPreisstufenIndex(o.preisstufe);
        }else{
            if(ticket instanceof NumTicket && o.ticket instanceof NumTicket){
                Integer thisNumTrip = ((NumTicket)ticket).getNumTrips();
                Integer otherNumTrip = ((NumTicket) o.ticket).getNumTrips();
                //TODO ggf. Reihenfolge ändern
                return  thisNumTrip.compareTo(otherNumTrip);
            }else{
                //TODO Zeittickets
                return ticket.getName().compareTo(o.ticket.getName());
            }
        }
    }
}
