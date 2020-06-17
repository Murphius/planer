package com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation;

import com.example.lkjhgf.publicTransport.provider.Farezone;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;

import de.schildbach.pte.dto.Trip;

public class FarezoneTrips {
    Farezone farezone;
    ArrayList<TripItem> tripItems;

    FarezoneTrips(Farezone f){
        farezone = f;
        tripItems = new ArrayList<>();
    }

    public void add(TripItem tripItem){
        tripItems.add(tripItem);
    }

    public int getSize(){
        return tripItems.size();
    }

    public int getID(){
        return farezone.getId();
    }

    public ArrayList<TripItem> getTripItems(){
        return tripItems;
    }

    public Farezone getFarezone() {
        return farezone;
    }
}
