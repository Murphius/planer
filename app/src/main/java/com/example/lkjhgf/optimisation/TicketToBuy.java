package com.example.lkjhgf.optimisation;

import androidx.annotation.NonNull;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.UUID;

/**
 * Enthält alle Informationen zu einem Ticket <br/>
 * Darunter fällt das Ticket, die Preisstufe des Tickets, die Fahrten für dass das Ticket genutzt
 * werden soll und eine ID
 */
public class TicketToBuy implements Comparable<TicketToBuy> {
    /**
     * Ticket(typ), der gekauft werden soll
     */
    private Ticket ticket;
    /**
     * Preisstufe des Fahrscheins
     */
    private String preisstufe;
    /**
     * Fahrten, für die der Fahrschein genutzt werden soll
     */
    private ArrayList<TripQuantity> tripQuantities;
    /**
     * Wie viele Fahrten mit dem Ticket noch gemacht werden können
     */
    private int freeTrips;
    /**
     * ID
     */
    private UUID ticketID;

    /**
     * Enthält die Informationen zu einer Fahrt und wie oft dieser diesem Ticket zugeordnet ist
     */
    public class TripQuantity {
        private int quantity;
        private TripItem trip;

        private TripQuantity(TripItem tripItem) {
            trip = tripItem;
            quantity = 1;
        }

        /**
         * Wird die gleiche Fahrt mehrfach hinzugefügt, so wird ihre Anzahl hochgezählt.
         *
         * @preconditions #checkContains(TripItem) stimmte für diese Fahrt
         */
        private void add() {
            quantity++;
        }

        /**
         * Überprüft, ob die übergebene Fahrt mit dem Attribut übereinstimmt <br/>
         * <p>
         * Überprüfung mittels TripID
         *
         * @param tripItem Fahrt die auf Gleichheit überprüft werden soll
         * @return true - wenn die IDs übereinstimmen; <br/>
         * false - wenn die IDs nicht übereinstimmen
         */
        boolean checkContains(TripItem tripItem) {
            return trip.getTrip().getId().equals(tripItem.getTrip().getId());
        }

        public int getQuantity() {
            return quantity;
        }

        public TripItem getTrip() {
            return trip;
        }
    }

    public TicketToBuy(Ticket ticket, String preisstufe) {
        this.ticket = ticket;
        this.preisstufe = preisstufe;
        tripQuantities = new ArrayList<>();
        calculateFreeTrips();
        ticketID = UUID.randomUUID();
    }

    /**
     * Ermittelt wie viele Fahrten auf dem Ticket noch frei sind <br/>
     * <p>
     * Bei Zeittickets ist dieser Wert immer maximal;
     * bei Mengenfahrscheinen hingegen die Anzahl der möglichen Fahrten - die genutze Anzahl
     */
    private void calculateFreeTrips() {
        if (freeTrips != Integer.MAX_VALUE) {
            if (ticket instanceof NumTicket) {
                int usedTrips = 0;
                //TODO überprüfen
                for (TripQuantity tripQuantity : tripQuantities) {
                    usedTrips += tripQuantity.quantity;
                }
                freeTrips = ((NumTicket) ticket).getNumTrips() - usedTrips;
            } else {
                freeTrips = Integer.MAX_VALUE;
            }
        }
    }

    /**
     * Fügt eine Fahrt der Liste der zugehörigen Fahrten hinzu <br/>
     * <p>
     * Wenn die Fahrt schon in der Liste zugehöriger Fahrten entahlten ist, wird ihre Anzahl erhöht; <br/>
     * falls nicht, wird ein neues {@link TripQuantity} Objekt erzeugt und der Liste hinzugefügt. <br/>
     * <p>
     * Für Mengenfahrscheine wird die Anzahl freier Fahrten korrigiert
     *
     * @param tripItem Fahrt die hinzugefügt werden soll
     */
    void addTripItem(TripItem tripItem) {
        //Für Mengenfahrscheine -> eine Fahrt weniger möglich
        if (freeTrips != Integer.MAX_VALUE) {
            freeTrips--;
        }
        //Prüfen, ob die Fahrt bereits enthalten ist
        for (TripQuantity quantity : tripQuantities) {
            if (quantity.checkContains(tripItem)) {
                //falls ja, die Anzahl um eins erhöhen
                quantity.add();
                return;
            }
        }
        //Fahrt noch nicht enthalten -> neu hinzufügen
        tripQuantities.add(new TripQuantity(tripItem));
    }

