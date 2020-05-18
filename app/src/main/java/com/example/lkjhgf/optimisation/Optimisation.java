package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Jeweiligen Optimierungsfunktionen und Hilfsfunktionen
 */
public class Optimisation {

    /**
     * Optimierung, bei der neue Fahrscheine gekauft werden können
     * <p>
     * Bei dieser Optimierung wird "von unten" optimiert, d.h. es wird immer versucht für jede Fahrt
     * der entsprechende Fahrschein mit einer möglichst geringen Preisstufe zu nehmen
     *
     * @param tickets         Alle Fahrscheine, die (unednlich oft) gekauft werden können
     * @param tripsToOptimise Fahrten, die optimiert werden sollen, Preisstufe aufsteigend sortiert
     * @return die Informationen zum letzten Fahrschein, über dessen Vorgänger lassen sich
     * alle benötigten Fahrscheine ermitteln
     * @preconditions Die Fahrten sind auf die verschiedenen Nutzerklassen aufgeteilt, und entsprechend
     * häufig in der Liste der zu optimierenden Fahrten enthalten. <br/>
     * Die Liste der Fahrten ist der gleichen Klasse zugeordnet, wie die Fahrscheine.
     */
    static TicketOptimisationHolder optimisationBuyNewTickets(ArrayList<Ticket> tickets,
                                                                     ArrayList<TripItem> tripsToOptimise) {
        int maxNumTripTicket = MainMenu.myProvider.getMaxNumTrip();

        //größeres Array, um keine IndexOutOfBounce Fehler zu bekommen, wenn das Minimum gesucht wird -> - maxNumTripTicket viele Fahrten
        TicketOptimisationHolder[] allPossibleTicketCombinationHolder = new TicketOptimisationHolder[maxNumTripTicket + tripsToOptimise.size()];

        //Initialisierung der ersten Felder, mit ohne Kosten
        for (int index = 0; index < maxNumTripTicket; index++) {
            allPossibleTicketCombinationHolder[index] = new TicketOptimisationHolder(null,
                    "",
                    0,
                    null);
        }

        for (int index = 0; index < tripsToOptimise.size(); index++) {
            //Enthält alle mögliche Kosten für den jeweiligen Trip
            ArrayList<Integer> costs = new ArrayList<>();
            ArrayList<Integer> preisstufenindex = new ArrayList<>();
            //Alle Tickets ausprobieren
            for (Ticket ticket : tickets) {
                //Ticket ist für die Preisstufe der Fahrt nicht nutzbar
                if(MainMenu.myProvider.getTicketPrice(ticket, tripsToOptimise.get(index).getPreisstufe()) == Integer.MAX_VALUE){
                    continue;
                }
                int preisstufenIndex = MainMenu.myProvider.getPreisstufenIndex(tripsToOptimise.get(index).getPreisstufe());
                //Versuchen, die maximale Preisstufe zu finden, deren Preis gleich mit dem Preis für die aktuelle Preisstufe ist
                //Je nach Provider auch auskommentierbar, beim VRR jedoch zB für Kindertickets nötig
                while (preisstufenIndex < MainMenu.myProvider.getPreisstufenSize() - 1){
                    String kleinePreisstufe = MainMenu.myProvider.getPreisstufe(preisstufenIndex);
                    String groesserePreisstufe = MainMenu.myProvider.getPreisstufe(preisstufenIndex + 1);
                    if(MainMenu.myProvider.getTicketPrice(ticket, kleinePreisstufe) >= MainMenu.myProvider.getTicketPrice(ticket, groesserePreisstufe)){
                        preisstufenIndex = preisstufenIndex + 1;
                    }else{
                        break;
                    }
                }
                preisstufenindex.add(preisstufenIndex);
                //Mehrere Fahrtenschein
                if (ticket instanceof NumTicket) {
                    NumTicket numTicket = (NumTicket) ticket;
                    //Kosten für diesen Fahrschein ermitteln
                    costs.add(allPossibleTicketCombinationHolder[maxNumTripTicket + index - numTicket.getNumTrips()].getAllCosts()
                            + MainMenu.myProvider.getTicketPrice(numTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex)));
                }
            }
            //Suche für diese Fahrt den günstigsten Preis
            int indexOfBestTicket = getMinCostsIndex(costs);
            //Das Ticket, dass diesen Preis erzeugt
            Ticket bestTicket = tickets.get(indexOfBestTicket);
            if (bestTicket instanceof NumTicket) {
                NumTicket numTicket = (NumTicket) bestTicket;
                // Alle Informationen zum Fahrschein hinzufügen
                allPossibleTicketCombinationHolder[index + maxNumTripTicket] = new TicketOptimisationHolder(
                        bestTicket,
                        MainMenu.myProvider.getPreisstufe(preisstufenindex.get(indexOfBestTicket)),
                        costs.get(indexOfBestTicket),
                        allPossibleTicketCombinationHolder[maxNumTripTicket + index - numTicket.getNumTrips()]);
            }
        }
        return addTicketToTrips(tripsToOptimise, allPossibleTicketCombinationHolder);
    }

    /**
     * Zuweisen der alten Fahrscheine mit freien Fahrten auf die neuen Fahrten
     * <p>
     * Im Gegensatz zu {@link #optimisationBuyNewTickets(ArrayList tripsToOptimise, ArrayList Tickets die man kaufen kann)}
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
    static void optimisationWithOldTickets(ArrayList<TicketToBuy> oldTickets,
                                           ArrayList<TripItem> tripItems) {
        //Fahrscheine -> kleinst möglichen wählen
        ListIterator<TicketToBuy> ticketToBuyIterator = oldTickets.listIterator();
        while (ticketToBuyIterator.hasNext()) {
            TicketToBuy currentTicket = ticketToBuyIterator.next();
            //Fahrten -> größt mögliche wählen
            for (ListIterator<TripItem> tripItemIterator = tripItems.listIterator(tripItems.size()); tripItemIterator.hasPrevious();) {
                TripItem currentTrip = tripItemIterator.previous();
                //Wenn die Preisstufe der Fahrt <= der Preisstufe des Tickets ist
                if (MainMenu.myProvider.getPreisstufenIndex(currentTrip.getPreisstufe()) <= MainMenu.myProvider.getPreisstufenIndex(
                        currentTicket.getPreisstufe())) {
                    //weise die Fahrt dem Fahrschein zu & andersrum
                    currentTrip.addTicket(currentTicket);
                    currentTicket.addTripItem(currentTrip);
                    //entferne die Fahrt aus den noch zu optimierenden Fahrten
                    tripItemIterator.remove();
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
    private static TicketOptimisationHolder addTicketToTrips(ArrayList<TripItem> tripsToOptimise,
                                                             TicketOptimisationHolder[] allPossibleTicketCombinationHolder) {
        //Zuweisen der Tickets an die Fahrten, dabei wird von hinten angefangen
        int tripIndex = tripsToOptimise.size() - 1;
        int ticketIndex = allPossibleTicketCombinationHolder.length - 1;

        //Über die Fahrten iterieren
        while (tripIndex > -1) {
            TripItem tripItem = tripsToOptimise.get(tripIndex);
            //Zuweisen des Fahrscheins an die Fahrt
            tripItem.addTicket(allPossibleTicketCombinationHolder[ticketIndex].getTicketToBuy());
            //Zuweisen der Fahrt an den Fahrschein
            allPossibleTicketCombinationHolder[ticketIndex].addTripItem(tripItem);
            //Falls es sich um ein Ticket mit mehreren Fahrten handelt, muss dies entsprechend häufig
            //der Fahrt zugewiesen werden (und andersrum)
            if (allPossibleTicketCombinationHolder[ticketIndex].getTicket() instanceof NumTicket) {
                NumTicket numTicket = (NumTicket) allPossibleTicketCombinationHolder[ticketIndex].getTicket();
                int numTrips = numTicket.getNumTrips();
                while (numTrips > 1 && tripIndex > 0) {
                    tripIndex -= 1;
                    tripsToOptimise.get(tripIndex).addTicket(allPossibleTicketCombinationHolder[ticketIndex].getTicketToBuy());
                    allPossibleTicketCombinationHolder[ticketIndex].addTripItem(tripsToOptimise.get(
                            tripIndex));
                    numTrips -= 1;
                }
                //Anpassen des Ticketindex
                ticketIndex -= numTicket.getNumTrips();
            }
            //Nächste Fahrt
            tripIndex -= 1;
        }
        return allPossibleTicketCombinationHolder[allPossibleTicketCombinationHolder.length - 1];
    }

    /**
     * Sucht in einer Liste nach dem Eintrag mit den geringsten Kosten
     *
     * @param costs Liste mit allen Kosten
     * @return Index des Eintrags mit dem geringsten Wert
     */
    private static int getMinCostsIndex(ArrayList<Integer> costs) {
        int minCost = costs.get(0);
        int index = 0;
        for (int i = 1; i < costs.size(); i++) {
            if (costs.get(i) < minCost) {
                minCost = costs.get(i);
                index = i;
            }
        }
        return index;
    }

}
