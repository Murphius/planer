package com.example.lkjhgf.publicTransport.provider;

import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

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

    /**
     * Initialisiert alle Attribute der Klasse
     * @param preisstufen Preisstufen des Providers
     * @param allTicketsHashMap Alle Tickets je Preisstufe
     * @param provider NetworkProvider für den Verkehrsverbund
     */
    void initialise(String[] preisstufen, HashMap<Fare.Type, ArrayList<Ticket>> allTicketsHashMap, NetworkProvider provider){
        this.preisstufen = preisstufen;
        this.allTicketsMap = allTicketsHashMap;
        this.provider = provider;
        maxNumTrip = calculateMaxNumTrip();
    }

    /**
     * Überprüft, ob ein String eine Preisstufe dieses Verkehrsverbundes ist
     * @param preisstufe zu überprüfende Zeichenfolge
     * @return true, wenn die Zeichenfolge in dem Array enthalten ist <br/>
     * false, falls nicht
     */
    public boolean checkContains(String preisstufe){
       return getPreisstufenIndex(preisstufe) != Integer.MAX_VALUE;
    }

    /**
     * @preconditions die Preisstufe ist gültig, oder es gilt die Postcondition
     * @postconditions es wird abgefangen, dass die Preisstufe nicht gültig ist
     * @param preisstufe
     * @return
     */
    public int getPreisstufenIndex(String preisstufe){
        for (int i = 0; i < preisstufen.length; i++){
            if (preisstufe.contains(preisstufen[i])){
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Sucht die maximale Fahrtenanzahl die ein NumTicket erlaubt
     * @return maximale Fahrtenanzahl
     */
    private int calculateMaxNumTrip(){
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
     * @preconditions Index innerhalb der Größe
     * @param index
     * @return
     */
    public String getPreisstufe(int index){
        return preisstufen[index];
    }

    public NetworkProvider getNetworkProvider(){
        return provider;
    }

    public HashMap<Fare.Type, ArrayList<Ticket>> getAllTickets(){
        return allTicketsMap;
    }

    /**
     * Umwandlung der Liste aller Fahrten in die entsprechenden Klassen
     * @param allTrips Liste aller Fahrten
     * @return Liste mit Listen, die den Fahrten einer jeweiligen Nutzerklasse entsprechen
     */
    public abstract HashMap<Fare.Type,ArrayList<TripItem>> createUserClassTripLists(ArrayList<TripItem> allTrips);

    public int getMaxNumTrip(){
        return maxNumTrip;
    }

    public int getTicketPrice(Ticket ticket, String preisstufe){
        return ticket.getPrice(getPreisstufenIndex(preisstufe));
    }

    public int getPreisstufenSize(){
        return preisstufen.length;
    }
}
