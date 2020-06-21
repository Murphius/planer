package com.example.lkjhgf.helper.util;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.Comparator;

public class TripItemTimeComparator implements Comparator<TripItem> {
    @Override
    public int compare(TripItem first, TripItem second) {
        long firstTime = first.getFirstDepartureTime().getTime();
        long secondTime = second.getFirstDepartureTime().getTime();
        if(firstTime - secondTime > 0){
            return 1;
        }else if(firstTime - secondTime == 0){
            return 0;
        }else {
            return -1;
        }
    }
}
