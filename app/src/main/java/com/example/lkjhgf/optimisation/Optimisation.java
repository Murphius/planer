package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class Optimisation {

    /**
     * Erzeugt eine neue Liste mit allen Fahrten, die optimiert werden sollen <br/>
     * <p>
     * Enthalten sind nur Fahrten mit einer gültigen Preisstufe und deren
     * Fahrschein noch nicht "angefangen" ist
     *
     * @param tripItems Liste mit allen Fahrten
     * @return Liste mit allen Fahrten die optimiert werden können
     */
    public static ArrayList<TripItem> removeTrips(ArrayList<TripItem> tripItems) {
        ArrayList<TripItem> newTripList = new ArrayList<>();
        for (TripItem tripItem : tripItems) {
            if (!tripItem.isComplete()) {
                if (MainMenu.myProvider.checkContains(tripItem.getPreisstufe())) {
                    newTripList.add(tripItem);
                    //TODO Ticketliste prüfen
                    //if(tripItem.getTrip().getFirstDepartureTime().after(Calendar.getInstance().getTime())){
                     //   tripItem.removeTickets();
                    //}
                }
            }
        }
        return newTripList;
    }

    /**
     *
     *
     *
     * ierungszauber
     */
    public static TicketInformationHolder optimisationBuyNewTickets(ArrayList<Ticket> tickets, ArrayList<TripItem> tripsToOptimise) {
        int maxNumTripTicket = MainMenu.myProvider.getMaxNumTrip();

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
            for (Ticket ticket : tickets) {
                //Mehrere Fahrtenschein
                if (ticket instanceof NumTicket) {
                    NumTicket numTicket = (NumTicket) ticket;
                    //Kosten für diesen Fahrschein ermitteln
                    costs.add(allPossibleTicketCombinationHolder[maxNumTripTicket + index - numTicket.getNumTrips()].getAllCosts()
                            + MainMenu.myProvider.getTicketPrice(numTicket, tripsToOptimise.get(index).getPreisstufe()));
                }
            }
            //Suche für diese Fahrt den günstigsten Preis
            int indexOfBestTicket = getMinCostsIndex(costs);
            //Das Ticket, dass diesen Preis erzeugt
            Ticket bestTicket = tickets.get(indexOfBestTicket);
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
            tripItem.addTicket(allPossibleTicketCombinationHolder[ticketIndex].getTicket(), allPossibleTicketCombinationHolder[ticketIndex].getPreisstufe(),allPossibleTicketCombinationHolder[ticketIndex].getTicket().getType());
            allPossibleTicketCombinationHolder[ticketIndex].addTripItem(tripItem);
            if (allPossibleTicketCombinationHolder[ticketIndex].getTicket() instanceof NumTicket) {
                NumTicket numTicket = (NumTicket) allPossibleTicketCombinationHolder[ticketIndex].getTicket();
                int numTrips = numTicket.getNumTrips();
                while (numTrips > 1 && tripIndex > 0) { //TODO wirklich > 0 nötig ?
                    tripIndex -= 1;
                    tripsToOptimise.get(tripIndex).addTicket(allPossibleTicketCombinationHolder[ticketIndex].getTicket(), allPossibleTicketCombinationHolder[ticketIndex].getPreisstufe(),allPossibleTicketCombinationHolder[ticketIndex].getTicket().getType());
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

    public static ArrayList<TicketToBuy> createTicketList(TicketInformationHolder lastBestTicket) {
        ArrayList<TicketToBuy> ticketList = new ArrayList<>();
        while (lastBestTicket.getTicket() != null) {
            ticketList.add(new TicketToBuy(lastBestTicket.getTicket(), lastBestTicket.getPreisstufe(), lastBestTicket.getTripList()));
            lastBestTicket = lastBestTicket.getPrevious();
        }
        return ticketList;
    }

    public static void optimisationWithOldTickets(ArrayList<TicketToBuy> oldTickets, ArrayList<TripItem> tripItems){
        ArrayList<TicketToBuy> inverseOldTickets = new ArrayList<>();
        for(int i = oldTickets.size() -1; i> -1; i--){
            inverseOldTickets.add(oldTickets.get(i));
        }
        ArrayList<TripItem> inverseTripItems = new ArrayList<>();
        for(int i = tripItems.size() -1; i > -1; i--){
            inverseTripItems.add(tripItems.get(i));
        }

        Iterator<TicketToBuy> it = inverseOldTickets.iterator();

        while (it.hasNext()){
            TicketToBuy currentTicket = it.next();
            for(Iterator<TripItem> itemIterator = inverseTripItems.iterator(); itemIterator.hasNext() && currentTicket != null;){
                TripItem currentTripItem = itemIterator.next();
                if(MainMenu.myProvider.getPreisstufenIndex(currentTripItem.getPreisstufe()) <= MainMenu.myProvider.getPreisstufenIndex(currentTicket.getPreisstufe())){
                    currentTripItem.addTicket(currentTicket.getTicket(), currentTicket.getPreisstufe(), currentTicket.getTicket().getType());
                    currentTicket.addTripItem(currentTripItem);
                    if(currentTicket.getFreeTrips() == 0 ){
                        it.remove();
                        if(it.hasNext()){
                            currentTicket = it.next();
                        }else{
                            currentTicket = null;
                        }
                    }
                    tripItems.remove(currentTripItem);
                }
            }
        }

    }
}
