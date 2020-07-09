package com.example.lkjhgf.publicTransport.provider;

import android.app.Activity;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.OptimisationUtil;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.numTicketOptimisation.NumTicketOptimisation;
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

import static com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.VRR_Farezones.createVRRFarezone;

public class MyVRRprovider extends MyProvider {

    private static final String vierStundenTicket_s = "4-StundenTicket";
    private static final String happyHourTicket_s = "Happy Hour Ticket";

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

    private static final String siebenTagesTicket_s = "7-TageTicket";
    private static final String dreissigTagesTicket_s = "30-TageTicket";

    private static final String einzelticket_e_s = "Einzelticket E";
    private static final String einzelticket_k_s = "Einzelticket K";
    private static final String viererticket_e_s = "4er-Ticket E";
    private static final String viererticket_k_s = "4er-Ticket K";
    private static final String zehnerticket_e_s = "10er-Ticket E";

    public static final TimeTicket vierStundenTicket = new TimeTicket(new int[]{420, 420, 420, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, vierStundenTicket_s, Fare.Type.ADULT, 4 * 60 * 60 * 1000, new int[]{3, 2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, 9, 3, true, 1);
    public static final TimeTicket happyHourTicket = new TimeTicket(new int[]{319, 319, 319, 319, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, happyHourTicket_s, Fare.Type.ADULT, 12 * 60 * 60 * 1000, new int[]{3, 2, 2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, 18, 6, false, 1);

    public static final TimeTicket tagesTicket_5 = new TimeTicket(new int[]{2120, 2120, 2120, 2120, 3070, 4410, 5200}, tagesTicket_5_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 5);
    public static final TimeTicket tagesTicket_4 = new TimeTicket(new int[]{1710, 1710, 1710, 1710, 2670, 3940, 4660}, tagesTicket_4_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 4);
    public static final TimeTicket tagesTicket_3 = new TimeTicket(new int[]{1420, 1420, 1420, 1420, 2270, 3470, 4120}, tagesTicket_3_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 3);
    public static final TimeTicket tagesTicket_2 = new TimeTicket(new int[]{1070, 1070, 1070, 1070, 1870, 3000, 3580}, tagesTicket_2_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 2);
    public static final TimeTicket tagesTicket_1 = new TimeTicket(new int[]{720, 720, 720, 720, 1470, 2530, 3040}, tagesTicket_1_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{5, 3, 3, 3, 3, 2, 2}, 0, 24, false, 1);

    public static final TimeTicket zweiTagesTicket_1 = new TimeTicket(new int[]{1370, 1370, 1370, 1370, 2790, 4810, 5780}, zweiTagesTicket_1_s, Fare.Type.ADULT, 48 * 60 * 60 * 1000, new int[]{9, 6, 6, 6, 5, 5, 5}, 0, 24, false, 1);
    public static final TimeTicket zweiTagesTicket_2 = new TimeTicket(new int[]{2030, 2030, 2030, 2030, 3550, 5700, 6800}, zweiTagesTicket_2_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 2);
    public static final TimeTicket zweiTagesTicket_3 = new TimeTicket(new int[]{2690, 2690, 2690, 2690, 4310, 6590, 7820}, zweiTagesTicket_3_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 3);
    public static final TimeTicket zweiTagesTicket_4 = new TimeTicket(new int[]{3350, 3350, 3350, 3350, 5070, 7480, 8840}, zweiTagesTicket_4_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 4);
    public static final TimeTicket zweiTagesTicket_5 = new TimeTicket(new int[]{4010, 4010, 4010, 4010, 5830, 8370, 9860}, zweiTagesTicket_5_s, Fare.Type.ADULT, 24 * 60 * 60 * 1000, new int[]{}, 0, 24, false, 5);

    public static final TimeTicket siebenTagesTicket = new TimeTicket(new int[]{2295, 2295, 2815, 2950, 4275, 5730, 7240}, siebenTagesTicket_s, Fare.Type.ADULT, 7 * 24 * 60 * 60 * 1000, new int[]{16, 11, 12, 13, 8, 5, 5}, 0, 24, false, 1);
    public static final TimeTicket dreissigTagesTicket = new TimeTicket(new int[]{7120, 7120, 7560, 7920, 11355, 15355, 19390}, dreissigTagesTicket_s, Fare.Type.ADULT, Math.multiplyExact((long) 30, (long) 24 * 60 * 60 * 1000), new int[]{51, 31, 33, 34, 24, 16, 17}, 0, 24, false, 1);

    public static final NumTicket einzelticket_e = new NumTicket(1, new int[]{170, 280, 280, 290, 600, 1280, 1570}, einzelticket_e_s, Fare.Type.ADULT);
    public static final NumTicket viererticket_e = new NumTicket(4, new int[]{610, 1070, 1070, 1070, 2250, 4690, 5710}, viererticket_e_s, Fare.Type.ADULT);
    public static final NumTicket zehnerticket_e = new NumTicket(10, new int[]{1420, 2290, 2290, 2290, 4600, 9315, 10485}, zehnerticket_e_s, Fare.Type.ADULT);

    public static final NumTicket einzelticket_k = new NumTicket(1, new int[]{170, 170, 170, 170, 170, 170, 170}, einzelticket_k_s, Fare.Type.CHILD);
    public static final NumTicket viererticket_k = new NumTicket(4, new int[]{610, 610, 610, 610, 610, 610, 610}, viererticket_k_s, Fare.Type.CHILD);

    /**
     * Verkehrsverbund Rhein Ruhr
     */
    public MyVRRprovider() {
        super();
        String[] farezones = {"K", "A1", "A2", "A3", "B", "C", "D"};

        ArrayList<Ticket> adultTickets = new ArrayList<>();
        adultTickets.add(einzelticket_e);
        adultTickets.add(viererticket_e);
        adultTickets.add(zehnerticket_e);
        adultTickets.add(happyHourTicket);
        adultTickets.add(vierStundenTicket);
        adultTickets.add(tagesTicket_1);
        adultTickets.add(siebenTagesTicket);
        adultTickets.add(dreissigTagesTicket);
        adultTickets.add(zweiTagesTicket_1);

        ArrayList<Ticket> childrenTickets = new ArrayList<>();
        childrenTickets.add(einzelticket_k);
        childrenTickets.add(viererticket_k);
        HashMap<Fare.Type, ArrayList<Ticket>> allTickets = new HashMap<>();
        allTickets.put(Fare.Type.ADULT, adultTickets);
        allTickets.put(Fare.Type.CHILD, childrenTickets);

        NetworkProvider provider = new VrrProvider();

        initialise(farezones, allTickets, provider, createVRRFarezone());
    }

    /**
     * Optimierung <br/>
     * <p>
     * 1. Vorbereitung <br/>
     * 2. Versuch, wenn möglich angefangene Fahrscheine erneut zu verwenden <br/>
     * 3. Optimierung mit Zeitfahrscheinen -> nach vorheriger Analyse günstiger als Fahrscheine mit fester Fahrtenanzahl
     * 4. Optimierung mit Fahrscheinen die eine feste Fahrtenanzahl haben
     * 5. Zusammenfassen von 4.
     *
     * @param tripItems1   Fahrten die optimiert werden müssen
     * @param savedTickets gespeicherte Tickets, ggf. wiederverwenden
     * @param activity     Aufrufende Aktivität -> zum Laden & speichern der Tickets benötigt
     * @return HashMap: Für jede Nutzerklasse die Liste an benötigten Tickets
     */
    @Override
    public HashMap<Fare.Type, ArrayList<TicketToBuy>> optimise(ArrayList<TripItem> tripItems1, HashMap<Fare.Type, ArrayList<TicketToBuy>> savedTickets, Activity activity) {
        System.gc();
        long startZeit = System.currentTimeMillis();

        //Entfernen von Fahrten mit fehlenden / falschen Preisstufen
        ArrayList<TripItem> tripItems = OptimisationUtil.removeTrips(tripItems1);
        // Fahrten nach den Preisstufen sortieren
        Collections.sort(tripItems, MainMenu.myProvider);
        //Fahrscheine der letzten Optimierung
        HashMap<Fare.Type, ArrayList<TicketToBuy>> activeTickets = new HashMap<>(savedTickets);
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
        //Zeitoptimierung
        HashMap<Fare.Type, ArrayList<TripItem>> tripsWithoutTimeTicket = MyVrrTimeOptimisationHelper.timeOptimisation(sortedTrips, allTicketLists);
        //NumTicketoptimierung
        HashMap<Fare.Type, ArrayList<TicketToBuy>> numTicket = NumTicketOptimisation.optimisationNewTickets(tripsWithoutTimeTicket);

        //Zusammenfassen der Ergebnisse
        MyVrrTimeOptimisationHelper.sumUpNumAndTimeAndOldTickets(allTicketLists, numTicket, activeTickets);

        long endZeit = System.currentTimeMillis();
        System.out.println("--------------------------------------------------------------------");
        System.out.println(((endZeit - startZeit) / 1000) + "s " + ((endZeit - startZeit) % 1000) + "ms");
        return allTicketLists;
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
    @Override
    public void sumUpTickets(ArrayList<TicketToBuy> ticketItems) {
        //Laufzeitverbesserung
        if (ticketItems.size() < 2) {
            return;
        }
        ArrayList<TicketToBuy> oneDayTickets = new ArrayList<>();
        ArrayList<TicketToBuy> twoDayTickets = new ArrayList<>();
        //Sammeln von 24h und 48h Tickets
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
        //Diese zusammenfassen
        if (oneDayTickets.size() > 1) {
            MyVrrTimeOptimisationHelper.sumUpTicketsUpTo5Persons(oneDayTickets, ticketItems, 1);
        }
        if (twoDayTickets.size() > 1) {
            MyVrrTimeOptimisationHelper.sumUpTicketsUpTo5Persons(twoDayTickets, ticketItems, 2);
        }
    }

    /**
     * Informationen darüber, wie die jeweiligen Tickets entwerten müssen <br/>
     * <p>
     * Untercheidung zu TimeTickets nötig, diese können nicht einfach zusammengefasst werden. <br/>
     * Des weiteren ist die Angabe für den Gültigkeitsbereich abweichend
     *
     * @param tickets    Tickets deren Informationen angezeigt werden sollen (Zusammengefasst!)
     * @param quantity   Häufigkeit des jeweiligen Tickets
     * @param preisstufe Preisstufe des jeweiligen Tickets
     * @param tripItem   Fahrt dem die ganzen Tickets angehören
     * @return Fahrscheininformationen für den Nutzer für die Fahrt
     * @preconditions Die Informationen der Listen tickets, quantity und preisstufe sind zusammengehörig
     * -> ticket an Position i wird quantity[i] mal in der preisstufe[i] benötigt
     */
    @Override
    public String getTicketInformationNumTicket(ArrayList<Ticket> tickets, ArrayList<Integer> quantity, ArrayList<String> preisstufe, TripItem tripItem) {
        StringBuilder value = new StringBuilder();
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
                if (preisstufe.get(i).equals(preisstufen[0])) {
                    value.append("\n \tEntwerten für die Starthaltestelle: ").append(UtilsString.setLocationName(tripItem.getTrip().from));
                } else if (preisstufe.get(i).equals(preisstufen[1]) || preisstufe.get(i).equals(preisstufen[2]) || preisstufe.get(i).equals(preisstufen[3])) {
                    if (MyVrrTimeOptimisationHelper.checkIsZweiWaben(tripItem.getCrossedFarezones())) {
                        value.append("\n \tEntwerten für die zwei Waben mit der Starthaltestelle: ").append(UtilsString.setLocationName(tripItem.getTrip().from));
                        value.append(" und der Zielhaltestelle ").append(UtilsString.setLocationName(tripItem.getTrip().to));
                    } else {
                        for (Farezone f : farezones) {
                            if (f.getId() == tripItem.getStartID() / 10) {
                                value.append("\n \tEntwerten für das Tarifgebiet: ").append(f.getId()).append(" - ").append(f.getName());
                            }
                        }
                    }
                } else if (!preisstufe.get(i).equals(preisstufen[preisstufen.length - 1])) {
                    for (Farezone f : farezones) {
                        if (f.getId() == tripItem.getStartID() / 10) {
                            value.append("\n \tEntwerten für das Tarifgebiet: ").append(f.getId()).append(" - ").append(f.getName());
                        }
                    }
                }
            }
            if (i != tickets.size() - 1 && tickets.get(i) instanceof NumTicket) {
                value.append("\n");
            }
        }
        return value.toString();
    }

    /**
     * Informationen zu den TimeTickets <br/>
     * <p>
     * Fügt dem String für jedes Ticket die Informationen: Name, Preisstufe, Startdatum und Zentralgebiet hinzu <br/>
     * Unterscheidet sich zu {@link #getTicketInformationNumTicket}, da hier die Fahrscheine nicht zusammengelegt sind,
     * und die Angabe für das Zentralgebiet anders funktioniert.
     *
     * @param timeTicketsToBuy Liste an Tickets die angezeigt werden soll
     * @return String mit den Informationen Geltungszeitraum, Zentralgebiet und Ticketname soweie Preisstufe, für jedes in timeTicketsToBuy
     * enthaltene Ticket
     */
    public String getTicketInformationTimeTicket(ArrayList<TicketToBuy> timeTicketsToBuy) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < timeTicketsToBuy.size(); i++) {
            TicketToBuy ticket = timeTicketsToBuy.get(i);
            result.append("1x ").append(ticket.getTicket().getName()).append(" Preisstufe: ").append(ticket.getPreisstufe());
            if (ticket.getPreisstufe().equals(preisstufen[1]) || ticket.getPreisstufe().equals(preisstufen[2]) || ticket.getPreisstufe().equals(preisstufen[3]) || ticket.getPreisstufe().equals(preisstufen[4])) {//Peisstufe A1-A3,B
                if (ticket.isZweiWabenTarif()) {
                    result.append("\nEntwerten für die Starthaltestelle: ").append(UtilsString.setLocationName(ticket.getTripList().get(0).getTrip().from));
                    result.append("\nund die Zielhaltestelle: ").append(UtilsString.setLocationName(ticket.getTripList().get(0).getTrip().to));
                } else {
                    for (Farezone f : farezones) {
                        if (f.getId() == ticket.getMainRegionID()) {
                            result.append("\nEntwerten für das Tarifgebiet: ").append(f.getId()).append(" - ").append(f.getName());
                            break;
                        }
                    }
                }
            } else if (ticket.getPreisstufe().equals(preisstufen[5])) { //Preisstufe C
                result.append("\nRegion: ").append(ticket.getMainRegionID());
            }//Preisstufe D hat keine Region, für die entwertet werden muss
            result.append("\nStartzeitpunkt: ").append(UtilsString.setDate(ticket.getFirstDepartureTime())).append(" ").append(UtilsString.setTime(ticket.getFirstDepartureTime()));
            if (i + 1 < timeTicketsToBuy.size()) {
                result.append("\n");
            }
        }
        return result.toString();
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

        if (preisstufe1.equals(preisstufen[0]) && preisstufe2.equals(preisstufen[0])) { //Beide Fahrten Kurzstrecke
            return 0;
        } else if (preisstufe1.equals(preisstufen[0])) { //Fahrt 1 ist Kurzstrecke und Fahrt 2 nicht
            return -1;
        } else if (preisstufe2.equals(preisstufen[0])) {//Fahrt 1 ist keine Kurzstrecke und Fahrt 2 ist Kurzstrecke
            return 1;
        } else {//Vergleich der anderen Preisstufen mittel der compareTo Methode von String
            return preisstufe1.compareTo(preisstufe2);
        }
    }
}