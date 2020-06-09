package com.example.lkjhgf.optimisation;


import android.util.Pair;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

public class TimeOptimisation {

    public static Pair<Integer, Integer> findBestTimeInterval(ArrayList<TripItem> tripItems, TimeTicket ticket) {
        int maxIndex = Integer.MAX_VALUE;
        int maxTrips = 0;

        for (int i = 0; i < tripItems.size(); i++) {
            if (ticket.isValidTrip(tripItems.get(i))) {
                maxIndex = i;
                maxTrips = 1;
                break;
            }
        }

        //Keine Fahrt liegt in dem Zeitintervall des Tickets
        if (maxIndex == Integer.MAX_VALUE) {
            return new Pair<>(0, 0);
        }
        long startTime = tripItems.get(maxIndex).getFirstDepartureTime().getTime();
        long endTime = tripItems.get(tripItems.size() - 1).getLastArrivalTime().getTime();
        //Zur Laufzeitoptimierung
        if (ticket.isValidTrip(tripItems.get(tripItems.size() - 1)) && endTime - startTime <= ticket.getMaxDuration()) {
            return new Pair<>(maxIndex, tripItems.size() - maxIndex);
        }

        for (int i = maxIndex; i < tripItems.size(); i++) {
            int counter = 1;
            for (int k = i + 1; k < tripItems.size(); k++) {
                startTime = tripItems.get(i).getFirstDepartureTime().getTime();
                endTime = tripItems.get(k).getLastArrivalTime().getTime();
                if (ticket.isValidTrip(tripItems.get(k)) && endTime - startTime <= ticket.getMaxDuration()) {
                    counter++;
                } else {
                    //TODO Überlappung von Fahrten verhindern
                    //break;
                }
            }
            if (counter > maxTrips) {
                maxTrips = counter;
                maxIndex = i;
            }
        }
        return new Pair<>(maxIndex, maxTrips);
    }

