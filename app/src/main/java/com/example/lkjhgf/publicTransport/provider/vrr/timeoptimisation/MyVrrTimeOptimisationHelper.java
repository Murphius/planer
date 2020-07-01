package com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.OptimisationUtil;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeOptimisation;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.publicTransport.provider.MyVRRprovider;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import de.schildbach.pte.dto.Fare;


public final class MyVrrTimeOptimisationHelper {
    public static  void timeOptimisation(HashMap<Integer, ArrayList<TripItem>> sortedUserClassTrips,
                                         HashMap<Fare.Type, ArrayList<TimeTicket>> timeTickets,
                                         HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketLists,
                                         Fare.Type type){
        ArrayList<TicketToBuy> ticketToBuyArrayList = new ArrayList<>();
        HashMap<String, ArrayList<TicketToBuy>> ticketsPerFarezone = new HashMap<>();
        for (Integer i : sortedUserClassTrips.keySet()) {
            int indexOfHappyHourTicket = timeTickets.get(type).indexOf(MyVRRprovider.happyHourTicket);
            if(indexOfHappyHourTicket > -1){
                timeTickets.get(type).remove(indexOfHappyHourTicket);
            }
            int indexOfVierStundenTicket = timeTickets.get(type).indexOf(MyVRRprovider.vierStundenTicket);
            if(indexOfVierStundenTicket > -1){
                timeTickets.get(type).remove(indexOfVierStundenTicket);
            }
            ArrayList<TicketToBuy> zws = TimeOptimisation.optimierungPreisstufeDNew(sortedUserClassTrips.get(i), timeTickets.get(type));
            TimeOptimisation.checkTicketForOtherTrips(sortedUserClassTrips.get(i), zws);
            ArrayList<TicketToBuy> e = ticketsPerFarezone.get("D");
            if(e == null){
                e = new ArrayList<>();
                ticketsPerFarezone.put("D", e);
            }
            e.addAll(zws);
            zws.clear();
            zws.addAll(TimeOptimisation.optimieriungPreisstufeC(sortedUserClassTrips.get(i), timeTickets.get(type)));
            e = ticketsPerFarezone.get("C");
            if(e == null){
                e = new ArrayList<>();
                ticketsPerFarezone.put("C", e);
            }
            e.addAll(zws);
            zws.clear();
            zws.addAll(TimeOptimisation.optimierungPreisstufeB(sortedUserClassTrips.get(i), timeTickets.get(type)));
            e = ticketsPerFarezone.get("B");
            if(e == null){
                e = new ArrayList<>();
                ticketsPerFarezone.put("B", e);
            }
            e.addAll(zws);
            zws.clear();
            if(indexOfVierStundenTicket > -1){
                timeTickets.get(type).add(indexOfVierStundenTicket, MyVRRprovider.vierStundenTicket);
            }
            if(indexOfHappyHourTicket > -1){
                timeTickets.get(type).add(indexOfHappyHourTicket, MyVRRprovider.happyHourTicket);
            }
            zws.addAll(TimeOptimisation.optimierungPreisstufeA(sortedUserClassTrips.get(i), timeTickets.get(type)));
            e = ticketsPerFarezone.get("A");
            if(e == null){
                e = new ArrayList<>();
                ticketsPerFarezone.put("A", e);
            }
            e.addAll(zws);
        }
        for(String s : ticketsPerFarezone.keySet()){
            MyVRRprovider.sumUpTickets(ticketsPerFarezone.get(s));
            ticketToBuyArrayList.addAll(ticketsPerFarezone.get(s));
        }

        for (TicketToBuy ticket : ticketToBuyArrayList) {
            for (TripItem tripItem : ticket.getTripList()) {
                tripItem.addTicket(ticket);
            }
        }
        allTicketLists.put(type, ticketToBuyArrayList);
    }

