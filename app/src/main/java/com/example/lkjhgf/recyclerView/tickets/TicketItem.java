package com.example.lkjhgf.recyclerView.tickets;

import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Die Informationen über ein mögliches Ticket <br/>
 *
 * @preconditions Der Nutzer hat (mehrere) Fahrten geplant die optimiert werden mussten, und für diese
 * wurden die optimalen Fahrscheine berechnet. <br/>
 * Information über die Antwort des Servers, keine Angaben vom Nutzer, die hier verwendet werden
 */
public class TicketItem implements Comparable<TicketItem> {

    /**
     * Gibt an, ob die zugehörigen Fahrten angezeigt werden sollen, oder nicht
     */
    private boolean showDetails;
    /**
     * Zu kaufendes Ticket
     */
    private TicketToBuy ticket;
    /**
     * Wie oft das Ticket gekauft werden soll
     */
    private int quantity;


    public TicketItem(TicketToBuy ticket, int quantity) {
        this.ticket = ticket;
        this.quantity = quantity;
        removeDuplicatedTrips();
    }

    //TODO weg ?
    /**
     * Entfernt Fahrten, die die gleiche ID haben, damit jede Fahrt nur einmal angezeigt wird
     */
    private void removeDuplicatedTrips() {
        Iterator<TripItem> it = ticket.getTripList().iterator();
        TripItem current = it.next();
        while (it.hasNext()) {
            TripItem next = it.next();
            if (current.equals(next)) {
                it.remove();
            } else {
                current = next;
            }
        }
    }

    public void setShowDetails() {
        showDetails = !showDetails;
    }

    boolean getShowDetails() {
        return showDetails;
    }

    public String getPreisstufe() {
        return ticket.getPreisstufe();
    }

    public Ticket getTicket() {
        return ticket.getTicket();
    }

    int getQuantity() {
        return quantity;
    }

    ArrayList<TripItem> getTripItems() {
        return ticket.getTripList();
    }

    @Override
    public int compareTo(TicketItem o) {
        return ticket.compareTo(o.ticket);
    }

}