    public static ArrayList<TicketToBuy> optimierungPreisstufeD(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> tickets) {
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 1;
        ArrayList<TripItem> tripsD = collectTripsPreisstufe(allTrips, preisstufenIndex);
        if (tripsD.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<TicketToBuy> toBuyArrayList = new ArrayList<>();

        for (int ticketIndex = 0; ticketIndex < tickets.size(); ticketIndex++) {
            if (allTrips.size() >= tickets.get(ticketIndex).getMinNumTrips(preisstufenIndex)) {
                Pair<Integer, Integer> bestTicketInterval = findBestTimeInterval(tripsD, tickets.get(ticketIndex));
                while (bestTicketInterval.second >= tickets.get(ticketIndex).getMinNumTrips(preisstufenIndex)) {
                    TicketToBuy ticketToBuy = new TicketToBuy(tickets.get(ticketIndex), MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                    toBuyArrayList.add(ticketToBuy);
                    int i = 0;
                    for (ListIterator<TripItem> listIterator = tripsD.listIterator(); i < bestTicketInterval.first + bestTicketInterval.second; ) {
                        TripItem current = listIterator.next();
                        if (i >= bestTicketInterval.first) {
                            listIterator.remove();
                            ticketToBuy.addTripItem(current);
                            allTrips.remove(current);
                        }
                        i++;
                    }
                    bestTicketInterval = findBestTimeInterval(tripsD, tickets.get(ticketIndex));
                }
                if (toBuyArrayList.size() > 1) {
                    Collections.sort(toBuyArrayList, (o1, o2) -> Math.toIntExact(o1.getFirstDepartureTime().getTime() - o2.getFirstDepartureTime().getTime()));
                    sumUpTickets(toBuyArrayList, tickets, ticketIndex, preisstufenIndex);
                }
            }

        }
        return toBuyArrayList;
    }

    private static ArrayList<TripItem> collectTripsPreisstufe(ArrayList<TripItem> allTrips, int preisstufenIndex) {
        ArrayList<TripItem> trips = new ArrayList<>();
        for (ListIterator<TripItem> tripIterator = allTrips.listIterator(allTrips.size()); tripIterator.hasPrevious(); ) {
            TripItem current = tripIterator.previous();
            if (MainMenu.myProvider.getPreisstufenIndex(current.getPreisstufe()) == preisstufenIndex) {
                trips.add(0, current);
            } else if (MainMenu.myProvider.getPreisstufenIndex(current.getPreisstufe()) < preisstufenIndex) {
                break;
            }
        }
        return trips;
    }

    private static void sumUpTickets(ArrayList<TicketToBuy> oldTickets, ArrayList<TimeTicket> timeTickets, int startIndex, int preisstufenIndex) {
        for (int ticketIndex = startIndex; ticketIndex < timeTickets.size(); ticketIndex++) {
            TimeTicket currentTicket = timeTickets.get(ticketIndex);
            int price = currentTicket.getPrice(preisstufenIndex);
            long duration = currentTicket.getMaxDuration();
            ArrayList<TicketToBuy> newTickets = new ArrayList<>();

            //TODO i++??
            for (int i = 0; i < oldTickets.size() && oldTickets.size() > 1; i++) {
                TicketToBuy current = oldTickets.get(i);
                long firstDate = current.getFirstDepartureTime().getTime();
                ArrayList<Integer> collectedTickets = new ArrayList<>();
                collectedTickets.add(i);
                price -= current.getTicket().getPrice(MainMenu.myProvider.getPreisstufenIndex(current.getPreisstufe()));
                for (int j = i + 1; j < oldTickets.size(); j++) {
                    long endDate = oldTickets.get(j).getLastArrivalTime().getTime();
                    if (endDate - firstDate <= duration) {
                        collectedTickets.add(j);
                        price -= oldTickets.get(j).getTicket().getPrice(MainMenu.myProvider.getPreisstufenIndex(oldTickets.get(j).getPreisstufe()));
                    } else {
                        break;
                    }
                }
                if (price <= 0) {
                    TicketToBuy nextTicket = new TicketToBuy(currentTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                    newTickets.add(nextTicket);
                    for (Integer t : collectedTickets) {
                        nextTicket.addTripItems(oldTickets.get(t).getTripList());
                    }
                    for (ListIterator<Integer> t = collectedTickets.listIterator(collectedTickets.size()); t.hasPrevious(); ) {
                        oldTickets.remove(t.previous().intValue());
                        t.remove();
                    }
                }
                collectedTickets.clear();
                price = timeTickets.get(ticketIndex).getPrice(preisstufenIndex);
            }
            oldTickets.addAll(newTickets);
            Collections.sort(oldTickets, (o1, o2) -> Math.toIntExact(o1.getFirstDepartureTime().getTime() - o2.getFirstDepartureTime().getTime()));
        }
    }

    public static void checkTicketForOtherTrips(ArrayList<TripItem> tripItems, ArrayList<TicketToBuy> ticketsToBuy) {
        for (TicketToBuy ticketToBuy : ticketsToBuy) {
            TimeTicket timeTicket = (TimeTicket) ticketToBuy.getTicket();
            for (Iterator<TripItem> tripItemIterator = tripItems.iterator(); tripItemIterator.hasNext(); ) {
                TripItem current = tripItemIterator.next();
                if (MainMenu.myProvider.getPreisstufenIndex(current.getPreisstufe()) <= MainMenu.myProvider.getPreisstufenIndex(ticketToBuy.getPreisstufe())) {
                    if (current.getFirstDepartureTime().getTime() >= ticketToBuy.getFirstDepartureTime().getTime()
                            && current.getLastArrivalTime().getTime() <= timeTicket.getMaxDuration() + ticketToBuy.getFirstDepartureTime().getTime()
                            && timeTicket.isValidTrip(current)) {
                        ticketToBuy.addTripItem(current);
                        tripItemIterator.remove();
                    }
                }
            }
        }
    }

}
