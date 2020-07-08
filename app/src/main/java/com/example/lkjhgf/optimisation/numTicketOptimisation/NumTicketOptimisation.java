package com.example.lkjhgf.optimisation.numTicketOptimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import de.schildbach.pte.dto.Fare;

public class NumTicketOptimisation {

    /**
     * Optimierung mit neuen Mengenfahrscheinen
     *
     * @param tripsWithoutTimeTicket zu optimierende Fahrten
     * @return Für jede Nutzerklasse die Liste an benötigten Fahrscheinen
     */
    public static HashMap<Fare.Type, ArrayList<TicketToBuy>> optimisationNewTickets(HashMap<Fare.Type, ArrayList<TripItem>> tripsWithoutTimeTicket) {
        HashMap<Fare.Type, NumTicketOptimisationHolder> numTicketOptimisationHolder = optimisationWithNewTickets(tripsWithoutTimeTicket);
        return buildTicketList(numTicketOptimisationHolder);
    }

    /**
     * Ermittelt die günstigsten neuen Fahrscheine für die zu optimierenden Fahrten
     *
     * @param userClassTripLists Zu optimierende Fahrten
     * @return für jede Nutzerklasse das beste letzte Ticket
     * @preconditions Möglichst viele Fahrten verwenden ein altes Ticket. Fahrten sind auf Nutzerklassen
     * aufgeteilt.
     */
    private static HashMap<Fare.Type, NumTicketOptimisationHolder> optimisationWithNewTickets(HashMap<Fare.Type, ArrayList<TripItem>> userClassTripLists) {
        //Optimieren mit neuen Fahrscheinen
        HashMap<Fare.Type, NumTicketOptimisationHolder> lastBestTickets = new HashMap<>();
        HashMap<Fare.Type, ArrayList<NumTicket>> allTicketsMap = MainMenu.myProvider.getNumTickets();
        for (Fare.Type type : userClassTripLists.keySet()) {
            if (allTicketsMap.containsKey(type)) {
                lastBestTickets.put(type, buyNewTickets(allTicketsMap.get(type), userClassTripLists.get(type)));
            } else {
                System.err.println("Fehler bei der Optimierung neuer Fahrscheine - unbekannter Tickettyp: " + type);
            }
        }
        return lastBestTickets;
    }

    /**
     * Optimierung, bei der neue Fahrscheine gekauft werden können
     * <p>
     * Bei dieser Optimierung wird "von unten" optimiert, d.h. es wird immer versucht für jede Fahrt
     * der entsprechende Fahrschein mit einer möglichst geringen Preisstufe zu nehmen
     *
     * @param tickets         Alle Mengenfahrscheine, die (unednlich oft) gekauft werden können
     * @param tripsToOptimise Fahrten, die optimiert werden sollen, Preisstufe aufsteigend sortiert
     * @return die Informationen zum letzten Fahrschein, über dessen Vorgänger lassen sich
     * alle benötigten Fahrscheine ermitteln
     * @preconditions Die Fahrten sind auf die verschiedenen Nutzerklassen aufgeteilt, und entsprechend
     * häufig in der Liste der zu optimierenden Fahrten enthalten. <br/>
     * Die Liste der Fahrten ist der gleichen Klasse zugeordnet, wie die Fahrscheine.
     */
    public static NumTicketOptimisationHolder buyNewTickets(ArrayList<NumTicket> tickets,
                                                            ArrayList<TripItem> tripsToOptimise) {
        int maxNumTripTicket = MainMenu.myProvider.getMaxNumTrip();
        //größeres Array, um keine IndexOutOfBounce Fehler zu bekommen, wenn das Minimum gesucht wird -> - maxNumTripTicket viele Fahrten
        NumTicketOptimisationHolder[] allPossibleTicketCombinationHolder = new NumTicketOptimisationHolder[maxNumTripTicket + tripsToOptimise.size()];
        //Initialisierung der ersten Felder, deren Kosteneinträge 0 sind
        for (int index = 0; index < maxNumTripTicket; index++) {
            allPossibleTicketCombinationHolder[index] = new NumTicketOptimisationHolder(null, "", 0, null);
        }
        for (int index = 0; index < tripsToOptimise.size(); index++) {
            //Enthält alle mögliche Kosten für den jeweiligen Trip
            ArrayList<Integer> costs = new ArrayList<>();
            ArrayList<Integer> farezoneIndexList = new ArrayList<>();
            //Alle Tickets ausprobieren
            for (Ticket ticket : tickets) {
                //Ticket ist für die Preisstufe der Fahrt nicht nutzbar
                if (MainMenu.myProvider.getTicketPrice(ticket, tripsToOptimise.get(index).getPreisstufe()) == Integer.MAX_VALUE) {
                    continue;
                }
                //Versuchen, die maximale Preisstufe zu finden, deren Preis gleich mit dem Preis für die aktuelle Preisstufe ist
                //Je nach Provider auch auskommentierbar, beim VRR jedoch zB für Kindertickets nötig
                int maxFarezoneIndex = Util.maxFarezoneIndex(MainMenu.myProvider.getPreisstufenIndex(tripsToOptimise.get(index).getPreisstufe()), ticket);
                farezoneIndexList.add(maxFarezoneIndex);
                //Mehrere Fahrtenschein
                if (ticket instanceof NumTicket) {
                    NumTicket numTicket = (NumTicket) ticket;
                    //Kosten für diesen Fahrschein ermitteln
                    costs.add(allPossibleTicketCombinationHolder[maxNumTripTicket + index - numTicket.getNumTrips()].getAllCosts()
                            + MainMenu.myProvider.getTicketPrice(numTicket, MainMenu.myProvider.getPreisstufe(maxFarezoneIndex)));
                }
            }
            //Suche für diese Fahrt den günstigsten Preis
            int indexOfBestTicket = Util.getMinCostsIndex(costs);
            //Das Ticket, dass diesen Preis erzeugt
            NumTicket bestTicket = tickets.get(indexOfBestTicket);
            // Alle Informationen zum Fahrschein hinzufügen
            allPossibleTicketCombinationHolder[index + maxNumTripTicket] = new NumTicketOptimisationHolder(bestTicket, MainMenu.myProvider.getPreisstufe(farezoneIndexList.get(indexOfBestTicket)),
                    costs.get(indexOfBestTicket), allPossibleTicketCombinationHolder[maxNumTripTicket + index - bestTicket.getNumTrips()]);
        }
        return addTicketToTrips(tripsToOptimise, allPossibleTicketCombinationHolder);
    }

