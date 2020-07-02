package com.example.lkjhgf.helper.util;

import com.example.lkjhgf.optimisation.TicketToBuy;

import java.util.Comparator;

public class TripQuantitiesTimeComparator implements Comparator<TicketToBuy.TripQuantity> {
    @Override
    public int compare(TicketToBuy.TripQuantity first, TicketToBuy.TripQuantity second) {
        long firstTime = first.getTripItem().getFirstDepartureTime().getTime();
        long secondTime = second.getTripItem().getFirstDepartureTime().getTime();
        if(firstTime - secondTime > 0){
            return 1;
        }else if(firstTime - secondTime == 0){
            return 0;
        }else {
            return -1;
        }
    }
}
