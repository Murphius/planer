package com.example.lkjhgf;

import android.app.Activity;

import androidx.test.rule.ActivityTestRule;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.optimisation.timeOptimisation.vrr.FarezoneD;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.schildbach.pte.dto.Fare;

public class UtilTest {


    public class TestTripItem extends TripItem {
        Date start;
        Date end;
        String preisstufe;
        int id;

        TestTripItem(Date start, Date end, String preisstufe, int id, HashMap<Fare.Type, Integer> travellingP) {
            super(travellingP, 35, region1);
            this.start = start;
            this.end = end;
            this.preisstufe = preisstufe;
            this.id = id;
        }

        TestTripItem(Date start, Date end, String preisstufe, int id, int startID, Set<Integer> crossedFarezones) {
            super(crossedFarezones, startID);
            this.start = start;
            this.end = end;
            this.preisstufe = preisstufe;
            this.id = id;
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

        @Override
        public String getTripID() {
            return id + "";
        }
    }

    @Rule
    public ActivityTestRule<MainMenu> rule = new ActivityTestRule<>(MainMenu.class);

    private static ArrayList<TimeTicket> timeTickets;
    private static Set<Integer> region1;

    @BeforeClass
    public static void oneTimeSetUp() {
        timeTickets = new ArrayList<>();
        timeTickets.add(new TimeTicket(new int[]{720, 720, 720, 720, 1470, 2530, 3040}, "24-StundenTicket-1", Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{5, 3, 3, 3, 3, 2, 2}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{1370, 1370, 1370, 1370, 2790, 4810, 5780}, "48-StundenTicket-1", Fare.Type.ADULT, 48 * 60 * 60 * 1000, new int[]{9, 6, 6, 6, 5, 5, 5}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{2295, 2295, 2815, 2950, 4275, 5730, 7240}, "7-TageTicket", Fare.Type.ADULT, 7 * 24 * 60 * 60 * 1000, new int[]{16, 11, 12, 13, 8, 5, 5}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{7120, 7120, 7560, 7920, 11355, 15355, 19390}, "30-TageTicket", Fare.Type.ADULT, Math.multiplyExact((long) 30, (long) 24 * 60 * 60 * 1000), new int[]{51, 31, 33, 34, 24, 16, 17}, 0, 24, false, 1));
        region1 = new HashSet<>();
        region1.add(170);
        region1.add(260);
        region1.add(350);
        region1.add(340);
        region1.add(440);
        region1.add(430);
    }

    @Test
    public void testRemoveOverlappingTrips(){
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 1;
        Set<Integer> region1 = new HashSet<>();
        region1.add(170);
        region1.add(260);
        region1.add(35);
        region1.add(340);
        region1.add(440);
        region1.add(430);

        startCalendar.set(2020, Calendar.JUNE, 20, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 10, 5);
        endCalendar.set(2020, Calendar.JUNE, 20, 10,35);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        UtilsList.removeOverlappingTrips(trips);
        Assert.assertFalse(trips.isEmpty());
        Assert.assertEquals(trips.size(), 1);
        Assert.assertEquals(trips.get(0).getTripID(), "1");

        trips.clear();
        id = 1;
        startCalendar.set(2020, Calendar.JUNE, 20, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 11, 15);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,20);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        UtilsList.removeOverlappingTrips(trips);
        Assert.assertFalse(trips.isEmpty());
        Assert.assertEquals(trips.size(), 1);
        Assert.assertEquals(trips.get(0).getTripID(), "1");

