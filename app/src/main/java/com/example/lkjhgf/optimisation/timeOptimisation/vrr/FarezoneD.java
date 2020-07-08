package com.example.lkjhgf.optimisation.timeOptimisation.vrr;

import android.util.Pair;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.TicketToBuyTimeComporator;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.optimisation.timeOptimisation.FarezoneUtil;
import com.example.lkjhgf.optimisation.timeOptimisation.Util;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

/**
 * Optimierung für die Preisstufe D
 */
public class FarezoneD {

    public static ArrayList<TicketToBuy> optimisation(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> tickets) {
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 1;
        ArrayList<TripItem> tripsD = Util.collectTripsPreisstufe(allTrips, preisstufenIndex);
        if (tripsD.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<TicketToBuy> toBuyArrayList = new ArrayList<>();
        for (int ticketIndex = 0; ticketIndex < tickets.size(); ticketIndex++) {
            if (allTrips.size() >= tickets.get(ticketIndex).getMinNumTrips(preisstufenIndex)) {
                Pair<Integer, Integer> bestTicketInterval = Util.findBestTimeInterval(tripsD, tickets.get(ticketIndex));//Finden des besten Intervall
                while (bestTicketInterval.second >= tickets.get(ticketIndex).getMinNumTrips(preisstufenIndex)) {
                    TicketToBuy ticketToBuy = new TicketToBuy(tickets.get(ticketIndex), MainMenu.myProvider.getPreisstufe(preisstufenIndex));//Neues Ticket
                    ticketToBuy.setValidFarezones( MainMenu.myProvider.getFarezones(), 0);//Gültigkeitsbereich
                    toBuyArrayList.add(ticketToBuy);//Ticket merken
                    removeTrips(tripsD, allTrips, ticketToBuy, bestTicketInterval.first, bestTicketInterval.second);//Fahrten aus der Liste der zu optimierenden Fahrten löschen
                    bestTicketInterval = Util.findBestTimeInterval(tripsD, tickets.get(ticketIndex));//Neues Intervall bestimmen
                }
                //Ggf. Zusammenfassen
                if (toBuyArrayList.size() > 1) {
                    Collections.sort(toBuyArrayList, new TicketToBuyTimeComporator());
                    Util.sumUpTickets(toBuyArrayList, tickets, ticketIndex + 1, preisstufenIndex);
                }
                FarezoneUtil.checkTicketsForOtherTrips(allTrips, toBuyArrayList);//Ggf. für andere Fahrten verwenden
                tripsD = Util.collectTripsPreisstufe(allTrips, preisstufenIndex);
            }
        }
        return toBuyArrayList;
    }

    /**
     * Löschen der Fahrten aus der Liste der zu optimierenden Fahrten
     * @param tripItems Liste aus der die Fahrten stammen
     * @param allTrips Alle Fahrten die optimiert werden sollen
     * @param ticketToBuy Neues Ticket, dem die Fahrten zugewiesen werden sollen
     * @param startIndex Start Index für die Liste tripItems
     * @param quantity wie viele Fahrten ab dem Startindex entfernt / hinzugefügt werden sollen
     */
    private static void removeTrips(ArrayList<TripItem> tripItems, ArrayList<TripItem> allTrips, TicketToBuy ticketToBuy, int startIndex, int quantity){
        int i = 0;
        for (ListIterator<TripItem> listIterator = tripItems.listIterator(); i < startIndex + quantity; ) {
            TripItem current = listIterator.next();
            if (i >= startIndex) {
                listIterator.remove();
                ticketToBuy.addTripItem(current);
                allTrips.remove(current);
            }
            i++;
        }
    }
}
