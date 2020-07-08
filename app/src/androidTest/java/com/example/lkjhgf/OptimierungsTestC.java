package com.example.lkjhgf;

import androidx.test.rule.ActivityTestRule;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.optimisation.timeOptimisation.vrr.FarezoneC;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.schildbach.pte.dto.Fare;

public class OptimierungsTestC {
    public class TestTripItem extends TripItem {
        Date start;
        Date end;
        String preisstufe;
        int id;

        TestTripItem(Date start, Date end, String preisstufe, int id, int startID, Set<Integer> crossedRegions) {
            super(crossedRegions, startID);
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
        timeTickets.add(new TimeTicket(new int[]{2295, 2295, 2815, 2950, 4275, 5730, 7240}, "7-TageTicket", Fare.Type.ADULT, 7 * 24 * 60 * 60 * 1000, new int[]{16, 11, 12, 13, 8, 5, 5}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{7120, 7120, 7560, 7920, 11355, 15355, 19390}, "30-TageTicket", Fare.Type.ADULT, 30L * 24L * 60L * 60L * 1000L, new int[]{51, 31, 33, 34, 24, 16, 17}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{1370, 1370, 1370, 1370, 2790, 4810, 5780}, "48-StundenTicket-1", Fare.Type.ADULT, 48 * 60 * 60 * 1000, new int[]{9, 6, 6, 6, 5, 5, 5}, 0, 24, false, 1));
    }

    @Test
    public void tripsOneRegionDayTicket() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;

        startCalendar.set(2020, Calendar.JULY, 1, 9, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 10, 30);
        Set<Integer> integers = new HashSet<>();
        integers.add(144);
        integers.add(141);
        integers.add(50);
        integers.add(54);
        integers.add(268);
        integers.add(262);
        integers.add(272);
        integers.add(360);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", id++, 140,integers)); //Hünxe -> Bochum

        startCalendar.set(2020, Calendar.JULY, 1, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", id++, 360, integers)); //Bochum  -> MH ad Ruhr

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneC.optimieriungPreisstufeC(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(tickets.isEmpty());
        Assert.assertEquals(tickets.size(), 1);
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "24-StundenTicket-1");
        //Lösung: 24h Ticket Region 7, alle Fahrten zugeordnet
    }

