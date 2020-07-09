package com.example.lkjhgf;

import androidx.test.rule.ActivityTestRule;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.TicketToBuyTimeComporator;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.optimisation.timeOptimisation.vrr.FarezoneA;
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
import java.util.HashSet;
import java.util.Set;

import de.schildbach.pte.dto.Fare;

public class OptimierungsTestA3 {

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
        timeTickets.add(new TimeTicket(new int[]{319, 319, 319, 319, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, "Happy Hour Ticket", Fare.Type.ADULT, 12 * 60 * 60 * 1000, new int[]{3, 2, 2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, 18, 6, false, 1));
        timeTickets.add(MyVRRprovider.vierStundenTicket);
        timeTickets.add(new TimeTicket(new int[]{720, 720, 720, 720, 1470, 2530, 3040}, "24-StundenTicket-1", Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{5, 3, 3, 3, 3, 2, 2}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{2295, 2295, 2815, 2950, 4275, 5730, 7240}, "7-TageTicket", Fare.Type.ADULT, 7 * 24 * 60 * 60 * 1000, new int[]{16, 11, 12, 13, 8, 5, 5}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{7120, 7120, 7560, 7920, 11355, 15355, 19390}, "30-TageTicket", Fare.Type.ADULT, 30L * 24L * 60L * 60L * 1000L, new int[]{51, 31, 33, 34, 24, 16, 17}, 0, 24, false, 1));
        timeTickets.add(new TimeTicket(new int[]{1370, 1370, 1370, 1370, 2790, 4810, 5780}, "48-StundenTicket-1", Fare.Type.ADULT, 48 * 60 * 60 * 1000, new int[]{9, 6, 6, 6, 5, 5, 5}, 0, 24, false, 1));
    }

    @Test
    public void oneTarifgebietHappyHTicket() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;

        Set<Integer> bochum = new HashSet<>();
        bochum.add(360);
        bochum.add(362);
        bochum.add(368);

        startCalendar.set(2020, Calendar.JULY, 1, 18, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 19, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 362, bochum));

