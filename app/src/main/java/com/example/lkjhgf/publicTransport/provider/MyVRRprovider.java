package com.example.lkjhgf.publicTransport.provider;

import android.app.Activity;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.OptimisationUtil;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketOptimisationHolder;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.MyVrrTimeOptimisationHelper;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Fare;
import de.schildbach.pte.dto.Location;

import static com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.VRR_Farezones.createVRRFarezone;

public class MyVRRprovider extends MyProvider {

    private static final String tagesTicket_1_s = "24-StundenTicket-1";
    private static final String tagesTicket_2_s = "24-StundenTicket-2";
    private static final String tagesTicket_3_s = "24-StundenTicket-3";
    private static final String tagesTicket_4_s = "24-StundenTicket-4";
    private static final String tagesTicket_5_s = "24-StundenTicket-5";
    private static final String zweiTagesTicket_1_s = "48-StundenTicket-1";
    private static final String zweiTagesTicket_2_s = "48h-StundenTicket-2";
    private static final String zweiTagesTicket_3_s = "48h-StundenTicket-3";
    private static final String zweiTagesTicket_4_s = "48h-StundenTicket-4";
    private static final String zweiTagesTicket_5_s = "48h-StundenTicket-5";

    public static final TimeTicket tagesTicket_5 = new TimeTicket(new int[]{2120, 2120, 2120, 2120, 3070, 4410, 5200}, tagesTicket_5_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 5);
    public static final TimeTicket tagesTicket_4 = new TimeTicket(new int[]{1710, 1710, 1710, 1710, 2670, 3940, 4660}, tagesTicket_4_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 4);
    public static final TimeTicket tagesTicket_3 = new TimeTicket(new int[]{1420, 1420, 1420, 1420, 2270, 3470, 4120}, tagesTicket_3_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 3);
    public static final TimeTicket tagesTicket_2 = new TimeTicket(new int[]{1070, 1070, 1070, 1070, 1870, 3000, 3580}, tagesTicket_2_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 2);
    public static final TimeTicket tagesTicket_1 = new TimeTicket(new int[]{720, 720, 720, 720, 1470, 2530, 3040}, tagesTicket_1_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 1);

    public static final TimeTicket zweiTagesTicket_1 = new TimeTicket(new int[]{1370, 1370, 1370, 1370, 2790, 4810, 5780}, zweiTagesTicket_1_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 1);
    public static final TimeTicket zweiTagesTicket_2 = new TimeTicket(new int[]{2030, 2030, 2030, 2030, 3550, 5700, 6800}, zweiTagesTicket_2_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 2);
    public static final TimeTicket zweiTagesTicket_3 = new TimeTicket(new int[]{2690, 2690, 2690, 2690, 4310, 6590, 7820}, zweiTagesTicket_3_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 3);
    public static final TimeTicket zweiTagesTicket_4 = new TimeTicket(new int[]{3350, 3350, 3350, 3350, 5070, 7480, 8840}, zweiTagesTicket_4_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 4);
    public static final TimeTicket zweiTagesTicket_5 = new TimeTicket(new int[]{4010, 4010, 4010, 4010, 5830, 8370, 9860}, zweiTagesTicket_5_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 5);


    private HashMap<Fare.Type, ArrayList<TimeTicket>> timeTickets;

