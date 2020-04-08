package com.example.lkjhgf.futureTrips.recyclerView;

import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.trip.secondView_service.secondView_components.Journey_item;

import java.util.ArrayList;

import de.schildbach.pte.dto.Trip;

public class TripItem {
    private Trip trip;
    private boolean isComplete;
    private int numAdult, numChildren;
    private ArrayList<Journey_item> journey_items;

    public TripItem(Trip trip, boolean isComplete){
        this.trip = trip;
        this.isComplete = isComplete;
        numAdult = 0;
        numChildren = 0;
        journey_items = Utils.journeyItems(trip.legs);
    }

    public TripItem(Trip trip, boolean isComplete, int numAdult, int numChildren){
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

    public int getNumAdult(){
        return numAdult;
    }

    public int getNumChildren() {
        return numChildren;
    }

    ArrayList<Journey_item> getJourney_items(){
        return journey_items;
    }

    @Override
    public boolean equals(Object o){
        if (! (o instanceof TripItem)){
            return false;
        }
        TripItem otherTripItem = (TripItem) o;
        return otherTripItem.trip.getId().equals(this.trip.getId());
    }
}
