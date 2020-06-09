package com.example.lkjhgf.optimisation;


import com.example.lkjhgf.optimierungDTest;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import de.schildbach.pte.dto.Fare;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

class TimeOptimisationTest extends junit.framework.TestCase{
    @Test
    void testFindBest24hInterval() {
    }
    @Test
    void testOptimierungPreisstufeD() {/*
        ArrayList<TimeTicket> timeTickets = new ArrayList<>();
        timeTickets.add(new TimeTicket(new int[]{720, 720, 720, 720, 1470, 2530, 3040}, "24-StundenTicket-1", Fare.Type.ADULT, 24 * 60 * 60 * 1000, 2));
        timeTickets.add(new TimeTicket(new int[]{1370, 1370, 1370, 1370, 2790, 4810, 5780}, "48-StundenTicket-1", Fare.Type.ADULT, 48 * 60 * 60 * 1000, 5));
        timeTickets.add(new TimeTicket(new int[]{2295, 2295, 2815, 2950, 4275, 5730, 7240}, "7-TageTicket", Fare.Type.ADULT, 7 * 24 * 60 * 60 * 1000, 5));
        timeTickets.add(new TimeTicket(new int[]{7120, 7120, 7560, 7920, 11355, 15355, 19390}, "30-TageTicket", Fare.Type.ADULT, 30 * 24 * 60 * 60 * 1000, 16));

        ArrayList<TripItem> trips = new ArrayList<>();
        //einzelne Fahrt
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.set(2020, 05,20, 15, 06);
        endCalendar.set(2020, 05, 20, 15, 6+38);
        trips.add(new optimierungDTest.TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D"));

        ArrayList<TicketToBuy> ticketToBuyArrayList = TimeOptimisation.optimierungPreisstufeD(trips, timeTickets);

        assert ticketToBuyArrayList.size() == 1;*/
    }
}
