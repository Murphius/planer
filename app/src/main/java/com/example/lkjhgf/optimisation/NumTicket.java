package com.example.lkjhgf.optimisation;

import de.schildbach.pte.dto.Fare;

/**
 * Fahrscheine, mit denen eine bestimme Anzahl an einzelnen Fahrten zurückgelegt werden kann
 */
public class NumTicket extends Ticket {
    /**
     * gibt die Anzahl einzelner Fahrten an
     */
    private int numTrips;

    /**
     * Erzeugt einen neuen Fahrschein mit einer festen Anzahl an Fahrten
     * @param numTrips Anzahl an Fahrten
     * @param prices Preisliste
     * @param name Fahrscheinname
     */
    public NumTicket(int numTrips, int[] prices, String name, Fare.Type type){
        super(prices, name, type);
        this.numTrips = numTrips;
    }

    /**
     * Gibt die Anzahl an einzelnen Fahrten zurück
     * @return Anzahl der einzelnen Fahrten, die mit dem Fahrschein absolviert werden können
     */
    public int getNumTrips(){
        return numTrips;
    }
}
