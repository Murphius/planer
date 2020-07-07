package com.example.lkjhgf.recyclerView.detailedView;

import com.example.lkjhgf.helper.util.Utils;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.recyclerView.detailedView.components.StopoverItem;

import java.util.ArrayList;

import de.schildbach.pte.dto.Stop;
import de.schildbach.pte.dto.Trip;

/**
 * Ein Abschnitt in dem der ÖPNV genutzt wird
 */
public class CloseUpPublicItem extends CloseUpItem {

    private int delayDeparture, delayArrival;
    private ArrayList<StopoverItem> stopoverItems;
    private String number, destination_of_number;
    private boolean departurePositionPlanned, arrivalPositionPlanned;

    private  String departure_platform, destination_platform;

    /**
     * Aufruf des Konstruktors der Oberklasse <br/>
     * Festlegen von Icon, Gleisen, Linienname, Linienziel und Zwischenhalten
     * @param publicTrip enthält alle Informationen zum Streckenabschnitt
     */
    public CloseUpPublicItem(Trip.Public publicTrip){
        super(publicTrip);

        Stop departureStop = publicTrip.departureStop;
        Stop destinationStop = publicTrip.arrivalStop;
        if(departureStop != null && departureStop.plannedDepartureTime != null){
            time_of_departure = UtilsString.setTime(departureStop.plannedDepartureTime);
        }
        if(destinationStop != null && destinationStop.plannedArrivalTime != null){
            time_of_arrival = UtilsString.setTime(destinationStop.plannedArrivalTime);
        }

        //Gleise
        departure_platform = UtilsString.platform(publicTrip.departureStop, false);
        destination_platform = UtilsString.platform(publicTrip.arrivalStop, true);
        if(publicTrip.departureStop.plannedDeparturePosition != null && publicTrip.departureStop.predictedDeparturePosition != null){
            departurePositionPlanned = publicTrip.departureStop.plannedDeparturePosition.equals(publicTrip.departureStop.predictedDeparturePosition);
        }else{
            departurePositionPlanned = true;
        }
        if(publicTrip.arrivalStop.plannedArrivalPosition != null && publicTrip.arrivalStop.predictedArrivalPosition != null ){
            arrivalPositionPlanned = publicTrip.arrivalStop.plannedArrivalPosition.equals(publicTrip.arrivalStop.predictedArrivalPosition);
        }else{
            arrivalPositionPlanned = true;
        }
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

    public boolean isArrivalPositionPlanned() {
        return arrivalPositionPlanned;
    }

    public boolean isDeparturePositionPlanned() {
        return departurePositionPlanned;
    }
}