    /**
     * Verkehrsverbund Rhein Ruhr
     */
    public MyVRRprovider() {
        super();

        String[] preisstufen = {"K", "A1", "A2", "A3", "B", "C", "D"};

        ArrayList<Ticket> adultTickets = new ArrayList<>();
        adultTickets.add(new NumTicket(1, new int[]{170, 280, 280, 290, 600, 1280, 1570}, "Einzelticket E", Fare.Type.ADULT));
        adultTickets.add(new NumTicket(4, new int[]{610, 1070, 1070, 1070, 2250, 4690, 5710},
                "4er-Ticket E", Fare.Type.ADULT));
        adultTickets.add(new NumTicket(10, new int[]{1420, 2290, 2290, 2290, 4600, 9315, 10485},
                "10er-Ticket E", Fare.Type.ADULT));

        ArrayList<Ticket> childrenTickets = new ArrayList<>();
        childrenTickets.add(new NumTicket(1, new int[]{170, 170, 170, 170, 170, 170, 170}, "Einzelticket K", Fare.Type.CHILD));
        childrenTickets.add(new NumTicket(4, new int[]{610, 610, 610, 610, 610, 610, 610}, "4er-Ticket K", Fare.Type.CHILD));

        HashMap<Fare.Type, ArrayList<Ticket>> allTickets = new HashMap<>();
        allTickets.put(Fare.Type.ADULT, adultTickets);
        allTickets.put(Fare.Type.CHILD, childrenTickets);

        timeTickets = new HashMap<>();
        ArrayList<TimeTicket> timeTicketsAdult = new ArrayList<>();
        timeTicketsAdult.add(new TimeTicket(new int[]{720, 720, 720, 720, 1470, 2530, 3040}, "24-StundenTicket-1", Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{5, 3, 3, 3, 3, 2, 2}, 0, 24, false, 1));
        timeTicketsAdult.add(new TimeTicket(new int[]{1370, 1370, 1370, 1370, 2790, 4810, 5780}, "48-StundenTicket-1", Fare.Type.ADULT, 48 * 60 * 60 * 1000, new int[]{9, 6, 6, 6, 5, 5, 5}, 0, 24, false, 1));
        timeTicketsAdult.add(new TimeTicket(new int[]{2295, 2295, 2815, 2950, 4275, 5730, 7240}, "7-TageTicket", Fare.Type.ADULT, 7 * 24 * 60 * 60 * 1000, new int[]{16, 11, 12, 13, 8, 5, 5}, 0, 24, false, 1));
        timeTicketsAdult.add(new TimeTicket(new int[]{7120, 7120, 7560, 7920, 11355, 15355, 19390}, "30-TageTicket", Fare.Type.ADULT, Math.multiplyExact((long) 30, (long) 24 * 60 * 60 * 1000), new int[]{51, 31, 33, 34, 24, 16, 17}, 0, 24, false, 1));
        timeTickets.put(Fare.Type.ADULT, timeTicketsAdult);

        NetworkProvider provider = new VrrProvider();

        initialise(preisstufen, allTickets, provider, createVRRFarezone());
    }

    /**
     * Sortiert die Fahrten auf die Nutzerklassen <br/>
     * <p>
     * Wenn eine Nutzerklasse mehrfach vorkommt, wird die Fahrt entsprechend oft hinzugefügt <br/>
     *
     * @param allTrips Liste aller Fahrten
     * @return HashMap mit den Fahrten aufgeteilt auf die Nutzerklassen, dabei entspricht einmal hinzufügen
     * einer Person
     */
    @Override
    public HashMap<Fare.Type, HashMap<Integer, ArrayList<TripItem>>> createUserClassHashMap(HashMap<Fare.Type, ArrayList<TripItem>> allTrips) {
        HashMap<Fare.Type, HashMap<Integer, ArrayList<TripItem>>> userClassTrips = new HashMap<>();
        for (Fare.Type type : allTrips.keySet()) {
            HashMap<Integer, ArrayList<TripItem>> iHashMap = userClassTrips.get(type);
            if (iHashMap == null) {
                iHashMap = new HashMap<>();
                userClassTrips.put(type, iHashMap);
            }
            ArrayList<TripItem> tripItems = allTrips.get(type);
            for (TripItem tripItem : tripItems) {
                for (int i = 1; i <= tripItem.getUserClassWithoutTicket(type); i++) {
                    ArrayList<TripItem> c = iHashMap.get(i);
                    if (c == null) {
                        c = new ArrayList<>();
                        iHashMap.put(i, c);
                    }
                    c.add(tripItem);
                }
            }
        }
        return userClassTrips;
    }

