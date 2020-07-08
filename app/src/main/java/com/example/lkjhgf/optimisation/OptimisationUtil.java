package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.schildbach.pte.dto.Fare;

/**
 * Enthält eine Funktion, die den gesammten Optimierungsprozess "steuert" und zugehörige Hilfsfunktionen
 */
public final class OptimisationUtil {

    /**
     * Entfernt aus der Liste der zu optimierenden Fahrten, Fahrten die einem angefangenen Ticket zugewiesen wurden
     * sowie noch nicht benötigte Fahrscheine aus der Liste der benötigten Fahrscheine<br/>
     * <p>
     * Angefangene Fahrscheine: zugeordnete Fahrten werden aus der Liste der zu optimierenden Fahrten entfernt <br/>
     * Neue Fahrscheine: Fahrscheine werden aus der Liste der benötigten Fahrscheine entfernt
     *
     * @param activeTickets      gespeicherte Fahrscheine
     * @param userClassTripLists Alle Fahrten die eine gültige Preisstufe haben
     * @param freeTickets        Liste mit Fahrscheinen, die mindestens eine freie Fahrt haben
     * @param allTicketLists     Liste mit Fahrscheinen, die für die Fahrten benötigt werden -> wird am Ende gespeichert
     */
    public static void cleanUpTicketsAndTrips(HashMap<Fare.Type, ArrayList<TicketToBuy>> activeTickets,
                                              HashMap<Fare.Type, ArrayList<TripItem>> userClassTripLists,
                                              HashMap<Fare.Type, ArrayList<TicketToBuy>> freeTickets,
                                              HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketLists) {
        for (Fare.Type currentType : activeTickets.keySet()) {
            //Die gespeicherten Fahrscheine der aktuellen Nutzerklasse
            ArrayList<TicketToBuy> userClassActiveTickets = activeTickets.get(currentType);
            //Fahrscheine mit freien Fahrten dieser Nutzerklasse
            ArrayList<TicketToBuy> freeUserClassTickets = new ArrayList<>();
            //Alle neuen Fahrscheine dieser Nutzerklasse
            ArrayList<TicketToBuy> allUserClassTickets = new ArrayList<>();
            //Fahrten dieser Nutzerklasse
            ArrayList<TripItem> userClassTrips = userClassTripLists.get(currentType);
            //Für jedes Ticket der Nutzerklasse:
            for (Iterator<TicketToBuy> userClassActiveTicketsIterator = userClassActiveTickets.iterator(); userClassActiveTicketsIterator.hasNext(); ) {
                TicketToBuy currentTicket = userClassActiveTicketsIterator.next();
                //Prüfen, ob es sich um ein zukünftiges Ticket handelt
                if (currentTicket.isFutureTicket()) {
                    //Falls ja, müssen zu erst die Fahrten des Tickets diesen Fahrscheine entfernt bekommen
                    removeTicketFromTrips(currentTicket, userClassTrips);
                    //Entfernen des zukünftigen Fahrscheins
                    userClassActiveTicketsIterator.remove();
                } else {
                    //Kein zukünftiges Ticket
                    saveTicketRemoveTripFromTripList(allUserClassTickets, currentTicket, userClassTrips, freeUserClassTickets);
                }
            }
            allTicketLists.put(currentType, allUserClassTickets);
            freeTickets.put(currentType, freeUserClassTickets);
        }
    }

    /**
     * Entfernt allen Fahrten, die dem Ticket zugewiesen sind, dieses Ticket
     * <p>
     * Wird für zukünftige Fahrscheine ausgeführt
     *
     * @param currentTicket  Aktuell Betrachtetes Ticket
     * @param userClassTrips Alle zu optimierenden Fahrten
     */
    private static void removeTicketFromTrips(TicketToBuy currentTicket, ArrayList<TripItem> userClassTrips) {
        //Über alle zugeordneten Fahrten iterieren
        for (TripItem currentTrip : currentTicket.getTripList()) {
            if (userClassTrips != null && !userClassTrips.isEmpty()) {
                int index = userClassTrips.indexOf(currentTrip);
                ArrayList<TripQuantity> tripQuantities = currentTicket.getTripQuantities();
                int quantityIndex = -1;
                for(int i = 0; i < tripQuantities.size(); i++){
                    if(tripQuantities.get(i).getTripItem().getTripID().equals(currentTrip.getTripID())){
                        quantityIndex = i;
                        break;
                    }
                }
                if(index != -1 && quantityIndex != -1){
                    int quantity = currentTicket.getTripQuantities().get(quantityIndex).getQuantity();
                    userClassTrips.get(index).removeTicket(currentTicket.getTicket().getType(), currentTicket.getTicketID(), quantity);
                }
            }
        }
    }

    /**
     * Entfernt die Fahrten, die dem Ticket zugewiesen sind, aus der Liste der zu optimierenden Fahrten <br/>
     * <p>
     * Wird für angefangene Fahrscheine ausgeführt
     *
     * @param allUserClassTickets  alle benötigten Fahrscheine für diese Nutzerklasse
     * @param currentTicket        aktuell betrachtets Ticket
     * @param userClassTrips       alle der Nutzerklasse zugeordnete Fahrten
     * @param freeUserClassTickets Fahrscheine der aktuellen Nutzerklasse mit mindestens einer freien Fahrt
     */
    private static void saveTicketRemoveTripFromTripList(ArrayList<TicketToBuy> allUserClassTickets,
                                                         TicketToBuy currentTicket,
                                                         ArrayList<TripItem> userClassTrips,
                                                         ArrayList<TicketToBuy> freeUserClassTickets) {
        //Ticket merken
        allUserClassTickets.add(currentTicket);
        //Die zugeordneten Fahrten dieses Tickets, aus der Liste der zu optimierenden Fahrten entfernen
        for (TripQuantity tripQuantity : currentTicket.getTripQuantities()) {
            int index = userClassTrips.indexOf(tripQuantity.getTripItem());
            if(index > -1){
                boolean needsTicket = false;
                for(Fare.Type type : tripQuantity.getTripItem().getNumUserClasses().keySet()){
                    if(tripQuantity.getTripItem().getUserClassWithoutTicket(type) != 0){
                        needsTicket = true;
                    }
                }
                if(!needsTicket){
                    userClassTrips.remove(index);
                }
            }
        }
        //Wenn noch freie Fahrten vorhanden sind -> diese zur Liste der Fahrscheine mit freien Fahrten
        //hinzufügen
        if (currentTicket.getFreeTrips() > 0) {
            freeUserClassTickets.add(currentTicket);
        }
    }

    /**
     * Erzeugt eine neue Liste mit allen Fahrten, die optimiert werden sollen <br/>
     * <p>
     * Enthalten sind nur Fahrten mit einer gültigen Preisstufe
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
                }
            }
        }
        return newTripList;
    }

}
