package com.example.lkjhgf.optimisation;

import androidx.annotation.NonNull;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.publicTransport.provider.Farezone;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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

    private Set<Farezone> validFarezones;
    private int mainRegionID;
    private boolean isZweiWabenTarif;

    /**
     * Enthält die Informationen zu einer Fahrt und wie oft dieser diesem Ticket zugeordnet ist
     */
    public class TripQuantity {
        private int quantity;
        private TripItem tripItem;

        private TripQuantity(TripItem tripItem) {
            this.tripItem = tripItem;
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
            return this.tripItem.getTripID().equals(tripItem.getTripID());
        }

        public int getQuantity() {
            return quantity;
        }

        public TripItem getTripItem() {
            return tripItem;
        }

        public void updateTrip(TripItem trip){
            this.tripItem = trip;
        }
    }

    public TicketToBuy(Ticket ticket, String preisstufe) {
        this.ticket = ticket;
        this.preisstufe = preisstufe;
        tripQuantities = new ArrayList<>();
        calculateFreeTrips();
        ticketID = UUID.randomUUID();
        validFarezones = new HashSet<>();
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
    public void addTripItem(TripItem tripItem) {
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
     * Fügt mehrere Fahrten dem Ticket hinzu <br/>
     * <p>
     * Ruft für jedes Item das hinzugefügt werden soll {@link #addTripItem(TripItem)} auf
     *
     * @param tripItems Liste der Fahrten die hinzugefügt werden soll
     */
    public void addTripItems(ArrayList<TripItem> tripItems) {
        for (TripItem t : tripItems) {
            addTripItem(t);
        }
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
            if (tripItem.tripItem.getFirstDepartureTime().before(Calendar.getInstance().getTime())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft ob alle Fahrten eines Fahrscheins mehr als der Offset in der Vergangenheit liegen
     * <p>
     * Der Offset soll 0 sein, beim laden für die Optimierung und 24h für das Anzeigen von Fahrscheinen
     *
     * @param offset gibt an, wie lange die Fahrten in der Vergangenheit liegen sollen
     * @return false - das Ticket ist gültig, oder hat noch mindestens eine freie Fahrt
     * true - das Ticket ist nicht mehr gültig oder alle Fahrten liegen in der Vergangenheit
     */
    public boolean isPastTicket(long offset) {
        if (ticket instanceof TimeTicket) {
            //TODO prüfen
            return getFirstDepartureTime().getTime() + ((TimeTicket) ticket).getMaxDuration() + offset < Calendar.getInstance().getTime().getTime();
        } else {
            if (freeTrips != 0) {
                return false;
            } else {
                for (TripQuantity tripItem : tripQuantities) {
                    if (tripItem.tripItem.getLastArrivalTime().getTime() + offset > Calendar.getInstance().getTime().getTime()) {
                        return false;
                    }
                }
                return true;
            }
        }
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
            if (currentTrip.tripItem.getTrip().getId().equals(tripID)) {
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
                    } else {
                        return tripQuantities.size() == 0;
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
            tripItems.add(tripQuantity.tripItem);
        }
        return tripItems;
    }

    public Date getFirstDepartureTime() {
        return tripQuantities.get(0).tripItem.getFirstDepartureTime();
    }

    public Date getLastArrivalTime() {
        return tripQuantities.get(tripQuantities.size() - 1).tripItem.getLastArrivalTime();
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

    public void setValidFarezones(Set<Farezone> validFarezones, int mainRegion) {
        this.validFarezones = validFarezones;
        this.mainRegionID = mainRegion;
    }

    public void setValidFarezones(Set<Farezone> validFarezones, int mainRegionID, boolean isZweiWabenTarif){
        setValidFarezones(validFarezones, mainRegionID);
        this.isZweiWabenTarif = isZweiWabenTarif;
    }

    public boolean isZweiWabenTarif() {
        return isZweiWabenTarif;
    }

    public Set<Farezone> getValidFarezones() {
        return validFarezones;
    }

    public boolean checkFarezone(TripItem tripItem) {
        for (Integer crossedFarezone : tripItem.getCrossedFarezones()) {
            boolean contains = false;
            for (Farezone f : validFarezones) {
                if(isZweiWabenTarif){
                    if(f.getId() == crossedFarezone){
                        contains = true;
                        break;
                    }
                }else{
                    if (f.getId() == crossedFarezone / 10) {
                        contains = true;
                        break;
                    }
                }
            }
            if (!contains) {
                return false;
            }
        }
        return true;
    }

    public boolean checkFarezones(ArrayList<TripItem> tripItems){
        for(TripItem tripItem :tripItems){
            if(! checkFarezone(tripItem)){
                return false;
            }
        }
        return true;
    }

    public int getMainRegionID(){
        return mainRegionID;
    }

    /**
     * Equals nicht anhand der ID sondern anhand der Namen und Preisstufen
     *
     * @param o zu vergleichendes Objekt
     * @return true - Preisstufe und Ticket stimmen überein; <br/>
     * false - Preisstufe oder Ticket stimmen nicht überein
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
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
