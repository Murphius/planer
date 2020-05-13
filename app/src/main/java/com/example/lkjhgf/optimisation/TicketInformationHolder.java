package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Enthält alle Informationen zu einem benötigten Fahrschein <br/>
 * <p>
 * Den Fahrschein, die Preisstufe, die zugeordneten Fahrten
 */
public class TicketInformationHolder implements Serializable {
    /**
     * Benötigtes Ticket
     */
    private TicketToBuy ticketToBuy;

    /**
     * Kosten, bis zu dem jeweiligen Ticket, benötigt für die Optimierung
     */
    private int allCosts;
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
     * @param allCosts      gesamt Kosten bis zu der jeweilgen Fahrt
     * @param previous   vorheriges Ticket
     */
    public TicketInformationHolder (Ticket ticket, String preisstufe, int allCosts, TicketInformationHolder previous) {
        this.ticketToBuy = new TicketToBuy(ticket,preisstufe);
        this.allCosts = allCosts;
        this.previous = previous;
    }

    /**
     * Fügt eine Fahrt zu dem Fahrschein hinzu
     *
     * @param tripItem Fahrt die hinzugefügt werden soll
     */
    public void addTripItem(TripItem tripItem) {
        ticketToBuy.addTripItem(tripItem);
    }

    public Ticket getTicket() {
        return ticketToBuy.getTicket();
    }
    public TicketToBuy getTicketToBuy(){
        return ticketToBuy;
    }


    public int getAllCosts(){
        return allCosts;
    }

    public TicketInformationHolder getPrevious() {
        return previous;
    }

    public String getPreisstufe(){
        return ticketToBuy.getPreisstufe();
    }

    @Override
    public String toString() {
        return ticketToBuy.toString() + ", Preisstufe: " + ticketToBuy.getPreisstufe();
    }

}
