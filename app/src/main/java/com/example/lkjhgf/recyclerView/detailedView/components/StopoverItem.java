package com.example.lkjhgf.recyclerView.detailedView.components;

/**
 * Klasse für die Zwischenhalte einer ÖPNV Verbindung<br/>
 *
 * Eventuell auch eleganter mit einem kompakteren Konstruktor lösbar
 */
public class StopoverItem {
    private String timeOfDeparture;
    private String nameOfStop;
    private int delay;

    public StopoverItem(String timeOfDeparture, String nameOfStop, int delay){
        this.nameOfStop = nameOfStop;
        this.timeOfDeparture = timeOfDeparture;
        this.delay = delay;
    }

    String getTimeOfDeparture(){
        return timeOfDeparture;
    }
    String getNameOfStop(){
        return nameOfStop;
    }
    int getDelay(){return delay;}

}
