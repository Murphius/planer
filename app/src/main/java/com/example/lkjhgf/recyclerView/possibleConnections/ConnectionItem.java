package com.example.lkjhgf.recyclerView.possibleConnections;

import com.example.lkjhgf.helper.futureTrip.MyTripList;
import com.example.lkjhgf.helper.util.Utils;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.recyclerView.possibleConnections.components.JourneyItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import de.schildbach.pte.dto.Trip;

/**
 * Die Informationen über eine mögliche Verbindung <br/>
 *
 * @preconditions Der Nutzer hat das Formular {@link com.example.lkjhgf.helper.form.Form} ausgefüllt,
 * für die Anfrage wurden mögliche Verbindungen gefunden. <br/>
 * <p>
 * Information über die Antwort des Servers, keine Angaben vom Nutzer, die hier verwendet werden
 */
public class ConnectionItem implements Serializable {
    private Trip trip;
    private Date firstDepartureTime, lastArrivalTime;
    private long delayDeparture, delayArrival;
    private ArrayList<JourneyItem> journeyItems;
    private long duration_hours, duration_minutes;

    public ConnectionItem(Trip trip, ArrayList<JourneyItem> journeyItems) {
        this.trip = trip;
        this.journeyItems = journeyItems;

        Trip.Leg firstLeg = trip.legs.get(0);
        Trip.Leg lastLeg = trip.legs.get(trip.legs.size()-1);
        // Abfahrtszeit
        if (firstLeg instanceof Trip.Individual) {
            firstDepartureTime = trip.getFirstDepartureTime();
            delayDeparture = 0;
        } else{
                Trip.Public publicTrip = (Trip.Public) firstLeg;
                if(publicTrip.departureStop != null){
                    firstDepartureTime = publicTrip.getDepartureTime(true);
                    delayDeparture = publicTrip.getDepartureDelay();
                }else {
                    firstDepartureTime = null;
                    delayDeparture = 0;
                }
        }
        //Ankunftszeit
        if (lastLeg instanceof Trip.Individual) {
            lastArrivalTime = trip.getLastArrivalTime();
            delayArrival = 0;
        } else{
            Trip.Public publicTrip = (Trip.Public) lastLeg;
            if(publicTrip.arrivalStop != null){
                lastArrivalTime = publicTrip.getArrivalTime(true);
                delayArrival = publicTrip.getArrivalDelay();
            }else{
                firstDepartureTime = null;
                delayArrival = 0;
            }
        }

        // Dauer der Fahrt
        duration_hours = Utils.durationToHour(trip.getDuration());
        duration_minutes = Utils.durationToMinutes(trip.getDuration());
    }

    public Trip getTrip() {
        return trip;
    }

    public Date getFirstDepartureTime() {
        return firstDepartureTime;
    }

    public Date getLastArrivalTime() {
        return lastArrivalTime;
    }

    public String getTimeOfArrival() {
        if (lastArrivalTime != null) {
            return UtilsString.setTime(lastArrivalTime);
        } else {
            return " ? ";
        }
    }

    public String getTimeOfDeparture() {
        if (firstDepartureTime != null) {
            return UtilsString.setTime(firstDepartureTime);
        } else {
            return " ? ";
        }
    }

    public String getPreisstufe() {
        if (trip.fares != null) {
            return trip.fares.get(0).units;
        } else {
            return  " ? ";
        }
    }

    public String getDurationString() {
        return UtilsString.durationString(duration_hours, duration_minutes);
    }

    public int getNumChanges() {
        if (trip.getNumChanges() == null) {
            return  0;
        } else {
            return trip.getNumChanges();
        }
    }

    public ArrayList<JourneyItem> getJourneyItems() {
        return journeyItems;
    }

    public long getDelayArrival() {
        return delayArrival;
    }

    public long getDelayDeparture() {
        return delayDeparture;
    }

    /**
     * Damit ein Nutzer nicht die gleiche Fahrt mehrfach hinzufügen kann, wird über die ID der
     * Trips überprüft, ob eine Fahrt schon enthalten ist <br/>
     * <p>
     * Erzeugung der IDs: {@link Trip#buildSubstituteId()} <br/>
     * Nutzung der Funktion in: {@link MyTripList#insertTrip(TripItem)}
     *
     * @param o zu vergleichendes Objekt
     * @return true - wenn die ID der Fahrten übereinstimmt <br/>
     * false - wenn das zu vergleichende Objekt kein ConnectionItem ist <br/>
     * false - wenn die IDs der Fahrten verschieden sind
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConnectionItem)) {
            return false;
        }
        ConnectionItem item = (ConnectionItem) o;
        return item.getTrip().getId().equals(this.getTrip().getId());
    }
}