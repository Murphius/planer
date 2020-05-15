package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.UUID;

public class TicketToBuy implements Comparable<TicketToBuy> {
    private Ticket ticket;
    private String preisstufe;
    private ArrayList<TripQuantity> tripList;
    private int freeTrips;
    private UUID ticketID;

    /**
     *
     */
    public class TripQuantity {
        private int quantity;
        private TripItem trip;

        private TripQuantity(TripItem tripItem) {
            trip = tripItem;
            quantity = 1;
        }

        private void add() {
            quantity++;
        }

        boolean checkContains(TripItem tripItem) {
            return trip.getTrip().getId().equals(tripItem.getTrip().getId());
        }

        public int getQuantity() {
            return quantity;
        }

        public TripItem getTrip(){
            return trip;
        }
    }

    public TicketToBuy(Ticket ticket, String preisstufe) {
        this.ticket = ticket;
        this.preisstufe = preisstufe;
        tripList = new ArrayList<>();
        calculateFreeTrips();
        ticketID = UUID.randomUUID();
    }

    private void calculateFreeTrips() {
        if (freeTrips != Integer.MAX_VALUE) {
            if (ticket instanceof NumTicket) {
                freeTrips = ((NumTicket) ticket).getNumTrips() - tripList.size();
            } else {
                freeTrips = Integer.MAX_VALUE;
            }
        }
    }

    public void addTripItem(TripItem tripItem) {
        if (freeTrips != Integer.MAX_VALUE) {
            freeTrips--;
        }
        for(TripQuantity quantity : tripList){
            if(quantity.checkContains(tripItem)){
                quantity.add();
                return;
            }
        }
        tripList.add(new TripQuantity(tripItem));
    }

    public ArrayList<TripItem> getTripList() {
        ArrayList<TripItem> tripItems = new ArrayList<>();
        for(TripQuantity tripQuantity : tripList){
            tripItems.add(tripQuantity.trip);
        }
        return tripItems;
    }

    public ArrayList<TripQuantity> getTripQuantities(){
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
        for (TripQuantity tripItem : tripList) {
            if (tripItem.trip.getTrip().getFirstDepartureTime().before(Calendar.getInstance().getTime())) {
                return false;
            }
        }
        return true;
    }

    public boolean isPastTicket() {
        for (TripQuantity tripItem : tripList) {
            if (tripItem.trip.getTrip().getLastArrivalTime().getTime() + (24 * 60 * 60 * 1000) > Calendar.getInstance().getTime().getTime()) {
                return false;
            }
        }
        return true;
    }

    public UUID getTicketID() {
        return ticketID;
    }

    public boolean removeTrip(String tripID) {
        for (Iterator<TripQuantity> tripItemIterator = tripList.iterator(); tripItemIterator.hasNext(); ) {
            TripQuantity currentTrip = tripItemIterator.next();
            if (currentTrip.trip.getTrip().getId().equals(tripID)) {
                int quantity = currentTrip.quantity;
                tripItemIterator.remove();
                if (ticket instanceof NumTicket) {
                    if (freeTrips != Integer.MAX_VALUE) {
                        freeTrips+= quantity;
                    }
                    if (((NumTicket) ticket).getNumTrips() == freeTrips) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TicketToBuy)) {
            return false;
        }
        TicketToBuy other = (TicketToBuy) o;
        if (ticket.equals(other.ticket)) {
            if (preisstufe.equals(((TicketToBuy) o).preisstufe)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(TicketToBuy o) {
        if (ticket.equals(o.ticket)) {
            return MainMenu.myProvider.getPreisstufenIndex(preisstufe) - MainMenu.myProvider.getPreisstufenIndex(o.preisstufe);
        } else {
            if (ticket instanceof NumTicket && o.ticket instanceof NumTicket) {
                Integer thisNumTrip = ((NumTicket) ticket).getNumTrips();
                Integer otherNumTrip = ((NumTicket) o.ticket).getNumTrips();
                return thisNumTrip.compareTo(otherNumTrip);
            } else {
                //TODO Zeittickets
                return ticket.getName().compareTo(o.ticket.getName());
            }
        }
    }

    @Override
    public String toString() {
        return ticket.getName() + " " + getTicketID().toString();
    }
}
