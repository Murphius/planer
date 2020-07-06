package com.example.lkjhgf.helper.util;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.Comparator;

/**
 * Vergleicht zwei TripItems nach ihrer Abfahrtszeit
 */
public class TripItemTimeComparator implements Comparator<TripItem> {
    @Override
    public int compare(TripItem first, TripItem second) {
        long result = first.getFirstDepartureTime().getTime() - second.getFirstDepartureTime().getTime();
        if(result > 0){
            return 1;
        }else if(result == 0){
            return 0;
        }else {
            return -1;
        }
    }
}