    /**
     * Prüft, ob alle zugeordneten Fahrten in der Zukunft liegen oder nicht
     *
     * @return false - wenn mindestens eine Fahrt in der Vergangenheit liegt (VOR dem aktuellen Zeitpunkt) <br/>
     * true - wenn alle Fahrten nach dem aktuellen Zeitpunkt liegen <br/>
     * Wichtig ist die Abfahrtszeit, nicht die Ankunftszeit
     */
    public boolean isFutureTicket() {
        for (TripQuantity tripItem : tripQuantities) {
            if (tripItem.trip.getTrip().getFirstDepartureTime().before(Calendar.getInstance().getTime())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft ob alle Fahrten eines Fahrscheins mehr als 24h in der Vergangenheit liegen
     *
     * @return false - mindestens eine Fahrt liegt innerhalb der letzten 24h (Ankunftszeit) <br/>
     * ture - alle Fahrten sind mehr als 24h vorbei
     */
    public boolean isPastTicket() {
        for (TripQuantity tripItem : tripQuantities) {
            if (tripItem.trip.getTrip().getLastArrivalTime().getTime() + (24 * 60 * 60 * 1000) > Calendar.getInstance().getTime().getTime()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Entfernt eine Fahrt aus der Liste der zugeorneten Fahrten
     *
     * @param tripID ID der zu löschenden Fahrt
     * @return true - wenn dadurch alle Fahrten des Fahrscheins frei sind, nur für Mengenfahrscheine relevant <br/>
     * false - wenn noch mindestens eine Fahrt des Fahrscheins genutzt wird
     */
    public boolean removeTrip(String tripID) {
        // über alle zugeordneten Fahrten iterieren
        for (Iterator<TripQuantity> tripQuantityIterator = tripQuantities.iterator(); tripQuantityIterator.hasNext(); ) {
            TripQuantity currentTrip = tripQuantityIterator.next();
            //prüfen, ob die IDs übereinstimmen
            if (currentTrip.trip.getTrip().getId().equals(tripID)) {
                int quantity = currentTrip.quantity;
                //Entfernen der Fahrt
                tripQuantityIterator.remove();
                if (ticket instanceof NumTicket) {
                    //Fahrten freigeben
                    if (freeTrips != Integer.MAX_VALUE) {
                        freeTrips += quantity;
                    }
                    //Ticket besteht nur noch aus freien Fahrten
                    if (((NumTicket) ticket).getNumTrips() == freeTrips) {
                        return true;
                    }
                }
            }
        }
        //TODO überarbeiten zu return tripQuantities.isEmpty();
        //Ticket hat noch nicht genutzte Fahrten
        return false;
    }

    public ArrayList<TripItem> getTripList() {
        ArrayList<TripItem> tripItems = new ArrayList<>();
        for (TripQuantity tripQuantity : tripQuantities) {
            tripItems.add(tripQuantity.trip);
        }
        return tripItems;
    }

    public ArrayList<TripQuantity> getTripQuantities() {
        return tripQuantities;
    }

    public String getPreisstufe() {
        return preisstufe;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public int getFreeTrips() {
        return freeTrips;
    }

    public UUID getTicketID() {
        return ticketID;
    }

    /**
     * Equals nicht anhand der ID sondern anhand der Namen und Preisstufen
     * @param o zu vergleichendes Objekt
     * @return true - Preisstufe und Ticket stimmen überein; <br/>
     * false - Preisstufe oder Ticket stimmen nicht überein
     */
    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if (!(o instanceof TicketToBuy)) {
            return false;
        }
        TicketToBuy other = (TicketToBuy) o;
        //
        if (ticket.equals(other.ticket)) {
            return preisstufe.equals(((TicketToBuy) o).preisstufe);
        }
        return false;
    }

    @Override
    public int compareTo(TicketToBuy o) {
        if (ticket.equals(o.ticket)) {
            // bei gleichen Tickets wird anhand der Preisstufen unterschieden, welches Ticket kleiner/größer ist
            return MainMenu.myProvider.getPreisstufenIndex(preisstufe) - MainMenu.myProvider.getPreisstufenIndex(o.preisstufe);
        } else {
            if (ticket instanceof NumTicket && o.ticket instanceof NumTicket) {
                Integer thisNumTrip = ((NumTicket) ticket).getNumTrips();
                Integer otherNumTrip = ((NumTicket) o.ticket).getNumTrips();
                //Bei Fahrscheinen mit einer unterschiedlichen Anzahl an Fahrten, wird anhand der möglichen Fahrten unterschieden
                return thisNumTrip.compareTo(otherNumTrip);
            } else {
                //TODO Zeittickets
                return ticket.getName().compareTo(o.ticket.getName());
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return ticket.getName() + " " + getTicketID().toString();
    }
}
