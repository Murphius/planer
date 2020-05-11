package com.example.lkjhgf.publicTransport.provider;

import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Fare;

public abstract class MyProvider implements Comparator<TripItem> {

    protected String[] preisstufen;
    private HashMap<Fare.Type, ArrayList<Ticket>> allTicketsMap;
    private NetworkProvider provider;
    private int maxNumTrip;

    void initialise(String[] preisstufen, HashMap<Fare.Type, ArrayList<Ticket>> allTicketsList, NetworkProvider provider){
        this.preisstufen = preisstufen;
        this.allTicketsMap = allTicketsList;
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

    private int calculateMaxNumTrip(){
        int maxNumTrip = 1;
        for(Iterator<Fare.Type> iterator = allTicketsMap.keySet().iterator(); iterator.hasNext();){
            Fare.Type key = iterator.next();
            ArrayList<Ticket> tickets = allTicketsMap.get(key);
            for(Ticket ticket : tickets){
                if(ticket instanceof NumTicket){
                    maxNumTrip = Math.max(maxNumTrip, ((NumTicket) ticket).getNumTrips());
                }
            }
        }
        return maxNumTrip;
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
}
