package com.example.lkjhgf.recyclerView.futureTrips;

import android.app.Activity;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.MyURLParameter;
import com.example.lkjhgf.helper.ticketOverview.groupedOverview.AllTickets;
import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.recyclerView.possibleConnections.components.JourneyItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import de.schildbach.pte.dto.Fare;

import org.javatuples.Triplet;

import de.schildbach.pte.dto.Trip;

/**
 * Enthält die Informationen für eine Reise
 */
public class TripItem implements Serializable {
    private Trip trip;
    private int startID;
    private Set<Integer> crossedFarezones;
    private String preisstufe;
    private boolean isComplete;
    private MyURLParameter myURLParameter;

    private class TripTicketInformationHolder implements Serializable {
        private Ticket ticket;
        private String ticketPreisstufe;
        private UUID ticketIdentifier;
        private int quantity;

        private TripTicketInformationHolder(Ticket ticket, String ticketPreisstufe, UUID ticketIdentifier) {
            this.ticket = ticket;
            this.ticketPreisstufe = ticketPreisstufe;
            this.ticketIdentifier = ticketIdentifier;
            quantity = 1;
        }

        private boolean checkContains(UUID otherIdentifier) {
            return ticketIdentifier.equals(otherIdentifier);
        }

        private void add() {
            quantity++;
        }

    }

    private HashMap<Fare.Type, Integer> numUserClasses;
    private HashMap<Fare.Type, Integer> usersWithoutTicket;
    private HashMap<Fare.Type, ArrayList<TripTicketInformationHolder>> allTicketInformations;

    /**
     * Konstruktor für Fahrten, deren Fahrkosten nicht optimiert werden sollen<br/>
     *
     * @param trip       - enthält alle Informationen zur Reise, die unabhängig von der Optimierung sind
     * @param isComplete - gibt an, ob die Fahrt bei der Optimierung berücksichtigt werden soll <br/>
     *                   false - keine Berücksichtigung
     */
    public TripItem(Trip trip, boolean isComplete, MyURLParameter myURLParameter) {
        this.trip = trip;
        this.isComplete = isComplete;
        preisstufe = UtilsString.setPreisstufenName(trip);
        this.myURLParameter = myURLParameter;
        numUserClasses = new HashMap<>();
        usersWithoutTicket = new HashMap<>();
        allTicketInformations = new HashMap<>();

    }

    //Nur zum Testen
    public TripItem(HashMap<Fare.Type, Integer> numUserClasses) {
        trip = null;
        preisstufe = "";
        this.numUserClasses = new HashMap(numUserClasses);
        allTicketInformations = new HashMap<>();
        usersWithoutTicket = new HashMap(numUserClasses);
    }

    //Nur zum Testen
    public TripItem(Set<Integer> crossedFarezones, int startID) {
        this(new HashMap<>());
        this.crossedFarezones = crossedFarezones;
        this.startID = startID;
    }

    /**
     * Konstruktor für Fahrten, deren Fahrkosten optimiert wrden sollen <br/>
     *
     * @param trip           - enthält alle Informationen zur Reise, die unabhängig von der Optimierung sind
     * @param isComplete     - gibt an, ob die Fahrt bei der Optimierung berücksichtigt werdne soll <br/>
     *                       true - soll berücksichtigt werden
     * @param numUserClasses gibt an, wie viele Personen einer Nutzerklasse reisen - stimmt zu Beginn mit der Anzahl
     *                       Personen ohne Ticket überein
     */
    public TripItem(Trip trip, boolean isComplete, HashMap<Fare.Type, Integer> numUserClasses, int startID, Set<Integer> crossedFarezones, MyURLParameter myURLParameter) {
        this(trip, isComplete,myURLParameter);
        this.startID = startID;
        this.crossedFarezones = crossedFarezones;
        this.numUserClasses = new HashMap<>(numUserClasses);
        this.usersWithoutTicket = new HashMap<>(numUserClasses);
    }

