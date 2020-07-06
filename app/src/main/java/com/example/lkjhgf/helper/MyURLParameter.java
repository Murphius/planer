package com.example.lkjhgf.helper;

import java.io.Serializable;
import java.util.Date;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.TripOptions;

/**
 * Enthält alle benötigten Informationen, um später die gleiche Abfrage an den Server zu stellen <br/>
 *
 * Um auch die unterschiedlichen TripOptions einer Fahrt beim Aktualisieren gleich zu haben, wie bei
 * der original Anfrage, werden die Anfrage Parameter in dieser Klasse gespeichert. <br/>
 * Zusätzlich ist so ein Zugriff auf einen eventuell Zwischenhalt möglich.
 */
public class MyURLParameter implements Serializable {

    private Date startDate;
    private Location startLocation, destinationLocation;
    private Location via;
    private TripOptions tripOptions;
    private boolean isDepartureTime;

    public MyURLParameter(Date startDate, Location startLocation, Location via, Location destinationLocation, boolean isDepartureTime, TripOptions tripOptions){
        this.startDate = startDate;
        this.startLocation = startLocation;
        this.via = via;
        this.destinationLocation = destinationLocation;
        this.isDepartureTime = isDepartureTime;
        this.tripOptions = tripOptions;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getVia() {
        return via;
    }

    public TripOptions getTripOptions() {
        return tripOptions;
    }

    public boolean isDepartureTime(){
        return isDepartureTime;
    }

    public void changeDate(Date newDate){
        startDate = newDate;
    }
}