        startCalendar.set(2020, Calendar.JULY, 1, 20, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 20, 15);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 368, bochum));

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertFalse(tickets.isEmpty());
        Assert.assertEquals(tickets.size(), 1);
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "Happy Hour Ticket");
        //Lösung: Happy Hour Ticket Tarifgebiet Bochum, alle Fahrten zugeordnet
    }

    @Test
    public void oneTarifgebietNoTicket() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;

        Set<Integer> bochum = new HashSet<>();
        bochum.add(360);
        bochum.add(362);
        bochum.add(368);

        startCalendar.set(2020, Calendar.JULY, 1, 18, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 19, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 362, bochum));

        startCalendar.set(2020, Calendar.JULY, 2, 5, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 6, 15);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 368, bochum));

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets =  FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Assert.assertTrue(tickets.isEmpty());
        Assert.assertFalse(trips.isEmpty());
        //Lösung: Kein Ticket, beide Fahrten ohne Ticket
    }

    @Test
    public void twoWabenHappyHourTicket() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;
        Set<Integer> bochumHerne = new HashSet<>();
        bochumHerne.add(360);
        bochumHerne.add(272);

        startCalendar.set(2020, Calendar.JULY, 1, 18, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 19, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 20, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 20, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets =  FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "Happy Hour Ticket");
        //Lösung: Happy Hour Ticket, Zwei Waben Tarif Herne - Bochum, alle Fahrten zugewiesen
        //keine Fahrten ohne Ticket
    }

    @Test
    public void twoWabenHappyHourTicket_Night() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;
        Set<Integer> bochumHerne = new HashSet<>();
        bochumHerne.add(360);
        bochumHerne.add(272);

        startCalendar.set(2020, Calendar.JULY, 1, 23, 45);
        endCalendar.set(2020, Calendar.JULY, 2, 0, 15);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 2, 2, 15);
        endCalendar.set(2020, Calendar.JULY, 2, 2, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets =  FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "Happy Hour Ticket");
        //Lösung: Happy Hour Ticket, Zwei Waben Tarif (272 Herne - 360 Bochum), alle Fahrten zugewiesen
        //keine Fahrten ohne Ticket
    }

    @Test
    public void threeWabenOneTicket() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;
        Set<Integer> bochumHerne = new HashSet<>();
        bochumHerne.add(360);
        bochumHerne.add(272);
        Set<Integer> herneHerten = new HashSet<>();
        herneHerten.add(272);
        herneHerten.add(176);


        startCalendar.set(2020, Calendar.JULY, 1, 18, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 19, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 20, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 20, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 22, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 22, 45);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, herneHerten));

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertFalse(trips.isEmpty());
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "Happy Hour Ticket");
        //Lösung: Happy Hour Ticket, Zwei Waben Tarif Herne (272) Bochum (360), Fahrt ID 1&2 zugeteilt
        //Eine Fahrt ohne Ticket (ID 3)
    }

    @Test
    public void oneRegionVariousTickets() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;
        Set<Integer> bochumHerne = new HashSet<>();
        bochumHerne.add(360);
        bochumHerne.add(272);

        startCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        for (int i = 1; i <= 10; i++) {
            startCalendar.set(2020, Calendar.JULY, i, 23, 45);
            endCalendar.set(2020, Calendar.JULY, i + 1, 0, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

            startCalendar.set(2020, Calendar.JULY, i + 1, 2, 0);
            endCalendar.set(2020, Calendar.JULY, i + 1, 2, 30);
            trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));
        }

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets =  FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;
        Collections.sort(tickets, new TicketToBuyTimeComporator());
        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        //Lösung: 1 7 Tage Ticket, 3 Happy Hour Ticket
        // Jeweils im zwei Waben Tarif Herne (272) -  Bochum (360)
        // 7 Tage Ticket: Fahrten mit den IDs 1-16, Gültigkeit: 01.07 10h bis 08.07 10h
        // Happy Hour Ticket: Jeweils zwei Fahrten eines Abends zugeordnet
        // - 08.07/09.07
        // - 09.07/10.07
        // - 10.07/11.07
        //Keine Fahrten ohne Tickets
    }

    @Test
    public void twoWabenDayTicket(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;
        Set<Integer> bochumHerne = new HashSet<>();
        bochumHerne.add(360);
        bochumHerne.add(272);

        startCalendar.set(2020, Calendar.JULY, 1, 18, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 18, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        ArrayList<TimeTicket> tarifgebietTickets = new ArrayList<>(timeTickets);
        ArrayList<TimeTicket> zweiWabenTickets = new ArrayList<>(timeTickets);
        int vierStundenTicketIndex = tarifgebietTickets.indexOf(MyVRRprovider.vierStundenTicket);
        tarifgebietTickets.remove(vierStundenTicketIndex);
        zweiWabenTickets.remove(vierStundenTicketIndex);
        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(tickets.get(0).getTicket().getName(), MyVRRprovider.tagesTicket_1.getName());
        //Lösung: 24h Ticket zwei Waben (272) Herne und (360) Bochum
        //Alle 5 Fahrten zugewiesen
    }

    @Test
    public void duesseldorfDayTicket(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;

        Set<Integer> duesseldorf = new HashSet<>();
        duesseldorf.add(430);
        duesseldorf.add(438);
        duesseldorf.add(436);

        startCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 438, duesseldorf));

        startCalendar.set(2020, Calendar.JULY, 1, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 436, duesseldorf));

        startCalendar.set(2020, Calendar.JULY, 1, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 438, duesseldorf));

        startCalendar.set(2020, Calendar.JULY, 1, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 436, duesseldorf));

        startCalendar.set(2020, Calendar.JULY, 1, 18, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 18, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 436, duesseldorf));

        ArrayList<TimeTicket> tarifgebietTickets = new ArrayList<>(timeTickets);
        ArrayList<TimeTicket> zweiWabenTickets = new ArrayList<>(timeTickets);
        int vierStundenTicketIndex = tarifgebietTickets.indexOf(MyVRRprovider.vierStundenTicket);
        tarifgebietTickets.remove(vierStundenTicketIndex);
        zweiWabenTickets.remove(vierStundenTicketIndex);
        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(tickets.get(0).getTicket().getName(), MyVRRprovider.tagesTicket_1.getName());
        //Lösung: 24h Ticket mit der Zentralregion Düsseldorf
        //Alle 5 Fahrten zugewiesen
        //Gültige Tarifgebiete des Tickets: 43 & 53
    }

    @Test
    public void wdhVorherigerTests(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;
        Set<Integer> bochumHerne = new HashSet<>();
        bochumHerne.add(360);
        bochumHerne.add(272);

        startCalendar.set(2020, Calendar.JULY, 1, 18, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 19, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 20, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 20, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "Happy Hour Ticket");
        //Lösung: Happy Hour Ticket, Zwei Waben Tarif Herne (272) & Bochum (360), alle Fahrten zugewiesen
        //keine Fahrten ohne Ticket

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        trips = new ArrayList<>();
        id = 1;
        bochumHerne = new HashSet<>();
        bochumHerne.add(360);
        bochumHerne.add(272);

        startCalendar.set(2020, Calendar.JULY, 1, 18, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 19, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 20, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 20, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        ArrayList<TimeTicket> tarifgebietTickets = new ArrayList<>(timeTickets);
        ArrayList<TimeTicket> zweiWabenTickets = new ArrayList<>(timeTickets);
        int vierStundenTicketIndex = tarifgebietTickets.indexOf(MyVRRprovider.vierStundenTicket);
        tarifgebietTickets.remove(vierStundenTicketIndex);
        zweiWabenTickets.remove(vierStundenTicketIndex);
        System.gc();
        startT = System.currentTimeMillis();
        tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        endT = System.currentTimeMillis();
        s = (endT - startT) / 1000;
        ms = (endT - startT) % 1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(tickets.get(0).getTicket().getName(), "Happy Hour Ticket");
        //Lösung: Happy Hour Ticket, Zwei Waben Tarif Herne (272) & Bochum (360), alle Fahrten zugewiesen
        //keine Fahrten ohne Ticket
    }

    @Test
    public void twoWabenSumUpTicketTest(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;
        Set<Integer> bochumHerne = new HashSet<>();
        bochumHerne.add(360);
        bochumHerne.add(272);

        startCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));


        startCalendar.set(2020, Calendar.JULY, 2, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 2, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 2, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 2, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));


        startCalendar.set(2020, Calendar.JULY, 3, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 3, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 3, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 3, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));


        startCalendar.set(2020, Calendar.JULY, 4, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 4, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 4, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 4, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 4, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 4, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 4, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 4, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));


        startCalendar.set(2020, Calendar.JULY, 5, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 5, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 5, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 5, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 5, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 5, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 5, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 5, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));


        ArrayList<TimeTicket> tarifgebietTickets = new ArrayList<>(timeTickets);
        ArrayList<TimeTicket> zweiWabenTickets = new ArrayList<>(timeTickets);
        int vierStundenTicketIndex = tarifgebietTickets.indexOf(MyVRRprovider.vierStundenTicket);
        tarifgebietTickets.remove(vierStundenTicketIndex);
        zweiWabenTickets.remove(vierStundenTicketIndex);
        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(tickets.get(0).getTicket().getName(), MyVRRprovider.siebenTagesTicket.getName());
        //Lösung: 7 Tage Ticket Zwei Waben Tarif Herne (272) und Bochum (360)
        //Alle 20 Fahrten zugewiesen
    }

    @Test
    public void oneTarifgebietSumUpTicketTest(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;
        Set<Integer> bochum = new HashSet<>();
        bochum.add(360);
        bochum.add(366);
        bochum.add(368);

        startCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 368, bochum));

        startCalendar.set(2020, Calendar.JULY, 1, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochum));

        startCalendar.set(2020, Calendar.JULY, 1, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 366, bochum));

        startCalendar.set(2020, Calendar.JULY, 1, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochum));


        startCalendar.set(2020, Calendar.JULY, 2, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 368, bochum));

        startCalendar.set(2020, Calendar.JULY, 2, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochum));

        startCalendar.set(2020, Calendar.JULY, 2, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 366, bochum));

        startCalendar.set(2020, Calendar.JULY, 2, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 368, bochum));


        startCalendar.set(2020, Calendar.JULY, 3, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 366, bochum));

        startCalendar.set(2020, Calendar.JULY, 3, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochum));

        startCalendar.set(2020, Calendar.JULY, 3, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 368, bochum));

        startCalendar.set(2020, Calendar.JULY, 3, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 366, bochum));


        startCalendar.set(2020, Calendar.JULY, 4, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 4, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 368, bochum));

        startCalendar.set(2020, Calendar.JULY, 4, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 4, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochum));

        startCalendar.set(2020, Calendar.JULY, 4, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 4, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 368, bochum));

        startCalendar.set(2020, Calendar.JULY, 4, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 4, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochum));


        startCalendar.set(2020, Calendar.JULY, 5, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 5, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 366, bochum));

        startCalendar.set(2020, Calendar.JULY, 5, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 5, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 368, bochum));

        startCalendar.set(2020, Calendar.JULY, 5, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 5, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 366, bochum));

        startCalendar.set(2020, Calendar.JULY, 5, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 5, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochum));


        ArrayList<TimeTicket> tarifgebietTickets = new ArrayList<>(timeTickets);
        ArrayList<TimeTicket> zweiWabenTickets = new ArrayList<>(timeTickets);
        int vierStundenTicketIndex = tarifgebietTickets.indexOf(MyVRRprovider.vierStundenTicket);
        tarifgebietTickets.remove(vierStundenTicketIndex);
        zweiWabenTickets.remove(vierStundenTicketIndex);
        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(tickets.get(0).getTicket().getName(), MyVRRprovider.siebenTagesTicket.getName());
        //Lösung: 7 Tage Ticket mit der Zentralregion Bochum (36)
        //Alle 20  Fahrten zugewiesen
    }

    @Test
    public void twoWabenOneTGEachTicket(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;
        Set<Integer> bochumHerne = new HashSet<>();
        bochumHerne.add(360);
        bochumHerne.add(272);

        Set<Integer> recklinghausenHerten = new HashSet<>();
        recklinghausenHerten.add(176);
        recklinghausenHerten.add(174);
        recklinghausenHerten.add(172);

        startCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 18, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 18, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 176, recklinghausenHerten));

        startCalendar.set(2020, Calendar.JULY, 1, 20, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 20, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 172, recklinghausenHerten));

        ArrayList<TimeTicket> tarifgebietTickets = new ArrayList<>(timeTickets);
        ArrayList<TimeTicket> zweiWabenTickets = new ArrayList<>(timeTickets);
        int vierStundenTicketIndex = tarifgebietTickets.indexOf(MyVRRprovider.vierStundenTicket);
        tarifgebietTickets.remove(vierStundenTicketIndex);
        zweiWabenTickets.remove(vierStundenTicketIndex);
        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Collections.sort(tickets, new TicketToBuyTimeComporator());
        Assert.assertEquals(tickets.get(0).getTicket().getName(), MyVRRprovider.tagesTicket_1.getName());
        Assert.assertEquals(tickets.get(1).getTicket().getName(), MyVRRprovider.happyHourTicket.getName());
        //Lösung: Tagesticket mit den zwei Waben Tarif Herne (272) und Bochum (360), HappyHour Ticket für das Tarifgebiet Herten /Recklinghausen (17)
        //Tagesticket: Fahrt 1-4
        //HappyH Ticket: Fahrt 5,6
    }

    @Test
    public void multipleWabenEachTicket(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;
        Set<Integer> bochumHerne = new HashSet<>();
        bochumHerne.add(360);
        bochumHerne.add(272);

        Set<Integer> herneHerten = new HashSet<>();
        herneHerten.add(272);
        herneHerten.add(176);

        startCalendar.set(2020, Calendar.JULY, 1, 18, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 18, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", id++, 176, herneHerten));

        startCalendar.set(2020, Calendar.JULY, 1, 20, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 20, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", id++, 272, herneHerten));

        startCalendar.set(2020, Calendar.JULY, 1, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 14, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 14, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 15, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 15, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 272, bochumHerne));

        startCalendar.set(2020, Calendar.JULY, 1, 16, 0);
        endCalendar.set(2020, Calendar.JULY, 1, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A3", id++, 360, bochumHerne));

        ArrayList<TimeTicket> tarifgebietTickets = new ArrayList<>(timeTickets);
        ArrayList<TimeTicket> zweiWabenTickets = new ArrayList<>(timeTickets);
        int vierStundenTicketIndex = tarifgebietTickets.indexOf(MyVRRprovider.vierStundenTicket);
        tarifgebietTickets.remove(vierStundenTicketIndex);
        zweiWabenTickets.remove(vierStundenTicketIndex);
        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();
        long s = (endT - startT) / 1000;
        long ms = (endT - startT) % 1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
        Assert.assertEquals(tickets.get(0).getTicket().getName(), MyVRRprovider.tagesTicket_1.getName());
        Assert.assertEquals(tickets.get(1).getTicket().getName(), MyVRRprovider.happyHourTicket.getName());
        //Lösung: Tagesticket mit den zwei Waben Herne (272) und Bochum (360), HappyHour Ticket für die zwei Waben Herne (272) und Herten (176)
        //Tagesticket: Fahrt 1-4
        //HappyH Ticket: Fahrt 5,6
    }
}