    /**
     * Teilt die Fahrten auf die Nutzerklassen auf
     * @param allTrips alle Fahrten
     * @return HashMap mit einer Liste an Fahrten für jede Nutzerklasse
     */
    @Override
    public HashMap<Fare.Type, ArrayList<TripItem>> createUserClassTripList(ArrayList<TripItem> allTrips) {
        HashMap<Fare.Type, ArrayList<TripItem>> userClassTrips = new HashMap<>();
        for (TripItem tripItem : allTrips) {
            for (Fare.Type type : tripItem.getNumUserClasses().keySet()) {
                ArrayList<TripItem> userClass = userClassTrips.get(type);
                if (userClass == null) {
                    userClass = new ArrayList<>();
                    userClassTrips.put(type, userClass);
                }
                if (tripItem.getNumUserClass(type) != 0) {
                    userClass.add(tripItem);
                }
            }
        }
        return userClassTrips;
    }

    /**
     * Fasst 24h und 48hTickets für mehrere Personen wenn möglich zusammen <br/>
     * <p>
     * Da das Tages- und Zweitagesticket auch für mehrere Personen verfügbar ist, wird versucht, bei
     * überschneidenden Zeitslots, diese zusammenzufassen, da die Fahrscheine für die jeweilige Personenanzahl
     * günstiger ist, als einzelne Tagestickets
     *
     * @param ticketItems alle Fahrscheine, die zusammengefasst werden sollen
     */
    public static void sumUpTickets(ArrayList<TicketToBuy> ticketItems) {
        //Laufzeitverbesserung
        if (ticketItems.size() < 2) {
            return;
        }
        ArrayList<TicketToBuy> oneDayTickets = new ArrayList<>();
        ArrayList<TicketToBuy> twoDayTickets = new ArrayList<>();

        for (Iterator<TicketToBuy> iterator = ticketItems.iterator(); iterator.hasNext(); ) {
            TicketToBuy current = iterator.next();
            if (current.getTicket().getName().equals(tagesTicket_1_s)) {
                oneDayTickets.add(current);
                iterator.remove();
            } else if (current.getTicket().getName().equals(zweiTagesTicket_1_s)) {
                twoDayTickets.add(current);
                iterator.remove();
            }
        }
        if (oneDayTickets.size() > 1) {
            MyVrrTimeOptimisationHelper.sumUpTicketsUpTo5Persons(oneDayTickets, ticketItems, 1);
        }
        if (twoDayTickets.size() > 1) {
            MyVrrTimeOptimisationHelper.sumUpTicketsUpTo5Persons(twoDayTickets, ticketItems, 2);
        }
    }