    public static ArrayList<TripItem> prepareNumTicketOptimisation(HashMap<Integer, ArrayList<TripItem>> sortedUserClassTrips,
                                                                   Fare.Type type){
        class tripCounter {
            int quantity;
            TripItem tripItem;

            tripCounter(TripItem tripItem) {
                this.tripItem = tripItem;
                quantity = 1;
            }

            public boolean equals(Object o) {
                if (!(o instanceof tripCounter)) {
                    return false;
                }
                return ((tripCounter) o).tripItem.getTripID().equals(tripItem.getTripID());
            }
        }

        ArrayList<tripCounter> trips = new ArrayList<>();
        for (Integer i : sortedUserClassTrips.keySet()) {
            for (TripItem item : sortedUserClassTrips.get(i)) {
                if (item.getUserClassWithoutTicket(type) != 0) {
                    int index = trips.indexOf(new tripCounter(item));
                    if (index > -1) {
                        trips.get(index).quantity++;
                    } else {
                        trips.add(new tripCounter(item));
                    }
                }
            }
        }
        ArrayList<TripItem> tripItemArrayList = new ArrayList<>();
        for (tripCounter t : trips) {
            for (int j = 1; j <= t.quantity; j++) {
                tripItemArrayList.add(t.tripItem);
            }
        }
        return tripItemArrayList;
    }

    public static void sumUpNumAndTimeAndOldTickets(HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketLists,
                                              HashMap<Fare.Type, ArrayList<TicketToBuy>> numTicket,
                                              HashMap<Fare.Type, ArrayList<TicketToBuy>> activeTickets){
        ArrayList<TicketToBuy> adultTimeTickets = allTicketLists.get(Fare.Type.ADULT);
        ArrayList<TicketToBuy> adultNumTickets = numTicket.get(Fare.Type.ADULT);
        ArrayList<TicketToBuy> adultOldTickets = activeTickets.get(Fare.Type.ADULT);
        if (adultTimeTickets == null) {
            adultTimeTickets = new ArrayList<>();
        }
        if (adultNumTickets == null) {
            adultNumTickets = new ArrayList<>();
        }
        if(adultOldTickets == null){
            adultOldTickets = new ArrayList<>();
        }
        ArrayList<TicketToBuy> childrenTimeTickets = allTicketLists.get(Fare.Type.CHILD);
        ArrayList<TicketToBuy> childrenNumTickets = numTicket.get(Fare.Type.CHILD);
        ArrayList<TicketToBuy> childrenOldTickets = activeTickets.get(Fare.Type.CHILD);
        if (childrenTimeTickets == null) {
            childrenTimeTickets = new ArrayList<>();
        }
        if (childrenNumTickets == null) {
            childrenNumTickets = new ArrayList<>();
        }
        if(childrenOldTickets == null){
            childrenOldTickets = new ArrayList<>();
        }

        adultTimeTickets.addAll(adultNumTickets);
        adultTimeTickets.addAll(adultOldTickets);
        childrenTimeTickets.addAll(childrenNumTickets);
        childrenTimeTickets.addAll(childrenOldTickets);
    }

