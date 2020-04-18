package com.example.lkjhgf.recyclerView.detailedView;

import com.example.lkjhgf.helper.util.UtilsList;
import com.google.android.gms.maps.model.LatLng;

import de.schildbach.pte.dto.Trip;

/**
 * Ein individueller Streckenabschnitt <br/>
 */
public class CloseUpPrivateItem extends CloseUpItem {

    /**
     * Neben dem Aufruf des Konstruktors der Oberklasse wird hier auch das Icon festgelegt
     * @param individualTrip - enthält alle Informationen zu dem Abschnitt
     */
    public CloseUpPrivateItem(Trip.Individual individualTrip){
        super(individualTrip);
        image_resource = UtilsList.iconIndividual(individualTrip.type);
    }

    /**
     * Wandelt die Anfangskoordinate von {@link de.schildbach.pte.dto.Point} in LatLng um
     * @return gibt die Anfangskoordinaten als LatLng zurück
     */
    public LatLng getDepartureLocation(){
        return new LatLng(leg.departure.coord.getLatAsDouble(), leg.departure.coord.getLonAsDouble());
    }

    /**
     * Wandelt die Zielkoordinate von {@link de.schildbach.pte.dto.Point} in LatLng um
     * @return gibt die Zielkoordinaten als LatLng zurück
     */
    public LatLng getDestinationLocation(){
        return new LatLng(leg.arrival.coord.getLatAsDouble(), leg.arrival.coord.getLonAsDouble());
    }

}
