package com.example.lkjhgf;

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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.schildbach.pte.dto.Fare;

public class OptimierungsTestD {

    public class TestTripItem extends TripItem {
        Date start;
        Date end;
        String preisstufe;
        int id;

        TestTripItem(Date start, Date end, String preisstufe, int id, HashMap<Fare.Type, Integer> travellingP, int startID, Set<Integer> crossedFarezones) {
            super(travellingP, startID, crossedFarezones);
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

    @BeforeClass
    public static void oneTimeSetUp() {
        timeTickets = new ArrayList<>();
        timeTickets.add(new TimeTicket(new int[]{720, 720, 720, 720, 1470, 2530, 3040}, "24-StundenTicket-1", Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{5, 3, 3, 3, 3, 2, 2}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{1370, 1370, 1370, 1370, 2790, 4810, 5780}, "48-StundenTicket-1", Fare.Type.ADULT, 48 * 60 * 60 * 1000, new int[]{9, 6, 6, 6, 5, 5, 5}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{2295, 2295, 2815, 2950, 4275, 5730, 7240}, "7-TageTicket", Fare.Type.ADULT, 7 * 24 * 60 * 60 * 1000, new int[]{16, 11, 12, 13, 8, 5, 5}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{7120, 7120, 7560, 7920, 11355, 15355, 19390}, "30-TageTicket", Fare.Type.ADULT, Math.multiplyExact((long) 30, (long) 24 * 60 * 60 * 1000), new int[]{51, 31, 33, 34, 24, 16, 17}, 0, 24, false, 1));
    }

    private ArrayList<TripItem> generateTripsWithoutUserInformation(int startIndex, int endIndex) {
        Set<Integer> region1 = new HashSet<>();
        region1.add(170);
        region1.add(260);
        region1.add(350);
        region1.add(340);
        region1.add(440);
        region1.add(430);

        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.set(2020, Calendar.MAY, 22, 15, 06);
        endCalendar.set(2020, Calendar.MAY, 22, 15, 6 + 38);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1, 43, region1);

        startCalendar.set(2020, Calendar.MAY, 23, 12, 15);
        endCalendar.set(2020, Calendar.MAY, 23, 12, 15 + 38);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2, 17, region1);

        startCalendar.set(2020, Calendar.MAY, 23, 11, 15);
        endCalendar.set(2020, Calendar.MAY, 23, 11, 15 + 38);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", 3, 26, region1);

        startCalendar.set(2020, Calendar.MAY, 24, 11, 15);
        endCalendar.set(2020, Calendar.MAY, 24, 11, 15 + 38);
        TripItem tripItem04 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 3, 34, region1);

        trips.add(tripItem01);
        trips.add(tripItem02);
        trips.add(tripItem03);
        trips.add(tripItem04);


        ArrayList<TripItem> tripSubList = new ArrayList<>();
        for (int j = startIndex; j < endIndex && j < trips.size(); j++) {
            tripSubList.add(trips.get(j));
        }

        Collections.sort(tripSubList, MainMenu.myProvider);
        return tripSubList;
    }
