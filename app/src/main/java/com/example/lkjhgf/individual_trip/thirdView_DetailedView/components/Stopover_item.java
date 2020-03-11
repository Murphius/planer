package com.example.lkjhgf.individual_trip.thirdView_DetailedView.components;

public class Stopover_item {
    private String timeOfDeparture;
    private String nameOfStop;

    public Stopover_item(String timeOfDeparture, String nameOfStop){
        this.nameOfStop = nameOfStop;
        this.timeOfDeparture = timeOfDeparture;
    }

    public String getTimeOfDeparture(){
        return timeOfDeparture;
    }
    public String getNameOfStop(){
        return nameOfStop;
    }

}
