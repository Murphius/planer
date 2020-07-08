package com.example.lkjhgf;

import androidx.test.rule.ActivityTestRule;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.optimisation.timeOptimisation.vrr.FarezoneB;
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

public class OptimierungsTestB {

    public class TestTripItem extends TripItem {
        Date start;
        Date end;
        String preisstufe;
        int id;

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
        Set<Integer> f1 = new HashSet<>();
        f1.add(176);
        f1.add(178);
        f1.add(154);
        f1.add(150);
        Set<Integer> f2 = new HashSet<>();
        f2.add(150);
        f2.add(152);
        f2.add(180);
        Set<Integer> f3 = new HashSet<>();
        f3.add(180);
        f3.add(170);
        f3.add(176);


        startCalendar.set(2020, Calendar.JULY, 1, 9, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 170, f1)); //Herten -> Marl

        startCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 150, f2)); //Marl -> Datteln

        startCalendar.set(2020, Calendar.JULY, 1, 12, 30);
        endCalendar.set(2020, Calendar.JULY, 1, 13, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 180, f3)); //Datteln -> Herten
        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneB.optimisation(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(tickets.isEmpty());
        Assert.assertEquals(tickets.size(), 1);
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "24-StundenTicket-1");
        //Lösung: 24h Ticket der Preisstufe B mit dem Zentralgebiet 6
    }

    @Test
    public void tripsOneRegionTwoDay() {
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 1;
        //Herten <-> Marl
        Set<Integer> f1 = new HashSet<>();
        f1.add(176);
        f1.add(178);
        f1.add(154);
        f1.add(150);
        //Datteln <-> Marl
        Set<Integer> f2 = new HashSet<>();
        f2.add(150);
        f2.add(152);
        f2.add(180);
        //Datteln <-> Herten
        Set<Integer> f3 = new HashSet<>();
        f3.add(180);
        f3.add(170);
        f3.add(176);
        startCalendar.set(2020, Calendar.JULY, 1, 9, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 170, f1)); //Herten -> Marl

        startCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 150, f2)); //Marl -> Datteln

        startCalendar.set(2020, Calendar.JULY, 1, 12, 30);
        endCalendar.set(2020, Calendar.JULY, 1, 13, 00);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 180, f3)); //Datteln -> Herten

        startCalendar.set(2020, Calendar.JULY, 2, 9, 1);
        endCalendar.set(2020, Calendar.JULY, 2, 10, 1);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 170, f1)); //Herten -> Marl

        startCalendar.set(2020, Calendar.JULY, 2, 11, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 150, f2)); //Marl -> Datteln

        startCalendar.set(2020, Calendar.JULY, 2, 12, 30);
        endCalendar.set(2020, Calendar.JULY, 2, 13, 00);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", id++, 180, f3)); //Datteln -> Herten

        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneB.optimisation(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(tickets.isEmpty());
        Assert.assertEquals(tickets.size(), 1);
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "48-StundenTicket-1");
        //Lösung: 48-h Ticket mit dem Zentralgebiet 17 (Herten/Recklinghausen)
        //Alle Fahrten diesem Ticket zugewiesen
    }

    @Test
    public void tripsOneRegionWeekTicket() {
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 0;
        Set<Integer> dattelnRE = new HashSet<>();
        dattelnRE.add(180);
        dattelnRE.add(170);
        dattelnRE.add(184);
        Set<Integer> gelsenkirchenBottrop = new HashSet<>();
        gelsenkirchenBottrop.add(264);
        gelsenkirchenBottrop.add(252);
        gelsenkirchenBottrop.add(254);
        gelsenkirchenBottrop.add(260);

        startCalendar.set(2020, Calendar.JUNE, 1, 10, 0);
        endCalendar.set(2020, Calendar.JUNE, 1, 10, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 170, dattelnRE));//RE -> Datteln

        startCalendar.set(2020, Calendar.JUNE, 1, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 1, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 180, dattelnRE));//Datteln -> RE

        startCalendar.set(2020, Calendar.JUNE, 1, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 1, 12, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 260, gelsenkirchenBottrop));//GE -> BOT

        startCalendar.set(2020, Calendar.JUNE, 3, 10, 5);
        endCalendar.set(2020, Calendar.JUNE, 3, 10, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 170, dattelnRE));//RE -> Datteln

        startCalendar.set(2020, Calendar.JUNE, 3, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 3, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 180, dattelnRE));//Datteln -> RE

        startCalendar.set(2020, Calendar.JUNE, 3, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 3, 12, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 260, gelsenkirchenBottrop));//GE -> BOT

        startCalendar.set(2020, Calendar.JUNE, 5, 10, 15);
        endCalendar.set(2020, Calendar.JUNE, 5, 10, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 170, dattelnRE));//RE -> Datteln

        startCalendar.set(2020, Calendar.JUNE, 5, 11, 0);
        endCalendar.set(2020, Calendar.JUNE, 5, 11, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 180, dattelnRE));//Datteln -> RE

        startCalendar.set(2020, Calendar.JUNE, 5, 12, 0);
        endCalendar.set(2020, Calendar.JUNE, 5, 12, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 260, gelsenkirchenBottrop));//GE -> BOT

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticketList = FarezoneB.optimisation(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(ticketList.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(ticketList.size(), 1);
        Assert.assertEquals(ticketList.get(0).getTicket().getName(), "7-TageTicket");
        //Lösung: 7 TagesTicket für alle Fahrten mit dem Zentralgebiet 15 Marl
    }

    @Test
    public void tripsOneRegionDifferentTickets() {
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 0;
        Set<Integer> dattelnRE = new HashSet<>();
        dattelnRE.add(180);
        dattelnRE.add(170);
        dattelnRE.add(184);
        Set<Integer> halternDorsten = new HashSet<>();
        halternDorsten.add(50);
        halternDorsten.add(58);
        halternDorsten.add(57);
        halternDorsten.add(59);
        halternDorsten.add(69);
        halternDorsten.add(60);
        Set<Integer> dattelnMarl = new HashSet<>();
        dattelnMarl.add(180);
        dattelnMarl.add(152);
        dattelnMarl.add(150);
        Set<Integer> dorstenRE = new HashSet<>();
        dorstenRE.add(50);
        dorstenRE.add(54);
        dorstenRE.add(150);
        dorstenRE.add(170);


        for (int i = 1; i <= 15; i++) {
            startCalendar.set(2020, Calendar.JULY, i * 2, 10, 0);
            endCalendar.set(2020, Calendar.JULY, i * 2, 10, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 180, dattelnRE));

            startCalendar.set(2020, Calendar.JULY, i * 2, 11, 0);
            endCalendar.set(2020, Calendar.JULY, i * 2, 11, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 60, halternDorsten));

            startCalendar.set(2020, Calendar.JULY, i * 2, 12, 0);
            endCalendar.set(2020, Calendar.JULY, i * 2, 12, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 50, dorstenRE));
        }
        for (int i = 1; i <= 15; i++) {
            startCalendar.set(2020, Calendar.AUGUST, i * 2, 10, 0);
            endCalendar.set(2020, Calendar.AUGUST, i * 2, 10, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 180, dattelnMarl));

            startCalendar.set(2020, Calendar.AUGUST, i * 2, 11, 0);
            endCalendar.set(2020, Calendar.AUGUST, i * 2, 11, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 60, halternDorsten));

            startCalendar.set(2020, Calendar.AUGUST, i * 2, 12, 0);
            endCalendar.set(2020, Calendar.AUGUST, i * 2, 12, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 50, dorstenRE));
        }
        trips.sort(Comparator.comparing(TripItem::getFirstDepartureTime));

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticketList = FarezoneB.optimisation(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(ticketList.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(ticketList.size(), 2);
        //Lösung: zwei 30 Tage Tickets
        //Fahrten mit der ID 1-45 gehören dem ersten Ticket an (Zentralregion 15 - Marl)
        //Fahrten mit der ID 45-90 gehören dem zweiten Ticket an (Zentralregion 15 - Marl)
    }

    @Test
    public void oneRegion7DayTicketRestTrips(){
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 0;
        HashSet<Integer> marlBottrop = new HashSet<>();
        marlBottrop.add(150);
        marlBottrop.add(250);
        marlBottrop.add(252);
        marlBottrop.add(268);
        marlBottrop.add(154);
        HashSet<Integer> dorstenBochum = new HashSet<>();
        dorstenBochum.add(50);
        dorstenBochum.add(54);
        dorstenBochum.add(56);
        dorstenBochum.add(154);
        dorstenBochum.add(178);
        dorstenBochum.add(272);
        dorstenBochum.add(360);
        for (int i = 1; i <= 6; i++) {
            if(i%2==0){
                //15 - Marl
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 250, marlBottrop));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 250, marlBottrop));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 250, marlBottrop));
            }else{
                //17 - RE
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 50, dorstenBochum));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 50, dorstenBochum));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 50, dorstenBochum));
            }
        }
        for(int i = 8; i <= 10; i++){
            if(i%2==0){
                //15 - Marl
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 250, marlBottrop));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 250, marlBottrop));
            }else{
                //17 - RE
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 50, dorstenBochum));
            }
        }
        System.gc();
        long startT = System.currentTimeMillis();
        trips.sort(Comparator.comparing(TripItem::getFirstDepartureTime));
        ArrayList<TicketToBuy> ticketList = FarezoneB.optimisation(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(ticketList.isEmpty());
        Assert.assertFalse(trips.isEmpty());
        Assert.assertEquals(ticketList.size(), 1);
        //Lösung: 1 7Tage Ticket für die Fahrten mit den IDs 1-18 (Zentralregion 26 - Gelsenkrichen)
        //Restfahrten: Fahrten mit den IDs 19-23
    }

    @Test
    public void differentRegions7DayTicket1(){
        //Nicht überlappende Geltungsbereiche
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 0;
        HashSet<Integer> region1 = new HashSet<>();
        region1.add(250);
        region1.add(252);
        region1.add(268);
        region1.add(154);
        region1.add(150);
        HashSet<Integer> region2 = new HashSet<>();
        region2.add(644);
        region2.add(640);
        region2.add(530);

        for (int i = 1; i <= 6; i++) {
            if(i%2==0){
                //15 Marl
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 250, region1));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 250, region1));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 250, region1));
            }else{
                //17 - RE
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 644, region2));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 644, region2));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 644, region2));
            }
        }
        System.gc();
        long startT = System.currentTimeMillis();
        trips.sort(Comparator.comparing(TripItem::getFirstDepartureTime));
        ArrayList<TicketToBuy> ticketList = FarezoneB.optimisation(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(ticketList.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(ticketList.size(), 2);
        //Lösung: zwei 7 Ttagestickets
        //Fahrten mit den IDs 4,5,6,10,11,12,16,17,18 sind dem Ticket mit der Zentralreion 25 (Bottrop)
        //Fahrtenn mit den IDs 1,2,3,7,8,9,13,14,15 sind dem Ticket mit der Zentralregion 64 (Hilden) zugeordnet
    }

    @Test
    public void differentRegion7DayTicket2(){
        //Überlappende Regionen
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 0;
        //Region 1: Fahrten von 15 (Dorsten) nach 18 (Datteln)
        HashSet<Integer> region1 = new HashSet<>();
        region1.add(150);
        region1.add(152);
        region1.add(180);
        region1.add(182);
        //Region 2: Fahrten von 36 (Bochum) nach 46 (Hattingen)
        HashSet<Integer> region2 = new HashSet<>();
        region2.add(360);
        region2.add(366);
        region2.add(460);
        region2.add(462);
        for (int i = 1; i <= 6; i++) {
            if(i%2==0){
                //Region 1
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 150, region1));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 150, region1));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 150, region1));
            }else{
                //Region 2
                startCalendar.set(2020, Calendar.JULY, i , 10, 0);
                endCalendar.set(2020, Calendar.JULY, i, 10, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 360, region2));

                startCalendar.set(2020, Calendar.JULY, i, 11, 0);
                endCalendar.set(2020, Calendar.JULY, i, 11, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 360, region2));

                startCalendar.set(2020, Calendar.JULY, i , 12, 0);
                endCalendar.set(2020, Calendar.JULY, i , 12, 30);
                trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 360, region2));
            }
        }

        System.gc();
        long startT = System.currentTimeMillis();
        trips.sort(Comparator.comparing(TripItem::getFirstDepartureTime));
        ArrayList<TicketToBuy> ticketList = FarezoneB.optimisation(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(ticketList.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(ticketList.size(), 2);
        //Lösung: 2 7Tages Ticket
        //Fahrten mit den IDs 4,5,6,10,11,12,16,17,18 sind dem Ticket mit dem Zentralgebiet 6 zugeordnet
        //Fahrten mit ungerader ID 1,2,3,7,8,9,13,14,15 sind dem Ticket mit dem Zentralgebiet 46 zugeordnet
    }

    @Test
    public void testOneRegionDayTicketTripWithLowerFarezone(){
        //24h Fahrten mit der Preisstufe B, sowie einer Fahrt der Preisstufe A2 in diesem Geltungsbereich
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        int id = 0;
        HashSet<Integer> t1 = new HashSet<>();
        t1.add(176);
        t1.add(272);
        t1.add(360);
        //Region 2: Fahrten von 36 (Bochum) nach 46 (Hattingen)
        HashSet<Integer> t2 = new HashSet<>();
        t2.add(360);
        t2.add(362);
        t2.add(370);
        t2.add(372);
        t2.add(374);
        t2.add(376);
        HashSet<Integer> t3 = new HashSet<>();
        t3.add(176);
        t3.add(270);
        t3.add(272);
        t3.add(370);
        t3.add(377);
        HashSet<Integer> t4 = new HashSet<>();
        t4.add(176);
        t4.add(170);

        startCalendar.set(2020, Calendar.JULY, 2 , 10, 0);
        endCalendar.set(2020, Calendar.JULY, 2 , 10, 15);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", ++id, 176, t4));

        startCalendar.set(2020, Calendar.JULY, 1 , 12, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 13, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 176, t1));

        startCalendar.set(2020, Calendar.JULY, 1, 15, 43);
        endCalendar.set(2020, Calendar.JULY, 1, 16, 00);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 360, t2));

        startCalendar.set(2020, Calendar.JULY, 2 , 7, 0);
        endCalendar.set(2020, Calendar.JULY, 2 , 8, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "B", ++id, 370, t3));

        System.gc();
        long start = System.currentTimeMillis();
        ArrayList<TicketToBuy> ticket = FarezoneB.optimisation(trips, timeTickets);
        long end = System.currentTimeMillis();
        long s = (end-start)/1000;
        long ms = (end-start)%1000;
        Assert.assertFalse(ticket.isEmpty());
        Assert.assertTrue(trips.isEmpty());
    }

}