    public static void optimisationWithOldTickets(HashMap<Fare.Type, ArrayList<TripItem>> trips, HashMap<Fare.Type, ArrayList<TicketToBuy>> activeTickets){
        HashMap<Fare.Type, ArrayList<TicketToBuy>> numTicketsHM = new HashMap<>();
        for (Fare.Type type: activeTickets.keySet()){
            ArrayList<TicketToBuy> timeTickets = new ArrayList<>();
            ArrayList<TicketToBuy> numTickets = new ArrayList<>();
            if(trips.get(type) == null){
                continue;
            }
            for(TicketToBuy ticketToBuy : activeTickets.get(type)){
                if(ticketToBuy.getTicket() instanceof NumTicket){
                    numTickets.add(ticketToBuy);
                }else{
                    timeTickets.add(ticketToBuy);
                }
            }
            for(Iterator<TripItem> tripItemIterator = trips.get(type).iterator(); tripItemIterator.hasNext();){
                TripItem current = tripItemIterator.next();
                if(current.getUserClassWithoutTicket(type) == 0){
                    continue;
                }
                for(TicketToBuy ticketToBuy : timeTickets){
                    TimeTicket t = (TimeTicket)ticketToBuy.getTicket();
                    if(t.isValidTrip(current)){
                        if(current.getFirstDepartureTime().getTime() >= ticketToBuy.getFirstDepartureTime().getTime()
                                && current.getLastArrivalTime().getTime() <= ticketToBuy.getFirstDepartureTime().getTime() + t.getMaxDuration()){
                            if(MainMenu.myProvider.getPreisstufenIndex(ticketToBuy.getPreisstufe()) <=3){
                                //TODO hier auf Waben und Tarifgebiet eingehen für die Preisstufe A
                            }else if(ticketToBuy.checkFarezone(current)){
                                ticketToBuy.addTripItem(current);
                                current.addTicket(ticketToBuy);
                            }
                        }
                    }
                }
            }
            numTicketsHM.put(type, numTickets);
        }
        OptimisationUtil.optimisationWithOldTickets(numTicketsHM, trips);
    }

    public static void sumUpTicketsUpTo5Persons(ArrayList<TicketToBuy> ticketList,
                            ArrayList<TicketToBuy> allTickets,
                            int i){
        for (Iterator<TicketToBuy> iterator = ticketList.iterator(); iterator.hasNext(); ) {
            TicketToBuy current = iterator.next();
            iterator.remove();
            ArrayList<TicketToBuy> collectedTickets = new ArrayList<>();
            collectedTickets.add(current);
            while (iterator.hasNext() && collectedTickets.size() < 5) {
                TicketToBuy next = iterator.next();
                if (next.getLastArrivalTime().getTime() - current.getFirstDepartureTime().getTime() <= ((TimeTicket) current.getTicket()).getMaxDuration()
                && current.checkFarezones(next.getTripList())) {
                    collectedTickets.add(next);
                    iterator.remove();
                } else {
                    break;
                }
            }
            TicketToBuy newTicket;
            if(i == 1){
                newTicket = oneDayTicket(current.getPreisstufe(), collectedTickets.size());
            }else{
                newTicket = twoDayTicket(current.getPreisstufe(), collectedTickets.size());
            }
            for (TicketToBuy t : collectedTickets) {
                newTicket.addTripItems(t.getTripList());
            }
            if(collectedTickets.size() > 0){
                newTicket.setValidFarezones(collectedTickets.get(0).getValidFarezones(), collectedTickets.get(0).getMainRegionID(), collectedTickets.get(0).isZweiWabenTarif());
            }
            allTickets.add(newTicket);
        }
    }

    private static TicketToBuy oneDayTicket(String preisstufe, int numP){
        switch (numP){
            case 1: return new TicketToBuy(MyVRRprovider.tagesTicket_1, preisstufe);
            case 2: return new TicketToBuy(MyVRRprovider.tagesTicket_2, preisstufe);
            case 3: return new TicketToBuy(MyVRRprovider.tagesTicket_3, preisstufe);
            case 4: return new TicketToBuy(MyVRRprovider.tagesTicket_4, preisstufe);
            default: return new TicketToBuy(MyVRRprovider.tagesTicket_5, preisstufe);
        }
    }

    private static TicketToBuy twoDayTicket(String preisstufe, int numP){
        switch (numP){
            case 1: return new TicketToBuy(MyVRRprovider.zweiTagesTicket_1, preisstufe);
            case 2: return new TicketToBuy(MyVRRprovider.zweiTagesTicket_2, preisstufe);
            case 3: return new TicketToBuy(MyVRRprovider.zweiTagesTicket_3, preisstufe);
            case 4: return new TicketToBuy(MyVRRprovider.zweiTagesTicket_4, preisstufe);
            default: return new TicketToBuy(MyVRRprovider.zweiTagesTicket_5, preisstufe);
        }
    }
}
