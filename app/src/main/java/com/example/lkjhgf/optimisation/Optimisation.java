package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;

public class Optimisation {

    /**
     * Entfernt alle Fahrten, die keine gültige Preisstufe haben, oder die ein "angefangenes"
     * Ticket benutzen vor der Optimierung
     *
     * @param tripItems Liste an Fahrten die bearbeitet werden soll
     */
    public static void removeTrips(ArrayList<TripItem> tripItems) {
        for(TripItem tripItem : tripItems){
            if(!tripItem.isComplete()){
                if(!VRRpreisstufenComparator.checkContains(tripItem.getPreisstufe())){
                    tripItems.remove(tripItem);
                }
            }
        }
    }

    public static ArrayList<TripItem> createAdultTripList(ArrayList<TripItem> allTrips, PreisProvider preisProvider) {
        ArrayList<TripItem> adultList = new ArrayList<>();
        for (TripItem tripItem : allTrips) {
            System.out.println(tripItem.getNumAdult());
            for (int i = 1; i <= tripItem.getNumAdult(); i++) {
                adultList.add(tripItem);
            }
        }
        System.out.println(adultList.size());
        return adultList;
    }

    /**
     * Optimierungszauber
     */
    public static TicketInformationHolder optimisation(PreisProvider preisProvider, ArrayList<TripItem> tripsToOptimise) {
        int maxNumTripTicket = preisProvider.getMaxNumTrip();

        //größeres Array, um keine IndexOutOfBounce Fehler zu bekommen, wenn das Minimum gesucht wird -> - maxNumTripTicket viele Fahrten
        TicketInformationHolder[] allPossibleTicketCombinationHolder = new TicketInformationHolder[maxNumTripTicket + tripsToOptimise.size()];

        //Initialisierung der ersten Felder, mit ohne Kosten
        for (int index = 0; index < maxNumTripTicket; index++) {
            allPossibleTicketCombinationHolder[index] = new TicketInformationHolder(null, "", 0, null);
        }

        for (int index = 0; index < tripsToOptimise.size(); index++) {
            //Enthält alle mögliche Kosten für den jeglichen Trip
            ArrayList<Integer> costs = new ArrayList<>();
            //Alle Tickets ausprobieren
            for (Ticket ticket : preisProvider.getAllTickets()) {
                //Mehrere Fahrtenschein
                if (ticket instanceof NumTicket) {
                    NumTicket numTicket = (NumTicket) ticket;
                    //Kosten für diesen Fahrschein ermitteln
                    costs.add(allPossibleTicketCombinationHolder[maxNumTripTicket + index - numTicket.getNumTrips()].getCosts()
                            + preisProvider.getPrice(numTicket, tripsToOptimise.get(index).getPreisstufe()));
                }
            }
            //Suche für diese Fahrt den günstigsten Preis
            int indexOfBestTicket = getMinCostsIndex(costs);
            //Das Ticket, dass diesen Preis erzeugt
            Ticket bestTicket = preisProvider.getAllTickets().get(indexOfBestTicket);
            if (bestTicket instanceof NumTicket) {
                NumTicket numTicket = (NumTicket) bestTicket;
                // Alle Informationen zum Fahrschein hinzufügen
                allPossibleTicketCombinationHolder[index + maxNumTripTicket] = new TicketInformationHolder(bestTicket, tripsToOptimise.get(index).getPreisstufe(),
                        costs.get(indexOfBestTicket), allPossibleTicketCombinationHolder[maxNumTripTicket + index - numTicket.getNumTrips()]);
            }
        }

        //Zuweisen der Tickets an die Fahrten
        int tripIndex = tripsToOptimise.size() - 1;
        int ticketIndex = allPossibleTicketCombinationHolder.length - 1;

        while (tripIndex > -1) {
            TripItem tripItem = tripsToOptimise.get(tripIndex);
            tripItem.addTicket(allPossibleTicketCombinationHolder[ticketIndex].getTicket(), allPossibleTicketCombinationHolder[ticketIndex].getPreisstufe());
            allPossibleTicketCombinationHolder[ticketIndex].addTripItem(tripItem);
            if (allPossibleTicketCombinationHolder[ticketIndex].getTicket() instanceof NumTicket) {
                NumTicket numTicket = (NumTicket) allPossibleTicketCombinationHolder[ticketIndex].getTicket();
                int numTrips = numTicket.getNumTrips();
                while (numTrips > 1 && tripIndex > 0) { //TODO wirklich > 0 nötig ?
                    tripIndex -= 1;
                    tripsToOptimise.get(tripIndex).addTicket(allPossibleTicketCombinationHolder[ticketIndex].getTicket(), allPossibleTicketCombinationHolder[ticketIndex].getPreisstufe());
                    allPossibleTicketCombinationHolder[ticketIndex].addTripItem(tripsToOptimise.get(tripIndex));
                    numTrips -= 1;
                }
                ticketIndex -= numTicket.getNumTrips();
            }
            tripIndex -= 1;
        }
        return allPossibleTicketCombinationHolder[allPossibleTicketCombinationHolder.length - 1];
    }

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

    public static ArrayList<TicketInformationHolder> createTicketList(TicketInformationHolder lastBestTicket) {
        ArrayList<TicketInformationHolder> ticketList = new ArrayList<>();
        while (lastBestTicket.getTicket() != null) {
            ticketList.add(lastBestTicket);
            lastBestTicket = lastBestTicket.getPrevious();
        }
        return ticketList;
    }
}