/*
    private ArrayList<TripItem> generateTripsWithUserInformation(int startIndex, int endIndex) {
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        HashMap<Fare.Type, Integer> num = new HashMap<>();
        num.put(Fare.Type.CHILD, 0);

        startCalendar.set(2020, Calendar.MAY, 22, 15, 6);
        endCalendar.set(2020, Calendar.MAY, 22, 15, 6 + 38);

        num.put(Fare.Type.ADULT, 2);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1, num);

        startCalendar.set(2020, Calendar.MAY, 23, 12, 15);
        endCalendar.set(2020, Calendar.MAY, 23, 12, 15 + 38);
        num.put(Fare.Type.ADULT, 1);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2, num);

        startCalendar.set(2020, Calendar.MAY, 23, 11, 15);
        endCalendar.set(2020, Calendar.MAY, 23, 11, 15 + 38);
        num.put(Fare.Type.ADULT, 3);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", 3, num);

        startCalendar.set(2020, Calendar.MAY, 24, 11, 15);
        endCalendar.set(2020, Calendar.MAY, 24, 11, 15 + 38);
        num.put(Fare.Type.ADULT, 2);
        TripItem tripItem04 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 4, num);

        trips.add(tripItem01);
        trips.add(tripItem02);
        trips.add(tripItem03);
        trips.add(tripItem04);


        ArrayList<TripItem> tripSubList = new ArrayList<>();
        for (int j = startIndex; j < endIndex && j < trips.size(); j++) {
            tripSubList.add(trips.get(j));
        }

        Collections.sort(tripSubList, MainMenu.myProvider);
        return tripSubList;
    }
*/
    private ArrayList<TicketToBuy> generateTicketsToBuy(int startIndex, int endIndex) {
        ArrayList<TicketToBuy> ticketToBuyArrayList = new ArrayList<>();

        TicketToBuy ticket01 = new TicketToBuy(timeTickets.get(0), "D");
        ticket01.addTripItems(generateTripsWithoutUserInformation(0, 3));

        TicketToBuy ticket02 = new TicketToBuy(timeTickets.get(0), "D");
        ticket02.addTripItems(generateTripsWithoutUserInformation(0, 2));

        TicketToBuy ticket03 = new TicketToBuy(timeTickets.get(1), "D");
        ticket03.addTripItems(generateTripsWithoutUserInformation(0, 4));

        TicketToBuy ticket04 = new TicketToBuy(timeTickets.get(0), "D");
        ticket04.addTripItems(generateTripsWithoutUserInformation(0, 3));

        ticketToBuyArrayList.add(ticket01);
        ticketToBuyArrayList.add(ticket02);
        ticketToBuyArrayList.add(ticket03);
        ticketToBuyArrayList.add(ticket04);

        ArrayList<TicketToBuy> subList = new ArrayList<>();
        for (int i = startIndex; i < endIndex && i < ticketToBuyArrayList.size(); i++) {
            subList.add(ticketToBuyArrayList.get(i));
        }
        return subList;
    }

    @Test
    public void oneTripNoTicket() {
        ArrayList<TripItem> trips = new ArrayList<>();
        Set<Integer> region1 = new HashSet<>();
        region1.add(170);
        region1.add(260);
        region1.add(350);
        region1.add(340);
        region1.add(440);
        region1.add(430);

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.set(2020, Calendar.MAY, 22, 15, 06);
        endCalendar.set(2020, Calendar.MAY, 22, 15, 6 + 38);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1, 43, region1));

        //einzelne Fahrt - Preisstufe D
        System.gc();
        long start = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticketToBuyArrayList = FarezoneD.optimisation(trips, timeTickets);
        long end = System.currentTimeMillis();
        long s = (end - start) / 1000;
        long ms = (end - start) % 1000;
        Assert.assertEquals(ticketToBuyArrayList.size(), 0);
    }

    @Test
    public void twoTripsOneRegionOneTicket(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        Set<Integer> region1 = new HashSet<>();
        region1.add(170);
        region1.add(260);
        region1.add(350);
        region1.add(340);
        region1.add(440);
        region1.add(430);
        int id = 0;
        //zwei Fahrten innerhalb von 24h - Preisstufe D
        startCalendar.set(2020, Calendar.MAY, 22, 15, 06);
        endCalendar.set(2020, Calendar.MAY, 22, 15, 6 + 38);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++id, 43, region1));

        startCalendar.set(2020, Calendar.MAY, 23, 12, 15);
        endCalendar.set(2020, Calendar.MAY, 23, 12, 15 + 38);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++id, 17, region1));
        System.gc();
        long start = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticketToBuyArrayList = FarezoneD.optimisation(trips, timeTickets);
        long end = System.currentTimeMillis();
        long s = (end - start) / 1000;
        long ms = (end - start) % 1000;
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertTrue(trips.isEmpty());
        //Ein 24h Ticket, alle Fahrten zugeordnet
    }

    @Test
    public void twoTripsTwoRegionOneTicket(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 0;
        Set<Integer> region1 = new HashSet<>();
        region1.add(170);
        region1.add(260);
        region1.add(350);
        region1.add(340);
        region1.add(440);
        region1.add(430);
        Set<Integer> region2 = new HashSet<>();
        region2.add(370);
        region2.add(470);
        region2.add(460);
        region2.add(550);
        region2.add(540);
        region2.add(640);
        region2.add(530);

        //zwei Fahrten innerhalb von 24h - Preisstufe D
        startCalendar.set(2020, Calendar.MAY, 22, 15, 06);
        endCalendar.set(2020, Calendar.MAY, 22, 15, 6 + 38);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++id, 43, region1));

        startCalendar.set(2020, Calendar.MAY, 23, 12, 15);
        endCalendar.set(2020, Calendar.MAY, 23, 12, 15 + 38);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++id, 53, region2));
        System.gc();
        long start = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticketToBuyArrayList = FarezoneD.optimisation(trips, timeTickets);
        long end = System.currentTimeMillis();
        long s = (end - start) / 1000;
        long ms = (end - start) % 1000;
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertTrue(trips.isEmpty());
        //Ein 24h Ticket, alle Fahrten zugeordnet
    }

    @Test
    public void weekTicketRestTrips(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 0;
        Set<Integer> region1 = new HashSet<>();
        region1.add(170);
        region1.add(260);
        region1.add(350);
        region1.add(340);
        region1.add(440);
        region1.add(430);

        for(int i = 1; i < 6; i++){
            startCalendar.set(2020, Calendar.MAY, i, 15, 6);
            endCalendar.set(2020, Calendar.MAY, i, 15, 6 + 38);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++id, 43, region1));

            startCalendar.set(2020, Calendar.MAY, i, 20, 6);
            endCalendar.set(2020, Calendar.MAY, i, 20, 6 + 38);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++id, 43, region1));
        }

        startCalendar.set(2020, Calendar.MAY, 20, 15, 6);
        endCalendar.set(2020, Calendar.MAY, 20, 15, 6 + 38);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++id, 43, region1));

        System.gc();
        long start = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticketToBuyArrayList = FarezoneD.optimisation(trips, timeTickets);
        long end = System.currentTimeMillis();
        long s = (end - start) / 1000;
        long ms = (end - start) % 1000;
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertFalse(trips.isEmpty());
        //Ein 7d Ticket, Fahrt mit der ID 11 ohne Ticket
    }

    @Test
    public void differentFarezonesOneTicket(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 0;
        Set<Integer> regionD = new HashSet<>();
        regionD.add(170);
        regionD.add(260);
        regionD.add(350);
        regionD.add(340);
        regionD.add(440);
        regionD.add(430);
        Set<Integer> regionC = new HashSet<>();
        regionC.add(180);
        regionC.add(150);
        regionC.add(260);
        regionC.add(250);

        startCalendar.set(2020, Calendar.MAY, 7, 15, 6);
        endCalendar.set(2020, Calendar.MAY, 7, 15, 6 + 38);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", ++id, 43, regionC));

        startCalendar.set(2020, Calendar.MAY, 7, 18, 6);
        endCalendar.set(2020, Calendar.MAY, 7, 18, 6 + 38);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", ++id, 43, regionC));

        for(int i = 1; i < 6; i++){
            startCalendar.set(2020, Calendar.MAY, i, 15, 6);
            endCalendar.set(2020, Calendar.MAY, i, 15, 6 + 38);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++id, 43, regionD));

            startCalendar.set(2020, Calendar.MAY, i, 20, 6);
            endCalendar.set(2020, Calendar.MAY, i, 20, 6 + 38);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++id, 43, regionD));
        }

        System.gc();
        long start = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticketToBuyArrayList = FarezoneD.optimisation(trips, timeTickets);
        long end = System.currentTimeMillis();
        long s = (end - start) / 1000;
        long ms = (end - start) % 1000;
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertFalse(trips.isEmpty());
        //Ein 7d Ticket Preisstufe D, alle Fahrten zugeordnet
    }

}
