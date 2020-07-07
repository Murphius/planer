package com.example.lkjhgf.recyclerView.futureTrips;

import com.example.lkjhgf.optimisation.Ticket;

import java.io.Serializable;
import java.util.UUID;

/**
 * Enthält alle Informationen zu einem Ticket, welches der Fahrt zugeordnet ist
 */
public class TripTicketInformationHolder implements Serializable {
    Ticket ticket;
    String ticketFarezone;
    UUID ticketIdentifier;
    /**
     * Wie oft das Ticket der Fahrt zugeordnet ist <br/>
     * <p>
     * zB eine Fahrt mit 4 Personen kann ein 4er Ticket 4x zugeorndet bekommen
     */
    int quantity;

    TripTicketInformationHolder(Ticket ticket, String ticketFarezone, UUID ticketIdentifier) {
        this.ticket = ticket;
        this.ticketFarezone = ticketFarezone;
        this.ticketIdentifier = ticketIdentifier;
        quantity = 1;
    }

    /**
     * Überprüft, ob ein Ticket der Fahrt schon zugeordnet ist
     *
     * @param otherIdentifier UUID des zu prüfenden Tickets
     * @return true - die UUID stimmt mit dieser überein, sonst false
     */
    boolean checkContains(UUID otherIdentifier) {
        return ticketIdentifier.equals(otherIdentifier);
    }

    /**
     * Wenn das Ticket ein weiteres mal der Fahrt zugeordnet wird, wird die Häufigkeit erhöht
     *
     * @preconditions Das Ticket ist bereits der Fahrt zugeordnet ({@link #checkContains(UUID ticketID)} = true)
     */
    void add() {
        quantity++;
    }

    public int getQuantity() {
        return quantity;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getTicketFarezone() {
        return ticketFarezone;
    }

    public UUID getTicketIdentifier() {
        return ticketIdentifier;
    }
}
