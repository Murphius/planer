package com.example.lkjhgf.trip.secondView_service;

import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.trip.secondView_service.secondView_components.Journey_item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import de.schildbach.pte.dto.Trip;

public class Connection_item implements Serializable {
    private Trip trip;
    private String time_of_arrival, time_of_departure, preisstufe;
    private int delayDeparture, delayArrival;
    private ArrayList<Journey_item> journey_items;
    private int num_changes;
    private long duration_hours, duration_minutes;

    public Connection_item(Trip trip, ArrayList<Journey_item> journey_items){
        this.trip = trip;
        this.journey_items = journey_items;
        long delayDepartureL = 0, arrivalDelayL = 0;
        Date arrival, departure;

        if(trip.legs.get(0) instanceof Trip.Individual){
            departure = trip.getFirstDepartureTime();
        }else if(trip.getFirstPublicLeg() != null){
            departure = trip.getFirstPublicLeg().departureStop.plannedDepartureTime;
            delayDepartureL = trip.getFirstPublicLeg().getDepartureDelay();
        }else{
            departure = null;
        }


        if(trip.legs.get(trip.legs.size()-1) instanceof  Trip.Individual){
            arrival = trip.getLastArrivalTime();
        }else if(trip.getLastPublicLeg() != null){
            arrival = trip.getLastPublicLeg().arrivalStop.predictedArrivalTime;
            arrivalDelayL = trip.getLastPublicLeg().getArrivalDelay();
        }else{
            arrival = null;
        }

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

        duration_hours = Utils.durationToHour(trip.getDuration());
        duration_minutes = Utils.durationToMinutes(trip.getDuration());

        if(trip.getNumChanges() == null){
            num_changes = 0;
        }else{
            num_changes = trip.getNumChanges();
        }

        if(trip.fares != null){
            preisstufe = trip.fares.get(0).units;
        }else{
            preisstufe = " ? ";
        }

        delayDeparture = Utils.longToMinutes(delayDepartureL);
        delayArrival = Utils.longToMinutes(arrivalDelayL);
    }

    public Trip getTrip(){
        return trip;
    }

    String get_time_of_arrival(){
        return time_of_arrival;
    }

    String get_time_of_departure(){
        return time_of_departure;
    }

    String get_preisstufe(){
        return preisstufe;
    }

    String get_duration_string(){
       return Utils.durationString(duration_hours, duration_minutes);
    }

    int get_num_changes(){
        return num_changes;
    }

    ArrayList<Journey_item> get_journey_items(){
        return journey_items;
    }

    int getDelayArrival() {
        return delayArrival;
    }

    int getDelayDeparture(){
        return delayDeparture;
    }

    @Override
    public boolean equals(Object o){
        if(! (o instanceof Connection_item)){
            return false;
        }
        Connection_item item = (Connection_item) o;
        return item.getTrip().getId().equals(this.getTrip().getId());
    }
}