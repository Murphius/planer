package com.example.lkjhgf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.schildbach.pte.dto.Trip;

public class Connection_item {
    private Trip trip;
    Date arrival, departure;
    private String time_of_arrival, time_of_departure, preisstufe;
    private ArrayList<Journey_item> journey_items;
    private int num_changes;
    private long duration_hours, duration_minutes;

    public Connection_item(Trip trip, ArrayList<Journey_item> journey_items){
        this.trip = trip;
        this.journey_items = journey_items;
        departure = trip.getFirstDepartureTime();
        arrival = trip.getLastArrivalTime();
        DateFormat dateFormat = new SimpleDateFormat("HH : mm");
        time_of_arrival = dateFormat.format(arrival);
        time_of_departure = dateFormat.format(departure);
        duration_hours = TimeUnit.HOURS.convert(trip.getDuration(), TimeUnit.MILLISECONDS) % 24;
        duration_minutes = TimeUnit.MINUTES.convert(trip.getDuration(), TimeUnit.MILLISECONDS) % 60;
        num_changes = trip.getNumChanges();
        if(trip.fares != null){
            preisstufe = trip.fares.get(0).units;
        }else{
            preisstufe = "?";
        }
    }

    public Trip getTrip(){
        return trip;
    }


    public String get_time_of_arrival(){
        return time_of_arrival;
    }
    public String get_time_of_departure(){
        return time_of_departure;
    }
    public String get_preisstufe(){
        return preisstufe;
    }
    public String get_duration_string(){
        String result = "";
        if(duration_hours >= 1){
            result += duration_hours + "h ";
        }
        if(duration_minutes < 1){
            result += "00";
        }else if(duration_minutes < 10){
            result += "0" + duration_minutes;
        }else{
            result += duration_minutes;
        }

        return result + "min";
    }
    public int get_num_changes(){
        return num_changes;
    }
    public ArrayList<Journey_item> get_journey_items(){
        return journey_items;
    }
}
