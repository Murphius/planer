package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import de.schildbach.pte.dto.Trip;

/**
 * Enthält alle Informationen zu einem benötigten Fahrschein <br/>
 * <p>
 * Den Fahrschein, die Preisstufe, die zugeordneten Fahrten
 */
public class TicketInformationHolder implements Serializable {
    /**
     * Benötigtes Ticket
     */
    private Ticket ticket;
    /**
     * Benötigter Fahrschein
     */
    private String preisstufe;
    /**
     * Fahrten, bei denen dieses Ticket genutzt werden soll
     */

    private ArrayList<TripItem> tripList;
    /**
     * Kosten, bis zu dem jeweiligen Ticket, benötigt für die Optimierung
     */
    private int costs;
    /**
     * Vorhergehendes Ticket, benötigt für die Optimierung
     */
    private TicketInformationHolder previous;

    /**
     * Enthält alle benötigten Informationen zum Fahrschein <br/>
     * <p>
     * Die Zuordnung der Fahrten zum Fahrschein erfolgt erst nach der Optimierung; deshalb wird
     * die Liste hier nur initialisiert, erhält jedoch noch keine Fahrten
     *
     * @param ticket     benötigter Fahrschein
     * @param preisstufe Preisstufe des Fahrscheins
     * @param costs      gesamt Kosten bis zu der jeweilgen Fahrt
     * @param previous   vorheriges Ticket
     */
    public TicketInformationHolder (Ticket ticket, String preisstufe, int costs, TicketInformationHolder previous) {
        this.ticket = ticket;
        this.preisstufe = preisstufe;
        this.costs = costs;
        this.previous = previous;
        tripList = new ArrayList<>();
    }

    /**
     * Fügt eine Fahrt zu dem Fahrschein hinzu
     *
     * @param tripItem Fahrt die hinzugefügt werden soll
     */
    public void addTripItem(TripItem tripItem) {
        tripList.add(tripItem);
    }

    public ArrayList<TripItem> getTripList() {
        return tripList;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public boolean isFutureTicket(){
        for(TripItem tripItem : tripList){
            Trip trip = tripItem.getTrip();
            if(trip.getFirstDepartureTime().before(Calendar.getInstance().getTime())){
                return false;
            }
        }
        return true;
    }

    public int getCosts(){
        return costs;
    }

    public TicketInformationHolder getPrevious() {
        return previous;
    }

    public String getPreisstufe(){
        return preisstufe;
    }

    @Override
    public String toString() {
        return ticket.toString() + ", Preisstufe: " + preisstufe;
    }

    public String showTicketInformation() {
        String value = toString() + "\n";
        for (TripItem tripItem : tripList) {
            value += "\t" + tripItem.toString() + "\n";
        }
        return value;
    }

    /**
     * Überprüft, ob alle Fahrten in der Vergangenheit liegen
     * @return true, wenn alle Fahrten vor dem aktuellen Zeitpunkt liegen
     * @return false, wenn mindestens eine Fahrt nach dem aktuellen Zeitpunkt liegt
     */
    public boolean isPastTicket(){
        for(TripItem tripItem : tripList){
            //86 400 000 sind 24h
            //Tickets, deren letzte Fahrt mehr als 24h her ist, werden somit nicht mehr angezeigt
            if(tripItem.getTrip().getLastArrivalTime().getTime() + (24*60*60*1000) > Calendar.getInstance().getTime().getTime()){
                return false;
            }
        }
        return true;
    }
}
