package com.example.lkjhgf;

import android.app.Activity;

import androidx.test.rule.ActivityTestRule;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.futureTrip.TripListComplete;
import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeOptimisation;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.publicTransport.provider.MyVRRprovider;
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

import de.schildbach.pte.dto.Fare;

public class optimierungsTestD {

    public class TestTripItem extends TripItem {
        Date start;
        Date end;
        String preisstufe;
        int id;

        TestTripItem(Date start, Date end, String preisstufe, int id, HashMap<Fare.Type, Integer> travellingP) {
            super(travellingP);
            this.start = start;
            this.end = end;
            this.preisstufe = preisstufe;
            this.id = id;
        }

        TestTripItem(Date start, Date end, String preisstufe, int id) {
            super(new HashMap<>());
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
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.set(2020, Calendar.MAY, 22, 15, 06);
        endCalendar.set(2020, Calendar.MAY, 22, 15, 6 + 38);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1);

        startCalendar.set(2020, Calendar.MAY, 23, 12, 15);
        endCalendar.set(2020, Calendar.MAY, 23, 12, 15 + 38);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2);

        startCalendar.set(2020, Calendar.MAY, 23, 11, 15);
        endCalendar.set(2020, Calendar.MAY, 23, 11, 15 + 38);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", 3);

        startCalendar.set(2020, Calendar.MAY, 24, 11, 15);
        endCalendar.set(2020, Calendar.MAY, 24, 11, 15 + 38);
        TripItem tripItem04 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 3);

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
    public void test3() {
        ArrayList<TripItem> trips = new ArrayList<>();
        //einzelne Fahrt - Preisstufe D
        trips = generateTripsWithoutUserInformation(0, 1);
        ArrayList<TicketToBuy> ticketToBuyArrayList = TimeOptimisation.optimierungPreisstufeD(trips, timeTickets);
        Assert.assertEquals(ticketToBuyArrayList.size(), 0);

        //zwei Fahrten innerhalb von 24h - Preisstufe D
        trips = generateTripsWithoutUserInformation(0, 2);
        ticketToBuyArrayList = TimeOptimisation.optimierungPreisstufeD(trips, timeTickets);
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
    }

    @Test
    public void test4() {
        ArrayList<TripItem> trips;
        ArrayList<TicketToBuy> ticketToBuyArrayList;
        //Drei Fahrten innerhalb von 24h - 2x D 1x C
        trips = generateTripsWithoutUserInformation(0, 3);
        ticketToBuyArrayList = TimeOptimisation.optimierungPreisstufeD(trips, timeTickets);
        TimeOptimisation.checkTicketForOtherTrips(trips, ticketToBuyArrayList);
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertEquals(ticketToBuyArrayList.get(0).getTripList().size(), 3);

        //Vier Fahrten - 3x innerhalb von 24h - 2x D 1x C - 1x außerhalb A2
        trips = generateTripsWithoutUserInformation(0, 4);
        ticketToBuyArrayList = TimeOptimisation.optimierungPreisstufeD(trips, timeTickets);
        TimeOptimisation.checkTicketForOtherTrips(trips, ticketToBuyArrayList);
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertEquals(ticketToBuyArrayList.get(0).getTripList().size(), 3);
        Assert.assertEquals(trips.size(), 1);
    }

    @Test
    public void test5() {
        ArrayList<TicketToBuy> ticketToBuyArrayList = generateTicketsToBuy(0, 2);
        Assert.assertEquals(ticketToBuyArrayList.size(), 2);
        MyVRRprovider.sumUpTickets(ticketToBuyArrayList);
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertEquals(ticketToBuyArrayList.get(0).getTripList().size(), 3);
    }

    @Test
    public void testMyVrrOptimise() {
        Activity activity = rule.getActivity();
        //Eine Fahrt mit einem Erwachsenen, Preisstufe D
        ArrayList<TripItem> tripItems = generateTripsWithUserInformation(1, 2);
        String id = tripItems.get(0).getTripID();
        HashMap<Fare.Type, ArrayList<TicketToBuy>> tickets01 = MainMenu.myProvider.optimise(tripItems, new HashMap<>(), activity);
        Assert.assertEquals(tripItems.size(), 1);
        Assert.assertEquals(tickets01.get(Fare.Type.ADULT).size(), 1);
        Assert.assertTrue(tickets01.get(Fare.Type.CHILD).isEmpty());
        Assert.assertEquals(tickets01.get(Fare.Type.ADULT).get(0).getTripList().size(), 1);
        Assert.assertEquals(tickets01.get(Fare.Type.ADULT).get(0).getTripList().get(0).getTripID(), id);
        Assert.assertEquals(tickets01.get(Fare.Type.ADULT).get(0).getTicket().getName(), "Einzelticket E");

        //Eine Fahrt mit einem Erwachsenem, einem Kind, Preisstufe D
        HashMap<Fare.Type, Integer> numP01 = new HashMap<>();
        numP01.put(Fare.Type.ADULT, 1);
        numP01.put(Fare.Type.CHILD, 1);
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.set(2020, Calendar.MAY, 23, 12, 15);
        endCalendar.set(2020, Calendar.MAY, 23, 12, 15 + 38);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1, numP01);
        tripItems.clear();
        tripItems.add(tripItem01);

        HashMap<Fare.Type, ArrayList<TicketToBuy>> tickets02 = MainMenu.myProvider.optimise(tripItems, new HashMap<>(), activity);
        Assert.assertEquals(tickets02.get(Fare.Type.ADULT).size(), 1);
        Assert.assertEquals(tickets02.get(Fare.Type.CHILD).size(), 1);
        Assert.assertEquals(tickets02.get(Fare.Type.ADULT).get(0).getTripList().size(), 1);
        Assert.assertEquals(tickets02.get(Fare.Type.CHILD).get(0).getTripList().size(), 1);
        Assert.assertEquals(tickets02.get(Fare.Type.ADULT).get(0).getTicket().getName(), "Einzelticket E");
        Assert.assertEquals(tickets02.get(Fare.Type.CHILD).get(0).getTicket().getName(), "Einzelticket K");

        //Eine Fahrt mit einem Erwachsenem, Preisstufe D
        //Eine Fahrt mit einem Erwachsenem & einem Kind, Preisstufe D
        numP01.put(Fare.Type.ADULT, 1);
        numP01.put(Fare.Type.CHILD, 1);
        HashMap<Fare.Type, Integer> numP02 = new HashMap<>();
        numP02.put(Fare.Type.ADULT, 1);
        numP02.put(Fare.Type.CHILD, 0);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1, numP02);
        startCalendar.set(2020, Calendar.MAY, 23, 15, 6);
        endCalendar.set(2020, Calendar.MAY, 23, 15, 6 + 38);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2, numP01);
        tripItems.clear();
        tripItems.add(tripItem02);
        tripItems.add(tripItem03);
        HashMap<Fare.Type, ArrayList<TicketToBuy>> tickets03 = MainMenu.myProvider.optimise(tripItems, new HashMap<>(), activity);
        Assert.assertEquals(tickets03.get(Fare.Type.ADULT).size(), 1);
        Assert.assertEquals(tickets03.get(Fare.Type.CHILD).size(), 1);
        Assert.assertEquals(tickets03.get(Fare.Type.ADULT).get(0).getTripList().size(), 2);
        Assert.assertEquals(tickets03.get(Fare.Type.CHILD).get(0).getTripList().size(), 1);
        Assert.assertEquals(tickets03.get(Fare.Type.ADULT).get(0).getTicket().getName(), "24-StundenTicket-1");
        Assert.assertEquals(tickets03.get(Fare.Type.CHILD).get(0).getTicket().getName(), "Einzelticket K");
    }

    @Test
    public void testcreateTripListForEachUser() {
        ArrayList<TripItem> trips = generateTripsWithUserInformation(0, 4);
        HashMap<Integer, ArrayList<TripItem>> hashMap = MainMenu.myProvider.createTripListForEachUser(trips, Fare.Type.ADULT);
        Assert.assertEquals(hashMap.keySet().size(), 3);
        Assert.assertEquals(hashMap.get(1).size(), 4);
        Assert.assertEquals(hashMap.get(2).size(), 3);
        Assert.assertEquals(hashMap.get(3).size(), 1);

        ArrayList<TicketToBuy> ticketToBuyArrayList = TimeOptimisation.optimierungPreisstufeD(hashMap.get(1), timeTickets);
        TimeOptimisation.checkTicketForOtherTrips(hashMap.get(1), ticketToBuyArrayList);
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertEquals(ticketToBuyArrayList.get(0).getTripList().size(), 3);
        Assert.assertEquals(hashMap.get(1).size(), 1);
    }

    @Test
    public void testVierStundenTicketValid() {
        TimeTicket vierHTicket = new TimeTicket(new int[]{720, 720, 720, 720, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, "4-StundenTicket-1", Fare.Type.ADULT, 4 * 60 * 60 * 1000, new int[]{3, 2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, 9, 3, true, 1);

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.set(2020, Calendar.JUNE, 2, 15, 06);
        endCalendar.set(2020, Calendar.JUNE, 2, 15, 6 + 38);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 8, 40);
        endCalendar.set(2020, Calendar.JUNE, 2, 9, 6);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 6, 8, 06);
        endCalendar.set(2020, Calendar.JUNE, 6, 10, 6 + 38);
        TripItem tripItem04 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 3, 1, 6 + 38);
        TripItem tripItem05 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 2, 23, 6 + 38);
        TripItem tripItem06 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 3, 3, 1);
        TripItem tripItem07 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        Assert.assertFalse(vierHTicket.isValidTrip(tripItem01));
        Assert.assertTrue(vierHTicket.isValidTrip(tripItem02));
        Assert.assertFalse(vierHTicket.isValidTrip(tripItem03));
        Assert.assertTrue(vierHTicket.isValidTrip(tripItem04));
        Assert.assertTrue(vierHTicket.isValidTrip(tripItem05));
        Assert.assertTrue(vierHTicket.isValidTrip(tripItem06));
        Assert.assertFalse(vierHTicket.isValidTrip(tripItem07));
    }

    @Test
    public void testHappyHourTicketValidTrip() {
        TimeTicket happyHourTicket = new TimeTicket(new int[]{1370, 1370, 1370, 1370, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, "Happy Hour Ticket", Fare.Type.ADULT, 12 * 60 * 60 * 1000, new int[]{2, 2, 2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, 18, 6, false, 1);

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.set(2020, Calendar.JUNE, 2, 15, 06);
        endCalendar.set(2020, Calendar.JUNE, 2, 15, 6 + 38);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 18, 40);
        endCalendar.set(2020, Calendar.JUNE, 2, 19, 6);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 6, 8, 06);
        endCalendar.set(2020, Calendar.JUNE, 7, 10, 6 + 38);
        TripItem tripItem04 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 6, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 7, 1, 6 + 38);
        TripItem tripItem05 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 3, 5, 6 + 38);
        TripItem tripItem06 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 3, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 5, 3, 1);
        TripItem tripItem07 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        Assert.assertFalse(happyHourTicket.isValidTrip(tripItem01));
        Assert.assertFalse(happyHourTicket.isValidTrip(tripItem02));
        Assert.assertTrue(happyHourTicket.isValidTrip(tripItem03));
        Assert.assertFalse(happyHourTicket.isValidTrip(tripItem04));
        Assert.assertTrue(happyHourTicket.isValidTrip(tripItem05));
        Assert.assertTrue(happyHourTicket.isValidTrip(tripItem06));
        Assert.assertFalse(happyHourTicket.isValidTrip(tripItem07));
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
        Assert.assertEquals(ticketResult.get(Fare.Type.ADULT).size(),1);
        Assert.assertEquals(ticketResult.get(Fare.Type.ADULT).get(0).getTicket().getName(), "10er-Ticket E");

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

    @Test
    public void loeschen_einer_fahrt_zukuenftigeTickets(){
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        HashMap<Fare.Type, Integer> numP = new HashMap<>();
        numP.put(Fare.Type.CHILD, 0);
        numP.put(Fare.Type.ADULT, 1);

        startCalendar.set(2020, Calendar.JUNE, 22, 15, 06);
        endCalendar.set(2020, Calendar.MAY, Calendar.JUNE, 15, 6 + 38);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", 1, numP);
        trips.add(tripItem01);

        startCalendar.set(2020, Calendar.JUNE, 24, 15, 06);
        endCalendar.set(2020, Calendar.JUNE, 24, 15, 6 + 38);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2, numP);
        trips.add(tripItem02);

        startCalendar.set(2020, Calendar.JUNE, 25, 10, 06);
        endCalendar.set(2020, Calendar.JUNE, 25, 10, 6 + 38);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 3, numP);
        trips.add(tripItem03);



    }

    @Test
    public void testNewOpti(){
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.set(2020, Calendar.JUNE, 20, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1));

        startCalendar.set(2020, Calendar.JUNE, 20, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2));

        startCalendar.set(2020, Calendar.JUNE, 20, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 3));

        ArrayList<TicketToBuy> tickets = TimeOptimisation.optimierungPreisstufeDNew(trips, timeTickets);
        Assert.assertEquals(tickets.size(), 1);
        Assert.assertEquals(tickets.get(0).getTripList().size(), 3);
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "24-StundenTicket-1");

        trips.clear();
        tickets.clear();

        startCalendar.set(2020, Calendar.JUNE, 22, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1));

        startCalendar.set(2020, Calendar.JUNE, 22, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2));

        startCalendar.set(2020, Calendar.JUNE, 22, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 3));


        startCalendar.set(2020, Calendar.JUNE, 24, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 4));

        startCalendar.set(2020, Calendar.JUNE, 24, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 5));

        startCalendar.set(2020, Calendar.JUNE, 24, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 6));


        startCalendar.set(2020, Calendar.JUNE, 26, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 26, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 7));

        startCalendar.set(2020, Calendar.JUNE, 26, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 26, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 8));

        startCalendar.set(2020, Calendar.JUNE, 26, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 26, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 9));


        startCalendar.set(2020, Calendar.JUNE, 29, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 10));

        startCalendar.set(2020, Calendar.JUNE, 29, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 11));

        startCalendar.set(2020, Calendar.JUNE, 29, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 12));


        startCalendar.set(2020, Calendar.JUNE, 30, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 30, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 13));

        startCalendar.set(2020, Calendar.JUNE, 30, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 30, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 14));

        startCalendar.set(2020, Calendar.JUNE, 30, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 30, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 15));

        System.gc();
        long start = System.currentTimeMillis();
        tickets = TimeOptimisation.optimierungPreisstufeDNew(trips, timeTickets);
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
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 1, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 1, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 1, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 1, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));


        startCalendar.set(2020, Calendar.JUNE, 3, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 3, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 3, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 3, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 3, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 3, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));


        startCalendar.set(2020, Calendar.JUNE, 5, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 5, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 5, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 5, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 5, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 5, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));


        startCalendar.set(2020, Calendar.JUNE, 15, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 15, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 15, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 15, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 15, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 15, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));


        startCalendar.set(2020, Calendar.JUNE, 22, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 22, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 22, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 22, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));


        startCalendar.set(2020, Calendar.JUNE, 24, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 24, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 24, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 24, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));


        startCalendar.set(2020, Calendar.JUNE, 29, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 29, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JUNE, 29, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 29, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));


        startCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 1, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));


        startCalendar.set(2020, Calendar.JULY, 6, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 6, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 6, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 6, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 6, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 6, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));


        startCalendar.set(2020, Calendar.JULY, 13, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 13, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 13, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 13, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 13, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 13, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));


        startCalendar.set(2020, Calendar.JULY, 20, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 20, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 20, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 20, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 20, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 20, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 27, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 27, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 27, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 27, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 27, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 27, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));


        startCalendar.set(2020, Calendar.JULY, 29, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 29, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 29, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 29, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 29, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 29, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 31, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 31, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 31, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 31, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        startCalendar.set(2020, Calendar.JULY, 31, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 31, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", ++i));

        System.out.println(i);
        System.gc();
        start = System.currentTimeMillis();
        tickets = TimeOptimisation.optimierungPreisstufeDNew(trips, timeTickets);
        ende = System.currentTimeMillis();
        sekunden = (ende-start) / 1000;
        msekunden = (ende-start)%1000;
        Assert.assertFalse(tickets.isEmpty());
    }

    @Test
    public void testRemoveOverlappingTrips(){
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 1;
        startCalendar.set(2020, Calendar.JUNE, 20, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 10,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        startCalendar.set(2020, Calendar.JUNE, 20, 10, 5);
        endCalendar.set(2020, Calendar.JUNE, 20, 10,35);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        UtilsList.removeOverlappingTrips(trips);
        Assert.assertFalse(trips.isEmpty());
        Assert.assertEquals(trips.size(), 1);
        Assert.assertEquals(trips.get(0).getTripID(), "1");

        trips.clear();
        id = 1;
        startCalendar.set(2020, Calendar.JUNE, 20, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        startCalendar.set(2020, Calendar.JUNE, 20, 11, 15);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,20);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        UtilsList.removeOverlappingTrips(trips);
        Assert.assertFalse(trips.isEmpty());
        Assert.assertEquals(trips.size(), 1);
        Assert.assertEquals(trips.get(0).getTripID(), "1");

        trips.clear();
        id = 1;
        startCalendar.set(2020, Calendar.JUNE, 20, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        startCalendar.set(2020, Calendar.JUNE, 20, 11, 15);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,20);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        startCalendar.set(2020, Calendar.JUNE, 20, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 20, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        startCalendar.set(2020, Calendar.JUNE, 20, 13, 15);
        endCalendar.set(2020, Calendar.JUNE, 20, 13,55);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        startCalendar.set(2020, Calendar.JUNE, 20, 13, 20);
        endCalendar.set(2020, Calendar.JUNE, 20, 13,54);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

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
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        startCalendar.set(2020, Calendar.JUNE, 20, 11, 15);
        endCalendar.set(2020, Calendar.JUNE, 20, 11,20);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        startCalendar.set(2020, Calendar.JUNE, 20, 11, 20);
        endCalendar.set(2020, Calendar.JUNE, 20, 12,30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        startCalendar.set(2020, Calendar.JUNE, 20, 13, 15);
        endCalendar.set(2020, Calendar.JUNE, 20, 13,55);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        startCalendar.set(2020, Calendar.JUNE, 20, 13, 20);
        endCalendar.set(2020, Calendar.JUNE, 20, 13,54);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", id++));

        UtilsList.removeOverlappingTrips(trips);
        Assert.assertFalse(trips.isEmpty());
        Assert.assertEquals(trips.size(),2);

    }

}