    @Override
    public HashMap<Fare.Type, ArrayList<TicketToBuy>> optimise(ArrayList<TripItem> tripItems1, HashMap<Fare.Type, ArrayList<TicketToBuy>> savedTickets, Activity activity) {
        System.gc();
        long startZeit = System.currentTimeMillis();
        //Entfernen von Fahrten mit fehlenden / falschen Preisstufen
        ArrayList<TripItem> tripItems = OptimisationUtil.removeTrips(tripItems1);
        // Fahrten nach den Preisstufen sortieren
        Collections.sort(tripItems, MainMenu.myProvider);
        //Fahrscheine der letzten Optimierung
        HashMap activeTickets = new HashMap(savedTickets);
        //HashMap mit allen benötigten Fahrscheinen
        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketLists = new HashMap<>();
        //HashMap mit Fahrscheinen, auf denen noch mindestens eine Fahrt frei ist
        HashMap<Fare.Type, ArrayList<TicketToBuy>> freeTickets = new HashMap<>();
        //Aufteilen der Fahrten auf die Nutzerklassen
        HashMap<Fare.Type, ArrayList<TripItem>> tripsPerUserClass = createUserClassTripList(tripItems);
        //Fahrscheine, die angefangen sind und noch freie Fahrten, sowie aktive Tickets und Fahrten die bereits gültige Fahrscheine haben entfernen
        OptimisationUtil.cleanUpTicketsAndTrips(activeTickets, tripsPerUserClass, freeTickets, allTicketLists);
        //Wenn möglich, alte Fahrscheine verwenden
        MyVrrTimeOptimisationHelper.optimisationWithOldTickets(tripsPerUserClass, freeTickets);
        //Aufteilen der Fahrten jeder Nutzerklasse auf die jeweilige Personenanzahl
        HashMap<Fare.Type, HashMap<Integer, ArrayList<TripItem>>> sortedTrips = createUserClassHashMap(tripsPerUserClass);

        HashMap<Fare.Type, ArrayList<TripItem>> tripsWithoutTimeTicket = new HashMap<>();
        //Zeitoptimierung
        for (Fare.Type type : sortedTrips.keySet()) {
            HashMap<Integer, ArrayList<TripItem>> sortedUserClassTrips = sortedTrips.get(type);
            ArrayList<TicketToBuy> ticketToBuyArrayList = new ArrayList<>();
            allTicketLists.put(type, ticketToBuyArrayList);
            if (sortedUserClassTrips == null) {
                continue;
            }
            if (timeTickets.get(type) != null && !timeTickets.get(type).isEmpty()) {
                MyVrrTimeOptimisationHelper.timeOptimisation(sortedUserClassTrips, timeTickets, allTicketLists, type);
            }
            tripsWithoutTimeTicket.put(type, MyVrrTimeOptimisationHelper.prepareNumTicketOptimisation(sortedUserClassTrips, type));
        }
        //NumTicketoptimierung
        HashMap<Fare.Type, TicketOptimisationHolder> numTicketOptimisationHolder = OptimisationUtil.optimisationWithNewTickets(tripsWithoutTimeTicket);
        HashMap<Fare.Type, ArrayList<TicketToBuy>> numTicket = new HashMap<>();
        OptimisationUtil.buildTicketList(numTicketOptimisationHolder, numTicket);
        //Zusammenfassen der Ergebnisse
        MyVrrTimeOptimisationHelper.sumUpNumAndTimeAndOldTickets(allTicketLists, numTicket, activeTickets);

        long endZeit = System.currentTimeMillis();
        System.out.println("--------------------------------------------------------------------");
        System.out.println(((endZeit - startZeit) / 1000) + "s " + ((endZeit - startZeit) % 1000) + "ms");
        return allTicketLists;
    }

