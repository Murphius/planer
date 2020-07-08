package com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.TicketToBuyTimeComporator;
import com.example.lkjhgf.helper.util.TripQuantitiesTimeComparator;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.OptimisationUtil;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeOptimisation;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.publicTransport.provider.MyVRRprovider;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import de.schildbach.pte.dto.Fare;

/**
 * Hilfsklasse für die Fahrscheinoptimierung im VRR
 */
public final class MyVrrTimeOptimisationHelper {

    /**
     * Optimierung mit neuen Zeitfahrscheinen <br/>
     * <p>
     * Anpassung der Fahrscheinliste für die jeweiligen Preisstufen <br/>
     * Ggf. zusammenfassen von Tickets einer Preisstufe <br/>
     *
     * @param sortedUserClassTrips Liste an Fahrten die optimiert werden sollen
     * @param timeTickets          Mögliche Tickets
     * @param allTicketLists       Benötigte Tickets
     * @param type                 Personenklasse die betrachtet werden soll
     */
    public static void timeOptimisation(HashMap<Integer, ArrayList<TripItem>> sortedUserClassTrips,
                                        HashMap<Fare.Type, ArrayList<TimeTicket>> timeTickets,
                                        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketLists,
                                        Fare.Type type) {
        //Liste an zu kaufenden Fahrscheinen
        ArrayList<TicketToBuy> ticketToBuyArrayList = new ArrayList<>();
        //Fahrscheine pro Preisstufe
        HashMap<String, ArrayList<TicketToBuy>> ticketsPerFarezone = new HashMap<>();
        //Für jede Personennr. optimieren
        for (Integer i : sortedUserClassTrips.keySet()) {
            //Preisstufe D - ohne Happy Hour Ticket &  ohne 4h Ticket
            int indexOfHappyHourTicket = timeTickets.get(type).indexOf(MyVRRprovider.happyHourTicket);
            if (indexOfHappyHourTicket > -1) {
                timeTickets.get(type).remove(indexOfHappyHourTicket);
            }
            int indexOfVierStundenTicket = timeTickets.get(type).indexOf(MyVRRprovider.vierStundenTicket);
            if (indexOfVierStundenTicket > -1) {
                timeTickets.get(type).remove(indexOfVierStundenTicket);
            }
            ArrayList<TicketToBuy> zws = TimeOptimisation.optimierungPreisstufeDNew(sortedUserClassTrips.get(i), timeTickets.get(type));
            TimeOptimisation.checkTicketForOtherTrips(sortedUserClassTrips.get(i), zws);
            ArrayList<TicketToBuy> e = ticketsPerFarezone.get("D");
            if (e == null) {
                e = new ArrayList<>();
                ticketsPerFarezone.put("D", e);
            }
            e.addAll(zws);
            zws.clear();
            zws.addAll(TimeOptimisation.optimieriungPreisstufeC(sortedUserClassTrips.get(i), timeTickets.get(type)));
            e = ticketsPerFarezone.get("C");
            if (e == null) {
                e = new ArrayList<>();
                ticketsPerFarezone.put("C", e);
            }
            e.addAll(zws);
            zws.clear();
            zws.addAll(TimeOptimisation.optimierungPreisstufeB(sortedUserClassTrips.get(i), timeTickets.get(type)));
            e = ticketsPerFarezone.get("B");
            if (e == null) {
                e = new ArrayList<>();
                ticketsPerFarezone.put("B", e);
            }
            e.addAll(zws);
            zws.clear();
            //Hinzufügen von HappyHour & 4h Ticket für die Preisstufe A
            if (indexOfVierStundenTicket > -1) {
                timeTickets.get(type).add(indexOfVierStundenTicket, MyVRRprovider.vierStundenTicket);
            }
            if (indexOfHappyHourTicket > -1) {
                timeTickets.get(type).add(indexOfHappyHourTicket, MyVRRprovider.happyHourTicket);
            }
            zws.addAll(TimeOptimisation.optimierungPreisstufeA(sortedUserClassTrips.get(i), timeTickets.get(type)));
            e = ticketsPerFarezone.get("A");
            if (e == null) {
                e = new ArrayList<>();
                ticketsPerFarezone.put("A", e);
            }
            e.addAll(zws);
        }
        //Fahrscheine der jeweiligen Preisstufe zusammenfassen
        for (String s : ticketsPerFarezone.keySet()) {
            MainMenu.myProvider.sumUpTickets(ticketsPerFarezone.get(s));
            //Tickets der Liste an Fahrscheinen hinzufügen
            ticketToBuyArrayList.addAll(ticketsPerFarezone.get(s));
        }

        //Bis jetzt haben die Fahrscheine zwar Fahrten zugewiesen bekommen, aber die Fahrten noch keine
        //Fahrscheine
        for (TicketToBuy ticket : ticketToBuyArrayList) {
            for (TripItem tripItem : ticket.getTripList()) {
                tripItem.addTicket(ticket);
            }
        }
        //Merken welche Tickets benötigt werden
        allTicketLists.put(type, ticketToBuyArrayList);
    }

