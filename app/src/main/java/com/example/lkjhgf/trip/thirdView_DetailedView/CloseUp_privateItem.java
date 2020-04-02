package com.example.lkjhgf.trip.thirdView_DetailedView;

import com.example.lkjhgf.helper.Utils;
import com.google.android.gms.maps.model.LatLng;

import de.schildbach.pte.dto.Trip;

public class CloseUp_privateItem extends CloseUp_item {

    public CloseUp_privateItem(Trip.Individual individualTrip){
        super(individualTrip);
        image_resource = Utils.iconIndividual(individualTrip.type);
    }

    public LatLng getDepartureLocation(){
        return new LatLng(leg.departure.coord.getLatAsDouble(), leg.departure.coord.getLonAsDouble());
    }

    public LatLng getDestinationLocation(){
        return new LatLng(leg.arrival.coord.getLatAsDouble(), leg.arrival.coord.getLonAsDouble());
    }

}
