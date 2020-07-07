package com.example.lkjhgf.publicTransport.provider;

import android.app.Activity;

import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Fare;

/**
 * Enthält alle Informationen zu einem Provider
 */
public abstract class MyProvider implements Comparator<TripItem> {
    /**
     * Preisstufen des Providers, aufsteigend sortierts
     */
    String[] preisstufen;
    /**
     * Für jede Nutzerklasse die Liste aller zugehörigen Tickets
     */
    private HashMap<Fare.Type, ArrayList<Ticket>> allTicketsMap;
    /**
     * zugehöriger Provider
     */
    private NetworkProvider provider;
    /**
     * Gibt an, wie viele Fahrten, das maximale NumTicket erlaubt
     */
    private int maxNumTrip;

    Set<Farezone> farezones;

    /**
     * Initialisiert alle Attribute der Klasse
     *
     * @param preisstufen       Preisstufen des Providers
     * @param allTicketsHashMap Alle Tickets je Preisstufe
     * @param provider          NetworkProvider für den Verkehrsverbund
     */
    void initialise(String[] preisstufen, HashMap<Fare.Type, ArrayList<Ticket>> allTicketsHashMap, NetworkProvider provider, Set<Farezone> farezones) {
        this.preisstufen = preisstufen;
        this.allTicketsMap = allTicketsHashMap;
        this.provider = provider;
        maxNumTrip = calculateMaxNumTrip();
        this.farezones = farezones;
    }

    /**
     * Sucht die maximale Fahrtenanzahl die ein NumTicket erlaubt
     *
     * @return maximale Fahrtenanzahl
     */
    private int calculateMaxNumTrip() {
        int maxNumTrip = 1;
        for (Fare.Type key : allTicketsMap.keySet()) {
            ArrayList<Ticket> tickets = allTicketsMap.get(key);
            for (Ticket ticket : tickets) {
                if (ticket instanceof NumTicket) {
                    maxNumTrip = Math.max(maxNumTrip, ((NumTicket) ticket).getNumTrips());
                }
            }
        }
        return maxNumTrip;
    }

    /**
     * Für jeden Provider muss die Optimierung manuell umgesetzt werden
     *
     * @param tripItems Liste an Fahrten die optimiert werden soll
     * @param activity  Aufrufende Aktivität -> zum Laden & speichern der Tickets benötigt
     * @return Liste an benötigten Fahrscheinen
     */
    public abstract HashMap<Fare.Type, ArrayList<TicketToBuy>> optimise(ArrayList<TripItem> tripItems, HashMap<Fare.Type, ArrayList<TicketToBuy>> savedTickets, Activity activity);

    /**
     * Fasst Tickets für mehrere Personen wenn möglich zusammen <br/>
     * <p>
     * Falls es Fahrscheine gibt, die es für mehrere Personen gibt, und diese günstiger sind, als
     * entsprechend oft den gleichen Fahrschein zu kaufen.
     *
     * @param ticketItems alle Fahrscheine, die zusammengefasst werden sollen
     */
    public abstract void sumUpTickets(ArrayList<TicketToBuy> ticketItems);

    /**
     * Teilt die Fahrten auf die Nutzerklassen auf
     *
     * @param allTrips alle Fahrten
     * @return HashMap mit einer Liste an Fahrten für jede Nutzerklasse
     */
    public HashMap<Fare.Type, ArrayList<TripItem>> createUserClassTripList(ArrayList<TripItem> allTrips) {
        HashMap<Fare.Type, ArrayList<TripItem>> userClassTrips = new HashMap<>();
        for (TripItem tripItem : allTrips) {
            for (Fare.Type type : tripItem.getNumUserClasses().keySet()) {
                ArrayList<TripItem> userClass = userClassTrips.get(type);
                if (userClass == null) {
                    userClass = new ArrayList<>();
                    userClassTrips.put(type, userClass);
                }
                if (tripItem.getNumUserClass(type) != 0) {
                    userClass.add(tripItem);
                }
            }
        }
        return userClassTrips;
    }