        trips.clear();
        id = 1;
        startCalendar.set(2020, Calendar.JUNE, 20, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 11, 15);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,20);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 13, 15);
        endCalendar.set(2020, Calendar.JUNE, 20, 13,55);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 13, 20);
        endCalendar.set(2020, Calendar.JUNE, 20, 13,54);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        UtilsList.removeOverlappingTrips(trips);
        Assert.assertFalse(trips.isEmpty());
        Assert.assertEquals(trips.size(), 3);
        Assert.assertEquals(trips.get(0).getTripID(),"1");
        Assert.assertEquals(trips.get(1).getTripID(),"3");
        Assert.assertEquals(trips.get(2).getTripID(),"4");

        trips.clear();
        id = 1;
        startCalendar.set(2020, Calendar.JUNE, 20, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 11, 15);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,20);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 11, 20);
        endCalendar.set(2020, Calendar.JUNE, 20, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 13, 15);
        endCalendar.set(2020, Calendar.JUNE, 20, 13,55);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 13, 20);
        endCalendar.set(2020, Calendar.JUNE, 20, 13,54);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++, 35, region1));

        UtilsList.removeOverlappingTrips(trips);
        Assert.assertFalse(trips.isEmpty());
        Assert.assertEquals(trips.size(),2);
    }

    @Test
    public void testOptimisation(){
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Set<Integer> region1 = new HashSet<>();
        region1.add(170);
        region1.add(260);
        region1.add(350);
        region1.add(340);
        region1.add(440);
        region1.add(430);

        startCalendar.set(2020, Calendar.JUNE, 20, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 20, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 3, 35, region1));

        ArrayList<TicketToBuy> tickets = FarezoneD.optimisation(trips, timeTickets);
        Assert.assertEquals(tickets.size(), 1);
        Assert.assertEquals(tickets.get(0).getTripList().size(), 3);
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "24-StundenTicket-1");

        trips.clear();
        tickets.clear();

        startCalendar.set(2020, Calendar.JUNE, 22, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 22, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 22, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 3, 35, region1));


        startCalendar.set(2020, Calendar.JUNE, 24, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 4, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 24, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 5, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 24, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 6, 35, region1));


        startCalendar.set(2020, Calendar.JUNE, 26, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 26, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 7, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 26, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 26, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 8, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 26, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 26, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 9, 35, region1));


        startCalendar.set(2020, Calendar.JUNE, 29, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 10, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 29, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 11, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 29, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 12, 35, region1));


        startCalendar.set(2020, Calendar.JUNE, 30, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 30, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 13, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 30, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 30, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 14, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 30, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 30, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 15, 35, region1));

        System.gc();
        long start = System.currentTimeMillis();
        tickets = FarezoneD.optimisation(trips, timeTickets);
        long ende = System.currentTimeMillis();
        long sekunden = (ende-start) / 1000;
        long msekunden = (ende-start)%1000;
        Assert.assertFalse(tickets.isEmpty());
        Assert.assertEquals(tickets.size(),2);
        boolean case1 = (tickets.get(0).getTicket().getName().equals("7-TageTicket")) && (tickets.get(1).getTicket().getName().equals("24-StundenTicket-1"));
        boolean case2 = (tickets.get(1).getTicket().getName().equals("7-TageTicket")) && (tickets.get(0).getTicket().getName().equals("24-StundenTicket-1"));
        Assert.assertTrue(case1||case2);

        trips.clear();
        tickets.clear();
        int i = 0;
        startCalendar.set(2020, Calendar.JUNE, 1, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 1, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 1, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 1, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 1, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 1, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));


        startCalendar.set(2020, Calendar.JUNE, 3, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 3, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 3, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 3, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 3, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 3, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));


        startCalendar.set(2020, Calendar.JUNE, 5, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 5, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 5, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 5, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 5, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 5, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));


        startCalendar.set(2020, Calendar.JUNE, 15, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 15, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 15, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 15, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 15, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 15, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));


        startCalendar.set(2020, Calendar.JUNE, 22, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 22, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 22, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));


        startCalendar.set(2020, Calendar.JUNE, 24, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 24, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 24, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));


        startCalendar.set(2020, Calendar.JUNE, 29, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 29, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JUNE, 29, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));


        startCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 1, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));


        startCalendar.set(2020, Calendar.JULY, 6, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 6, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 6, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 6, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 6, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 6, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));


        startCalendar.set(2020, Calendar.JULY, 13, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 13, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 13, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 13, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 13, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 13, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));


        startCalendar.set(2020, Calendar.JULY, 20, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 20, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 20, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 20, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 20, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 20, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 27, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 27, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 27, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 27, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 27, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 27, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));


        startCalendar.set(2020, Calendar.JULY, 29, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 29, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 29, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 29, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 29, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 29, 12,30);
        trips.add(new TestTripItem (startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 31, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 31, 10,30);
        trips.add(new TestTripItem (startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 31, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 31, 11,30);
        trips.add(new TestTripItem (startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        startCalendar.set(2020, Calendar.JULY, 31, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 31, 12,30);
        trips.add(new TestTripItem (startCalendar.getTime(), endCalendar.getTime(), "D", ++i, 35, region1));

        System.out.println(i);
        System.gc();
        start = System.currentTimeMillis();
        tickets = FarezoneD.optimisation(trips, timeTickets);
        ende = System.currentTimeMillis();
        sekunden = (ende-start) / 1000;
        msekunden = (ende-start)%1000;
        Assert.assertFalse(tickets.isEmpty());
    }

    @Test
    public void VRRoptimierung_keine_aktiven_tickets(){
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        HashMap<Fare.Type, Integer> numP = new HashMap<>();
        numP.put(Fare.Type.CHILD, 0);

        startCalendar.set(2020, Calendar.JUNE, 22, 15, 06);
        endCalendar.set(2020, Calendar.MAY, Calendar.JUNE, 15, 6 + 38);
        numP.put(Fare.Type.ADULT, 8);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", 1, numP);
        trips.add(tripItem01);
        Activity activity = rule.getActivity();
        HashMap<Fare.Type, ArrayList<TicketToBuy>> ticketResult = MainMenu.myProvider.optimise(trips, new HashMap<>(), activity);

        Assert.assertTrue(ticketResult.get(Fare.Type.CHILD).isEmpty());
        Assert.assertFalse(ticketResult.get(Fare.Type.ADULT).isEmpty());
        Assert.assertEquals(ticketResult.get(Fare.Type.ADULT).size(),1);
        Assert.assertEquals(ticketResult.get(Fare.Type.ADULT).get(0).getTicket().getName(), "10er-Ticket E");

        trips.clear();
        trips.add(tripItem01);
        startCalendar.set(2020, Calendar.JUNE, 23, 12, 15);
        endCalendar.set(2020, Calendar.JUNE, 23, 12, 15 + 38);
        numP.put(Fare.Type.ADULT,2);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2, numP);
        trips.add(tripItem02);
        ticketResult = MainMenu.myProvider.optimise(trips, ticketResult, activity);

        Assert.assertTrue(ticketResult.get(Fare.Type.CHILD).isEmpty());
        Assert.assertFalse(ticketResult.get(Fare.Type.ADULT).isEmpty());
        Assert.assertEquals(ticketResult.get(Fare.Type.ADULT).size(),3);
        //10er Ticketet C, 2x Einzelticket D

        startCalendar.set(2020, Calendar.JUNE, 24, 12, 15);
        endCalendar.set(2020, Calendar.JUNE, 24, 12, 15 + 38);
        numP.put(Fare.Type.ADULT,1);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2, numP);
        trips.add(tripItem03);
        startCalendar.set(2020, Calendar.JUNE, 24, 15, 15);
        endCalendar.set(2020, Calendar.JUNE, 24, 15, 15 + 38);
        numP.put(Fare.Type.ADULT,1);
        TripItem tripItem04 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2, numP);
        trips.add(tripItem04);
        ticketResult = MainMenu.myProvider.optimise(trips, ticketResult, activity);

        Assert.assertTrue(ticketResult.get(Fare.Type.CHILD).isEmpty());
        Assert.assertFalse(ticketResult.get(Fare.Type.ADULT).isEmpty());
        Assert.assertEquals(ticketResult.get(Fare.Type.ADULT).size(),2);
        Assert.assertEquals(ticketResult.get(Fare.Type.ADULT).get(1).getTicket().getName(), "10er-Ticket E");
        Assert.assertEquals(ticketResult.get(Fare.Type.ADULT).get(0).getTicket().getName(), "24-StundenTicket-1");
    }
}