    /**
     * Weist neuen Fahrten bereits gekaufte Fahrscheine zu
     * <p>
     * Ruft für jede Nutzerklasse {@link #useOldTickets(ArrayList freeTcikets, ArrayList Fahrten, Fare.Type)} auf
     *
     * @param freeTickets        Fahrscheine mit mindestens einer freien Fahrt
     * @param userClassTripLists zu optimierende Fahrscheine
     */
    public static void useOldTickets(HashMap<Fare.Type, ArrayList<TicketToBuy>> freeTickets, HashMap<Fare.Type, ArrayList<TripItem>> userClassTripLists) {
        if (!freeTickets.isEmpty()) {
            for (Fare.Type type : freeTickets.keySet()) {
                if (!freeTickets.get(type).isEmpty()) {
                    useOldTickets(freeTickets.get(type), userClassTripLists.get(type), type);
                }
            }
        }
    }

    /**
     * Zuweisen der alten Fahrscheine mit freien Fahrten auf die neuen Fahrten
     * <p>
     * Im Gegensatz zu {@link #buyNewTickets(ArrayList tripsToOptimise, ArrayList Tickets die man kaufen kann)}
     * wird hier "von oben" optimiert, d.h. die Fahrscheine werden von unten durchlaufen und die Fahrten von oben, um
     * wenn möglich Fahrscheinpreisstufe = Fahrtenpreisstufe zu erreichen und falls dies nicht möglich ist, die kleinst
     * mögliche größere Ticketpreisstufe zu wählen
     *
     * @param oldTickets bereits genutzte Fahrscheine mit mindestens einer freien Fahrt
     * @param tripItems  zu optimierende Fahrten
     * @preconditions Aufteilung der Fahrten in die Nutzerklassen, entsprechend häufig enthalten in
     * der Liste <br/>
     * Die Fahrten sind aufsteigend sortiert.
     * @postconditions Für die noch nicht zugewiesenen Fahrten werden neue Fahrscheine optimiert
     */
    private static void useOldTickets(ArrayList<TicketToBuy> oldTickets,
                                      ArrayList<TripItem> tripItems,
                                      Fare.Type type) {
        //Fahrscheine -> kleinst möglichen wählen
        ListIterator<TicketToBuy> ticketToBuyIterator = oldTickets.listIterator();
        while (ticketToBuyIterator.hasNext()) {
            TicketToBuy currentTicket = ticketToBuyIterator.next();
            //Fahrten -> größt mögliche wählen
            for (ListIterator<TripItem> tripItemIterator = tripItems.listIterator(tripItems.size()); tripItemIterator.hasPrevious(); ) {
                TripItem currentTrip = tripItemIterator.previous();
                //Nutzerklasse benötigt keinen Fahrschein -> Überspringen
                if (currentTrip.getUserClassWithoutTicket(type) == 0) {
                    continue;
                }
                //Wenn die Preisstufe der Fahrt <= der Preisstufe des Tickets ist
                if (MainMenu.myProvider.getPreisstufenIndex(currentTrip.getPreisstufe()) <= MainMenu.myProvider.getPreisstufenIndex(currentTicket.getPreisstufe())) {
                    //Weise so oft zu, wie möglich -> Nutzer ohne Ticket und Ticket freie Fahrten besitzt
                    while (currentTrip.getUserClassWithoutTicket(type) > 0 && currentTicket.getFreeTrips() > 0) {
                        //weise die Fahrt dem Fahrschein zu & andersrum
                        currentTrip.addTicket(currentTicket);
                        currentTicket.addTripItem(currentTrip);
                    }
                    //wenn das Ticket keine weiteren freien Fahrten mehr hat, entferne es aus der Liste der möglichen Fahrscheine
                    if (currentTicket.getFreeTrips() == 0) {
                        ticketToBuyIterator.remove();
                        break;
                    }
                }
            }
        }
    }

