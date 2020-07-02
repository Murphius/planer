package com.example.lkjhgf.optimisation;


import android.util.Pair;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.TicketToBuyTimeComporator;
import com.example.lkjhgf.helper.util.TripItemTimeComparator;
import com.example.lkjhgf.helper.util.TripQuantitiesTimeComparator;
import com.example.lkjhgf.publicTransport.provider.Farezone;
import com.example.lkjhgf.publicTransport.provider.MyVRRprovider;
import com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.FarezoneTrips;
import com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.VRR_Farezones;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import org.javatuples.Quartet;
import org.javatuples.Triplet;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;


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
                    break;
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
                    Collections.sort(toBuyArrayList, new TicketToBuyTimeComporator());
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

    public static ArrayList<TicketToBuy> optimierungPreisstufeDNew(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> tickets) {
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
                    Collections.sort(toBuyArrayList, new TicketToBuyTimeComporator());
                    sumUpTicketsNew(toBuyArrayList, tickets, ticketIndex + 1, preisstufenIndex);
                }
                checkTicketForOtherTrips(allTrips, toBuyArrayList);
                tripsD = collectTripsPreisstufe(allTrips, preisstufenIndex);
            }
        }
        Set<Farezone> farezoneList = MainMenu.myProvider.getFarezones();
        for (TicketToBuy ticket : toBuyArrayList) {
            ticket.setValidFarezones(farezoneList, 0);
        }
        return toBuyArrayList;
    }

    private static void sumUpTicketsNew(ArrayList<TicketToBuy> oldTickets, ArrayList<TimeTicket> timeTickets, int startIndex, int preisstufenIndex) {
        for (int ticketIndex = startIndex; ticketIndex < timeTickets.size(); ticketIndex++) {
            TimeTicket currentTicket = timeTickets.get(ticketIndex);
            int price = currentTicket.getPrice(preisstufenIndex);
            ArrayList<TicketToBuy> newTickets = new ArrayList<>();
            Triplet<Integer, Integer, Integer> bestIntervall = findBestTicketIntervall(oldTickets, currentTicket);
            while (price - bestIntervall.getValue2() < 0) {
                TicketToBuy newTicketToBuy = new TicketToBuy(currentTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                newTickets.add(newTicketToBuy);
                ArrayList<Integer> collectedTickets = new ArrayList<>();
                for (int i = bestIntervall.getValue0(); i < bestIntervall.getValue0() + bestIntervall.getValue1(); i++) {
                    collectedTickets.add(i);
                }
                for (Integer t : collectedTickets) {
                    newTicketToBuy.addTripItems(oldTickets.get(t).getTripList());
                }
                newTicketToBuy.setValidFarezones(oldTickets.get(collectedTickets.get(0)).getValidFarezones(), oldTickets.get(collectedTickets.get(0)).getMainRegionID(), oldTickets.get(0).isZweiWabenTarif());
                for (ListIterator<Integer> t = collectedTickets.listIterator(collectedTickets.size()); t.hasPrevious(); ) {
                    oldTickets.remove(t.previous().intValue());
                    t.remove();
                }
                collectedTickets.clear();
                bestIntervall = findBestTicketIntervall(oldTickets, currentTicket);
            }
            oldTickets.addAll(newTickets);
            Collections.sort(oldTickets, new TicketToBuyTimeComporator());
        }
    }

    private static Triplet<Integer, Integer, Integer> findBestTicketIntervall(ArrayList<TicketToBuy> ticketsToBuy, TimeTicket ticket) {
        int maxIndex = Integer.MAX_VALUE;
        int maxTrips = 0;
        int maxPrice = 0;

        for (int i = 0; i < ticketsToBuy.size(); i++) {
            boolean isValidTicket = true;
            for (TripItem tripItem : ticketsToBuy.get(i).getTripList()) {
                if (!ticket.isValidTrip(tripItem)) {
                    isValidTicket = false;
                    break;
                }
            }
            if (isValidTicket) {
                long startTime = ticketsToBuy.get(i).getFirstDepartureTime().getTime();
                long endTime = ticketsToBuy.get(i).getLastArrivalTime().getTime();
                if (endTime - startTime <= ticket.getMaxDuration()) {
                    maxIndex = i;
                    maxTrips = 1;
                    break;
                }
            }
        }
        if (maxIndex == Integer.MAX_VALUE) {
            return new Triplet<>(0, 0, 0);
        }

        for (int i = maxIndex; i < ticketsToBuy.size(); i++) {
            int price = ticketsToBuy.get(i).getTicket().getPrice(MainMenu.myProvider.getPreisstufenIndex(ticketsToBuy.get(i).getPreisstufe()));
            int counter = 1;
            for (int k = i + 1; k < ticketsToBuy.size(); k++) {
                boolean isValid = true;
                for (TripItem tripItem : ticketsToBuy.get(k).getTripList()) {
                    if (!ticket.isValidTrip(tripItem)) {
                        isValid = false;
                        break;
                    }
                }
                if (isValid) {
                    long startTime = ticketsToBuy.get(i).getFirstDepartureTime().getTime();
                    long endTime = ticketsToBuy.get(k).getLastArrivalTime().getTime();
                    if (endTime - startTime <= ticket.getMaxDuration()) {
                        price += ticketsToBuy.get(k).getTicket().getPrice(MainMenu.myProvider.getPreisstufenIndex(ticketsToBuy.get(k).getPreisstufe()));
                        counter++;
                    } else {
                        break;
                    }
                }
            }

            if (price > maxPrice) {
                maxPrice = price;
                maxTrips = counter;
                maxIndex = i;
            }
        }
        return new Triplet<>(maxIndex, maxTrips, maxPrice);
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
            Collections.sort(oldTickets, new TicketToBuyTimeComporator());
        }
    }

    public static void checkTicketForOtherTrips(ArrayList<TripItem> tripItems, ArrayList<TicketToBuy> ticketsToBuy) {
        for (TicketToBuy ticketToBuy : ticketsToBuy) {
            TimeTicket timeTicket = (TimeTicket) ticketToBuy.getTicket();
            for (Iterator<TripItem> tripItemIterator = tripItems.iterator(); tripItemIterator.hasNext(); ) {
                TripItem current = tripItemIterator.next();

                if (MainMenu.myProvider.getPreisstufenIndex(current.getPreisstufe()) <= MainMenu.myProvider.getPreisstufenIndex(ticketToBuy.getPreisstufe())) {
                    if(timeTicket.isValidTrip(current)){
                        if(checkIfTripIsInRegion(current, ticketToBuy.getValidFarezones())){
                            long minStartTime = Math.min(current.getFirstDepartureTime().getTime(), ticketToBuy.getFirstDepartureTime().getTime());
                            long maxEndTime = Math.max(current.getLastArrivalTime().getTime(), ticketToBuy.getLastArrivalTime().getTime());
                            if(maxEndTime - minStartTime <= timeTicket.getMaxDuration()){
                                ticketToBuy.addTripItem(current);
                                tripItemIterator.remove();
                                Collections.sort(ticketToBuy.getTripQuantities(), new TripQuantitiesTimeComparator());
                            }
                        }
                    }
                    /*if (current.getFirstDepartureTime().getTime() >= ticketToBuy.getFirstDepartureTime().getTime()
                            && current.getLastArrivalTime().getTime() <= timeTicket.getMaxDuration() + ticketToBuy.getFirstDepartureTime().getTime()
                            && timeTicket.isValidTrip(current)
                            && checkIfTripIsInRegion(current, ticketToBuy.getValidFarezones())) {
                        ticketToBuy.addTripItem(current);
                        tripItemIterator.remove();
                        Collections.sort(ticketToBuy.getTripList(), new TripItemTimeComparator());
                    }*/
                }
            }
        }
    }

    public static void checkTicketForOtherTripsZweiWaben(ArrayList<TripItem> allTrips, ArrayList<TicketToBuy> ticketsToBuy) {
        for (TicketToBuy ticketToBuy : ticketsToBuy) {
            TimeTicket timeTicket = (TimeTicket) ticketToBuy.getTicket();
            for (Iterator<TripItem> tripItemIterator = allTrips.iterator(); tripItemIterator.hasNext(); ) {
                TripItem current = tripItemIterator.next();
                if (MainMenu.myProvider.getPreisstufenIndex(current.getPreisstufe()) <= MainMenu.myProvider.getPreisstufenIndex(ticketToBuy.getPreisstufe())) {
                    if (timeTicket.isValidTrip(current)) {
                        if (checkIfTripIsInZweiWaben(current, ticketToBuy.getValidFarezones())) {
                            long minStartTime = Math.min(current.getFirstDepartureTime().getTime(), ticketToBuy.getLastArrivalTime().getTime());
                            long maxEndTime = Math.max(current.getLastArrivalTime().getTime(), ticketToBuy.getLastArrivalTime().getTime());
                            if (maxEndTime - minStartTime <= timeTicket.getMaxDuration()) {
                                ticketToBuy.addTripItem(current);
                                tripItemIterator.remove();
                                allTrips.remove(current);
                                Collections.sort(ticketToBuy.getTripQuantities(), new TripQuantitiesTimeComparator());
                            }
                        }
                    }
                    /*if (current.getFirstDepartureTime().getTime() >= ticketToBuy.getFirstDepartureTime().getTime()
                            && current.getLastArrivalTime().getTime() <= timeTicket.getMaxDuration() + ticketToBuy.getFirstDepartureTime().getTime()
                            )) {
                        ticketToBuy.addTripItem(current);
                        tripItemIterator.remove();
                        allTrips.remove(current);
                        Collections.sort(ticketToBuy.getTripList(), new TripItemTimeComparator());
                    }*/
                }
            }
        }
    }

    public static ArrayList<TicketToBuy> optimieriungPreisstufeC(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> timeTickets) {
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 2;
        ArrayList<TripItem> tripsC = collectTripsPreisstufe(allTrips, preisstufenIndex);

        if (tripsC.isEmpty()) {
            return new ArrayList<>();
        }

        HashMap<Integer, Set<FarezoneTrips>> possibleRegions = VRR_Farezones.regionenHashMap();
        HashMap<Integer, ArrayList<TicketToBuy>> ticketsPerRegion = new HashMap<>();


        for (int ticketIndex = 0; ticketIndex < timeTickets.size(); ticketIndex++) {
            TimeTicket possibleTicket = timeTickets.get(ticketIndex);
            boolean repeat = true;
            while (repeat) {
                if (allTrips.size() < possibleTicket.getMinNumTrips(preisstufenIndex)) {
                    break;
                }
                fahrtenRegionenZuweisen(tripsC, possibleRegions);
                HashMap<Integer, Triplet<Integer, Integer, ArrayList<TripItem>>> bestTicketIntervallPerRegion = new HashMap<>();
                for (Integer i : possibleRegions.keySet()) {
                    ArrayList<TripItem> regionTrips = checkNeighbourhood(possibleRegions.get(i));
                    if (!regionTrips.isEmpty()) {
                        Collections.sort(regionTrips, new TripItemTimeComparator());
                        Pair<Integer, Integer> bestTicketIntervall = findBestTimeInterval(regionTrips, possibleTicket);
                        if (bestTicketIntervall.second > 0) {
                            bestTicketIntervallPerRegion.put(i, new Triplet<>(bestTicketIntervall.first, bestTicketIntervall.second, regionTrips));
                        }
                    }
                }
                int max = 0;
                Integer maxRegion = 0;
                for (Integer i : bestTicketIntervallPerRegion.keySet()) {
                    if (bestTicketIntervallPerRegion.get(i).getValue1() > max) {
                        max = bestTicketIntervallPerRegion.get(i).getValue1();
                        maxRegion = i;
                    } else if (bestTicketIntervallPerRegion.get(i).getValue1() == max) {
                        int currentRegionSize = checkNeighbourhood(possibleRegions.get(i)).size();
                        int maxRegionSize = checkNeighbourhood(possibleRegions.get(maxRegion)).size();
                        if (currentRegionSize > maxRegionSize) {
                            maxRegion = i;
                        } else if (ticketsPerRegion.get(i) != null && ticketsPerRegion.get(maxRegion) != null) {
                            if (ticketsPerRegion.get(i).size() > ticketsPerRegion.get(maxRegion).size()) {
                                maxRegion = i;
                            }
                        } else if (ticketsPerRegion.get(i) != null) {
                            maxRegion = i;
                        }
                    }
                }
                if (maxRegion != 0) {
                    if (bestTicketIntervallPerRegion.get(maxRegion).getValue1() >= possibleTicket.getMinNumTrips(preisstufenIndex)) {
                        TicketToBuy ticket = new TicketToBuy(possibleTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                        ticket.setValidFarezones(changeFarezoneTripsToFarezone(possibleRegions.get(maxRegion)), maxRegion);
                        for (int j = bestTicketIntervallPerRegion.get(maxRegion).getValue0(); j < bestTicketIntervallPerRegion.get(maxRegion).getValue0() + bestTicketIntervallPerRegion.get(maxRegion).getValue1(); j++) {
                            TripItem tripItem = bestTicketIntervallPerRegion.get(maxRegion).getValue2().get(j);
                            ticket.addTripItem(tripItem);
                            tripsC.remove(tripItem);
                            allTrips.remove(tripItem);
                        }
                        ArrayList<TicketToBuy> ticketToBuyArrayList = ticketsPerRegion.get(maxRegion);
                        if (ticketToBuyArrayList == null) {
                            ticketToBuyArrayList = new ArrayList<>();
                            ticketsPerRegion.put(maxRegion, ticketToBuyArrayList);
                        }
                        ticketToBuyArrayList.add(ticket);
                    } else {
                        repeat = false;
                    }
                } else {
                    repeat = false;
                }
            }
            //Zusammenfassen der Tickets
            for (int region : ticketsPerRegion.keySet()) {
                if (ticketsPerRegion.get(region).size() > 1) {
                    Collections.sort(ticketsPerRegion.get(region), new TicketToBuyTimeComporator());
                    sumUpTicketsNew(ticketsPerRegion.get(region), timeTickets, ticketIndex + 1, preisstufenIndex);
                    checkTicketForOtherTrips(allTrips, ticketsPerRegion.get(region));
                    tripsC = collectTripsPreisstufe(allTrips, preisstufenIndex);
                }
            }


        }
        ArrayList<TicketToBuy> result = new ArrayList<>();

        for (Integer region : ticketsPerRegion.keySet()) {
            ArrayList<TicketToBuy> z = ticketsPerRegion.get(region);
            if (z != null) {
                result.addAll(z);
            }
        }

        return result;
    }

    public static ArrayList<TicketToBuy> optimierungPreisstufeB(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets) {
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 3;
        ArrayList<TripItem> tripsB = collectTripsPreisstufe(allTrips, preisstufenIndex);
        if (tripsB.isEmpty()) {
            return new ArrayList<>();
        }
        Graph<FarezoneTrips, DefaultEdge> vrr_farezone_graph = VRR_Farezones.create_vrr_farezone_graph();
        HashMap<Farezone, ArrayList<TicketToBuy>> ticketsPerRegion = new HashMap<>();
        HashMap<Farezone, Set<Farezone>> regionen = new HashMap<>();
        for (int i = 0; i < possibleTickets.size(); i++) {
            TimeTicket possibleTicket = possibleTickets.get(i);
            //Laufzeitoptimierung: Egal wie die Fahrten über das Gebiet verteilt sind, wenn die gesamte Anzahl kleiner ist, als die
            //Anzahl benötigter Fahrten, damit das Ticket sich lohnt, ist keine weitere Betrachtung nötig
            boolean repeat = true;
            while (repeat) {
                //Laufzeitoptimierung: Egal wie die Fahrten über das Gebiet verteilt sind, wenn die gesamte Anzahl kleiner ist, als die
                //Anzahl benötigter Fahrten, damit das Ticket sich lohnt, ist keine weitere Betrachtung nötig
                if (tripsB.size() < possibleTicket.getMinNumTrips(preisstufenIndex)) {
                    break;
                }
                fahrtenKnotenZuweisen(vrr_farezone_graph, tripsB);
                HashMap<Farezone, Quartet<Integer, Integer, ArrayList<TripItem>, Set<FarezoneTrips>>> bestTicketIntervalPerFarezone = new HashMap<>();
                for (FarezoneTrips farezone : vrr_farezone_graph.vertexSet()) {
                    //D.h. das Tarifgebiet ist ein Zentralgebiet
                    Set<DefaultEdge> edges = vrr_farezone_graph.outgoingEdgesOf(farezone);
                    if (!edges.isEmpty()) {
                        Set<FarezoneTrips> neighbourhood = new HashSet<>();
                        neighbourhood.add(farezone);
                        for (DefaultEdge e : edges) {
                            neighbourhood.add(vrr_farezone_graph.getEdgeTarget(e));
                        }
                        ArrayList<TripItem> neighbourhoodTrips = checkNeighbourhood(neighbourhood);
                        if (!neighbourhoodTrips.isEmpty()) {
                            Collections.sort(neighbourhoodTrips, new TripItemTimeComparator());
                            Pair<Integer, Integer> bestTicketIntervall = findBestTimeInterval(neighbourhoodTrips, possibleTicket);
                            //Nur Regionen mit mindestens einer Fahrt sind von Interesse
                            if (bestTicketIntervall.second > 0) {
                                bestTicketIntervalPerFarezone.put(farezone.getFarezone(), new Quartet(bestTicketIntervall.first, bestTicketIntervall.second, neighbourhoodTrips, neighbourhood));
                            }
                        }
                    }
                }
                int max = 0;
                Farezone maxFarezone = null;
                for (Farezone f : bestTicketIntervalPerFarezone.keySet()) {
                    if (bestTicketIntervalPerFarezone.get(f).getValue1() > max) {
                        max = bestTicketIntervalPerFarezone.get(f).getValue1();
                        maxFarezone = f;
                    } else if (bestTicketIntervalPerFarezone.get(f).getValue1() == max) {
                        int currentNeighbourhoodSize = checkNeighbourhood(bestTicketIntervalPerFarezone.get(f).getValue3()).size();
                        int maxNeighbourhoodSize = checkNeighbourhood(bestTicketIntervalPerFarezone.get(maxFarezone).getValue3()).size();
                        if (currentNeighbourhoodSize > maxNeighbourhoodSize) {
                            maxFarezone = f;
                        } else if (ticketsPerRegion.get(f) != null && ticketsPerRegion.get(maxFarezone) != null) {
                            if (ticketsPerRegion.get(f).size() > ticketsPerRegion.get(maxFarezone).size()) {
                                maxFarezone = f;
                            }
                        } else if (ticketsPerRegion.get(f) != null) {
                            maxFarezone = f;
                        }
                    }
                }
                if (maxFarezone != null) {
                    if (bestTicketIntervalPerFarezone.get(maxFarezone).getValue1() >= possibleTicket.getMinNumTrips(preisstufenIndex)) {
                        //Ticketerstellen mit den Fahrten
                        TicketToBuy ticketToBuy = new TicketToBuy(possibleTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                        for (int j = bestTicketIntervalPerFarezone.get(maxFarezone).getValue0(); j < bestTicketIntervalPerFarezone.get(maxFarezone).getValue0() + bestTicketIntervalPerFarezone.get(maxFarezone).getValue1(); j++) {
                            TripItem tripItem = bestTicketIntervalPerFarezone.get(maxFarezone).getValue2().get(j);
                            ticketToBuy.addTripItem(tripItem);
                            //Fahrten aus der Liste der zu optimierenden Fahrten entfernen
                            allTrips.remove(tripItem);
                            tripsB.remove(tripItem);
                        }
                        Set<Farezone> regionenSet = regionen.get(maxFarezone);
                        if (regionenSet == null) {
                            regionenSet = new HashSet<>();
                            for (FarezoneTrips farezoneTrips : bestTicketIntervalPerFarezone.get(maxFarezone).getValue3()) {
                                regionenSet.add(new Farezone(farezoneTrips.getFarezone()));
                            }
                            regionen.put(maxFarezone, regionenSet);
                        }
                        ticketToBuy.setValidFarezones(regionenSet, maxFarezone.getId());
                        //Speichern des Tickets
                        ArrayList<TicketToBuy> ticketToBuyArrayList = ticketsPerRegion.get(maxFarezone);
                        if (ticketToBuyArrayList == null) {
                            ticketToBuyArrayList = new ArrayList<>();
                            ticketsPerRegion.put(maxFarezone, ticketToBuyArrayList);
                        }
                        ticketToBuyArrayList.add(ticketToBuy);
                    } else {
                        repeat = false;
                    }
                } else {
                    repeat = false;
                }
            }
            //Zusammenfassen der Tickets
            for (Farezone farezone : ticketsPerRegion.keySet()) {
                if (ticketsPerRegion.get(farezone).size() > 1) {
                    Collections.sort(ticketsPerRegion.get(farezone), new TicketToBuyTimeComporator());
                    sumUpTicketsNew(ticketsPerRegion.get(farezone), possibleTickets, i + 1, preisstufenIndex);
                }
                //gucken ob das Ticket auch für andere Fahrten anwendbar ist
                checkTicketForOtherTrips(allTrips, ticketsPerRegion.get(farezone));
                tripsB = collectTripsPreisstufe(allTrips, preisstufenIndex);
                //Ticket -> Geltungsbereich zuweisen
                for (TicketToBuy ticket : ticketsPerRegion.get(farezone)) {
                    ticket.setValidFarezones(regionen.get(farezone), farezone.getId());
                }
            }
        }

        ArrayList<TicketToBuy> result = new ArrayList<>();

        for (Farezone f : ticketsPerRegion.keySet()) {
            result.addAll(ticketsPerRegion.get(f));
        }
        return result;
    }

    public static ArrayList<TicketToBuy> optimierungPreisstufeA(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets) {
        ArrayList<TicketToBuy> result = new ArrayList<>();
        ArrayList<TimeTicket> zweiWabenTicketList = new ArrayList<>(possibleTickets);
        ArrayList<TimeTicket> tarifgebietTicketList = new ArrayList<>(possibleTickets);

        //A3
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 4;
        int indexOfVierStundenTicket_2 = zweiWabenTicketList.indexOf(MyVRRprovider.vierStundenTicket);
        if (indexOfVierStundenTicket_2 > -1) {
            zweiWabenTicketList.remove(indexOfVierStundenTicket_2);
            tarifgebietTicketList.remove(indexOfVierStundenTicket_2);
        }
        result.addAll(optimierungPreisstufeA(allTrips, tarifgebietTicketList, zweiWabenTicketList, preisstufenIndex));

        //A2
        preisstufenIndex--;
        if (indexOfVierStundenTicket_2 > -1) {
            tarifgebietTicketList.add(indexOfVierStundenTicket_2, MyVRRprovider.vierStundenTicket);
        }
        result.addAll(optimierungPreisstufeA(allTrips, tarifgebietTicketList, zweiWabenTicketList, preisstufenIndex));

        //A1
        preisstufenIndex--;
        result.addAll(optimierungPreisstufeA(allTrips, tarifgebietTicketList, zweiWabenTicketList, preisstufenIndex));
        return result;
    }

    public static ArrayList<TicketToBuy> optimierungPreisstufeA(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTicketsTarifgebiet, ArrayList<TimeTicket> possibleTicketsZweiWaben, int preisstufenIndex) {
        ArrayList<TicketToBuy> result = new ArrayList<>();
        ArrayList<TripItem> tripsPreisstufe = collectTripsPreisstufe(allTrips, preisstufenIndex);
        if (tripsPreisstufe.isEmpty()) {
            return result;
        }
        HashMap<Farezone, ArrayList<TripItem>> tarifgebietTrips = new HashMap<>();
        HashMap<HashSet<Integer>, ArrayList<TripItem>> zweiWabenTarif = new HashMap<>();
        splitTrips(tripsPreisstufe, tarifgebietTrips, zweiWabenTarif);
        //Tarifgebiet Optimierung
        result.addAll(tarifgebietOptimierung(tarifgebietTrips, allTrips, possibleTicketsTarifgebiet, preisstufenIndex));
        //Zwei Waben Optimierung
        result.addAll(zweiWabenOptimierung(zweiWabenTarif, allTrips, possibleTicketsZweiWaben, preisstufenIndex));
        return result;
    }

    public static ArrayList<TicketToBuy> optimierungPreisstufeA3(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets) {
        ArrayList<TicketToBuy> result = new ArrayList<>();
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 4;
        ArrayList<TripItem> tripsA3 = collectTripsPreisstufe(allTrips, preisstufenIndex);
        if (tripsA3.isEmpty()) {
            return result;
        }
        HashMap<Farezone, ArrayList<TripItem>> tarifgebietTrips = new HashMap<>();
        HashMap<HashSet<Integer>, ArrayList<TripItem>> zweiWabenTarif = new HashMap<>();
        splitTrips(tripsA3, tarifgebietTrips, zweiWabenTarif);
        //Tarifgebiet Optimierung
        result.addAll(tarifgebietOptimierung(tarifgebietTrips, allTrips, possibleTickets, preisstufenIndex));
        //Zwei Waben Optimierung
        result.addAll(zweiWabenOptimierung(zweiWabenTarif, allTrips, possibleTickets, preisstufenIndex));
        return result;
    }

    public static ArrayList<TicketToBuy> optimierungPreisstufeA2(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets) {
        ArrayList<TicketToBuy> result = new ArrayList<>();
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 5;
        ArrayList<TripItem> tripsA2 = collectTripsPreisstufe(allTrips, preisstufenIndex);
        if (tripsA2.isEmpty()) {
            return result;
        }
        HashMap<Farezone, ArrayList<TripItem>> tarifgebietTrips = new HashMap<>();
        HashMap<HashSet<Integer>, ArrayList<TripItem>> zweiWabenTarif = new HashMap<>();
        splitTrips(tripsA2, tarifgebietTrips, zweiWabenTarif);
        //Tarifgebiet Optimierung
        result.addAll(tarifgebietOptimierung(tarifgebietTrips, allTrips, possibleTickets, preisstufenIndex));
        ArrayList<TimeTicket> ticketsA3 = new ArrayList<>(possibleTickets);
        ticketsA3.remove(MyVRRprovider.vierStundenTicket);
        result.addAll(zweiWabenOptimierung(zweiWabenTarif, allTrips, possibleTickets, preisstufenIndex));
        return result;
    }

    public static ArrayList<TicketToBuy> optimierungPreisstufeA1(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets) {
        ArrayList<TicketToBuy> result = new ArrayList<>();
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 6;
        ArrayList<TripItem> tripsA1 = collectTripsPreisstufe(allTrips, preisstufenIndex);
        if (tripsA1.isEmpty()) {
            return result;
        }
        HashMap<Farezone, ArrayList<TripItem>> tarifgebietTrips = new HashMap<>();
        HashMap<HashSet<Integer>, ArrayList<TripItem>> zweiWabenTarif = new HashMap<>();
        splitTrips(tripsA1, tarifgebietTrips, zweiWabenTarif);
        //Tarifgebiet Optimierung
        result.addAll(tarifgebietOptimierung(tarifgebietTrips, allTrips, possibleTickets, preisstufenIndex));
        //Zwei Waben Optimierung: Dabei ist das 4h Ticket NICHT erlaubt
        possibleTickets.remove(MyVRRprovider.vierStundenTicket);
        result.addAll(zweiWabenOptimierung(zweiWabenTarif, allTrips, possibleTickets, preisstufenIndex));
        return result;
    }

    private static ArrayList<TicketToBuy> zweiWabenOptimierung(HashMap<HashSet<Integer>, ArrayList<TripItem>> zweiWabenTarif, ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets, int preisstufenIndex) {
        //Zwei Waben Optimierung
        ArrayList<TicketToBuy> result = new ArrayList<>();
        for (Set<Integer> zweiWaben : zweiWabenTarif.keySet()) {
            ArrayList<TicketToBuy> currentTicketList = new ArrayList<>();
            ArrayList<TripItem> tarfigebietTrips = zweiWabenTarif.get(zweiWaben);
            Collections.sort(tarfigebietTrips, new TripItemTimeComparator());
            for (int ticketIndex = 0; ticketIndex < possibleTickets.size(); ticketIndex++) {
                TimeTicket possibleTicket = possibleTickets.get(ticketIndex);
                while (tarfigebietTrips.size() >= possibleTicket.getMinNumTrips(preisstufenIndex)) {
                    Pair<Integer, Integer> bestTicketIntervall = findBestTimeInterval(tarfigebietTrips, possibleTicket);
                    if (bestTicketIntervall.second < possibleTicket.getMinNumTrips(preisstufenIndex)) {
                        break;
                    }
                    TicketToBuy ticketToBuy = new TicketToBuy(possibleTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                    int i = 0;
                    for (ListIterator<TripItem> listIterator = tarfigebietTrips.listIterator(); i < bestTicketIntervall.first + bestTicketIntervall.second; ) {
                        TripItem current = listIterator.next();
                        if (i >= bestTicketIntervall.first) {
                            listIterator.remove();
                            ticketToBuy.addTripItem(current);
                            allTrips.remove(current);
                        }
                        i++;
                    }
                    ArrayList<Integer> list = new ArrayList<>(zweiWaben);
                    Set<Farezone> x = new HashSet<>();
                    x.add(new Farezone(list.get(0), ""));
                    x.add(new Farezone(list.get(1), ""));
                    ticketToBuy.setValidFarezones(x, list.get(0), true);
                    currentTicketList.add(ticketToBuy);
                }
                if (currentTicketList.size() > 1) {
                    Collections.sort(currentTicketList, new TicketToBuyTimeComporator());
                    sumUpTicketsNew(currentTicketList, possibleTickets, ticketIndex + 1, preisstufenIndex);
                }
                //Ticket auch für andere Fahrten anwendbar ?
                checkTicketForOtherTripsZweiWaben(allTrips, currentTicketList);
                for (TicketToBuy ticket : currentTicketList) {
                    ArrayList<Integer> list = new ArrayList<>(zweiWaben);
                    Set<Farezone> x = new HashSet<>();
                    x.add(new Farezone(list.get(0), ""));
                    x.add(new Farezone(list.get(1), ""));
                    ticket.setValidFarezones(x, list.get(0), true);
                }
            }
            result.addAll(currentTicketList);
        }
        return result;
    }

    private static ArrayList<TicketToBuy> tarifgebietOptimierung(HashMap<Farezone, ArrayList<TripItem>> tarifgebietTrips, ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets, int preisstufenIndex) {
        ArrayList<TicketToBuy> result = new ArrayList<>();
        Set<Farezone> farezones = VRR_Farezones.createVRRFarezone();
        for (Farezone tarifgebiet : tarifgebietTrips.keySet()) {
            ArrayList<TicketToBuy> currentTicketList = new ArrayList<>();
            ArrayList<TripItem> tarfigebietTrips = tarifgebietTrips.get(tarifgebiet);
            Collections.sort(tarfigebietTrips, new TripItemTimeComparator());
            for (int ticketIndex = 0; ticketIndex < possibleTickets.size(); ticketIndex++) {
                TimeTicket possibleTicket = possibleTickets.get(ticketIndex);
                //Überprüfen, ob genug Fahrten für das jeweilige Ticket in der Region stattfinden
                while (tarfigebietTrips.size() >= possibleTickets.get(ticketIndex).getMinNumTrips(preisstufenIndex)) {
                    Pair<Integer, Integer> bestTicketIntervall = findBestTimeInterval(tarfigebietTrips, possibleTicket);
                    //Prüfen, ob es Fahrten gibt, die zu dem Ticket passen
                    if (bestTicketIntervall.second < possibleTicket.getMinNumTrips(preisstufenIndex)) {
                        break;
                    }
                    TicketToBuy ticketToBuy = new TicketToBuy(possibleTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                    int i = 0;
                    for (ListIterator<TripItem> listIterator = tarfigebietTrips.listIterator(); i < bestTicketIntervall.first + bestTicketIntervall.second; ) {
                        TripItem current = listIterator.next();
                        if (i >= bestTicketIntervall.first) {
                            listIterator.remove();
                            ticketToBuy.addTripItem(current);
                            allTrips.remove(current);
                        }
                        i++;
                    }
                    Set<Farezone> x = new HashSet<>();
                    x.add(tarifgebiet);
                    //Für diese fünf Tarifgebiete ist der Gültigkeitsbereich nicht nur das Tarifgebiet, sondern die ganze Stadt
                    if (tarifgebiet.getId() == 37) {
                        x.add(farezones.stream().filter(f -> f.getId() == 38).findFirst().get());
                    } else if (tarifgebiet.getId() == 43) {
                        x.add(farezones.stream().filter(f -> f.getId() == 53).findFirst().get());
                    } else if (tarifgebiet.getId() == 23) {
                        x.add(farezones.stream().filter(f -> f.getId() == 33).findFirst().get());
                    } else if (tarifgebiet.getId() == 35) {
                        x.add(farezones.stream().filter(f -> f.getId() == 45).findFirst().get());
                    } else if (tarifgebiet.getId() == 65) {
                        x.add(farezones.stream().filter(f -> f.getId() == 66).findFirst().get());
                    }
                    ticketToBuy.setValidFarezones(x, tarifgebiet.getId());
                    currentTicketList.add(ticketToBuy);
                }
                //Zusammenfassen der Tickets
                if (currentTicketList.size() > 1) {
                    Collections.sort(currentTicketList, new TicketToBuyTimeComporator());
                    sumUpTicketsNew(currentTicketList, possibleTickets, ticketIndex + 1, preisstufenIndex);
                }
                //Ticket auch für andere Fahrten anwendbar ?
                checkTicketForOtherTrips(allTrips, currentTicketList);
                for (TicketToBuy ticket : currentTicketList) {
                    Set<Farezone> x = new HashSet<>();
                    x.add(tarifgebiet);
                    //Für diese fünf Tarifgebiete ist der Gültigkeitsbereich nicht nur das Tarifgebiet, sondern die ganze Stadt
                    if (tarifgebiet.getId() == 37) {
                        x.add(farezones.stream().filter(f -> f.getId() == 38).findFirst().get());
                    } else if (tarifgebiet.getId() == 43) {
                        x.add(farezones.stream().filter(f -> f.getId() == 53).findFirst().get());
                    } else if (tarifgebiet.getId() == 23) {
                        x.add(farezones.stream().filter(f -> f.getId() == 33).findFirst().get());
                    } else if (tarifgebiet.getId() == 35) {
                        x.add(farezones.stream().filter(f -> f.getId() == 45).findFirst().get());
                    } else if (tarifgebiet.getId() == 65) {
                        x.add(farezones.stream().filter(f -> f.getId() == 66).findFirst().get());
                    }
                    ticket.setValidFarezones(x, tarifgebiet.getId());
                }
            }
            result.addAll(currentTicketList);
        }
        return result;
    }

    private static void fahrtenKnotenZuweisen(Graph<FarezoneTrips, DefaultEdge> graph, ArrayList<TripItem> tripItems) {
        for (FarezoneTrips f : graph.vertexSet()) {
            f.clearTrips();
        }
        for (TripItem item : tripItems) {
            Optional<FarezoneTrips> farezone = graph.vertexSet().stream()
                    .filter(s -> s.getID() == item.getStartID() / 10).findFirst();
            if (!farezone.isPresent()) {
                // Fehler
            } else {
                farezone.get().add(item);
            }
        }
    }

    private static ArrayList<TripItem> checkNeighbourhood(Set<FarezoneTrips> neighbourhood) {
        ArrayList<TripItem> tripsToOptimise = new ArrayList<>();
        for (FarezoneTrips farezoneTrips : neighbourhood) {
            for (TripItem item : farezoneTrips.getTripItems()) {
                if (checkIfTripIsInRegion(item, changeFarezoneTripsToFarezone(neighbourhood))) {
                    tripsToOptimise.add(item);
                }
            }
        }
        return tripsToOptimise;
    }

    private static Set<Farezone> changeFarezoneTripsToFarezone(Set<FarezoneTrips> farezoneTripsSet) {
        Set<Farezone> farezoneSet = new HashSet<>();
        for (FarezoneTrips f : farezoneTripsSet) {
            farezoneSet.add(new Farezone(f.getFarezone()));
        }
        return farezoneSet;
    }

    private static void fahrtenRegionenZuweisen(ArrayList<TripItem> tripItems, HashMap<Integer, Set<FarezoneTrips>> regions) {
        for (Integer region : regions.keySet()) {
            for (FarezoneTrips farezoneTrips : regions.get(region)) {
                farezoneTrips.clearTrips();
            }
        }
        for (TripItem tripItem : tripItems) {
            for (int i : regions.keySet()) {
                Set<FarezoneTrips> farezones = regions.get(i);
                boolean loop = true;
                for (FarezoneTrips f : farezones) {
                    if (f.getID() == tripItem.getStartID() / 10
                            && !f.getTripItems().contains(tripItem)) {
                        f.add(tripItem);
                        loop = false;
                        break;
                    }
                }
                if (!loop) {
                    break;
                }
            }
        }
    }

    private static boolean checkIfTripIsInRegion(TripItem tripItem, Set<Farezone> ticketFarezones) {
        for (Integer crossedFarezone : tripItem.getCrossedFarezones()) {
            boolean contains = false;
            for (Farezone f : ticketFarezones) {
                if (crossedFarezone / 10 == f.getId()) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkIfTripIsInZweiWaben(TripItem tripItem, Set<Farezone> ticketFarezones) {
        for (Integer crossedFarezone : tripItem.getCrossedFarezones()) {
            boolean contains = false;
            for (Farezone f : ticketFarezones) {
                if (crossedFarezone == f.getId()) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                return false;
            }
        }
        return true;
    }

    private static void splitTrips(ArrayList<TripItem> tripList, HashMap<Farezone, ArrayList<TripItem>> tarifgebietTrips, HashMap<HashSet<Integer>, ArrayList<TripItem>> zweiWabenTarif) {
        Set<Farezone> farezones = VRR_Farezones.createVRRFarezone();
        for (TripItem item : tripList) {
            boolean contains = false;
            ArrayList<Integer> crossedFarezones = new ArrayList<>(item.getCrossedFarezones());
            int crossedFarezone_0 = crossedFarezones.get(0);
            for (Integer crossedFarezone : crossedFarezones) {
                if (crossedFarezone / 10 != crossedFarezone_0 / 10) {
                    contains = true;
                    HashSet<Integer> zweiWaben = new HashSet<>();
                    zweiWaben.add(crossedFarezone);
                    zweiWaben.add(crossedFarezone_0);
                    ArrayList<TripItem> oldTrips = zweiWabenTarif.get(zweiWaben);
                    if (oldTrips == null) {
                        oldTrips = new ArrayList<>();
                        zweiWabenTarif.put(zweiWaben, oldTrips);
                    }
                    oldTrips.add(item);
                    break;
                }
            }
            if (!contains) {
                Farezone tarifgebiet = farezones.stream().filter(f -> f.getId() == crossedFarezone_0 / 10).findFirst().get();
                //Sonderfälle: Diese Städte müssen insgesamt betrachtet werden, und nicht nur das einzelne Tarifgebiet
                if (tarifgebiet.getId() == 38) {
                    tarifgebiet = farezones.stream().filter(f -> f.getId() == 37).findFirst().get();
                } else if (tarifgebiet.getId() == 53) {
                    tarifgebiet = farezones.stream().filter(f -> f.getId() == 43).findFirst().get();
                } else if (tarifgebiet.getId() == 33) {
                    tarifgebiet = farezones.stream().filter(f -> f.getId() == 23).findFirst().get();
                } else if (tarifgebiet.getId() == 45) {
                    tarifgebiet = farezones.stream().filter(f -> f.getId() == 35).findFirst().get();
                } else if (tarifgebiet.getId() == 66) {
                    tarifgebiet = farezones.stream().filter(f -> f.getId() == 65).findFirst().get();
                }
                ArrayList<TripItem> oldTrips = tarifgebietTrips.get(tarifgebiet);
                if (oldTrips == null) {
                    oldTrips = new ArrayList<>();
                    tarifgebietTrips.put(tarifgebiet, oldTrips);
                }
                oldTrips.add(item);
            }
        }
    }


}
