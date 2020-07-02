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
     * Überprüft, ob ein String eine Preisstufe dieses Verkehrsverbundes ist
     *
     * @param preisstufe zu überprüfende Zeichenfolge
     * @return true, wenn die Zeichenfolge in dem Array enthalten ist <br/>
     * false, falls nicht
     */
    public boolean checkContains(String preisstufe) {
        return getPreisstufenIndex(preisstufe) != Integer.MAX_VALUE;
    }

    /**
     * Erstellt für jede Anzahl an Personen einer Personenklasse, die zugeordnete Fahrtenliste <br/>
     * <p>
     * Da die Fahrscheine nicht Personengebunden sind (außer das Monatsticket), wird jeweils nur die Anzahl der
     * Personen betrachtet. Wenn eine Fahrt nur eine Person hat, so wird die Fahrt nur dem Schlüssel 1
     * zugeordnet. Bei mehr als einer Person hingegen, wird die Fahrt auch den weiteren Schlüsseln zugeordnet
     *
     * @param tripItems Alle Fahrten, die der Nutzer geplant hat
     * @param type      Nutzerklasse die betrachtet werden soll
     * @return Für jede Personnenanzahl die zu berücksichtigen Fahrscheine
     */
    public HashMap<Integer, ArrayList<TripItem>> createTripListForEachUser(ArrayList<TripItem> tripItems, Fare.Type type) {
        HashMap<Integer, ArrayList<TripItem>> tripListForEachUser = new HashMap<>();
        for (TripItem trip : tripItems) {
            for (int numP = 1; numP <= trip.getNumUserClass(type); numP++) {
                ArrayList<TripItem> currentList = tripListForEachUser.get(numP);
                if (currentList == null) {
                    currentList = new ArrayList<>();
                    tripListForEachUser.put(numP, currentList);
                }
                currentList.add(trip);
            }
        }
        return tripListForEachUser;
    }

    /**
     * Für jeden Provider muss die Optimierung manuell umgesetzt werden
     *
     * @param tripItems Liste an Fahrten die optimiert werden soll
     * @param activity Aufrufende Aktivität -> zum Laden & speichern der Tickets benötigt
     * @return Liste an benötigten Fahrscheinen
     */
    public abstract HashMap<Fare.Type, ArrayList<TicketToBuy>> optimise(ArrayList<TripItem> tripItems, HashMap<Fare.Type, ArrayList<TicketToBuy>> savedTickets, Activity activity);

    public abstract HashMap<Fare.Type, ArrayList<TripItem>> createUserClassTripList(ArrayList<TripItem> allTrips);
    /**
     * @param preisstufe
     * @return
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
     * @param index
     * @return
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

    public Set<Farezone> getFarezones(){
        return farezones;
    }

    /**
     * Umwandlung der Liste aller Fahrten in die entsprechenden Klassen
     *
     * @param allTrips Liste aller Fahrten
     * @return Liste mit Listen, die den Fahrten einer jeweiligen Nutzerklasse entsprechen
     */
    public abstract HashMap<Fare.Type, HashMap<Integer,ArrayList<TripItem>>> createUserClassHashMap(HashMap<Fare.Type,ArrayList<TripItem>> allTrips);

    public abstract String getTicketInformationNumTicket(ArrayList<Ticket> tickets, ArrayList<Integer> quantity, ArrayList<String> preisstufe, TripItem tripItem);

    public abstract String getTicketInformationTimeTicket(ArrayList<TicketToBuy> timeTicketsToBuy);

    public int getMaxNumTrip() {
        return maxNumTrip;
    }

    public int getTicketPrice(Ticket ticket, String preisstufe) {
        return ticket.getPrice(getPreisstufenIndex(preisstufe));
    }

    public int getPreisstufenSize() {
        return preisstufen.length;
    }

}
