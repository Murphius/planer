package com.example.lkjhgf.recyclerView.detailedView;

import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.helper.UtilsString;
import com.example.lkjhgf.helper.UtilsList;
import com.example.lkjhgf.recyclerView.detailedView.components.StopoverItem;

import java.util.ArrayList;

import de.schildbach.pte.dto.Trip;

/**
 * Ein Abschnitt in dem der ÖPNV genutzt wird
 */
public class CloseUpPublicItem extends CloseUpItem {

    private int delayDeparture, delayArrival;
    private ArrayList<StopoverItem> stopoverItems;
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
        departure_platform = UtilsString.platform(publicTrip.departureStop, false);
        destination_platform = UtilsString.platform(publicTrip.arrivalStop, true);

        //Linienname
        number = publicTrip.line.label;
        //Endhaltestelle der Linie
        if(publicTrip.destination != null){
            destination_of_number = UtilsString.setLocationName(publicTrip.destination);
        }
        // Icon
        image_resource = UtilsList.iconPublic(publicTrip.line.product);

        // Verspätungen
        delayDeparture = Utils.longToInt(publicTrip.getDepartureDelay());
        delayArrival = Utils.longToInt(publicTrip.getArrivalDelay());

        //Zwischenhalte
        stopoverItems = UtilsList.createStopoverList(publicTrip);
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
    public ArrayList<StopoverItem> getStopoverItems(){
        return stopoverItems;
    }
}