    /**
     * Vorbereitung der Optimierung mit Fahrscheinen, die eine feste Anzahl an Fahrscheinen haben <br/>
     * <p>
     * Die Verbindung zwischen Personennr. und Fahrtenliste wird gelöst. <br/>
     * In der Liste kommt eine Fahrt so oft vor, wie die Fahrt noch Nutzer ohne Ticket hat.
     *
     * @param sortedUserClassTrips Fahrten die optimiert werden sollen
     * @param type                 aktuell betrachtete Nutzerklasse
     * @return Liste an Fahrten, die jeweils einer Nutzerklasse angehören <br/>
     * Eine Fahrt ist so oft in der Liste enthalten, wie sie Personen des Types ohne Ticket hat
     * @preconditions Zeitoptimierung ist abgeschlossen
     */
    public static ArrayList<TripItem> prepareNumTicketOptimisation(HashMap<Integer, ArrayList<TripItem>> sortedUserClassTrips, Fare.Type type) {
        /**
         * Zählt, wie oft eine Fahrt enthalten ist
         */
        class tripCounter {
            int quantity;
            TripItem tripItem;

            tripCounter(TripItem tripItem) {
                this.tripItem = tripItem;
                quantity = 1;
            }

            public boolean equals(Object o) {
                if (!(o instanceof tripCounter)) {
                    return false;
                }
                return ((tripCounter) o).tripItem.getTripID().equals(tripItem.getTripID());
            }
        }
        //Lösen der Verbindung Personennr. <-> TripListe, stattdessen eine gemeinsame Liste
        ArrayList<tripCounter> trips = new ArrayList<>();
        for (Integer i : sortedUserClassTrips.keySet()) {
            for (TripItem item : sortedUserClassTrips.get(i)) {
                if (item.getUserClassWithoutTicket(type) != 0) {
                    int index = trips.indexOf(new tripCounter(item));
                    if (index > -1) {
                        trips.get(index).quantity++;
                    } else {
                        trips.add(new tripCounter(item));
                    }
                }
            }
        }
        //Neue Liste erstellen, in der jedes Item so oft vor kommt, wie es quantity besitzt.
        ArrayList<TripItem> tripItemArrayList = new ArrayList<>();
        for (tripCounter t : trips) {
            for (int j = 1; j <= t.quantity; j++) {
                tripItemArrayList.add(t.tripItem);
            }
        }
        return tripItemArrayList;
    }

