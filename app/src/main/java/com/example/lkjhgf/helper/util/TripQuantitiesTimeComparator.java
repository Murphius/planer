package com.example.lkjhgf.helper.util;

import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TripQuantity;

import java.util.Comparator;

/**
 * Vergleicht zwei TripQuantities anhand ihrer Abfahrtszeit
 */
public class TripQuantitiesTimeComparator implements Comparator<TripQuantity> {
    @Override
    public int compare(TripQuantity first, TripQuantity second) {
        long result = first.getTripItem().getFirstDepartureTime().getTime() - second.getTripItem().getFirstDepartureTime().getTime();
        if(result > 0){
            return 1;
        }else if(result == 0){
            return 0;
        }else {
            return -1;
        }
    }
}
