package com.example.lkjhgf.helper.util;

import com.example.lkjhgf.optimisation.TicketToBuy;

import java.util.Comparator;

public class TicketToBuyTimeComporator implements Comparator<TicketToBuy> {
    @Override
    public int compare(TicketToBuy o1, TicketToBuy o2) {
        long result = o1.getFirstDepartureTime().getTime() - o2.getFirstDepartureTime().getTime();
        if (result == 0) {
            return 0;
        } else if (result > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
