package com.example.lkjhgf.recyclerView.possibleConnections;

import com.example.lkjhgf.helper.Utils;
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
    private String time_of_arrival, time_of_departure, preisstufe;
    private int delayDeparture, delayArrival;
    private ArrayList<JourneyItem> journeyItems;
    private int num_changes;
    private long duration_hours, duration_minutes;

    public ConnectionItem(Trip trip, ArrayList<JourneyItem> journeyItems){
        this.trip = trip;
        this.journeyItems = journeyItems;
        long delayDepartureL = 0, arrivalDelayL = 0;
        Date arrival, departure;

        // Abfahrtszeit
        if(trip.legs.get(0) instanceof Trip.Individual){
            departure = trip.getFirstDepartureTime();
        }else if(trip.getFirstPublicLeg() != null){
            departure = trip.getFirstPublicLeg().departureStop.plannedDepartureTime;
            delayDepartureL = trip.getFirstPublicLeg().getDepartureDelay();
        }else{
            departure = null;
        }

        //Ankunftszeit
        if(trip.legs.get(trip.legs.size()-1) instanceof  Trip.Individual){
            arrival = trip.getLastArrivalTime();
        }else if(trip.getLastPublicLeg() != null){
            arrival = trip.getLastPublicLeg().arrivalStop.predictedArrivalTime;
            arrivalDelayL = trip.getLastPublicLeg().getArrivalDelay();
        }else{
            arrival = null;
        }

        //Die Zeit als String speichern, um später die Ansicht damit zu füllen
        if(arrival != null){
            time_of_arrival = Utils.setTime(arrival);
        }else{
            time_of_arrival = " ? ";
        }
        if(departure != null){
            time_of_departure = Utils.setTime(departure);
        }else{
            time_of_departure = " ? ";
        }

        // Dauer der Fahrt
        duration_hours = Utils.durationToHour(trip.getDuration());
        duration_minutes = Utils.durationToMinutes(trip.getDuration());

        //Anzahlumstiege
        if(trip.getNumChanges() == null){
            num_changes = 0;
        }else{
            num_changes = trip.getNumChanges();
        }

        //Preisstufe
        if(trip.fares != null){
            preisstufe = trip.fares.get(0).units;
        }else{
            preisstufe = " ? ";
        }

        // Verspätung bei der Abfahrt / Ankunft
        delayDeparture = Utils.longToMinutes(delayDepartureL);
        delayArrival = Utils.longToMinutes(arrivalDelayL);
    }

    public Trip getTrip(){
        return trip;
    }

    public String getTimeOfArrival(){
        return time_of_arrival;
    }

    public String getTimeOfDeparture(){
        return time_of_departure;
    }

    public String getPreisstufe(){
        return preisstufe;
    }

    public String getDurationString(){
       return Utils.durationString(duration_hours, duration_minutes);
    }

    public int getNumChanges(){
        return num_changes;
    }

    public ArrayList<JourneyItem> getJourneyItems(){
        return journeyItems;
    }

    public int getDelayArrival() {
        return delayArrival;
    }

    public int getDelayDeparture(){
        return delayDeparture;
    }

    /**
     * Damit ein Nutzer nicht die gleiche Fahrt mehrfach hinzufügen kann, wird über die ID der
     * Trips überprüft, ob eine Fahrt schon enthalten ist <br/>
     *
     * Erzeugung der IDs: {@link Trip#buildSubstituteId()} <br/>
     * Nutzung der Funktion in: {@link com.example.lkjhgf.helper.futureTrip.MyTrip#insertTrip(TripItem)}
     * @param o zu vergleichendes Objekt
     * @return true - wenn die ID der Fahrten übereinstimmt
     * @return  false - wenn das zu vergleichende Objekt kein ConnectionItem ist
     * @return  false - wenn die IDs der Fahrten verschieden sind
     */
    @Override
    public boolean equals(Object o){
        if(! (o instanceof ConnectionItem)){
            return false;
        }
        ConnectionItem item = (ConnectionItem) o;
        return item.getTrip().getId().equals(this.getTrip().getId());
    }
}