package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

/**
 * Enthält die Informationen zu einer Fahrt und wie oft dieser diesem Ticket zugeordnet ist
 */
public class TripQuantity {
    private int quantity;
    private TripItem tripItem;

    TripQuantity(TripItem tripItem) {
        this.tripItem = tripItem;
        quantity = 1;
    }

    /**
     * Wird die gleiche Fahrt mehrfach hinzugefügt, so wird ihre Anzahl hochgezählt.
     *
     * @preconditions #checkContains(TripItem) stimmte für diese Fahrt
     */
    void add() {
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

    public void updateTrip(TripItem trip) {
        this.tripItem = trip;
    }
}