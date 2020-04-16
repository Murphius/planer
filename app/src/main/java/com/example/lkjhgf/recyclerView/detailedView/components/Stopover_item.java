package com.example.lkjhgf.recyclerView.detailedView.components;

/**
 * Klasse für die Zwischenhalte einer ÖPNV Verbindung<br/>
 *
 * Eventuell auch eleganter mit einem kompakteren Konstruktor lösbar
 */
public class Stopover_item {
    private String timeOfDeparture;
    private String nameOfStop;
    private int delay;

    public Stopover_item(String timeOfDeparture, String nameOfStop, int delay){
        this.nameOfStop = nameOfStop;
        this.timeOfDeparture = timeOfDeparture;
        this.delay = delay;
    }

    public String getTimeOfDeparture(){
        return timeOfDeparture;
    }
    public String getNameOfStop(){
        return nameOfStop;
    }
    public int getDelay(){return delay;}

}