    /**
     * Zusammenfassen von den alten Tickets, den NumTickets und den TimeTickets <br/>
     * <p>
     * Erzeugen einer HashMap, die für jede Nutzerklasse alle jeweiligen Fahrscheine enthält
     *
     * @param timeTickets   neue Zeitfahrscheine
     * @param numTicket     neue Fahrscheine mit fester Fahrtenanzahl
     * @param activeTickets bereits gekaufte Fahrscheine, die noch aktuell sind
     */
    public static void sumUpNumAndTimeAndOldTickets(HashMap<Fare.Type, ArrayList<TicketToBuy>> timeTickets,
                                                    HashMap<Fare.Type, ArrayList<TicketToBuy>> numTicket,
                                                    HashMap<Fare.Type, ArrayList<TicketToBuy>> activeTickets) {
        ArrayList<TicketToBuy> allTickets = timeTickets.get(Fare.Type.ADULT);
        ArrayList<TicketToBuy> adultNumTickets = numTicket.get(Fare.Type.ADULT);
        ArrayList<TicketToBuy> adultOldTickets = activeTickets.get(Fare.Type.ADULT);
        if (allTickets == null) {
            allTickets = new ArrayList<>();
        }
        if (adultNumTickets == null) {
            adultNumTickets = new ArrayList<>();
        }
        if (adultOldTickets == null) {
            adultOldTickets = new ArrayList<>();
        }
        ArrayList<TicketToBuy> childrenTimeTickets = timeTickets.get(Fare.Type.CHILD);
        ArrayList<TicketToBuy> childrenNumTickets = numTicket.get(Fare.Type.CHILD);
        ArrayList<TicketToBuy> childrenOldTickets = activeTickets.get(Fare.Type.CHILD);
        if (childrenTimeTickets == null) {
            childrenTimeTickets = new ArrayList<>();
        }
        if (childrenNumTickets == null) {
            childrenNumTickets = new ArrayList<>();
        }
        if (childrenOldTickets == null) {
            childrenOldTickets = new ArrayList<>();
        }

        allTickets.addAll(adultNumTickets);
        allTickets.addAll(adultOldTickets);
        childrenTimeTickets.addAll(childrenNumTickets);
        childrenTimeTickets.addAll(childrenOldTickets);
    }

    /**
     * Optimierung mit alten Fahrscheinen <br/>
     * <p>
     * Dabei werden wenn möglich, zu erst aktive, gespeicherte Zeitfahrscheine verwendet, anschließend
     * wird versucht übrige NumTickets zu verwenden.
     *
     * @param trips         zu optimierende Fahrten
     * @param activeTickets gespeicherte Tickets mit mind. einer freien Fahrt, oder deren Dauer bereits angefangen hat,
     *                      aber noch nicht vorbei ist.
     * @preconditions Fahrten, die nicht optimiert werden sollen / können wurden aussortiert
     * @postconditions Fahrten, die keinen passenden Fahrschein erhalten haben, erhalten einen neuen Fahrschein
     */
    public static void optimisationWithOldTickets(HashMap<Fare.Type, ArrayList<TripItem>> trips, HashMap<Fare.Type, ArrayList<TicketToBuy>> activeTickets) {
        HashMap<Fare.Type, ArrayList<TicketToBuy>> numTicketsHM = new HashMap<>();
        //Für jede Nutzerklasse gucken
        for (Fare.Type type : activeTickets.keySet()) {
            ArrayList<TicketToBuy> timeTickets = new ArrayList<>();
            ArrayList<TicketToBuy> numTickets = new ArrayList<>();
            if (trips.get(type) == null) {
                continue;
            }
            //Aufsplitten der gespeicherten Fahrscheine in NumTickets und TimeTickets
            if (activeTickets.get(type) == null) {
                continue;
            }
            for (TicketToBuy ticketToBuy : activeTickets.get(type)) {
                if (ticketToBuy.getTicket() instanceof NumTicket) {
                    numTickets.add(ticketToBuy);
                } else {
                    timeTickets.add(ticketToBuy);
                }
            }
            //Fahrten der Liste hinzufügen
            for (TripItem current : trips.get(type)) {
                //Wenn die Nutzerklasse kein Ticket mehr benötigt, muss die Fahrt nicht in die entpsprechende Liste
                //gepackt werden
                if (current.getUserClassWithoutTicket(type) == 0) {
                    continue;
                }
                //Prüfen, ob ein aktives TimeTicket für die Fahrt genutzt werden kann
                oldTimeTicketOptimisation(timeTickets, current);
            }
            numTicketsHM.put(type, numTickets);
        }
        //Optimierung mit NumTickets
        OptimisationUtil.optimisationWithOldTickets(numTicketsHM, trips);
    }

