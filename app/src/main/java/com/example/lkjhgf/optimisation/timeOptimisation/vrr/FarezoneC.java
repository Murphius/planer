package com.example.lkjhgf.optimisation.timeOptimisation.vrr;

import android.util.Pair;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.TicketToBuyTimeComporator;
import com.example.lkjhgf.helper.util.TripItemTimeComparator;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.optimisation.timeOptimisation.FarezoneUtil;
import com.example.lkjhgf.optimisation.timeOptimisation.Util;
import com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.FarezoneTrip;
import com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.VRR_Farezones;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Optimierung der Preisstufe C
 */
public class FarezoneC {

    public static ArrayList<TicketToBuy> optimieriungPreisstufeC(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> timeTickets) {
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 2;
        ArrayList<TripItem> tripsC = Util.collectTripsPreisstufe(allTrips, preisstufenIndex);//Sammeln der entsprechenden Fahrten
        if (tripsC.isEmpty()) {
            return new ArrayList<>();
        }

        HashMap<Integer, Set<FarezoneTrip>> possibleRegions = VRR_Farezones.regionenHashMap();
        HashMap<Integer, ArrayList<TicketToBuy>> ticketsPerRegion = new HashMap<>();

        for (int ticketIndex = 0; ticketIndex < timeTickets.size(); ticketIndex++) {
            TimeTicket possibleTicket = timeTickets.get(ticketIndex);
            while (true) {
                if (allTrips.size() < possibleTicket.getMinNumTrips(preisstufenIndex)) {
                    //Wenn insgesamt weniger Fahrten als für das Ticket benötigt vorhanden sind
                    break;
                }
                assignTripsToRegions(tripsC, possibleRegions);
                HashMap<Integer, Triplet<Integer, Integer, ArrayList<TripItem>>> bestTicketIntervallPerRegion = new HashMap<>();
                for (Integer i : possibleRegions.keySet()) {//Bestimme für jede Region, wie viele Fahrten das Ticket nutzen könnten
                    Triplet<Integer, Integer, ArrayList<TripItem>> regionMax = calculateMaxTripNum(i, possibleTicket, possibleRegions);
                    if (regionMax != null) {
                        bestTicketIntervallPerRegion.put(i, regionMax);
                    }
                }
                //Region mit der maximalen Fahrtenanzahl
                Integer maxRegion = searchMaxRegion(bestTicketIntervallPerRegion, possibleRegions, ticketsPerRegion);
                if (maxRegion == 0) {
                    break;
                }

                if (bestTicketIntervallPerRegion.get(maxRegion).getValue1() >= possibleTicket.getMinNumTrips(preisstufenIndex)) {
                    TicketToBuy ticket = new TicketToBuy(possibleTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                    Util.deleteTrips(allTrips, bestTicketIntervallPerRegion.get(maxRegion).getValue2(), bestTicketIntervallPerRegion.get(maxRegion).getValue0(),
                            bestTicketIntervallPerRegion.get(maxRegion).getValue1(), ticket);
                    ArrayList<TicketToBuy> ticketToBuyArrayList = ticketsPerRegion.get(maxRegion);
                    if (ticketToBuyArrayList == null) {
                        ticketToBuyArrayList = new ArrayList<>();
                        ticketsPerRegion.put(maxRegion, ticketToBuyArrayList);
                    }
                    //Gültigkeitsbereich
                    ticket.setValidFarezones(FarezoneUtil.changeFarezoneTripsToFarezone(possibleRegions.get(maxRegion)), maxRegion);
                    //Ticket merken
                    ticketToBuyArrayList.add(ticket);
                } else {
                    break;
                }
                tripsC = Util.collectTripsPreisstufe(allTrips, preisstufenIndex);
            }
            //Zusammenfassen der Tickets
            for (int region : ticketsPerRegion.keySet()) {
                if (ticketsPerRegion.get(region).size() > 1) {
                    Collections.sort(ticketsPerRegion.get(region), new TicketToBuyTimeComporator());
                    Util.sumUpTickets(ticketsPerRegion.get(region), timeTickets, ticketIndex + 1, preisstufenIndex);
                    FarezoneUtil.checkTicketsForOtherTrips(allTrips, ticketsPerRegion.get(region));
                }
            }
            tripsC = Util.collectTripsPreisstufe(allTrips, preisstufenIndex);
        }
        //Aus den Listen für jede Region eine gemeinsame Ticketliste bauen
        ArrayList<TicketToBuy> result = new ArrayList<>();
        for (Integer region : ticketsPerRegion.keySet()) {
            ArrayList<TicketToBuy> z = ticketsPerRegion.get(region);
            if (z != null) {
                result.addAll(z);
            }
        }
        return result;
    }

    /**
     * Bestimmt für die Region, wie viele Fahrten mit dem Ticket gemacht werden können
     *
     * @param regionID       betrachtete Region
     * @param possibleTicket betrachtetes Ticket
     * @return Triplet mit den Informationen: <br/>
     * value0 -> Startindex des Intervalls <br/>
     * value1 -> Wie viele Fahrten im Intervall liegen <br/>
     * value2 -> Alle Fahrten der Region<br/>
     * @postconditions Null Wert wird abgefangen und nicht in die HashMap eingefügt
     */
    private static Triplet<Integer, Integer, ArrayList<TripItem>> calculateMaxTripNum(Integer regionID, TimeTicket possibleTicket,
                                                                                      HashMap<Integer, Set<FarezoneTrip>> possibleRegions) {
        ArrayList<TripItem> regionTrips = FarezoneUtil.checkNeighbourhood(possibleRegions.get(regionID));
        if (!regionTrips.isEmpty()) {
            Collections.sort(regionTrips, new TripItemTimeComparator());
            Pair<Integer, Integer> bestTicketIntervall = Util.findBestTimeInterval(regionTrips, possibleTicket);
            if (bestTicketIntervall.second > 0) {
                return new Triplet<>(bestTicketIntervall.first, bestTicketIntervall.second, regionTrips);
            }
        }
        return null;
    }

    /**
     * Bestimmt die Region, die die meisten Fahrten mit dem Ticket abdecken kann
     *
     * @param bestTicketIntervallPerRegion liefert die Informationen zu allen Regionen
     * @param possibleRegions              betrachtete Regionen
     * @param ticketsPerRegion             Tickets, die die jeweiligen Regionen schon nutzen
     * @return ID der besten Region
     */
    private static int searchMaxRegion(HashMap<Integer, Triplet<Integer, Integer, ArrayList<TripItem>>> bestTicketIntervallPerRegion,
                                       HashMap<Integer, Set<FarezoneTrip>> possibleRegions, HashMap<Integer, ArrayList<TicketToBuy>> ticketsPerRegion) {
        int max = 0;
        Integer maxRegion = 0;
        for (Integer i : bestTicketIntervallPerRegion.keySet()) {
            if (bestTicketIntervallPerRegion.get(i).getValue1() > max) {//Mehr Fahrten
                max = bestTicketIntervallPerRegion.get(i).getValue1();
                maxRegion = i;
            } else if (bestTicketIntervallPerRegion.get(i).getValue1() == max) {//Wähle die Region mit mehr Fahrten
                int currentRegionSize = FarezoneUtil.checkNeighbourhood(possibleRegions.get(i)).size();
                int maxRegionSize = FarezoneUtil.checkNeighbourhood(possibleRegions.get(maxRegion)).size();
                if (currentRegionSize > maxRegionSize) {
                    maxRegion = i;
                } else if (ticketsPerRegion.get(i) != null && ticketsPerRegion.get(maxRegion) != null) {//Gleich viele Fahrten, wähle die Region mit mehr Tickets
                    //-> vermutlich lassen sich die Tickets zusammenfassen
                    if (ticketsPerRegion.get(i).size() > ticketsPerRegion.get(maxRegion).size()) {
                        maxRegion = i;
                    }
                } else if (ticketsPerRegion.get(i) != null) {//Region mit mehr Tickets
                    maxRegion = i;
                }
            }
        }
        return maxRegion;
    }

    /**
     * Weißt die Fahrt den Regionen zu <br/>
     * <p>
     * Jede Fahrt wird dem Tarifgebiet zugeordnet, wo die Fahrt startet,
     * dabei daruf achten, dass jede Fahrt nur einmal zugewiesen wird, auch wenn sie in mehreren
     * Regionen liegt
     * <br/>
     * Für jeden Durchlauf, müssen die Fahrten neu zugeordnet werden -> erst einmal leeren
     *
     * @param tripItems Zu optimierende Fahrten
     * @param regions   mögliche, sich überlappende Regionen
     */
    private static void assignTripsToRegions(ArrayList<TripItem> tripItems, HashMap<Integer, Set<FarezoneTrip>> regions) {
        for (Integer region : regions.keySet()) {
            for (FarezoneTrip farezoneTrip : regions.get(region)) {
                farezoneTrip.clearTrips();
            }
        }
        for (TripItem tripItem : tripItems) {
            for (int i : regions.keySet()) {
                Set<FarezoneTrip> farezones = regions.get(i);
                boolean loop = true;
                for (FarezoneTrip f : farezones) {
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
}
