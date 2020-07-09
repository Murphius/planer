package com.example.lkjhgf.optimisation.timeOptimisation;

import android.util.Pair;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.TicketToBuyTimeComporator;
import com.example.lkjhgf.helper.util.TripQuantitiesTimeComparator;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

/**
 * Hilfsfunktionen, die für alle Preisstufen benötigt werden
 */
public class Util {

    /**
     * Sammelt die Fahrten mit der jeweiligen Preisstufe
     *
     * @param allTrips         Alle Fahrten, die optimiert werden müssen
     * @param preisstufenIndex gibt die Preisstufe an
     * @return Alle Fahrten der jeweiligen Preisstufe
     */
    public static ArrayList<TripItem> collectTripsPreisstufe(ArrayList<TripItem> allTrips, int preisstufenIndex) {
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

    /**
     * Versucht Fahrscheine zusammenzufassen <br/>
     * <p>
     * Versucht Tickets so zusammenzufassen, dass durch ein größeres Ticket, ein längerer
     * Geltungsbereich entsteht, aber die Kosten gleich oder geringer sind
     *
     * @param oldTickets       alte Fahrscheine
     * @param timeTickets      mögliche Fahrscheine
     * @param startIndex       für timeTickets, ab welchem Index versucht werden soll, diese zusammenzufassen
     * @param preisstufenIndex betrachtete Preisstufe
     */
    public static void sumUpTickets(ArrayList<TicketToBuy> oldTickets, ArrayList<TimeTicket> timeTickets, int startIndex, int preisstufenIndex) {
        for (int ticketIndex = startIndex; ticketIndex < timeTickets.size(); ticketIndex++) {
            TimeTicket currentTicket = timeTickets.get(ticketIndex);
            int price = currentTicket.getPrice(preisstufenIndex); //Preis des aktuell betrachteten größeren Tickets
            ArrayList<TicketToBuy> newTickets = new ArrayList<>(); //Fahrscheine, die neu hinzukommen würden
            Triplet<Integer, Integer, Integer> bestIntervall = findBestTicketIntervall(oldTickets, currentTicket); //Sucht das beste Intervall, für das neue Ticket
            while (price - bestIntervall.getValue2() < 0) {//wenn der Preis der eingesparten Tickets größer ist, als der Preis des "besseren" Tickets
                //Neues Ticket
                TicketToBuy newTicketToBuy = new TicketToBuy(currentTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                newTickets.add(newTicketToBuy);
                ArrayList<Integer> collectedTickets = new ArrayList<>();//Sammeln der Fahrscheine, die wegfallen
                for (int i = bestIntervall.getValue0(); i < bestIntervall.getValue0() + bestIntervall.getValue1(); i++) {
                    collectedTickets.add(i);
                }
                for (Integer t : collectedTickets) {//Dem neuen Ticket die zugeordneten Fahrten der wegfallenden Fahrten zuordnen
                    newTicketToBuy.addTripItems(oldTickets.get(t).getTripList());
                }
                Collections.sort(newTicketToBuy.getTripQuantities(), new TripQuantitiesTimeComparator()); //Sortieren der Fahrten nach Abfahrtszeit
                //Gültigkeitsbereich des neuen Tickets setzen
                newTicketToBuy.setValidFarezones(oldTickets.get(collectedTickets.get(0)).getValidFarezones(), oldTickets.get(collectedTickets.get(0)).getMainRegionID(), oldTickets.get(0).isZweiWabenTarif());
                for (ListIterator<Integer> t = collectedTickets.listIterator(collectedTickets.size()); t.hasPrevious(); ) { //Löschen der alten Fahrscheine aus der Liste der benötigten Fahrscheine
                    oldTickets.remove(t.previous().intValue());
                    t.remove();
                }
                collectedTickets.clear();
                bestIntervall = findBestTicketIntervall(oldTickets, currentTicket);//erneute Suche nach Fahrscheinen, die ersetzt werden können
            }
            oldTickets.addAll(newTickets);
            Collections.sort(oldTickets, new TicketToBuyTimeComporator());
        }
    }

    /**
     * Sucht das beste Intervall, welches durch das Ticket abgedeckt werden kann und dabei einen hohen Wert erzielt <br/>
     * <p>
     * Es ist nicht wichtig, wie viele Tickets abgedeckt werden, sondern, wie viel diese Fahrscheine zusammen kosten <br/>
     * Vollständige Suche, mit Verschiebung des Startindexes -> wenn Fahrt n nicht mehr im Gültigkeitsbereich liegt,
     * liegt auch Fahrt n+1 nicht mehr im Zeitfenster des Tickets
     *
     * @param ticketsToBuy Liste an Fahrscheinen die ersetzt werden sollen
     * @param ticket       Ersatzfahrschein
     * @return Triplet mit value0 -> startIndex des maximalen Intervalls, <br/>
     * value1-> wie viele Tickets entfallen <br/>
     * value2 -> wie viel diese Tickets zusammen kosten
     * @preconditions Die Tickets sind nach ihrer ersten Abfahrtszeit sortiert
     */
    private static Triplet<Integer, Integer, Integer> findBestTicketIntervall(ArrayList<TicketToBuy> ticketsToBuy, TimeTicket ticket) {
        int maxIndex = Integer.MAX_VALUE;
        int maxTrips = 0;
        int maxPrice = 0;

        for (int i = 0; i < ticketsToBuy.size(); i++) {
            if (isNewValidTicket(ticketsToBuy.get(i), ticket)) {
                maxIndex = i;
                maxTrips = 1;
                break;
            }
        }
        //Keine passenden Tickets gefunden
        if (maxIndex == Integer.MAX_VALUE) {
            return new Triplet<>(0, 0, 0);
        }
        for (int i = maxIndex; i < ticketsToBuy.size(); i++) {
            if (!isNewValidTicket(ticketsToBuy.get(i), ticket)) {
                continue;
            }
            int price = ticketsToBuy.get(i).getTicket().getPrice(MainMenu.myProvider.getPreisstufenIndex(ticketsToBuy.get(i).getPreisstufe()));//Preis des aktuellen Tickets
            int counter = 1;//Wie viele Tickets, ab diesem Ticket genutzt werden können
            for (int k = i + 1; k < ticketsToBuy.size(); k++) {
                if (isNewValidTicket(ticketsToBuy.get(k), ticketsToBuy.get(i), ticket)) {
                    price += ticketsToBuy.get(k).getTicket().getPrice(MainMenu.myProvider.getPreisstufenIndex(ticketsToBuy.get(k).getPreisstufe()));
                    counter++;
                } else {
                    break;
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

    /**
     * Sucht das beste Intervall für ein Ticket heraus <br/>
     * <p>
     * Es wird jedes mögliche Intervall überprüft <br/>
     * Aufgrund der  Sortierung der Fahrten, muss jedoch nicht wirklich jedes Intervall geprüft werden, sondern
     * immer nur der Startzeitpunkt verändert werden und geguckt werden, bis zu welcher Fahrt die
     * Fahrten noch im Intervall liegen
     *
     * @param tripItems Fahrten die optimiert werden soll
     * @param ticket    Ticket das überprüft werden soll
     * @return Pair.first -> Startindex für das größte Intervall, Pair.second -> wie viele Fahrten in dem Intervall liegen
     * @preconditions Fahrten sind chronologisch nach ihrer Abfahrtszeit sortiert und gehören einer Preisstufe an
     */
    public static Pair<Integer, Integer> findBestTimeInterval(ArrayList<TripItem> tripItems, TimeTicket ticket) {
        int maxIndex = Integer.MAX_VALUE;
        int maxTrips = 0;
        //Suche die erste Fahrt, für die das Ticket genutzt werden kann
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
        for (int i = maxIndex; i < tripItems.size(); i++) {
            int counter = 1;
            for (int k = i + 1; k < tripItems.size(); k++) {
                long startTime = tripItems.get(i).getFirstDepartureTime().getTime();
                long endTime = tripItems.get(k).getLastArrivalTime().getTime();
                //Prüfen, ob das Ticket, für diese Fahrt genutzt werden kann
                if (ticket.isValidTrip(tripItems.get(k)) && endTime - startTime <= ticket.getMaxDuration()) {
                    counter++;
                } else {//Wenn nein, brich die suche für diesen Startindex ab
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

    /**
     * Prüft, ob alle Fahrten, die einem Ticket zugeordnet sind, sich durch das neue Ticket ersetzen lassen
     *
     * @param ticketToBuy zu ersetzendes Ticket
     * @param ticket      neues Ticket
     * @return true - wenn das Ticket für alle Fahrten genutzt werden kann & die alle Fahrten in den
     * Nutzungszeitraum des Tickets fallen; sonst false
     */
    private static boolean isNewValidTicket(TicketToBuy ticketToBuy, TimeTicket ticket) {
        for (TripItem tripItem : ticketToBuy.getTripList()) {
            if (!ticket.isValidTrip(tripItem)) {
                return false;
            }
        }
        //Prüfe, ob alle Fahrten zusammen dem Ticket zugeordnet werden können
        //Wenn jede einzelne Fahrt für das Ticket erlaubt ist, muss nur noch geguckt werden, ob der gesamte Nutzungszeitrum kürzer ist, als die
        //maximale Dauer des Tickets
        long startTime = ticketToBuy.getFirstDepartureTime().getTime();
        long endTime = ticketToBuy.getLastArrivalTime().getTime();
        return endTime - startTime <= ticket.getMaxDuration();
    }

    /**
     * Prüft, ob sich die zwei Tickets durch das neue Ticket ersetzen lassen
     * <p>
     * Zu erst wird geprüft, ob das Ticket, welches hinzugefügt werden soll, überhaupt durch das
     * Ticket abedeckt werden darf, falls ja, wird geprüft, ob beide Tickets zusammen durch das
     * neue Ticket abgedeckt werden würden
     *
     * @param ticketToAdd        Ticket das mit dem start-Ticket zusammengelegt werden soll
     * @param currentStartTicket Ticket das als Startpunkt dient
     * @param ticket             Ticket, dass die beiden Tickets ersetzen soll
     * @return true - alle Fahrten des neuen Tickets dürfen mit diesem Ticket zurückgelegt werden
     * und der gesamte Zeitraum zwischen erster Fahrt und letzter Fahrt wird durch dieses Ticket
     * abgedeckt <br/>
     * false - eine Fahrt des neuen Tickets darf nicht mit diesem Ticket zurückgelegt werden, oder
     * der Zeitraum zwischen beiden Fahrscheinen kann nicht durch dieses Ticket abgedeckt werden
     * @preconditions Die erste Fahrt von currentStartTicket liegt vor der ersten Fahrt von ticketToAdd
     */
    private static boolean isNewValidTicket(TicketToBuy ticketToAdd, TicketToBuy currentStartTicket, TimeTicket ticket) {
        if (isNewValidTicket(ticketToAdd, ticket)) {
            long startTime = currentStartTicket.getFirstDepartureTime().getTime();
            long endTime = ticketToAdd.getLastArrivalTime().getTime();
            return endTime - startTime <= ticket.getMaxDuration();
        }
        return false;
    }

    /**
     * Löschen der Fahrten mit Fahschein aus der Liste der zu optimierenden Fahrten
     * <p>
     *
     * @param regionTrips Fahrten der betrachteten Region
     * @param ticketToBuy Fahrschein der gekauft werden soll - Fahrten werden zugeordnet
     * @param allTrips    Alle zu optimierenden Fahrten
     * @param startIndex  gibt die erste zu löschende Fahrt an
     * @param numTrips    gibt an wie viele Fahrten ab dem Startindex gelöscht werden sollen
     */
    public static void deleteTrips(ArrayList<TripItem> allTrips, ArrayList<TripItem> regionTrips, int startIndex, int numTrips, TicketToBuy ticketToBuy) {
        for (int j = startIndex; j < startIndex + numTrips; j++) {
            TripItem tripItem = regionTrips.get(j);
            allTrips.remove(tripItem);
            ticketToBuy.addTripItem(tripItem);
        }
    }
}