    /**
     * Optimierung mit Zeitfahrscheinen <br/>
     * <p>
     * Für die Optimierung selbst wird {@link #timeOptimisation(HashMap sortedUserClassTrips, HashMap timeTickets, HashMap allTicketLists, Fare.Type)} aufgerufen. <br/>
     * Sammelt die Fahrten jeder Nutzerstufe, die noch kein Ticket erhalten haben
     *
     * @param sortedTrips    Zu optimierende Fahrten
     * @param allTicketLists Liste aller benötigten Fahrscheine
     * @return Fahrten ohne Tickets
     * @preconditions Aufteilung der Fahrten auf Nutzerstufen und Personennr.
     * @postconditions Optimierung der verbleibenden Fahrten ohne Ticket
     */
    public static HashMap<Fare.Type, ArrayList<TripItem>> timeOptimisation(HashMap<Fare.Type, HashMap<Integer, ArrayList<TripItem>>> sortedTrips, HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketLists) {
        HashMap<Fare.Type, ArrayList<TripItem>> tripsWithoutTimeTicket = new HashMap<>();
        for (Fare.Type type : sortedTrips.keySet()) {
            HashMap<Integer, ArrayList<TripItem>> sortedUserClassTrips = sortedTrips.get(type);
            ArrayList<TicketToBuy> ticketToBuyArrayList = new ArrayList<>();
            allTicketLists.put(type, ticketToBuyArrayList);
            if (sortedUserClassTrips == null) {
                continue;
            }
            HashMap<Fare.Type, ArrayList<TimeTicket>> timeTickets = MainMenu.myProvider.getTimeTickets();
            if (timeTickets.get(type) != null && !timeTickets.get(type).isEmpty()) {
                MyVrrTimeOptimisationHelper.timeOptimisation(sortedUserClassTrips, timeTickets, allTicketLists, type);
            }
            tripsWithoutTimeTicket.put(type, MyVrrTimeOptimisationHelper.prepareNumTicketOptimisation(sortedUserClassTrips, type));
        }
        return tripsWithoutTimeTicket;
    }