    /**
     * Jede Nutzerklasse erhält eine HashMap, wo jede Person eine eigene Fahrtenliste erhält <br/>
     * <p>
     * <p>
     * Vorbereitung für die Optimierung. <br/>
     * Wenn eine Fahrt nur eine Person hat, so wird die Fahrt nur dem Schlüssel 1
     * zugeordnet. Bei mehr als einer Person hingegen, wird die Fahrt auch den weiteren Schlüsseln zugeordnet. <br/>
     * <p>
     * Eine Fahrt mit 2 Erwachsenen und einem Kind wird also wie folgt eingefügt: <br/>
     * Kinder: 1 - F <br/>
     * Erwachsene: 1 - F <br/>
     * 2 - F
     *
     * @param allTrips Liste aller Fahrten
     * @return HashMap mit den Fahrten aufgeteilt auf die Nutzerklassen
     */
    public HashMap<Fare.Type, HashMap<Integer, ArrayList<TripItem>>> createUserClassHashMap(HashMap<Fare.Type, ArrayList<TripItem>> allTrips) {
        HashMap<Fare.Type, HashMap<Integer, ArrayList<TripItem>>> userClassTrips = new HashMap<>();
        for (Fare.Type type : allTrips.keySet()) {
            //Hash Map mit der Personenanzahl und der entsprechenden Anzahl Personen
            HashMap<Integer, ArrayList<TripItem>> iHashMap = userClassTrips.get(type);
            if (iHashMap == null) {
                iHashMap = new HashMap<>();
                userClassTrips.put(type, iHashMap);
            }
            //Fahrten der Nutzerklasse
            ArrayList<TripItem> tripItems = allTrips.get(type);
            for (TripItem tripItem : tripItems) {
                //Für die entsprechende Personennr. die Fahrt zur Liste hinzufügen
                for (int i = 1; i <= tripItem.getUserClassWithoutTicket(type); i++) {
                    ArrayList<TripItem> c = iHashMap.get(i);
                    if (c == null) {
                        c = new ArrayList<>();
                        iHashMap.put(i, c);
                    }
                    c.add(tripItem);
                }
            }
        }
        return userClassTrips;
    }

    /**
     * Überprüft, ob ein String eine Preisstufe dieses Verkehrsverbundes ist
     *
     * @param preisstufe zu überprüfende Zeichenfolge
     * @return true, wenn die Zeichenfolge in dem Array enthalten ist <br/>
     * false, falls nicht
     */
    public boolean checkContains(String preisstufe) {
        return getPreisstufenIndex(preisstufe) != Integer.MAX_VALUE;
    }

    public abstract String getTicketInformationNumTicket(ArrayList<Ticket> tickets, ArrayList<Integer> quantity, ArrayList<String> preisstufe, TripItem tripItem);

    public abstract String getTicketInformationTimeTicket(ArrayList<TicketToBuy> timeTicketsToBuy);

    public int getTicketPrice(Ticket ticket, String preisstufe) {
        return ticket.getPrice(getPreisstufenIndex(preisstufe));
    }

    public int getPreisstufenSize() {
        return preisstufen.length;
    }

    public Set<Farezone> getFarezones() {
        return farezones;
    }

    public int getMaxNumTrip() {
        return maxNumTrip;
    }

    /**
     * @param index Stelle der Preisstufe
     * @return Preisstufe für den entsprechenden Index als String
     * @preconditions Index innerhalb der Größe
     */
    public String getPreisstufe(int index) {
        return preisstufen[index];
    }

    public NetworkProvider getNetworkProvider() {
        return provider;
    }

    public HashMap<Fare.Type, ArrayList<Ticket>> getAllTickets() {
        return allTicketsMap;
    }

    public HashMap<Fare.Type, ArrayList<NumTicket>> getNumTickets() {
        HashMap<Fare.Type, ArrayList<NumTicket>> numTicketMap = new HashMap<>();
        for (Fare.Type type : allTicketsMap.keySet()) {
            ArrayList<NumTicket> numTicketType = new ArrayList<>();
            numTicketMap.put(type, numTicketType);
            if (allTicketsMap.get(type) == null) {
                continue;
            }
            for (Ticket ticket : allTicketsMap.get(type)) {
                if (ticket instanceof NumTicket) {
                    numTicketType.add((NumTicket) ticket);
                }
            }
        }
        return numTicketMap;
    }

    public HashMap<Fare.Type, ArrayList<TimeTicket>> getTimeTickets() {
        HashMap<Fare.Type, ArrayList<TimeTicket>> timeTicketMap = new HashMap<>();
        for (Fare.Type type : allTicketsMap.keySet()) {
            ArrayList<TimeTicket> resultType = new ArrayList<>();
            timeTicketMap.put(type, resultType);
            if (allTicketsMap.get(type) == null) {
                continue;
            }
            ArrayList<Ticket> tickets = allTicketsMap.get(type);
            for (Ticket ticket : tickets) {
                if (ticket instanceof TimeTicket) {
                    resultType.add((TimeTicket) ticket);
                }
            }
        }
        return timeTicketMap;
    }

    /**
     * @return Index der jeweiligen Preisstufe, Integer Max falls die Preisstufe ungültig ist
     * @preconditions die Preisstufe ist gültig, oder es gilt die Postcondition
     * @postconditions es wird abgefangen, dass die Preisstufe nicht gültig ist
     */
    public int getPreisstufenIndex(String preisstufe) {
        for (int i = 0; i < preisstufen.length; i++) {
            if (preisstufe.contains(preisstufen[i])) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }
}