    /**
     * Weist jeder Fahrt die benötigten Fahrscheine zu und jedem Fahrschein die zugehörigen Fahrten
     *
     * @param tripsToOptimise                    Fahrten die optimiert werden sollen
     * @param allPossibleTicketCombinationHolder ermittelte kostengünstigste Fahrscheine bis zur jeweiligen Position
     * @return das letzte benötigte Ticket, über diesen erhält man Zugriff auf alle weiteren benötigten Fahrscheine
     */
    private static NumTicketOptimisationHolder addTicketToTrips(ArrayList<TripItem> tripsToOptimise,
                                                                NumTicketOptimisationHolder[] allPossibleTicketCombinationHolder) {
        //Zuweisen der Tickets an die Fahrten, dabei wird von hinten angefangen
        int tripIndex = tripsToOptimise.size() - 1;
        int ticketIndex = allPossibleTicketCombinationHolder.length - 1;

        //Über die Fahrten iterieren
        while (tripIndex > -1) {
            TripItem tripItem = tripsToOptimise.get(tripIndex);
            //Zuweisen des Fahrscheins an die Fahrt
            tripItem.addTicket(allPossibleTicketCombinationHolder[ticketIndex].getTicketToBuy());
            tripIndex -= 1;
            //Zuweisen der Fahrt an den Fahrschein
            allPossibleTicketCombinationHolder[ticketIndex].addTripItem(tripItem);
            //Falls es sich um ein Ticket mit mehreren Fahrten handelt, muss dies entsprechend häufig
            //der Fahrt zugewiesen werden (und andersrum)
            NumTicket numTicket = allPossibleTicketCombinationHolder[ticketIndex].getTicket();
            int numTrips = numTicket.getNumTrips();
            while (numTrips > 1 && tripIndex > -1) {
                tripsToOptimise.get(tripIndex).addTicket(allPossibleTicketCombinationHolder[ticketIndex].getTicketToBuy());
                allPossibleTicketCombinationHolder[ticketIndex].addTripItem(tripsToOptimise.get(tripIndex));
                numTrips -= 1;
                tripIndex -= 1;
            }
            //Anpassen des Ticketindex
            ticketIndex -= numTicket.getNumTrips();
        }
        return allPossibleTicketCombinationHolder[allPossibleTicketCombinationHolder.length - 1];
    }

    /**
     * Erstellt die Liste der benöitgten Fahrscheine
     *
     * @param lastBestTickets das letzte Ticket der Optimierung für alle Nutzerklassen
     * @return Liste aller benötigten Fahrscheine
     */
    public static HashMap<Fare.Type, ArrayList<TicketToBuy>> buildTicketList(HashMap<Fare.Type, NumTicketOptimisationHolder> lastBestTickets) {
        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketLists = new HashMap<>();
        for (Fare.Type type : lastBestTickets.keySet()) {
            ArrayList<TicketToBuy> ticketList = createTicketList(lastBestTickets.get(type));
            if (allTicketLists.containsKey(type)) {
                allTicketLists.get(type).addAll(ticketList);
            } else {
                allTicketLists.put(type, ticketList);
            }
        }
        return allTicketLists;
    }

    /**
     * Erzeugt die Liste der benötigten Fahrscheine anhand der Vorgänger des übergebenen Tickets
     * <p>
     * Mittels previous von lastBestTicket wird der Vorgänger des aktuellen Ticketets ermittelt.
     * Diese werden solange der Liste hinzugefügt, bis der Vorgänger kein Ticket mehr besitzt
     *
     * @param lastBestTicket bester letzter Fahrschein
     * @return Liste aller benötigten Fahrscheinen
     */
    public static ArrayList<TicketToBuy> createTicketList(NumTicketOptimisationHolder lastBestTicket) {
        ArrayList<TicketToBuy> ticketList = new ArrayList<>();
        while (lastBestTicket.getTicket() != null) {
            ticketList.add(lastBestTicket.getTicketToBuy());
            lastBestTicket = lastBestTicket.getPrevious();
        }
        return ticketList;
    }


}