    /**
     * Überprüft, ob alle Waben einem Tarifgebiet angehören
     *
     * @param crossedFarezonesSet Waben die durchfahren werden
     * @return true wenn alle Waben / 10 das gleiche Ergebnis liefern, sonst false
     */
    public static boolean checkIsZweiWaben(Set<Integer> crossedFarezonesSet) {
        ArrayList<Integer> crossedFarezones = new ArrayList<>(crossedFarezonesSet);
        int firstZone = crossedFarezones.get(0);
        for (Integer crossedFarezone : crossedFarezones) {
            if (crossedFarezone / 10 != firstZone / 10) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fasst 24h und 48h Tickets, die den gleichen Geltungsbereich haben, und deren Nutzungszeitraum sich komplett überlappt,
     * zu Fahrscheinen mit bis zu 5 Personen zusammen
     *
     * @param ticketList Liste mit Fahrscheinen die zusammen gefasst werden sollen
     * @param allTickets Fahrscheine, die gekauft werden sollen
     * @param days       ob das 24h Ticket oder das 48h Ticket betrachtet werden soll
     */
    public static void sumUpTicketsUpTo5Persons(ArrayList<TicketToBuy> ticketList,
                                                ArrayList<TicketToBuy> allTickets,
                                                int days) {
        Collections.sort(ticketList, new TicketToBuyTimeComporator());
        ArrayList<Integer> collectedIndices = new ArrayList<>();
        for (int i = 0; i < ticketList.size(); i++) {
            if (collectedIndices.indexOf(i) > -1) {
                continue;
            }
            ArrayList<TicketToBuy> collectedTickets = new ArrayList<>();
            TicketToBuy current = ticketList.get(i);
            collectedTickets.add(current);
            collectedIndices.add(i);
            for (int j = i + 1; j < ticketList.size() && collectedTickets.size() < 5; j++) {
                if (collectedIndices.indexOf(j) > -1) {
                    continue;
                }
                TicketToBuy next = ticketList.get(j);
                //Laufzeitoptimierung
                if (next.getFirstDepartureTime().getTime() - current.getFirstDepartureTime().getTime() > ((TimeTicket) current.getTicket()).getMaxDuration()) {
                    break;
                }
                if (next.getLastArrivalTime().getTime() - current.getFirstDepartureTime().getTime() <= ((TimeTicket) current.getTicket()).getMaxDuration()
                        && current.checkFarezones(next.getTripList())) {
                    collectedTickets.add(next);
                    collectedIndices.add(j);
                }
            }
            TicketToBuy newTicket;
            if (days == 1) {
                newTicket = oneDayTicket(current.getPreisstufe(), collectedTickets.size());
            } else {
                newTicket = twoDayTicket(current.getPreisstufe(), collectedTickets.size());
            }
            for (TicketToBuy t : collectedTickets) {
                newTicket.addTripItems(t.getTripList());
            }
            newTicket.setValidFarezones(collectedTickets.get(0).getValidFarezones(), collectedTickets.get(0).getMainRegionID(), collectedTickets.get(0).isZweiWabenTarif());
            allTickets.add(newTicket);
        }
    }

    /**
     * Erzeugt ein neues 24h Ticket für numP viele Personen
     *
     * @param preisstufe Preisstufe des Tickets
     * @param numP       Anzahl reisender Personen
     * @return TicketToBuy der übergebenen Preisstufe für numP viele Personen
     * @preconditions numP <= 5
     */
    private static TicketToBuy oneDayTicket(String preisstufe, int numP) {
        switch (numP) {
            case 1:
                return new TicketToBuy(MyVRRprovider.tagesTicket_1, preisstufe);
            case 2:
                return new TicketToBuy(MyVRRprovider.tagesTicket_2, preisstufe);
            case 3:
                return new TicketToBuy(MyVRRprovider.tagesTicket_3, preisstufe);
            case 4:
                return new TicketToBuy(MyVRRprovider.tagesTicket_4, preisstufe);
            default:
                return new TicketToBuy(MyVRRprovider.tagesTicket_5, preisstufe);
        }
    }

    /**
     * Erzeugt ein neues 48h Ticket für numP viele Personen
     *
     * @param preisstufe Preisstufe des Tickets
     * @param numP       Anzahl reisender Personen
     * @return TicketToBuy der übergebenen Preisstufe für numP viele Personen
     * @preconditions numP <= 5
     */
    private static TicketToBuy twoDayTicket(String preisstufe, int numP) {
        switch (numP) {
            case 1:
                return new TicketToBuy(MyVRRprovider.zweiTagesTicket_1, preisstufe);
            case 2:
                return new TicketToBuy(MyVRRprovider.zweiTagesTicket_2, preisstufe);
            case 3:
                return new TicketToBuy(MyVRRprovider.zweiTagesTicket_3, preisstufe);
            case 4:
                return new TicketToBuy(MyVRRprovider.zweiTagesTicket_4, preisstufe);
            default:
                return new TicketToBuy(MyVRRprovider.zweiTagesTicket_5, preisstufe);
        }
    }

    /**
     * Überprüft, ob ein aktives Ticket für die Fahrt benutzt werden kann <br/>
     *
     * @param timeTickets Liste aktiver TimeTickets
     * @param current zu optimierende Fahrt
     */
    private static void oldTimeTicketOptimisation(ArrayList<TicketToBuy> timeTickets, TripItem current) {
        //Prüfen, ob ein aktives TimeTicket für die Fahrt genutzt werden kann
        for (TicketToBuy ticketToBuy : timeTickets) {
            TimeTicket t = (TimeTicket) ticketToBuy.getTicket();
            if (MainMenu.myProvider.getPreisstufenIndex(current.getPreisstufe()) <= MainMenu.myProvider.getPreisstufenIndex(ticketToBuy.getPreisstufe())) {
                if (t.isValidTrip(current)) {
                    if (ticketToBuy.checkFarezone(current)) {
                        long minStartTime = Math.min(current.getFirstDepartureTime().getTime(), ticketToBuy.getFirstDepartureTime().getTime());
                        long maxEndTime = Math.max(current.getLastArrivalTime().getTime(), ticketToBuy.getLastArrivalTime().getTime());
                        if (maxEndTime - minStartTime <= t.getMaxDuration()) {
                            ticketToBuy.addTripItem(current);
                            current.addTicket(ticketToBuy);
                            Collections.sort(ticketToBuy.getTripQuantities(), new TripQuantitiesTimeComparator());
                        }
                    }
                }
            }
        }
    }
}
