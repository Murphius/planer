package com.example.lkjhgf.optimisation.timeOptimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.TripQuantitiesTimeComparator;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.publicTransport.provider.Farezone;
import com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.FarezoneTrip;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Hilfsklassen, die sich mit den Tarifgebieten beschäftigen
 */
public class FarezoneUtil {
    /**
     * Erstellt aus der Menge an FarezoneTrips eine Menge, die nur die entsprechenden Tarifgebiete
     * enthält
     *
     * @param farezoneTripSet Tarifgebiete mit zugehörigen Fahrten
     * @return Tarifgebiete ohne Fahrten
     */
    public static Set<Farezone> changeFarezoneTripsToFarezone(Set<FarezoneTrip> farezoneTripSet) {
        Set<Farezone> farezoneSet = new HashSet<>();
        for (FarezoneTrip f : farezoneTripSet) {
            farezoneSet.add(new Farezone(f.getFarezone()));
        }
        return farezoneSet;
    }

    /**
     * Prüft, ob die Fahrscheine noch für weitere Fahrten verwendet werden können
     * <p>
     * Es wird versucht, möglichst viele Fahrten den zu kaufenden Fahrscheinen zuzuweisen
     *
     * @param tripItems    Fahrten die noch keinen Fahrschein besitzen
     * @param ticketsToBuy Fahrscheine die gekauft werden müssen
     */
    public static void checkTicketsForOtherTrips(ArrayList<TripItem> tripItems, ArrayList<TicketToBuy> ticketsToBuy) {
        for (TicketToBuy ticketToBuy : ticketsToBuy) {
            TimeTicket timeTicket = (TimeTicket) ticketToBuy.getTicket();
            for (Iterator<TripItem> tripItemIterator = tripItems.iterator(); tripItemIterator.hasNext(); ) {
                TripItem current = tripItemIterator.next();
                //Preisstufen vergleich
                if (MainMenu.myProvider.getPreisstufenIndex(current.getPreisstufe()) > MainMenu.myProvider.getPreisstufenIndex(ticketToBuy.getPreisstufe())) {
                    break;
                }
                if (!timeTicket.isValidTrip(current)) {
                    break;
                }
                //Ticket ist Nutzbar für die Fahrt
                if (!checkIfTripIsInRegion(current, ticketToBuy.getValidFarezones())) {
                    break;
                }
                //Prüft, ob die benötigten Regionen abgedeckt sind
                long minStartTime = Math.min(current.getFirstDepartureTime().getTime(), ticketToBuy.getFirstDepartureTime().getTime());
                long maxEndTime = Math.max(current.getLastArrivalTime().getTime(), ticketToBuy.getLastArrivalTime().getTime());
                if (maxEndTime - minStartTime <= timeTicket.getMaxDuration()) {
                    //Ggf. wird die Startzeit des Tickets nach vorne gezogen -> Gefahr eines lokalen Minimums
                    ticketToBuy.addTripItem(current);
                    tripItemIterator.remove();
                    Collections.sort(ticketToBuy.getTripQuantities(), new TripQuantitiesTimeComparator());
                }
            }
        }
    }

    /**
     * Prüft für eine Region, ob für jede Fahrt, deren Startgebiet in dieser liegt, ob alle
     * gekreuzten Tarifgebiete ebenfalls in der Region enthalten sind.<br/>
     * <p>
     * Erstellt eine Liste aller Fahrten, deren benötigte Tarifgebiete, in der Region liegen<br/>
     * Um dies zu prüfen, wird für jede Fahrt {@link #checkIfTripIsInRegion(TripItem, Set Region)}
     * aufgerufen.
     *
     * @param neighbourhood Region, deren Fahrten gesammelt werden sollen
     * @return Liste an Fahrten, deren gesamter Fahrtverlauf in der Region liegen
     */
    public static ArrayList<TripItem> checkNeighbourhood(Set<FarezoneTrip> neighbourhood) {
        ArrayList<TripItem> tripsToOptimise = new ArrayList<>();
        for (FarezoneTrip farezoneTrip : neighbourhood) {
            for (TripItem item : farezoneTrip.getTripItems()) {
                if (checkIfTripIsInRegion(item, changeFarezoneTripsToFarezone(neighbourhood))) {
                    tripsToOptimise.add(item);
                }
            }
        }
        return tripsToOptimise;
    }

    /**
     * Prüft, ob alle Zwischenhalte einer Fahrt in der Region liegen
     *
     * @param tripItem        Fahrt, deren benötigte Regionen geprüft werden
     * @param ticketFarezones Region, die vorgegeben ist
     * @return true - alle benötigten Tarifgebiete sind in der Region enthalten; sonst false
     */
    public static boolean checkIfTripIsInRegion(TripItem tripItem, Set<Farezone> ticketFarezones) {
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
}
