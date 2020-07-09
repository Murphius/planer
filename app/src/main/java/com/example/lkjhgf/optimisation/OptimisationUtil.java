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
     * Leert die Liste zugehöriger Fahrten eines Tickets<br/>
     * <p>
     * Wird für zukünftige Fahrscheine ausgeführt <br/>
     * Die Anzahl nutzer ohne Tickets von jedem zugehörigen Fahrschein erhöht sich
     *
     * @param currentTicket  Aktuell Betrachtetes Ticket
     * @param userClassTrips Alle zu optimierenden Fahrten einer Nutzerklasse
     * @preconditions Die Nutzerklasse des Tickets stimmt mit der der Fahrtenliste überein
     */
    private static void removeTicketFromTrips(TicketToBuy currentTicket, ArrayList<TripItem> userClassTrips) {
        //Über alle zugeordneten Fahrten iterieren
        for (TripQuantity tripQuantity : currentTicket.getTripQuantities()) {
            if (userClassTrips != null && !userClassTrips.isEmpty()) {
                //Index der jeweiligen Fahrt in der Liste aller Fahrten holen
                int index = userClassTrips.indexOf(tripQuantity.getTripItem());
                if (index != -1) {
                    //Anzahl nutzer ohne Tickets entsprechend oft erhöhen
                    int quantity = tripQuantity.getQuantity();
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
     * @preconditions Glecihe Nutzerklasse für die Tickets und Fahrten
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
            if (index > -1) {
                if (!needsTicket(tripQuantity.getTripItem())) {//Wenn keine Person mehr ein Ticket benötigt, kann die Fahrt aus
                    // der Liste der zu optimierenden Fahrten entfernt werden
                    userClassTrips.remove(index);
                }
            }
        }
        //Wenn noch freie Fahrten vorhanden sind -> diese zur Liste der Fahrscheine mit freien Fahrten hinzufügen
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

    /**
     * Prüft, ob mindestens eine Person noch kein Ticket hat <br/>
     *
     * @param tripItem zu prüfende Fahrt
     * @return true - mindestens eine Person ist ohne Ticket; false - alle Personen haben ein Ticket
     */
    private static boolean needsTicket(TripItem tripItem) {
        for (Fare.Type type : tripItem.getNumUserClasses().keySet()) {
            if (tripItem.getUserClassWithoutTicket(type) != 0) {
                return true;
            }
        }
        return false;
    }
}
