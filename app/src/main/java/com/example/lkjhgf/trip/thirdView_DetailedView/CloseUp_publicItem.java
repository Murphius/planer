package com.example.lkjhgf.trip.thirdView_DetailedView;

import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.trip.thirdView_DetailedView.components.Stopover_item;

import java.util.ArrayList;

import de.schildbach.pte.dto.Trip;

public class CloseUp_publicItem extends CloseUp_item {

    private int delayDeparture, delayArrival;
    private ArrayList<Stopover_item> stopoverItems;
    private String number, destination_of_number;


    private  String departure_platform, destination_platform;

    public CloseUp_publicItem(Trip.Public publicTrip){
        super(publicTrip);

        departure_platform = Utils.platform(publicTrip.departureStop, false);
        destination_platform = Utils.platform(publicTrip.arrivalStop, true);

        number = publicTrip.line.label;
        if(publicTrip.destination != null){
            destination_of_number = Utils.setLocationName(publicTrip.destination);
        }
        image_resource = Utils.iconPublic(publicTrip.line.product);

        delayDeparture = Utils.longToMinutes(publicTrip.getDepartureDelay());
        delayArrival = Utils.longToMinutes(publicTrip.getArrivalDelay());

        stopoverItems = Utils.createStopoverList(publicTrip);
    }

    public int getDelayDeparture(){
        return delayDeparture;
    }
    public int getDelayArrival(){
        return delayArrival;
    }
    public String getNumber(){
        return number;
    }
    public String getDestination_of_number() {
        return destination_of_number;
    }
    public String getDeparturePlatform(){
        return departure_platform;
    }
    public String getDestinationPlatform(){
        return destination_platform;
    }
    public ArrayList<Stopover_item> getStopoverItems(){
        return stopoverItems;
    }
}
