package com.example.lkjhgf.optimisation;

import androidx.annotation.NonNull;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.io.Serializable;

/**
 * Enthält alle Informationen zu einem benötigten Fahrschein <br/>
 * <p>
 * Den Fahrschein, die Preisstufe, die zugeordneten Fahrten
 */
public class TicketOptimisationHolder implements Serializable {
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
    private TicketOptimisationHolder previous;

    /**
     * Enthält alle benötigten Informationen zum Fahrschein <br/>
     * <p>
     * Die Zuordnung der Fahrten zum Fahrschein erfolgt erst nach der Optimierung; deshalb wird
     * die Liste hier nur initialisiert, erhält jedoch noch keine Fahrten
     *
     * @param ticket     benötigter Fahrschein
     * @param preisstufe Preisstufe des Fahrscheins
     * @param allCosts   gesamt Kosten bis zu der jeweilgen Fahrt
     * @param previous   vorheriges Ticket
     */
    TicketOptimisationHolder(Ticket ticket,
                             String preisstufe,
                             int allCosts,
                             TicketOptimisationHolder previous) {
        this.ticketToBuy = new TicketToBuy(ticket, preisstufe);
        this.allCosts = allCosts;
        this.previous = previous;
    }

    /**
     * Fügt eine Fahrt zu dem Fahrschein hinzu
     *
     * @param tripItem Fahrt die hinzugefügt werden soll
     */
    void addTripItem(TripItem tripItem) {
        ticketToBuy.addTripItem(tripItem);
    }

    TicketOptimisationHolder getPrevious() {
        return previous;
    }

    public Ticket getTicket() {
        return ticketToBuy.getTicket();
    }

    TicketToBuy getTicketToBuy() {
        return ticketToBuy;
    }

    int getAllCosts() {
        return allCosts;
    }

    public String getPreisstufe() {
        return ticketToBuy.getPreisstufe();
    }

    @NonNull
    @Override
    public String toString() {
        return ticketToBuy.toString() + ", Preisstufe: " + ticketToBuy.getPreisstufe();
    }

}
