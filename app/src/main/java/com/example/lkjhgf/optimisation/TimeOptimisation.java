package com.example.lkjhgf.optimisation;


import android.util.Pair;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.publicTransport.provider.Farezone;
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
                    Collections.sort(toBuyArrayList, (o1, o2) -> {
                        long result = o1.getFirstDepartureTime().getTime() - o2.getFirstDepartureTime().getTime();
                        if (result == 0) {
                            return 0;
                        } else if (result > 0) {
                            return 1;
                        } else {
                            return -1;
                        }
                    });
                    //TODO prüfen ob mit +1 oder ohne
                    sumUpTicketsNew(toBuyArrayList, tickets, ticketIndex + 1, preisstufenIndex);
                    //TODO hier noch ggf. prüfen, ob Fahrten hinzugenommen werden können?
                }
            }
        }
        ArrayList<Farezone> farezoneList = MainMenu.myProvider.getFarezones();
        for (TicketToBuy ticket : toBuyArrayList) {
            ticket.setValidFarezones(farezoneList, farezoneList.get(0));
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
                for (ListIterator<Integer> t = collectedTickets.listIterator(collectedTickets.size()); t.hasPrevious(); ) {
                    oldTickets.remove(t.previous().intValue());
                    t.remove();
                }
                collectedTickets.clear();
                bestIntervall = findBestTicketIntervall(oldTickets, currentTicket);
            }
            oldTickets.addAll(newTickets);
            Collections.sort(oldTickets, (o1, o2) -> {
                long result = o1.getFirstDepartureTime().getTime() - o2.getFirstDepartureTime().getTime();
                if (result == 0) {
                    return 0;
                } else if (result > 0) {
                    return 1;
                } else {
                    return -1;
                }
            });
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
                        //TODO Regionen überprüfen
                        ticketToBuy.addTripItem(current);
                        tripItemIterator.remove();
                    }
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

        ArrayList<TicketToBuy> ticketsToBuy = new ArrayList<>();

        for (int ticketIndex = 0; ticketIndex < timeTickets.size(); ticketIndex++) {
            if (allTrips.size() >= timeTickets.get(ticketIndex).getMinNumTrips(preisstufenIndex)) {
                Pair<Integer, Integer> bestTicketInterval = findBestTimeInterval(tripsC, timeTickets.get(ticketIndex));
                while (bestTicketInterval.second >= timeTickets.get(ticketIndex).getMinNumTrips(preisstufenIndex)) {
                    TicketToBuy ticketToBuy = new TicketToBuy(timeTickets.get(ticketIndex), MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                    ticketsToBuy.add(ticketToBuy);
                    int i = 0;
                    for (ListIterator<TripItem> listIterator = tripsC.listIterator(); i < bestTicketInterval.first + bestTicketInterval.second; ) {
                        TripItem current = listIterator.next();
                        if (i >= bestTicketInterval.first) {
                            listIterator.remove();
                            ticketToBuy.addTripItem(current);
                            allTrips.remove(current);
                        }
                        i++;
                    }
                    bestTicketInterval = findBestTimeInterval(tripsC, timeTickets.get(ticketIndex));
                }
                if (ticketsToBuy.size() > 1) {
                    Collections.sort(ticketsToBuy, (o1, o2) -> Math.toIntExact(o1.getFirstDepartureTime().getTime() - o2.getFirstDepartureTime().getTime()));
                    sumUpTickets(ticketsToBuy, timeTickets, ticketIndex, preisstufenIndex);
                }
            }

        }
        return ticketsToBuy;
    }

    public static ArrayList<TicketToBuy> optimierungPreisstufeB(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets) {
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 3;
        ArrayList<TripItem> tripsB = collectTripsPreisstufe(allTrips, preisstufenIndex);
        Graph<FarezoneTrips, DefaultEdge> vrr_farezone_graph = VRR_Farezones.create_vrr_farezone_graph();
        HashMap<Farezone, ArrayList<TicketToBuy>> ticketsPerRegion = new HashMap<>();
        HashMap<Farezone, Set<Farezone>> regionen = new HashMap<>();
        for (int i = 0; i< possibleTickets.size(); i++) {
            TimeTicket possibleTicket = possibleTickets.get(i);
            //Laufzeitoptimierung: Egal wie die Fahrten über das Gebiet verteilt sind, wenn die gesamte Anzahl kleiner ist, als die
            //Anzahl benötigter Fahrten, damit das Ticket sich lohnt, ist keine weitere Betrachtung nötig
            boolean repeat = true;
            while (repeat) {
                //Laufzeitoptimierung: Egal wie die Fahrten über das Gebiet verteilt sind, wenn die gesamte Anzahl kleiner ist, als die
                //Anzahl benötigter Fahrten, damit das Ticket sich lohnt, ist keine weitere Betrachtung nötig
                if(tripsB.size() < possibleTicket.getMinNumTrips(preisstufenIndex)){
                    break;
                }
                fahrtenKnotenZuweisen(vrr_farezone_graph, tripsB);
                HashMap<Farezone, Quartet<Integer, Integer, ArrayList<TripItem>, Set<Farezone>>> bestTicketIntervalPerFarezone = new HashMap<>();
                ArrayList<FarezoneTrips> unusedFarezones = new ArrayList<>();
                for (Iterator<FarezoneTrips> iterator = vrr_farezone_graph.vertexSet().iterator(); iterator.hasNext();) {
                    FarezoneTrips farezone = iterator.next();
                    //D.h. das Tarifgebiet ist ein Zentralgebiet
                    Set<DefaultEdge> edges = vrr_farezone_graph.outgoingEdgesOf(farezone);
                    if (!edges.isEmpty()) {
                        Set<FarezoneTrips> neighbourhood = new HashSet<>();
                        neighbourhood.add(farezone);
                        for (DefaultEdge e : edges) {
                            neighbourhood.add(vrr_farezone_graph.getEdgeTarget(e));
                        }
                        ArrayList<TripItem> neighbourhoodTrips = checkNeighbourhood(neighbourhood);
                        if(neighbourhoodTrips.isEmpty()){
                            unusedFarezones.add(farezone);
                        }else{
                            Collections.sort(neighbourhoodTrips, (o1, o2) -> {
                                long result = o1.getFirstDepartureTime().getTime() - o2.getFirstDepartureTime().getTime();
                                if (result == 0) {
                                    return 0;
                                } else if (result > 0) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            });
                            Pair<Integer, Integer> bestTicketIntervall = findBestTimeInterval(neighbourhoodTrips, possibleTicket);
                            //Nur Regionen mit mindestens einer Fahrt sind von Interesse
                            if(bestTicketIntervall.second > 0){
                                Set<Farezone> farezones = new HashSet<>();
                                for(FarezoneTrips f : neighbourhood){
                                    farezones.add(f.getFarezone());
                                }
                                bestTicketIntervalPerFarezone.put(farezone.getFarezone(), new Quartet(bestTicketIntervall.first, bestTicketIntervall.second, neighbourhoodTrips, farezones));
                            }
                        }

                    }
                }
                for(FarezoneTrips f : unusedFarezones){
                    vrr_farezone_graph.removeVertex(f);
                }
                unusedFarezones.clear();
                int max = 0;
                Farezone maxFarezone = null;
                for (Farezone f : bestTicketIntervalPerFarezone.keySet()) {
                    if (bestTicketIntervalPerFarezone.get(f).getValue1() > max) {
                        max = bestTicketIntervalPerFarezone.get(f).getValue1();
                        maxFarezone = f;
                    }
                }
                if (maxFarezone != null) {
                    if (bestTicketIntervalPerFarezone.get(maxFarezone).getValue1() >= possibleTicket.getMinNumTrips(preisstufenIndex)) {
                        //Ticketerstellen mit den Fahrten
                        TicketToBuy ticketToBuy = new TicketToBuy(possibleTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                        for(int j = bestTicketIntervalPerFarezone.get(maxFarezone).getValue0(); j < bestTicketIntervalPerFarezone.get(maxFarezone).getValue0()+bestTicketIntervalPerFarezone.get(maxFarezone).getValue1(); j++){
                            TripItem tripItem = bestTicketIntervalPerFarezone.get(maxFarezone).getValue2().get(j);
                            ticketToBuy.addTripItem(tripItem);
                            //Fahrten aus der Liste der zu optimierenden Fahrten entfernen
                            tripsB.remove(tripItem);
                            allTrips.remove(tripItem);

                        }

                        Set<Farezone> regionenSet = regionen.get(maxFarezone);
                        if(regionenSet == null){
                            regionenSet = bestTicketIntervalPerFarezone.get(maxFarezone).getValue3();
                            regionen.put(maxFarezone, regionenSet);
                        }
                        //Speichern des Tickets
                        ArrayList<TicketToBuy> ticketToBuyArrayList = ticketsPerRegion.get(maxFarezone);
                        if(ticketToBuyArrayList == null){
                            ticketToBuyArrayList = new ArrayList<>();
                            ticketsPerRegion.put(maxFarezone, ticketToBuyArrayList);
                        }
                        ticketToBuyArrayList.add(ticketToBuy);
                    }
                }else{
                    repeat = false;
                }
            }
            //Zusammenfassen der Tickets
            for(Farezone farezone : ticketsPerRegion.keySet()){
                if(ticketsPerRegion.get(farezone).size() > 1){
                        Collections.sort(ticketsPerRegion.get(farezone), (o1, o2) -> {
                            long result = o1.getFirstDepartureTime().getTime() - o2.getFirstDepartureTime().getTime();
                            if (result == 0) {
                                return 0;
                            } else if (result > 0) {
                                return 1;
                            } else {
                                return -1;
                            }
                        });
                    //TODO prüfen ob mit +1 oder ohne
                    if(ticketsPerRegion.get(farezone).size() > 1)
                        sumUpTicketsNew(ticketsPerRegion.get(farezone), possibleTickets, i+1, preisstufenIndex);
                }
                //Ticket -> Geltungsbereich zuweisen
                for(TicketToBuy ticket : ticketsPerRegion.get(farezone)){
                    ticket.setValidFarezones(new ArrayList<>(regionen.get(farezone)), farezone);
                }
            }
        }

        ArrayList<TicketToBuy> result = new ArrayList<>();

        for (ArrayList t : ticketsPerRegion.values())
        {
            result.addAll(t);
        }
        return result;
    }

    private static void fahrtenKnotenZuweisen(Graph<FarezoneTrips, DefaultEdge> graph, ArrayList<TripItem> tripItems) {
        for (TripItem item : tripItems) {
            for (FarezoneTrips farezone : graph.vertexSet()) {
                if (item.getStartID()/10 == farezone.getID()) {
                    farezone.add(item);
                    break;
                }
            }
        }
    }

    private static ArrayList<TripItem> checkNeighbourhood(Set<FarezoneTrips> neighbourhood) {
        ArrayList<TripItem> tripsToOptimise = new ArrayList<>();
        for (FarezoneTrips farezoneTrips : neighbourhood) {
            for (Iterator<TripItem> itemIterator = farezoneTrips.getTripItems().iterator(); itemIterator.hasNext(); ) {
                TripItem item = itemIterator.next();
                for (FarezoneTrips f : neighbourhood) {
                    if (f.getID() == item.getDestinationID() / 10) {
                        tripsToOptimise.add(item);
                        break;
                    }
                }
            }
        }
        return tripsToOptimise;
    }




}
