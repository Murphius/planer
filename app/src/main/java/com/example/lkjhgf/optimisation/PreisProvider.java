package com.example.lkjhgf.optimisation;

import java.util.ArrayList;

/**
 * Enthält alle Fahrscheine eines Provider <br/>
 * <p>
 * Ermittelt den Preis eines Fahrscheins, für eine Preisstufe <br/>
 * Umwandlung Preisstufe <-> Array Position <br/>
 * <p>
 * Die Liste aller Tickets wird für die Optimierung benötigt
 */
public abstract class PreisProvider {
    /**
     * enthält alle Fahrscheine eines Verkehrsverbundes
     */
    private final ArrayList<Ticket> allTickets;

    public PreisProvider(ArrayList<Ticket> allTickets) {
        this.allTickets = allTickets;
    }

    /**
     * Umwandlung Preisstufe <-> Array Position (für die Preisliste) <br/>
     *
     * @param ticket     für das die Preisauskunft gesucht wird
     * @param preisstufe gibt die Preisstufe des Fahrscheins an
     * @return gibt den Preis für das Ticket in der jeweiligen Preisstufe zurück,
     * falls die Preisstufe nicht existiert, wird MAX.Value zurückgegeben
     */
    public abstract int getPrice(Ticket ticket, String preisstufe);

    public ArrayList<Ticket> getAllTickets() {
        return allTickets;
    }

    /**
     * Liefert die maximale Anzahl an Fahrten welche mit einem Ticket zurückgelegt werden muss
     *
     * @return maximal Fahrtenanzahl die mit einem Ticket zurückgelegt werden kann
     */
    public int getMaxNumTrip() {
        int maxNumTrip = 1;
        for (Ticket ticket : allTickets) {
            if (ticket instanceof NumTicket) {
                maxNumTrip = Math.max(maxNumTrip, ((NumTicket) ticket).getNumTrips());
            }
        }
        return maxNumTrip;
    }

    public int getMaxNumPreisstufen(){
        int maxNumPreisstufen = 1;
        for(Ticket ticket : allTickets){
            maxNumPreisstufen = Math.max(maxNumPreisstufen, ticket.getPrices().length);
        }
        return maxNumPreisstufen;
    }
}
