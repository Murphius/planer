package com.example.lkjhgf.optimisation.timeOptimisation.vrr;

import android.util.Pair;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.TicketToBuyTimeComporator;
import com.example.lkjhgf.helper.util.TripItemTimeComparator;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.optimisation.timeOptimisation.FarezoneUtil;
import com.example.lkjhgf.optimisation.timeOptimisation.Util;
import com.example.lkjhgf.publicTransport.provider.Farezone;
import com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.FarezoneTrip;
import com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.VRR_Farezones;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import org.javatuples.Quartet;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FarezoneB {

    public static ArrayList<TicketToBuy> optimisation(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets) {
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 3;
        ArrayList<TripItem> tripsB = Util.collectTripsPreisstufe(allTrips, preisstufenIndex);
        if (tripsB.isEmpty()) {
            return new ArrayList<>();
        }
        Graph<FarezoneTrip, DefaultEdge> vrrFarezoneGraph = VRR_Farezones.createVrrFarezoneGraph();
        HashMap<Farezone, ArrayList<TicketToBuy>> ticketsPerRegion = new HashMap<>();
        HashMap<Farezone, Set<Farezone>> regionen = new HashMap<>();
        for (int i = 0; i < possibleTickets.size(); i++) {
            TimeTicket possibleTicket = possibleTickets.get(i);
            while (true) {
                //Laufzeitoptimierung: Egal wie die Fahrten über das Gebiet verteilt sind, wenn die gesamte Anzahl kleiner ist, als die
                //Anzahl benötigter Fahrten, damit das Ticket sich lohnt, ist keine weitere Betrachtung nötig
                if (tripsB.size() < possibleTicket.getMinNumTrips(preisstufenIndex)) {
                    break;
                }
                addTripsToNodes(vrrFarezoneGraph, tripsB);
                HashMap<Farezone, Quartet<Integer, Integer, ArrayList<TripItem>, Set<FarezoneTrip>>> bestTicketIntervalPerFarezone = new HashMap<>();
                for (FarezoneTrip farezone : vrrFarezoneGraph.vertexSet()) {
                    Quartet<Integer, Integer, ArrayList<TripItem>, Set<FarezoneTrip>> regionTicketResult = calculateMaxTrips(farezone, possibleTicket);
                    if (regionTicketResult != null) {
                        bestTicketIntervalPerFarezone.put(farezone.getFarezone(), regionTicketResult);
                    }
                }
                //Suche die beste Region
                Farezone maxFarezone = findBestRegion(bestTicketIntervalPerFarezone, ticketsPerRegion);
                if (maxFarezone == null) {
                    break;
                }
                if (bestTicketIntervalPerFarezone.get(maxFarezone).getValue1() >= possibleTicket.getMinNumTrips(preisstufenIndex)) {
                    //Ticketerstellen mit den Fahrten
                    TicketToBuy ticketToBuy = new TicketToBuy(possibleTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                    Util.deleteTrips(allTrips, bestTicketIntervalPerFarezone.get(maxFarezone).getValue2(),
                            bestTicketIntervalPerFarezone.get(maxFarezone).getValue0(), bestTicketIntervalPerFarezone.get(maxFarezone).getValue1(), ticketToBuy);
                    setValidFarezones(maxFarezone, ticketToBuy, regionen, bestTicketIntervalPerFarezone);
                    //Speichern des Tickets
                    ArrayList<TicketToBuy> ticketToBuyArrayList = ticketsPerRegion.get(maxFarezone);
                    if (ticketToBuyArrayList == null) {
                        ticketToBuyArrayList = new ArrayList<>();
                        ticketsPerRegion.put(maxFarezone, ticketToBuyArrayList);
                    }
                    ticketToBuyArrayList.add(ticketToBuy);
                } else {
                    break;
                }
            }
            //Zusammenfassen der Tickets
            for (Farezone farezone : ticketsPerRegion.keySet()) {
                if (ticketsPerRegion.get(farezone).size() > 1) {
                    Collections.sort(ticketsPerRegion.get(farezone), new TicketToBuyTimeComporator());
                    Util.sumUpTickets(ticketsPerRegion.get(farezone), possibleTickets, i + 1, preisstufenIndex);
                }
                //gucken ob das Ticket auch für andere Fahrten anwendbar ist
                FarezoneUtil.checkTicketsForOtherTrips(allTrips, ticketsPerRegion.get(farezone));
                //Tickets -> Geltungsbereich zuweisen
                for (TicketToBuy ticket : ticketsPerRegion.get(farezone)) {
                    ticket.setValidFarezones(regionen.get(farezone), farezone.getId());
                }
            }
            //Da sich die Geltungsbereiche überlappen, müssen die Fahrten der Preisstufe B neu gesammelt werden, bevor das ganze von vorne beginnen kann
            tripsB = Util.collectTripsPreisstufe(allTrips, preisstufenIndex);
        }
        //Die jeweiligen Tickets einer Region zu einer Liste zusammensetzen
        ArrayList<TicketToBuy> result = new ArrayList<>();
        for (Farezone f : ticketsPerRegion.keySet()) {
            result.addAll(ticketsPerRegion.get(f));
        }
        return result;
    }

    /**
     * Ermittelt für das Tarifgebiet die maximale Anzahl an Fahrten mit dem Ticket
     *
     * @param farezone       zu betrachtendes Tarifgebiet
     * @param possibleTicket zu nutzendes Ticket
     * @return Quartet mit den Werten: <br/>
     * get0 -> Startindex des maximalen Intervalls
     * get1 -> Wie viele Fahrten, in dem maximalen Intervall liegen
     * get2 -> Alle Fahrten, die von dem Ticket mit der Zentralregion abgedeckt werden
     * get3 -> Bereich, in dem das Ticket gültig ist
     * @postconditions Der Wert null wird abgefangen, und nicht in die Zentralregionen-HashMap eingefügt
     */
    private static Quartet<Integer, Integer, ArrayList<TripItem>, Set<FarezoneTrip>> calculateMaxTrips(FarezoneTrip farezone, TimeTicket possibleTicket) {
        Graph<FarezoneTrip, DefaultEdge> vrrFarezoneGraph = VRR_Farezones.createVrrFarezoneGraph();
        Set<DefaultEdge> edges = vrrFarezoneGraph.outgoingEdgesOf(farezone);
        if (!edges.isEmpty()) {
            //D.h. das Tarifgebiet ist ein Zentralgebiet
            Set<FarezoneTrip> neighbourhood = new HashSet<>();
            neighbourhood.add(farezone);
            for (DefaultEdge e : edges) {//Holen aller Fahrten, die in den "benachbarten" Tarifgebieten liegen
                neighbourhood.add(vrrFarezoneGraph.getEdgeTarget(e));
            }
            //Liste an Fahrten, die innerhalb des Tarifgebiets und seinen Nachbarn liegen
            ArrayList<TripItem> neighbourhoodTrips = FarezoneUtil.checkNeighbourhood(neighbourhood);
            if (!neighbourhoodTrips.isEmpty()) {
                Collections.sort(neighbourhoodTrips, new TripItemTimeComparator());
                //Ermittle das beste Zeitintervall für die Region und das Ticket
                Pair<Integer, Integer> bestTicketIntervall = Util.findBestTimeInterval(neighbourhoodTrips, possibleTicket);
                //Nur Regionen mit mindestens einer Fahrt sind von Interesse
                if (bestTicketIntervall.second > 0) {
                    return new Quartet<>(bestTicketIntervall.first, bestTicketIntervall.second, neighbourhoodTrips, neighbourhood);
                }
            }
        }
        //Zur Unterscheidung
        return null;
    }

    /**
     * Ermittelt die Zentralregion, mit der die meisten Fahrten möglich sind
     *
     * @param bestTicketIntervalPerFarezone enthält für jedes Zentralgebiet, wie viele Fahrten möglich sind
     * @param ticketsPerRegion              enthält, für welche Region, welches Ticket gekauft wurde
     * @return das Zentralgebiet, welches die meisten Fahrten enthält
     */
    private static Farezone findBestRegion(HashMap<Farezone, Quartet<Integer, Integer, ArrayList<TripItem>, Set<FarezoneTrip>>> bestTicketIntervalPerFarezone,
                                           HashMap<Farezone, ArrayList<TicketToBuy>> ticketsPerRegion) {
        int max = 0;
        Farezone maxFarezone = null;
        for (Farezone f : bestTicketIntervalPerFarezone.keySet()) {
            if (bestTicketIntervalPerFarezone.get(f).getValue1() > max) {// Die Region deckt die meisten Fahrten ab
                max = bestTicketIntervalPerFarezone.get(f).getValue1();
                maxFarezone = f;
            } else if (bestTicketIntervalPerFarezone.get(f).getValue1() == max) {//Die Region deckt genauso viele Fahrten wie das Maximum ab
                //Dann wähle die Region mit mehr Fahrten insgesamt
                int currentNeighbourhoodSize = FarezoneUtil.checkNeighbourhood(bestTicketIntervalPerFarezone.get(f).getValue3()).size();
                int maxNeighbourhoodSize = FarezoneUtil.checkNeighbourhood(bestTicketIntervalPerFarezone.get(maxFarezone).getValue3()).size();
                if (currentNeighbourhoodSize > maxNeighbourhoodSize) {
                    maxFarezone = f;
                } else if (ticketsPerRegion.get(f) != null && ticketsPerRegion.get(maxFarezone) != null) {//Sonst wähle die Region, die die meisten Tickets hat
                    //Ansatz: diese lassen sich dann vermutlich zusammenfassen
                    if (ticketsPerRegion.get(f).size() > ticketsPerRegion.get(maxFarezone).size()) {
                        maxFarezone = f;
                    }
                } else if (ticketsPerRegion.get(f) != null) {//Region mit mehr Tickets
                    maxFarezone = f;
                }
            }
        }
        return maxFarezone;
    }

    /**
     * Zuweisen der gültigen Regionen an das Ticket
     *
     * @param maxFarezone                   beste Zentralregion
     * @param ticketToBuy                   Ticket, das eine Zentralregion zugewiesen bekommen soll
     * @param regionen                      zum einfacheren Zugriff auf Gültigkeitsbereiche
     * @param bestTicketIntervalPerFarezone wenn die Region noch nicht gesetzt ist, kann so auf diese Zugegriffen werden
     */
    private static void setValidFarezones(Farezone maxFarezone, TicketToBuy ticketToBuy, HashMap<Farezone, Set<Farezone>> regionen,
                                          HashMap<Farezone, Quartet<Integer, Integer, ArrayList<TripItem>, Set<FarezoneTrip>>> bestTicketIntervalPerFarezone) {
        Set<Farezone> regionenSet = regionen.get(maxFarezone);
        if (regionenSet == null) {
            regionenSet = new HashSet<>();
            for (FarezoneTrip farezoneTrip : bestTicketIntervalPerFarezone.get(maxFarezone).getValue3()) {
                regionenSet.add(new Farezone(farezoneTrip.getFarezone()));
            }
            regionen.put(maxFarezone, regionenSet);
        }
        ticketToBuy.setValidFarezones(regionenSet, maxFarezone.getId());
    }

    /**
     * Weißt eine Fahrt dem Knoten zu, dessen ID dem Starttarifgebiet der Fahrt entspricht <br/>
     * <p>
     * Da sich die Geltungsbereiche überlappen, muss nach jeder Optimierung die Zuordnung von 0 anfangen
     * -> löschen aller bereits zugeordneten Fahrten
     *
     * @param graph     liefert die Struktur für die Tarifgebiete
     * @param tripItems Fahrten die optimiert bzw hier aufgeteilt werden sollen
     * @preconditions tripItems sind nur die Fahrten der Preisstufe B
     */
    private static void addTripsToNodes(Graph<FarezoneTrip, DefaultEdge> graph, ArrayList<TripItem> tripItems) {
        for (FarezoneTrip f : graph.vertexSet()) {
            f.clearTrips();
        }
        for (TripItem item : tripItems) {
            Optional<FarezoneTrip> farezone = graph.vertexSet().stream()
                    .filter(s -> s.getID() == item.getStartID() / 10).findFirst();
            if (!farezone.isPresent()) {
                // Fehler
            } else {
                farezone.get().add(item);
            }
        }
    }
}
