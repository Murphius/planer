package com.example.lkjhgf.helper;

import java.io.Serializable;
import java.util.Date;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.TripOptions;

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
}
