package com.example.lkjhgf;

import androidx.test.rule.ActivityTestRule;

import com.example.lkjhgf.activities.MainMenu;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.schildbach.pte.dto.Fare;

public class OptimierungsTestA {

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
    public void differentRegionsEachOneTicket(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;

        //Preisstufe A1
        Set<Integer> recklinghausen = new HashSet<>();
        recklinghausen.add(176);
        recklinghausen.add(174);
        recklinghausen.add(172);

        startCalendar.set(2020, Calendar.JULY, 3, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A1", id++, 174, recklinghausen));

        startCalendar.set(2020, Calendar.JULY, 3, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 12, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A1", id++, 172, recklinghausen));

        startCalendar.set(2020, Calendar.JULY, 3, 13, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 13, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A1", id++, 174, recklinghausen));

        startCalendar.set(2020, Calendar.JULY, 3, 13, 45);
        endCalendar.set(2020, Calendar.JULY, 3, 13, 59);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A1", id++, 176, recklinghausen));

        //Preisstufe A2
        Set<Integer> muelheim = new HashSet<>();
        muelheim.add(344);
        muelheim.add(442);

        startCalendar.set(2020, Calendar.JULY, 2, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", id++, 442, muelheim));

        startCalendar.set(2020, Calendar.JULY, 2, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 12, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", id++, 344, muelheim));

        startCalendar.set(2020, Calendar.JULY, 2, 13, 0);
        endCalendar.set(2020, Calendar.JULY, 2, 13, 45);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", id++, 442, muelheim));

        //Preisstufe A3
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

        System.gc();
        long startT = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long endT = System.currentTimeMillis();

        long s = (endT-startT)/1000;
        long ms = (endT-startT)%1000;

        Assert.assertTrue(trips.isEmpty());
        Assert.assertFalse(tickets.isEmpty());
        //Lösung: 3 Tickets:
        //          - Düsseldorf: 24h Ticket (A3) gültige Tarifgebiete: 43 & 53 - 5 Fahrten
        //          - Mühlheim: 24h Ticket (A2) gültige Waben: 344 & 442 - 3 Fahrten
        //          - Recklinghausen: 4h Ticket (A1) gültiges Tarifgebiet: 17 - 4 Fahrten
    }

    @Test
    public void test(){
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        ArrayList<TripItem> trips = new ArrayList<>();
        int id = 1;

        //Preisstufe A1
        Set<Integer> recklinghausen = new HashSet<>();
        recklinghausen.add(178);

        //Preisstufe A1
        Set<Integer> marlHerten = new HashSet<>();
        marlHerten.add(178);
        marlHerten.add(154);


        startCalendar.set(2020, Calendar.JULY, 3, 9, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 9, 45);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A1", id++, 178, marlHerten));

        startCalendar.set(2020, Calendar.JULY, 3, 10, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 11, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A1", id++, 178, recklinghausen));

        startCalendar.set(2020, Calendar.JULY, 3, 12, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 12, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A1", id++, 178, recklinghausen));

        startCalendar.set(2020, Calendar.JULY, 3, 13, 30);
        endCalendar.set(2020, Calendar.JULY, 3, 14, 0);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A1", id++, 178, recklinghausen));

        startCalendar.set(2020, Calendar.JULY, 3, 14, 45);
        endCalendar.set(2020, Calendar.JULY, 3, 16, 30);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A1", id++, 178, marlHerten));

        startCalendar.set(2020, Calendar.JULY, 3, 17, 0);
        endCalendar.set(2020, Calendar.JULY, 3, 18, 45);
        trips.add(new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A1", id++, 178, marlHerten));

        System.gc();
        long start = System.currentTimeMillis();
        ArrayList<TicketToBuy> tickets = FarezoneA.optimierungPreisstufeA(trips, timeTickets);
        long end = System.currentTimeMillis();

        long s = (end-start)/1000;
        long ms = (end-start)%1000;

        Assert.assertFalse(tickets.isEmpty());
        Assert.assertTrue(trips.isEmpty());
    }

}