    @Test
    public void tripsOneRegionTwoDay() {
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Set<Integer> integers = new HashSet<>();
        integers.add(144);
        integers.add(141);
        integers.add(50);
        integers.add(54);
        integers.add(268);
        integers.add(262);
        integers.add(272);
        integers.add(360);
        integers.add(180);
        int id = 1;

        startCalendar.set(2020, Calendar.JULY, 1, 9, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        trips.add(new  TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", id++, 144, integers));

        startCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 30);
        trips.add(new  TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", id++, 50, integers));

        startCalendar.set(2020, Calendar.JULY, 1, 12, 30);
        endCalendar.set(2020, Calendar.JULY, 1, 13, 00);
        trips.add(new  TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", id++, 262, integers));

        startCalendar.set(2020, Calendar.JULY, 2, 9, 1);
        endCalendar.set(2020, Calendar.JULY, 2, 10, 1);
        trips.add(new  TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", id++, 360, integers));

        startCalendar.set(2020, Calendar.JULY, 2, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 11, 30);
        trips.add(new  TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", id++, 54, integers)); //Marl -> Datteln

        startCalendar.set(2020, Calendar.JULY, 2, 12, 30);
        endCalendar.set(2020, Calendar.JULY, 2, 13, 00);
        trips.add(new  TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", id++, 180, integers)); //Datteln -> Herten

        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneC.optimieriungPreisstufeC(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(tickets.size(), 1);
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "48-StundenTicket-1");
        //Lösung: 48h Ticket mit der Region 7, alle Fahrten zugeordnet
    }

    @Test
    public void oneRegion7DayTicketNoRestTrips(){
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Set<Integer> integers1 = new HashSet<>();
        integers1.add(144);
        integers1.add(141);
        integers1.add(50);
        integers1.add(54);
        integers1.add(268);
        integers1.add(262);
        integers1.add(272);
        integers1.add(360);
        Set<Integer> integers2 = new HashSet<>();
        integers2.add(250);
        integers2.add(264);
        integers2.add(262);
        integers2.add(176);
        integers2.add(174);
        integers2.add(282);
        integers2.add(290);
        int id = 0;
        for (int i = 1; i <= 6; i++) {
            if(i%2==0){
                //15 - Marl
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 140, integers1));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 140, integers1));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 140, integers1));
            }else{
                //17 - RE
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 250, integers2));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 250, integers2));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 250, integers2));
            }
        }
        System.gc();
        long startT = System.currentTimeMillis();
        trips.sort(Comparator.comparing(TripItem::getFirstDepartureTime));
        ArrayList<TicketToBuy> ticketList = FarezoneC.optimieriungPreisstufeC(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(ticketList.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(ticketList.size(), 1);
        //Lösung: 7 Tage Ticket Region 7, alle Fahrten zugeordnet
    }

    @Test
    public void oneRegion7DayTicketNoRestTrips2(){
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 0;
        Set<Integer> integers1 = new HashSet<>();
        integers1.add(144);
        integers1.add(141);
        integers1.add(50);
        integers1.add(54);
        integers1.add(268);
        integers1.add(262);
        integers1.add(272);
        integers1.add(360);
        Set<Integer> integers2 = new HashSet<>();
        integers2.add(250);
        integers2.add(264);
        integers2.add(262);
        integers2.add(176);
        integers2.add(174);
        integers2.add(282);
        integers2.add(290);
        for (int i = 1; i <= 6; i++) {
            if(i%2==0){
                //15 - Marl
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 260, integers1));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 260, integers1));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 260, integers1));
            }else{
                //17 - RE
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 60, integers2));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 60, integers2));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 60, integers2));
            }
        }
        System.gc();
        long startT = System.currentTimeMillis();
        trips.sort(Comparator.comparing(TripItem::getFirstDepartureTime));
        ArrayList<TicketToBuy> ticketList = FarezoneC.optimieriungPreisstufeC(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(ticketList.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(ticketList.size(), 1);
        //Lösung: 7 Tage Ticket Region 7, alle Fahrten zugeordnet
    }

    @Test
    public void twoRegion7DayTicketOneRestTrip(){
        //Wochenticket innerhalb einer Region, einzelne Fahrt außerhalb dieser Region
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 0;
        Set<Integer> integers1 = new HashSet<>();
        integers1.add(144);
        integers1.add(141);
        integers1.add(50);
        integers1.add(54);
        integers1.add(268);
        integers1.add(262);
        integers1.add(272);
        integers1.add(360);
        Set<Integer> integers2 = new HashSet<>();
        integers2.add(250);
        integers2.add(264);
        integers2.add(262);
        integers2.add(176);
        integers2.add(174);
        integers2.add(282);
        integers2.add(290);
        Set<Integer> integers3 = new HashSet<>();
        integers3.add(133);
        integers3.add(100);
        integers3.add(40);
        integers3.add(20);
        integers3.add(120);

        for (int i = 1; i <= 6; i++) {
            if(i%2==0){
                //15 - Marl
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 260, integers1));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 260, integers1));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 260, integers1));
            }else{
                //17 - RE
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 60, integers2));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 60, integers2));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 60, integers2));
            }
        }
        startCalendar.set(2020, Calendar.JULY, 6 , 18, 0);
        endCalendar.set(2020, Calendar.JULY, 6 , 18, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(),  "C", ++id, 133, integers3));
        trips.sort(Comparator.comparing(TripItem::getFirstDepartureTime));
        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticketList = FarezoneC.optimieriungPreisstufeC(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(ticketList.isEmpty());
        Assert.assertFalse(trips.isEmpty());
        Assert.assertEquals(trips.size(), 1);
        Assert.assertEquals(ticketList.size(), 1);
        //Lösung: Fahrt mit der ID 19 hat kein Ticket
        //Alle anderen Fahrten sind einem 7Tage Ticket mit der Zentralregion 7 zugewiesen
    }


}
