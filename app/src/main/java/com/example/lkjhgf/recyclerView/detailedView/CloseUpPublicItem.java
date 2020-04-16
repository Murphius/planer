package com.example.lkjhgf.recyclerView.detailedView;

import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.recyclerView.detailedView.components.Stopover_item;

import java.util.ArrayList;

import de.schildbach.pte.dto.Trip;

/**
 * Ein Abschnitt in dem der ÖPNV genutzt wird
 */
public class CloseUpPublicItem extends CloseUpItem {

    private int delayDeparture, delayArrival;
    private ArrayList<Stopover_item> stopoverItems;
    private String number, destination_of_number;

    private  String departure_platform, destination_platform;

    /**
     * Aufruf des Konstruktors der Oberklasse <br/>
     * Festlegen von Icon, Gleisen, Linienname, Linienziel und Zwischenhalten
     * @param publicTrip enthält alle Informationen zum Streckenabschnitt
     */
    public CloseUpPublicItem(Trip.Public publicTrip){
        super(publicTrip);

        //Gleise
        departure_platform = Utils.platform(publicTrip.departureStop, false);
        destination_platform = Utils.platform(publicTrip.arrivalStop, true);

        //Linienname
        number = publicTrip.line.label;
        //Endhaltestelle der Linie
        if(publicTrip.destination != null){
            destination_of_number = Utils.setLocationName(publicTrip.destination);
        }
        // Icon
        image_resource = Utils.iconPublic(publicTrip.line.product);

        // Verspätungen
        delayDeparture = Utils.longToMinutes(publicTrip.getDepartureDelay());
        delayArrival = Utils.longToMinutes(publicTrip.getArrivalDelay());

        //Zwischenhalte
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
    public String getDestinationOfNumber() {
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