    public Trip getTrip() {
        return trip;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public int getStartID() {
        return startID;
    }

    public Set<Integer> getCrossedFarezones() {
        return crossedFarezones;
    }

    public int getNumUserClass(Fare.Type type) {
        try {
            return numUserClasses.get(type);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Fügt der Fahrt ein weiteres Ticket hinzu <br/>
     * <p>
     * Wenn das Ticket bereits enthalten ist, dann wird nur die Häufigkeit des Tickets erhöht, sonst wird
     * das Ticket neu in die Liste der zugeordneten Tickets eingefügt <br/>
     * Jedes Ticket verringert die Anzahl der Nutzer ohne Tickets um 1
     *
     * @param ticket Ticket, dass der Fahrt hinzugefügt werden soll
     */
    public void addTicket(TicketToBuy ticket) {
        ArrayList<TripTicketInformationHolder> c = allTicketInformations.get(ticket.getTicket().getType());
        if (c == null || c.isEmpty()) {
            c = new ArrayList<>();
            allTicketInformations.put(ticket.getTicket().getType(), c);
        }
        if (c.isEmpty()) {
            c.add(new TripTicketInformationHolder(ticket.getTicket(), ticket.getPreisstufe(), ticket.getTicketID()));
        } else {
            boolean contains = false;
            for (TripTicketInformationHolder t : c) {
                if (t.checkContains(ticket.getTicketID())) {
                    t.add();
                    contains = true;
                }
            }
            if (!contains) {
                c.add(new TripTicketInformationHolder(ticket.getTicket(), ticket.getPreisstufe(), ticket.getTicketID()));
            }
        }
        if (ticket.getTicket() instanceof TimeTicket) {
            int value = Math.max(0, usersWithoutTicket.get(ticket.getTicket().getType()) - ((TimeTicket) ticket.getTicket()).getNumPersons());
            usersWithoutTicket.put(ticket.getTicket().getType(), value);
        } else {
            usersWithoutTicket.put(ticket.getTicket().getType(), usersWithoutTicket.get(ticket.getTicket().getType()) - 1);
        }
    }

    /**
     * Erzeugt einen String zur Ausgabe aller benötigter Fahrscheine für die Fahrt <br/>
     * <p>
     * In diesem String sind gleiche Tickets (d.h. Fahrschein und Preisstufe identisch) zusammengefasst,
     * so dass für den Nutzer direkt ersichtlich ist, welche Fahrscheine er wie oft für diese Fahrt benötigt
     *
     * @return einen String, der alle Informationen zu den Fahrscheinen enthält
     */
    String getTicketListAsString() {
        Triplet<ArrayList<Ticket>, ArrayList<Integer>, ArrayList<String>> allInformations = shortTicketInformationNumTicket();
        return buildStringTicketList(allInformations.getValue0(), allInformations.getValue1(), allInformations.getValue2()).toString();
    }

    /**
     * Fasst die Tickets mit fester Fahrtenanzahl zusammen, und zählt die entsprechende Häufigkeit
     *
     * @return Ein Triple mit den Tickets, der jeweiligen Häufigkeit und den Preisstufen
     */
    private Triplet<ArrayList<Ticket>, ArrayList<Integer>, ArrayList<String>> shortTicketInformationNumTicket() {
        ArrayList<Ticket> ticketsToUse = new ArrayList<>();
        ArrayList<String> preisstufeToUse = new ArrayList<>();
        ArrayList<Integer> num = new ArrayList<>();

        for (Fare.Type type : allTicketInformations.keySet()) {
            ArrayList<TripTicketInformationHolder> ticketList = allTicketInformations.get(type);
            if (ticketList != null && !ticketList.isEmpty()) {
                ticketsToUse.add(ticketList.get(0).ticket);
                preisstufeToUse.add(ticketList.get(0).ticketPreisstufe);
                num.add(ticketList.get(0).quantity);
                for (int i = 1; i < ticketList.size(); i++) {
                    boolean contains = false;
                    for (int j = 0; j < ticketsToUse.size() && !contains; j++) {
                        if (ticketList.get(i).ticket.getName().equals(ticketsToUse.get(j).getName())) {
                            if (ticketList.get(i).ticketPreisstufe.equals(preisstufeToUse.get(j))) {
                                num.set(j, num.get(j) + ticketList.get(i).quantity);
                            } else {
                                ticketList.add(ticketList.get(i));
                                preisstufeToUse.add(ticketList.get(i).ticketPreisstufe);
                                num.add(ticketList.get(i).quantity);
                            }
                            contains = true;
                        }
                    }
                    if (!contains) {
                        ticketsToUse.add(ticketList.get(i).ticket);
                        preisstufeToUse.add(ticketList.get(i).ticketPreisstufe);
                        num.add(ticketList.get(i).quantity);
                    }
                }
            }
        }
        return Triplet.with(ticketsToUse, num, preisstufeToUse);
    }

    private ArrayList<TicketToBuy> shortTicketInformationTimeTicket(Activity activity) {
        ArrayList<TicketToBuy> tripTickets = new ArrayList<>();
        HashMap<Fare.Type, ArrayList<TicketToBuy>> allTickets = AllTickets.loadTickets(activity);
        for (Fare.Type type : allTicketInformations.keySet()) {
            ArrayList<TripTicketInformationHolder> informationHolder = allTicketInformations.get(type);
            ArrayList<TicketToBuy> savedTickets = allTickets.get(type);
            if (!informationHolder.isEmpty() && informationHolder != null
                    && !savedTickets.isEmpty() && savedTickets != null) {
                for (int i = 0; i < informationHolder.size(); i++) {
                    for (int j = 0; j < savedTickets.size(); j++) {
                        if (informationHolder.get(i).ticketIdentifier.equals(savedTickets.get(j).getTicketID())) {
                            if (savedTickets.get(j).getTicket() instanceof TimeTicket) {
                                tripTickets.add(savedTickets.get(j));
                            }
                            break;
                        }
                    }
                }

            }
        }
        return tripTickets;
    }

    /**
     * String in dem die Tickets, ihre Häufigkeit sowie Preisstufe enthalten ist. Zusätzlich ist auch der Ort
     * des Entwertens angegeben
     *
     * @return Preisstufe, Ticket, Häufigkeit, Entwerten des Tickets als String
     */
    public String getDetailedTicketListAsString(Activity activity) {
        Triplet<ArrayList<Ticket>, ArrayList<Integer>, ArrayList<String>> allInformations = shortTicketInformationNumTicket();
        String numTicketInformation = MainMenu.myProvider.getTicketInformationNumTicket(allInformations.getValue0(), allInformations.getValue1(), allInformations.getValue2(), this);
        String timeTicketInformation = MainMenu.myProvider.getTicketInformationTimeTicket(shortTicketInformationTimeTicket(activity));
        return numTicketInformation + "\n" + timeTicketInformation;
    }

    /**
     * Überprüft, ob mindestens ein Ticket der Fahrt zugeordnet ist
     *
     * @return true - keine Tickets zugeordnet, false - mindestens ein Ticket
     */
    public boolean hasNoTicket() {
        for (Fare.Type key : allTicketInformations.keySet()) {
            if (!allTicketInformations.get(key).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<UUID> getTicketIDs(Fare.Type type) {
        ArrayList<UUID> ticketIDs = new ArrayList<>();
        if (allTicketInformations.containsKey(type)) {
            for (TripTicketInformationHolder t : allTicketInformations.get(type)) {
                ticketIDs.add(t.ticketIdentifier);
            }
        }
        return ticketIDs;
    }

    public void removeTickets(Fare.Type type, UUID uuid) {
        removeTickets(type, uuid, 1);
    }

    /**
     * Entfernt das Ticket mit der jeweiligen ID
     *
     * @param type gibt den Type des Tickets an, beschleunigt das Finden des Tickets
     * @param uuid ID des zu löschenden Tickets
     */
    public void removeTickets(Fare.Type type, UUID uuid, int quantity) {
        ArrayList<TripTicketInformationHolder> d = allTicketInformations.get(type);
        for (Iterator<TripTicketInformationHolder> iterator = d.iterator(); iterator.hasNext(); ) {
            TripTicketInformationHolder current = iterator.next();
            if (current.ticketIdentifier.equals(uuid)) {
                current.quantity -= quantity;
                if (current.quantity == 0) {
                    iterator.remove();
                }
                int numPersonsOnTicket;
                if (current.ticket instanceof TimeTicket) {
                    numPersonsOnTicket = ((TimeTicket) current.ticket).getNumPersons();
                } else {
                    numPersonsOnTicket = quantity;
                }
                //TODO
                int value = Math.min(numPersonsOnTicket + usersWithoutTicket.get(type), numUserClasses.get(type));
                usersWithoutTicket.put(type, value);
            }
        }
    }

    /**
     * Zwei Fahrten sind identisch, wenn sie die gleiche ID besitzen <br/>
     * Die Erzeugung von IDs erfolgt in {@link Trip#buildSubstituteId()} <br/>
     *
     * @param o zu Vergleichendes Objekt
     * @return boolean - Gibt an, ob zwei Fahrten identisch sind
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TripItem)) {
            return false;
        }
        TripItem otherTripItem = (TripItem) o;
        return otherTripItem.getTripID().equals(this.getTripID());
    }

    public HashMap<Fare.Type, Integer> getNumUserClasses() {
        return numUserClasses;
    }

    ArrayList<JourneyItem> getJourneyItems() {
        return UtilsList.journeyItems(trip.legs);
    }

    public String getPreisstufe() {
        return preisstufe;
    }

    public Date getFirstDepartureTime() {
        return trip.getFirstDepartureTime();
    }

    public Date getLastArrivalTime() {
        return trip.getLastArrivalTime();
    }

    public int getUserClassWithoutTicket(Fare.Type type) {
        return usersWithoutTicket.get(type);
    }

    public String getTripID() {
        return trip.getId();
    }

    public MyURLParameter getMyURLParameter(){
        return myURLParameter;
    }

    /**
     * Erzeugt den zusammenhängenden String aus den Parametern
     *
     * @param ticketsToUse    zu nutzende Fahrscheine
     * @param num             wie oft dieses Ticket gebraucht wird
     * @param preisstufeToUse Preisstufe des Tickets
     * @return Stringbuilder zusammengebaut aus den Informationen
     */
    private StringBuilder buildStringTicketList(ArrayList<Ticket> ticketsToUse, ArrayList<Integer> num, ArrayList<String> preisstufeToUse) {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < ticketsToUse.size(); i++) {
            if (ticketsToUse.get(i) instanceof NumTicket) {
                NumTicket numTicket = (NumTicket) ticketsToUse.get(i);
                int wholeTickets = num.get(i) / numTicket.getNumTrips();
                int restTicket = num.get(i) % numTicket.getNumTrips();
                if (wholeTickets != 0) {
                    value.append(wholeTickets).append("x ").append(numTicket.toString()).append("\n \tPreisstufe: ").append(preisstufeToUse.get(i)).append("\n \talle Fahrten entwerten");
                }
                if (restTicket != 0) {
                    value.append("1x ").append(numTicket.toString()).append(" \n \tPreisstufe: ").append(preisstufeToUse.get(i)).append("\n \t").append(restTicket).append("x entwerten");
                }
            } else {
                TimeTicket timeTicket = (TimeTicket) ticketsToUse.get(i);
                int wholeTickets = num.get(i);
                value.append(wholeTickets).append("x ").append(timeTicket.toString()).append("\n \tPreisstufe: ").append(preisstufeToUse.get(i));
            }
            if (i != ticketsToUse.size() - 1) {
                value.append("\n");
            }
        }
        return value;
    }
}