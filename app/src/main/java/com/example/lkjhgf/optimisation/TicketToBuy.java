package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Calendar;

public class TicketToBuy implements Comparable<TicketToBuy>{
    private Ticket ticket;
    private String preisstufe;
    private ArrayList<TripItem> tripList;
    private int freeTrips;

    public TicketToBuy(Ticket ticket, String preisstufe){
        this.ticket = ticket;
        this.preisstufe = preisstufe;
        tripList = new ArrayList<>();
        calculateFreeTrips();
    }

    public TicketToBuy(Ticket ticket, String preisstufe, ArrayList<TripItem> tripList){
        this.ticket = ticket;
        this.preisstufe = preisstufe;
        this.tripList = tripList;
        calculateFreeTrips();
    }

    private void calculateFreeTrips(){
        if(freeTrips != Integer.MAX_VALUE){
            if(ticket instanceof NumTicket){
                freeTrips = ((NumTicket) ticket).getNumTrips() - tripList.size();
            }else{
                freeTrips = Integer.MAX_VALUE;
            }
        }
    }

    public void addTripItem(TripItem tripItem){
        tripList.add(0,tripItem);
        if(freeTrips != Integer.MAX_VALUE){
            freeTrips--;
        }
    }

    public ArrayList<TripItem> getTripList() {
        return tripList;
    }

    public String getPreisstufe() {
        return preisstufe;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public int getFreeTrips() {
        return freeTrips;
    }

    public boolean isFutureTicket() {
        for(TripItem tripItem : tripList){
            if(tripItem.getTrip().getFirstDepartureTime().before(Calendar.getInstance().getTime())){
                return false;
            }
        }
        return true;
    }

    public boolean isPastTicket(){
         for(TripItem tripItem : tripList){
             if(tripItem.getTrip().getLastArrivalTime().getTime() + (24*60*60*1000) > Calendar.getInstance().getTime().getTime()){
                 return false;
             }
         }
         return true;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof TicketToBuy)){
            return false;
        }
        TicketToBuy other = (TicketToBuy) o;
        if(ticket.equals(other.ticket)){
            if(preisstufe.equals(((TicketToBuy) o).preisstufe)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(TicketToBuy o) {
        if(ticket.equals(o.ticket)){
            return MainMenu.myProvider.getPreisstufenIndex(preisstufe) - MainMenu.myProvider.getPreisstufenIndex(o.preisstufe);
        }else{
            if(ticket instanceof NumTicket && o.ticket instanceof NumTicket){
                Integer thisNumTrip = ((NumTicket)ticket).getNumTrips();
                Integer otherNumTrip = ((NumTicket) o.ticket).getNumTrips();
                return  thisNumTrip.compareTo(otherNumTrip);
            }else{
                //TODO Zeittickets
                return ticket.getName().compareTo(o.ticket.getName());
            }
        }
    }
}
