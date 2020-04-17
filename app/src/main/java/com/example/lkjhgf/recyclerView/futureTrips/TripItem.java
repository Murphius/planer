package com.example.lkjhgf.recyclerView.futureTrips;

import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.recyclerView.possibleConnections.components.JourneyItem;

import java.util.ArrayList;

import de.schildbach.pte.dto.Trip;

/**
 * Enthält die Informationen für eine Reise
 */
public class TripItem {
    private Trip trip;
    private boolean isComplete;
    private int numAdult, numChildren;
    /**
     * Kurze Zusammenfassung der Abfolge von Verkehrsmitteln
     */
    private ArrayList<JourneyItem> journeyItems;

    /**
     * Konstruktor für Fahrten, deren Fahrkosten nicht optimiert werden sollen<br/>
     *
     * @param trip       - enthält alle Informationen zur Reise, die unabhängig von der Optimierung sind
     * @param isComplete - gibt an, ob die Fahrt bei der Optimierung berücksichtigt werden soll <br/>
     *                   false - keine Berücksichtigung
     */
    public TripItem(Trip trip, boolean isComplete) {
        this.trip = trip;
        this.isComplete = isComplete;
        numAdult = 0;
        numChildren = 0;
        journeyItems = Utils.journeyItems(trip.legs);
    }

    /**
     * Konstruktor für Fahrten, deren Fahrkosten optimiert wrden sollen <br/>
     *
     * @param trip        - enthält alle Informationen zur Reise, die unabhängig von der Optimierung sind
     * @param isComplete  - gibt an, ob die Fahrt bei der Optimierung berücksichtigt werdne soll <br/>
     *                    true - soll berücksichtigt werden
     * @param numAdult    - gibt die Anzahl reisender Kinder an
     * @param numChildren - gibt die Anzahl reisender Erwachsener an
     */
    public TripItem(Trip trip, boolean isComplete, int numAdult, int numChildren) {
        this(trip, isComplete);
        this.numAdult = numAdult;
        this.numChildren = numChildren;
    }

    public Trip getTrip() {
        return trip;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public int getNumAdult() {
        return numAdult;
    }

    public int getNumChildren() {
        return numChildren;
    }

    ArrayList<JourneyItem> getJourneyItems() {
        return journeyItems;
    }

    /**
     * Zwei Fahrten sind identisch, wenn sie die gleiche ID besitzen <br/>
     * Die Erzeugung von IDs erfolgt in {@link Trip#buildSubstituteId()} <br/>
     *
     * @param o zu Vergleichendes Objekt
     * @return boolean - Gibt an, ob zwei Fahrten identisch sind
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TripItem)) {
            return false;
        }
        TripItem otherTripItem = (TripItem) o;
        return otherTripItem.trip.getId().equals(this.trip.getId());
    }
}
