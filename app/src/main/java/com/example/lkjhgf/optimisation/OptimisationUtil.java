package com.example.lkjhgf.optimisation;

import android.app.Activity;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import de.schildbach.pte.dto.Fare;

/**
 * Enthält eine Funktion, die den gesammten Optimierungsprozess "steuert" und zugehörige Hilfsfunktionen
 */
public final class OptimisationUtil {

    /**
     * Funktion, die die komplette Optimierung "steuert"
     *
     * @param tripItems Liste der zu optimierenden Fahrten
     * @param activity  Aufrufende Aktivität -> zum Laden & speichern der Tickets benötigt
     * @return die Liste der benötigten Fahrscheine für jede Nutzerklasse
     */
    public static HashMap<Fare.Type, ArrayList<TicketToBuy>> startOptimisation(ArrayList<TripItem> tripItems, Activity activity) {
        //Fahrten ohne Preisstufe sowie nicht zu optimierende Fahrten aussortieren
        ArrayList<TripItem> copy = removeTrips(tripItems);
        // Fahrten nach den Preisstufen sortieren
        Collections.sort(copy, MainMenu.myProvider);
        //Fahrten auf Nutzerklassen aufteilen
        HashMap<Fare.Type, ArrayList<TripItem>> userClassTripLists = MainMenu.myProvider.createUserClassTripLists(copy);
        //Fahrscheine der letzten Optimierung
        HashMap activeTickets = new HashMap(AllTickets.loadTickets(activity));
        //Liste mit allen benötigten Fahrscheinen
        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketLists = new HashMap<>();
        //Liste mit Fahrscheinen, auf denen noch mindestens eine Fahrt frei ist
        HashMap<Fare.Type, ArrayList<TicketToBuy>> freeTickets = new HashMap<>();
        //Entfernt zukünftige Tickets, bereits optimierte Fahrscheine
        cleanUpTicketsAndTrips(activeTickets, userClassTripLists, freeTickets, allTicketLists);

        //Optimierung
        optimisationWithOldTickets(freeTickets, userClassTripLists);
        HashMap<Fare.Type, TicketOptimisationHolder> lastBestTickets = optimisationWithNewTickets(userClassTripLists);

        buildTicketList(lastBestTickets, allTicketLists);

        return allTicketLists;
    }

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
    private static void cleanUpTicketsAndTrips(HashMap<Fare.Type, ArrayList<TicketToBuy>> activeTickets,
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
            for (Iterator<TicketToBuy> it2 = userClassActiveTickets.iterator(); it2.hasNext(); ) {
                TicketToBuy currentTicket = it2.next();
                //Prüfen, ob es sich um ein zukünftiges Ticket handelt
                if (currentTicket.isFutureTicket()) {
                    //Falls ja, müssen zu erst die Fahrten des Tickets die Fahrscheine freigegeben bekommen
                    removeTicketFromTrips(currentTicket, userClassTrips);
                    //Entfernen des zukünftigen Fahrscheins
                    it2.remove();
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
                if (index != -1) {
                    userClassTrips.get(index).removeTickets(currentTicket.getTicket().getType(), currentTicket.getTicketID());
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
        for (TicketToBuy.TripQuantity tripQuantity : currentTicket.getTripQuantities()) {
            int quantity = tripQuantity.getQuantity();
            for (int i = 0; i < quantity; i++) {
                userClassTrips.remove(tripQuantity.getTrip());
            }
        }
        //Wenn noch freie Fahrten vorhanden sind -> diese zur Liste der Fahrscheine mit freien Fahrten
        //hinzufügen
        if (currentTicket.getFreeTrips() > 0) {
            freeUserClassTickets.add(currentTicket);
        }
    }

    /**
     * Erstellt die Liste der benöitgten Fahrscheine
     *
     * @param lastBestTickets das letzte Ticket der Optimierung für alle Nutzerklassen
     * @param allTicketLists  Liste aller benötigten Fahrscheine
     */
    private static void buildTicketList(HashMap<Fare.Type, TicketOptimisationHolder> lastBestTickets,
                                        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketLists) {
        //TicketListe Bauen
        for (Fare.Type type : lastBestTickets.keySet()) {
            ArrayList<TicketToBuy> ticketList = createTicketList(lastBestTickets.get(type));
            if (allTicketLists.containsKey(type)) {
                allTicketLists.get(type).addAll(ticketList);
            } else {
                allTicketLists.put(type, ticketList);
            }
        }
    }

    /**
     * Weist neuen Fahrten bereits gekaufte Fahrscheine zu
     *
     * @param freeTickets        Fahrscheine mit mindestens einer freien Fahrt
     * @param userClassTripLists zu optimierende Fahrscheine
     */
    private static void optimisationWithOldTickets(HashMap<Fare.Type, ArrayList<TicketToBuy>> freeTickets, HashMap<Fare.Type, ArrayList<TripItem>> userClassTripLists) {
        //TODO dieser Teil funktioniert nur für NumTicket
        //Optimierung mit alten Fahrscheinen
        if (!freeTickets.isEmpty()) {
            for (Fare.Type type : freeTickets.keySet()) {
                if (!freeTickets.get(type).isEmpty()) {
                    Optimisation.optimisationWithOldTickets(freeTickets.get(type), userClassTripLists.get(type));
                }
            }
        }
    }

    /**
     * Ermittelt die günstigsten neuen Fahrscheine für die zu optimierenden Fahrten
     *
     * @param userClassTripLists Zu optimierende Fahrten
     * @return für jede Nutzerklasse das beste letzte Ticket
     */
    private static HashMap<Fare.Type, TicketOptimisationHolder> optimisationWithNewTickets(HashMap<Fare.Type, ArrayList<TripItem>> userClassTripLists) {
        //Optimieren mit neuen Fahrscheinen
        HashMap<Fare.Type, TicketOptimisationHolder> lastBestTickets = new HashMap<>();
        HashMap<Fare.Type, ArrayList<Ticket>> allTicketsMap = MainMenu.myProvider.getAllTickets();
        for (Fare.Type type : userClassTripLists.keySet()) {
            if (allTicketsMap.containsKey(type)) {
                lastBestTickets.put(type, Optimisation.optimisationBuyNewTickets(allTicketsMap.get(type), userClassTripLists.get(type)));
            } else {
                System.err.println("Fehler bei der Optimierung neuer Fahrscheine - unbekannter Tickettyp: " + type);
            }
        }
        return lastBestTickets;
    }

    /**
     * Erzeugt eine neue Liste mit allen Fahrten, die optimiert werden sollen <br/>
     * <p>
     * Enthalten sind nur Fahrten mit einer gültigen Preisstufe und deren
     * Fahrschein noch nicht "angefangen" ist
     *
     * @param tripItems Liste mit allen Fahrten
     * @return Liste mit allen Fahrten die optimiert werden können
     */
    static ArrayList<TripItem> removeTrips(ArrayList<TripItem> tripItems) {
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
     * Erzeugt die Liste der benötigten Fahrscheine anhand der Vorgänger des übergebenen Tickets
     * <p>
     * Mittels previous von lastBestTicket wird der Vorgänger des aktuellen Ticketets ermittelt.
     * Diese werden solange der Liste hinzugefügt, bis der Vorgänger kein Ticket mehr besitzt
     *
     * @param lastBestTicket bester letzter Fahrschein
     * @return Liste aller benötigten Fahrscheinen
     */
    static ArrayList<TicketToBuy> createTicketList(TicketOptimisationHolder lastBestTicket) {
        ArrayList<TicketToBuy> ticketList = new ArrayList<>();
        while (lastBestTicket.getTicket() != null) {
            ticketList.add(lastBestTicket.getTicketToBuy());
            lastBestTicket = lastBestTicket.getPrevious();
        }
        return ticketList;
    }
}
