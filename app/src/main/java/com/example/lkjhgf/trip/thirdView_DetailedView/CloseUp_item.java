package com.example.lkjhgf.trip.thirdView_DetailedView;

import com.example.lkjhgf.helper.Utils;

import de.schildbach.pte.dto.Trip;

public abstract class CloseUp_item {

    private String time_of_departure, time_of_arrival;
    private  String departure, destination;
    int image_resource;
    Trip.Leg leg;

    private boolean showDetails;


    public CloseUp_item(Trip.Leg leg){
        this.leg = leg;
        time_of_departure = Utils.setTime(leg.getDepartureTime());
        time_of_arrival = Utils.setTime(leg.getArrivalTime());

        departure = Utils.setLocationName(leg.departure);
        destination = Utils.setLocationName(leg.arrival);

        showDetails = false;
    }

    public String getTime_of_departure() {
        return time_of_departure;
    }

    public String getTime_of_arrival() {
        return time_of_arrival;
    }

    public int getImage_resource() {
        return image_resource;
    }

    public String getDestination() {
        return destination;
    }

    public String getDeparture() {
        return departure;
    }

    public boolean isShowDetails() {
        return showDetails;
    }

    public void setShowDetails(){
        showDetails = !showDetails;
    }

    //TODO hier soll das Ticket zuruckgegeben werden
    public Object getTicket(){
        return  null;
    }

}
