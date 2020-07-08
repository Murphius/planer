package com.example.lkjhgf;

import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.schildbach.pte.dto.Fare;

public class optimierungDTest extends junit.framework.TestCase{

    public class TestTicketItem extends TicketToBuy {
        Date start;
        Date end;
        TestTicketItem(Ticket ticket, String preisstufe, Date start, Date end){
            super(ticket, preisstufe);
            this.start = start;
            this.end = end;
        }

        @Override
        public Date getLastArrivalTime() {
            return end;
        }

        @Override
        public Date getFirstDepartureTime() {
            return start;
        }
    }

    public class TestTripItem extends TripItem {
        Date start;
        Date end;
        String preisstufe;

        TestTripItem(Date start, Date end, String preisstufe){
            super();
            this.start = start;
            this.end = end;
            this.preisstufe = preisstufe;
        }

        @Override
        public Date getFirstDepartureTime() {
            return start;
        }

        @Override
        public Date getLastArrivalTime() {
            return end;
        }

        @Override
        public String getPreisstufe() {
            return preisstufe;
        }
    }

    @Test
    public void test3(){
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
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D"));

        /*ArrayList<TicketToBuy> ticketToBuyArrayList = TimeOptimisation.optimierungPreisstufeD(trips, timeTickets);

        assert ticketToBuyArrayList.size() == 1;
        */


    }
}
