package com.example.lkjhgf;

import androidx.test.rule.ActivityTestRule;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeOptimisation;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import de.schildbach.pte.dto.Fare;

public class optimierungsTestB {

    public class TestTripItem extends TripItem {
        Date start;
        Date end;
        String preisstufe;
        int id;
        int startID, destinationID;

        TestTripItem(Date start, Date end, String preisstufe, int id, HashMap<Fare.Type, Integer> travellingP, int startID, int destinationID) {
            super(travellingP);
            this.start = start;
            this.end = end;
            this.preisstufe = preisstufe;
            this.id = id;
            this.startID = startID;
            this.destinationID = destinationID;
        }

        TestTripItem(Date start, Date end, String preisstufe, int id, int startID, int destinationID) {
            super(new HashMap<>());
            this.start = start;
            this.end = end;
            this.preisstufe = preisstufe;
            this.id = id;
            this.startID = startID;
            this.destinationID = destinationID;
        }

        @Override
        public Date getFirstDepartureTime() {
            return start;
        }

        public long getFirstDeparture()
        {
            return start.getTime();
        }

        @Override
        public Date getLastArrivalTime() {
            return end;
        }

        @Override
        public String getPreisstufe() {
            return preisstufe;
        }

        @Override
        public String getTripID() {
            return id + "";
        }

        @Override
        public int getDestinationID() {
            return destinationID;
        }

        @Override
        public int getStartID() {
            return startID;
        }
    }

    @Rule
    public ActivityTestRule<MainMenu> rule = new ActivityTestRule<>(MainMenu.class);

    private static ArrayList<TimeTicket> timeTickets;

    @BeforeClass
    public static void oneTimeSetUp() {
        timeTickets = new ArrayList<>();
        timeTickets.add(new TimeTicket(new int[]{720, 720, 720, 720, 1470, 2530, 3040}, "24-StundenTicket-1", Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{5, 3, 3, 3, 3, 2, 2}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{1370, 1370, 1370, 1370, 2790, 4810, 5780}, "48-StundenTicket-1", Fare.Type.ADULT, 48 * 60 * 60 * 1000, new int[]{9, 6, 6, 6, 5, 5, 5}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{2295, 2295, 2815, 2950, 4275, 5730, 7240}, "7-TageTicket", Fare.Type.ADULT, 7 * 24 * 60 * 60 * 1000, new int[]{16, 11, 12, 13, 8, 5, 5}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{7120, 7120, 7560, 7920, 11355, 15355, 19390}, "30-TageTicket", Fare.Type.ADULT, 30L * 24L * 60L * 60L * 1000L, new int[]{51, 31, 33, 34, 24, 16, 17}, 0, 24, false, 1));
    }

    @Test
    public void tripsOneRegionDayTicket() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;

        startCalendar.set(2020, Calendar.JULY, 1, 9, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 170, 150)); //Herten -> Marl

        startCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 150, 180)); //Marl -> Datteln

        startCalendar.set(2020, Calendar.JULY, 1, 12, 30);
        endCalendar.set(2020, Calendar.JULY, 1, 13, 00);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 180, 170)); //Datteln -> Herten
        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = TimeOptimisation.optimierungPreisstufeB(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(tickets.isEmpty());
        Assert.assertEquals(tickets.size(), 1);
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "24-StundenTicket-1");
    }

    @Test
    public void tripsOneRegionTwoDay() {
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 1;

        startCalendar.set(2020, Calendar.JULY, 1, 9, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 170, 150)); //Herten -> Marl

        startCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 150, 180)); //Marl -> Datteln

        startCalendar.set(2020, Calendar.JULY, 1, 12, 30);
        endCalendar.set(2020, Calendar.JULY, 1, 13, 00);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 180, 170)); //Datteln -> Herten

        startCalendar.set(2020, Calendar.JULY, 2, 9, 1);
        endCalendar.set(2020, Calendar.JULY, 2, 10, 1);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 170, 150)); //Herten -> Marl

        startCalendar.set(2020, Calendar.JULY, 2, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 150, 180)); //Marl -> Datteln

        startCalendar.set(2020, Calendar.JULY, 2, 12, 30);
        endCalendar.set(2020, Calendar.JULY, 2, 13, 00);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 180, 170)); //Datteln -> Herten

        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = TimeOptimisation.optimierungPreisstufeB(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(tickets.isEmpty());
        Assert.assertEquals(tickets.size(), 1);
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "48-StundenTicket-1");
    }

    @Test
    public void tripsOneRegionWeekTicket() {
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 0;
        startCalendar.set(2020, Calendar.JUNE, 1, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 1, 10, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 170, 180));

        startCalendar.set(2020, Calendar.JUNE, 1, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 1, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 180, 170));

        startCalendar.set(2020, Calendar.JUNE, 1, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 1, 12, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 260, 250));

        startCalendar.set(2020, Calendar.JUNE, 3, 10, 5);
        endCalendar.set(2020, Calendar.JUNE, 3, 10, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 170, 180));

        startCalendar.set(2020, Calendar.JUNE, 3, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 3, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 180, 170));

        startCalendar.set(2020, Calendar.JUNE, 3, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 3, 12, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 260, 250));

        startCalendar.set(2020, Calendar.JUNE, 5, 10, 15);
        endCalendar.set(2020, Calendar.JUNE, 5, 10, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 170, 180));

        startCalendar.set(2020, Calendar.JUNE, 5, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 5, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 180, 170));

        startCalendar.set(2020, Calendar.JUNE, 5, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 5, 12, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 260, 250));

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticketList = TimeOptimisation.optimierungPreisstufeB(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(ticketList.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(ticketList.size(), 1);
        Assert.assertEquals(ticketList.get(0).getTicket().getName(), "7-TageTicket");
    }

    @Test
    public void tripsOneRegionDifferentTickets() {
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 0;

        for (int i = 1; i <= 15; i++) {
            startCalendar.set(2020, Calendar.JULY, i * 2, 10, 0);
            endCalendar.set(2020, Calendar.JULY, i * 2, 10, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 180, 150));

            startCalendar.set(2020, Calendar.JULY, i * 2, 11, 0);
            endCalendar.set(2020, Calendar.JULY, i * 2, 11, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 60, 50));

            startCalendar.set(2020, Calendar.JULY, i * 2, 12, 0);
            endCalendar.set(2020, Calendar.JULY, i * 2, 12, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 50, 170));
        }
        for (int i = 1; i <= 15; i++) {
            startCalendar.set(2020, Calendar.AUGUST, i * 2, 10, 0);
            endCalendar.set(2020, Calendar.AUGUST, i * 2, 10, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 180, 150));

            startCalendar.set(2020, Calendar.AUGUST, i * 2, 11, 0);
            endCalendar.set(2020, Calendar.AUGUST, i * 2, 11, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 60, 50));

            startCalendar.set(2020, Calendar.AUGUST, i * 2, 12, 0);
            endCalendar.set(2020, Calendar.AUGUST, i * 2, 12, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 50, 170));
        }
        trips.sort(Comparator.comparing(TripItem::getFirstDepartureTime));

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticketList = TimeOptimisation.optimierungPreisstufeB(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(ticketList.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(ticketList.size(), 2);
    }

    @Test
    public void differentRegionsDayTicket(){
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 0;
        for (int i = 1; i <= 5; i++) {
            if(i%2==0){
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 250, 150));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 60, 50));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 50, 170));
            }else{
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 180, 150));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 60, 50));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 50, 360));
            }
        }
        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticketList = TimeOptimisation.optimierungPreisstufeB(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(ticketList.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(ticketList.size(), 2);
    }
}
