package com.example.lkjhgf.recyclerView.possibleConnections.components;

import java.io.Serializable;

/**
 * Enthält die Information über das Icon (gibt an welche Art von Transport), sowie die Dauer
 * (Individueller Transport) oder über die Linie (ÖPNV)
 */
public class JourneyItem implements Serializable {
    private int imageResource;
    private String timeOrLine;

    public JourneyItem(int imageResource, String timeOrLine){
        this.imageResource = imageResource;
        this.timeOrLine = timeOrLine;
    }

    int getImageResource(){
        return imageResource;
    }
    String getTimeOrLine(){
        return timeOrLine;
    }
}