    @Override
    public String getTicketInformation(ArrayList<Ticket> tickets, ArrayList<Integer> quantity, ArrayList<String> preisstufe, TripItem tripItem) {
        StringBuilder value = new StringBuilder();
        Location startLocation, destinationLocation;
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i) instanceof NumTicket) {
                int wholeTickets = quantity.get(i) / ((NumTicket) tickets.get(i)).getNumTrips();
                int restTicket = quantity.get(i) % ((NumTicket) tickets.get(i)).getNumTrips();
                if (wholeTickets != 0) {
                    value.append(wholeTickets).append("x ").append(tickets.get(i).toString()).append("\n \tPreisstufe: ").append(preisstufe.get(i)).append("\n \talle Fahrten entwerten");
                }
                if (restTicket != 0) {
                    value.append("1x ").append(tickets.get(i).toString()).append(" \n \tPreisstufe: ").append(preisstufe.get(i)).append("\n \t").append(restTicket).append("x entwerten");
                }
                startLocation = tripItem.getTrip().from;
                destinationLocation = tripItem.getTrip().to;
                if (preisstufe.get(i).equals(preisstufen[0])) {
                    value.append("\n \tEntwerten für die Starthaltestelle: ").append(UtilsString.setLocationName(tripItem.getTrip().from));
                } else if (preisstufe.get(i).equals(preisstufen[1]) || preisstufe.get(i).equals(preisstufen[2]) || preisstufe.get(i).equals(preisstufen[3])) {
                    /*String startID = startLocation.id.substring(1);
                    String destinationID = destinationLocation.id.substring(1);
                    int startIDint = Integer.parseInt(startID);
                    int destinationIDint = Integer.parseInt(destinationID);

                    testAsyncTaskextends wabenTask = new testAsyncTaskextends();
                    wabenTask.execute(startIDint, destinationIDint);
                    ArrayList<Integer> waben = new ArrayList<>();
                    try {
                        waben = wabenTask.get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (waben.size() == 2) {
                        if (waben.get(0) / 10 == waben.get(1) / 10) {
                            value.append("\n \tEntwerten für das Tarifgebiet: ").append(waben.get(0));
                        } else {
                            value.append("\n \tEntwerten für die zwei Waben mit der Starthaltestelle: ").append(UtilsString.setLocationName(tripItem.getTrip().from));
                            value.append(" und der Zielhaltestelle ").append(UtilsString.setLocationName(tripItem.getTrip().to));
                        }
                    }*/
                    int startID = tripItem.getStartID();
                    //TODO
                    if(startID/10 != 10){
                        value.append("\n \tEntwerten für die zwei Waben mit der Starthaltestelle: ").append(UtilsString.setLocationName(tripItem.getTrip().from));
                        value.append(" und der Zielhaltestelle ").append(UtilsString.setLocationName(tripItem.getTrip().to));
                    }else{
                        for(Farezone f : farezones){
                            if(f.getId()/10 == startID){
                                value.append("\n \tEntwerten für das Tarifgebiet: ").append(f.getName());
                            }
                        }
                    }
                } else {
                    /*startLocation = tripItem.getTrip().from;
                    String startID = startLocation.id.substring(1);
                    int startIDint = Integer.parseInt(startID);
                    testAsyncTaskextends wabenTask = new testAsyncTaskextends();
                    wabenTask.execute(startIDint);
                    ArrayList<Integer> waben = new ArrayList<>();
                    try {
                        waben = wabenTask.get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!waben.isEmpty()) {
                        value.append("\n\tEntwerten für das Tarifgebiet: ").append(waben.get(0) / 10);
                    }*/
                    for(Farezone f : farezones) {
                        if (f.getId() == tripItem.getStartID()/10) {
                            value.append("\n \tEntwerten für das Tarifgebiet: ").append(f.getName());
                        }
                    }
                }
            } else {
                if(preisstufe.get(i).equals(preisstufen[preisstufen.length-1])){
                    value.append(quantity.get(i) + "x ").append(tickets.get(i).toString()).append(" \n \tPreisstufe: ").append(preisstufe.get(i));
                }else if(preisstufe.get(i).equals(preisstufen[preisstufen.length-2])){
                    value.append(quantity.get(i) + "x ").append(tickets.get(i).toString()).append(" \n \tPreisstufe: ").append(preisstufe.get(i));
                    value.append("entwerten für die Region: ");
                    //TODO
                }else if(preisstufe.get(i).equals(preisstufen[preisstufen.length-3])){
                    value.append(quantity.get(i) + "x ").append(tickets.get(i).toString()).append(" \n \tPreisstufe: ").append(preisstufe.get(i));
                    value.append("entwerten mit dem Zentralgebiet: ");
                    //TODO
                }else{
                    //TODO Preisstufe K/A1/A2/A3
                }

                //TODO für andere Preisstufen als D muss die Region festgelegt werden
            }


            if (i != tickets.size() - 1) {
                value.append("\n");
            }
        }
        return value.toString();
    }

    /**
     * Sortiert die Fahrten nach ihrer Preisstufe <br/>
     * <p>
     * Es kann nicht anhand der Strings sortiert werden, da K die kleinste Preisstufe ist; <br/>
     * Deshalb wird erst dieser Sonderfall betrachtet, und anschließend die Compare Methode für
     * Strings aufgerufen.
     *
     * @param tripItem1 erste zu vergleichende Fahrt
     * @param tripItem2 zweite zu vergelichende Fahrt
     * @return 0 bei gleichheit, -1 wenn die erste Preisstufe < zweite Preisstufe, 1 wenn Preisstufe 2 < Preisstufe 1
     */
    @Override
    public int compare(TripItem tripItem1, TripItem tripItem2) {
        String preisstufe1 = tripItem1.getPreisstufe();
        String preisstufe2 = tripItem2.getPreisstufe();

        if (preisstufe1 == preisstufen[0] && preisstufe2 == preisstufen[0]) {
            return 0;
        } else if (preisstufe1 == preisstufen[0] && preisstufe2 != preisstufen[0]) {
            return -1;
        } else if (preisstufe1 != preisstufen[0] && preisstufe2 == preisstufen[0]) {
            return 1;
        } else {
            return preisstufe1.compareTo(preisstufe2);
        }
    }
}
