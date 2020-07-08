package com.example.lkjhgf.optimisation.numTicketOptimisation;

import androidx.annotation.NonNull;

import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.io.Serializable;

/**
 * Enthält alle Informationen zu einem benötigten Fahrschein <br/>
 * <p>
 * Den Fahrschein, die Preisstufe, die zugeordneten Fahrten
 */
public class NumTicketOptimisationHolder implements Serializable {
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
    private NumTicketOptimisationHolder previous;

    /**
     * Enthält alle benötigten Informationen zum Fahrschein <br/>
     * <p>
     *
     * @param ticket     benötigter Fahrschein
     * @param preisstufe Preisstufe des Fahrscheins
     * @param allCosts   gesamt Kosten bis zu der jeweilgen Fahrt
     * @param previous   Vorgänger Ticket
     */
    NumTicketOptimisationHolder(NumTicket ticket,
                                String preisstufe,
                                int allCosts,
                                NumTicketOptimisationHolder previous) {
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

    NumTicketOptimisationHolder getPrevious() {
        return previous;
    }

    public NumTicket getTicket() {
        return (NumTicket) ticketToBuy.getTicket();
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
