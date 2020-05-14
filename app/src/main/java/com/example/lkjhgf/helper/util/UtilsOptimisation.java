package com.example.lkjhgf.helper.util;

import android.app.Activity;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.optimisation.Optimisation;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketOptimisationHolder;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import de.schildbach.pte.dto.Fare;

public final class UtilsOptimisation {

    public static HashMap<Fare.Type, ArrayList<TicketToBuy>> brauchtEinenTollenNamen(ArrayList<TripItem> tripItems, Activity activity) {
        //Fahrten ohne Preisstufe sowie nicht zu optimierende Fahrten aussortieren
        ArrayList<TripItem> copy = Optimisation.removeTrips(tripItems);
        // Fahrten nach den Preisstufen sortieren
        Collections.sort(copy, MainMenu.myProvider);
        //Fahrten auf Nutzerklassen aufteilen
        HashMap<Fare.Type, ArrayList<TripItem>> userClassTripLists = MainMenu.myProvider.createUserClassTripLists(copy);

        //Fahrscheine der letzten Optimierung
        HashMap<Fare.Type, ArrayList<TicketToBuy>> activeTickets = new HashMap(AllTickets.loadTickets(activity));
        //Liste mit allen benötigten Fahrscheinen
        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketLists = new HashMap<>();
        //Liste mit Fahrscheinen, auf denen noch mindestens eine Fahrt frei ist
        HashMap<Fare.Type, ArrayList<TicketToBuy>> freeTickets = new HashMap<>();

        for (Iterator<Fare.Type> it = activeTickets.keySet().iterator(); it.hasNext(); ) {
            //Die gespeicherten Fahrscheine der aktuellen Nutzerklasse
            Fare.Type currentType = it.next();
            ArrayList<TicketToBuy> userClassActiveTickets = activeTickets.get(currentType);
            //Fahrscheine mit freien Fahrten dieser Nutzerklasse
            ArrayList<TicketToBuy> freeUserClassTickets = new ArrayList<>();
            //Alle neuen Fahrscheine dieser Nutzerklasse
            ArrayList<TicketToBuy> allUserClassTickets = new ArrayList<>();
            //Fahrten dieser Nutzerklasse
            ArrayList<TripItem> userClassTrips = userClassTripLists.get(currentType);
            //Für jedes Ticket der Nutzerklasse:
            for (Iterator<TicketToBuy> it2 = userClassActiveTickets.iterator(); it2.hasNext(); ) {
                TicketToBuy currentTicket = it2.next();
                //Prüfen, ob es sich um ein zukünftiges Ticket handelt
                if (currentTicket.isFutureTicket()) {
                    //Falls ja, müssen zu erst die Fahrten des Tickets die Fahrscheine freigegeben bekommen

                    //Über alle zugeordneten Fahrten iterieren
                    for (Iterator<TripItem> tripItemIterator = currentTicket.getTripList().iterator(); tripItemIterator.hasNext(); ) {
                        TripItem currentTrip = tripItemIterator.next();
                        if (userClassTrips != null && !userClassTrips.isEmpty()) {
                            int index = userClassTrips.indexOf(currentTrip);
                            if (index != -1) {
                                userClassTrips.get(index).removeTickets(currentType, currentTicket.getTicketID());
                            }
                        }
                    }
                    //Entfernen des zukünftigen Fahrscheins
                    it2.remove();
                } else {
                    //Kein zukünftiges Ticket
                    //Ticket merken
                    allUserClassTickets.add(currentTicket);
                    //Die zugeordneten Fahrten dieses Tickets, aus der Liste der zu optimierenden Fahrten entfernen
                    for(TicketToBuy.TripQuantity tripQuantity : currentTicket.getTripQuantities()){
                        int quantity = tripQuantity.getQuantity();
                        for(int i = 0; i < quantity; i++){
                            userClassTrips.remove(tripQuantity.getTrip());
                        }
                    }

                    //Wenn noch freie Fahrten vorhanden sind -> diese zur Liste der Fahrscheine mit freien Fahrten
                    //hinzufügen
                    if (currentTicket.getFreeTrips() > 0) {
                        freeUserClassTickets.add(currentTicket);
                    }
                }
            }
            allTicketLists.put(currentType, allUserClassTickets);
            freeTickets.put(currentType, freeUserClassTickets);
        }

        //Optimieren
        //TODO dieser Teil funktioniert nur für NumTicket
        //Optimierung mit alten Fahrscheinen
        if (!freeTickets.isEmpty()) {
            for (Fare.Type type : freeTickets.keySet()) {
                if (!freeTickets.get(type).isEmpty()) {
                    Optimisation.optimisationWithOldTickets(freeTickets.get(type), userClassTripLists.get(type));
                }
            }
        }

        //Optimieren mit neuen Fahrscheinen
        HashMap<Fare.Type, TicketOptimisationHolder> lastBestTickets = new HashMap<>();
        HashMap<Fare.Type, ArrayList<Ticket>> allTicketsMap = MainMenu.myProvider.getAllTickets();
        for (Iterator<Fare.Type> iterator = userClassTripLists.keySet().iterator(); iterator.hasNext(); ) {
            Fare.Type type = iterator.next();
            if (allTicketsMap.containsKey(type)) {
                lastBestTickets.put(type, Optimisation.optimisationBuyNewTickets(allTicketsMap.get(type), userClassTripLists.get(type)));
            } else {
                System.err.println("Fehler bei der Optimierung neuer Fahrscheine - unbekannter Tickettyp: " + type);
            }
        }
        //TicketListe Bauen
        for (Fare.Type type : lastBestTickets.keySet()) {
            ArrayList<TicketToBuy> ticketList = Optimisation.createTicketList(lastBestTickets.get(type));
            if (allTicketLists.containsKey(type)) {
                allTicketLists.get(type).addAll(ticketList);
            } else {
                allTicketLists.put(type, ticketList);
            }

        }

        return allTicketLists;
    }


}
