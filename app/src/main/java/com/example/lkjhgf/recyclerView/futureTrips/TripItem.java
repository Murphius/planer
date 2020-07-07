package com.example.lkjhgf.recyclerView.futureTrips;

import android.app.Activity;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.MyURLParameter;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.helper.util.Utils;
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
    /**
     * Zugeordnete Fahrt
     */
    private Trip trip;
    /**
     * Start Wabe
     */
    private int startID;
    /**
     * Durchfahrene Waben im verlauf der Fahrt
     */
    private Set<Integer> crossedFarezones;
    private String preisstufe;
    /**
     * Gibt an, ob die Fahrt optimiert werden soll oder nicht
     */
    private boolean isComplete;

    private HashMap<Fare.Type, Integer> numUserClasses;
    private HashMap<Fare.Type, Integer> usersWithoutTicket;
    private HashMap<Fare.Type, ArrayList<TripTicketInformationHolder>> allTicketInformations;
    private MyURLParameter myURLParameter;

    /**
     * Konstruktor für Fahrten, deren Fahrkosten nicht optimiert werden sollen<br/>
     *
     * @param trip           - enthält alle Informationen zur Reise, die unabhängig von der Optimierung sind
     * @param isComplete     - gibt an, ob die Fahrt bei der Optimierung berücksichtigt werden soll <br/>
     *                       false - keine Berücksichtigung
     * @param myURLParameter - Für das spätere Aktualisieren von Fahrten benötigt
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

    /**
     * Konstruktor für Fahrten, deren Fahrkosten optimiert wrden sollen <br/>
     *
     * @param trip             - enthält alle Informationen zur Reise, die unabhängig von der Optimierung sind
     * @param isComplete       - gibt an, ob die Fahrt bei der Optimierung berücksichtigt werdne soll <br/>
     *                         true -> soll berücksichtigt werden
     * @param numUserClasses   gibt an, wie viele Personen einer Nutzerklasse reisen - stimmt zu Beginn mit der Anzahl
     *                         Personen ohne Ticket überein
     * @param startID          - Wabe, die als erstes eindeutig identifiziert wird
     * @param crossedFarezones - Alle Tarifgebiete / Waben, die durchfahren werden
     * @param myURLParameter   - Um die Verbindungsdaten später zu Aktualisieren
     */
    public TripItem(Trip trip, boolean isComplete, HashMap<Fare.Type, Integer> numUserClasses, int startID, Set<Integer> crossedFarezones, MyURLParameter myURLParameter) {
        this(trip, isComplete, myURLParameter);
        this.startID = startID;
        this.crossedFarezones = crossedFarezones;
        this.numUserClasses = new HashMap<>(numUserClasses);
        this.usersWithoutTicket = new HashMap<>(numUserClasses);
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
        //Holen der entsprechenden TicketInformationen für die Nutzerklasse des Tickets
        ArrayList<TripTicketInformationHolder> c = allTicketInformations.get(ticket.getTicket().getType());
        if (c == null) {
            c = new ArrayList<>();
            allTicketInformations.put(ticket.getTicket().getType(), c);
        }
        //Wenn noch keine Fahrscheine zugeordnet sind, kann das Ticket ohne überprüfung hinzugefügt werden
        if (c.isEmpty()) {
            c.add(new TripTicketInformationHolder(ticket.getTicket(), ticket.getPreisstufe(), ticket.getTicketID()));
        } else {
            //Überprüfen, ob das Ticket schon der Fahrt zugeordnet ist
            boolean contains = false;
            for (TripTicketInformationHolder t : c) {
                if (t.checkContains(ticket.getTicketID())) {
                    //Falls ja, erhöhe die Häufigkeit
                    t.add();
                    contains = true;
                }
            }
            if (!contains) {
                //Wenn nicht, füge das Ticket neu hinzu
                c.add(new TripTicketInformationHolder(ticket.getTicket(), ticket.getPreisstufe(), ticket.getTicketID()));
            }
        }
        if (ticket.getTicket() instanceof TimeTicket) {
            //Bei Fahrscheinen mit mehreren Personen, muss darauf geachtet werden, dass die Anzahl Personen ohne Ticket nicht negativ wird
            //zB 2 Personen die mit einem Fahrschein für drei Personen reisen
            int value = Math.max(0, usersWithoutTicket.get(ticket.getTicket().getType()) - ((TimeTicket) ticket.getTicket()).getNumPersons());
            usersWithoutTicket.put(ticket.getTicket().getType(), value);
        } else {
            //Dies kann bei Fahrscheinen mit einer festen Anzahl an Fahrten nicht geschehen, in diesem Fall wird die Anzahl Personen ohne Fahrscheine
            //einfach verringert
            usersWithoutTicket.put(ticket.getTicket().getType(), usersWithoutTicket.get(ticket.getTicket().getType()) - 1);
        }
    }

    /**
     * Entfernt das Ticket mit der jeweiligen ID
     * <p>
     * Das Ticket wird aus der Liste der zugeordneten Personen entfernt und die Anzahl Personen ohne
     * Tickets wird erhöht
     *
     * @param type     gibt den Type des Tickets an, beschleunigt das Finden des Tickets
     * @param uuid     ID des zu löschenden Tickets
     * @param quantity gibt an, wie oft das Ticket von der Fahrt entfernt werden soll (zB wenn ein
     *                 4er Ticket der Fahrt 2x zugeordnet ist, müssen zwei Personen ohne Ticket gesetzt
     *                 werden)
     */
    public void removeTicket(Fare.Type type, UUID uuid, int quantity) {
        ArrayList<TripTicketInformationHolder> ticketInformationsUserClass = allTicketInformations.get(type);
        //Prüfe, ob die Fahrt überhaupt ein Ticket für diese Nutzerklasse hat
        if (ticketInformationsUserClass == null) {
            return;
        }
        //Prüfe alle zugeordneten Tickets, ob das zu löschende Ticket dabei ist
        for (Iterator<TripTicketInformationHolder> iterator = ticketInformationsUserClass.iterator(); iterator.hasNext(); ) {
            TripTicketInformationHolder current = iterator.next();
            if (current.ticketIdentifier.equals(uuid)) {
                //Das zu löschende Ticket
                //Die Häufigkeit des Tickets um die angegebene Anzahl verringern
                current.quantity -= quantity;
                //Wenn das Ticket dadurch nicht mehr dieser Fahrt zugeordnet ist, lösche das Ticket aus der Liste zugeordneter Tickets
                //Wird nicht zwangsweise 0, zB wenn die Fahrt editiert wird von 3 auf 2 Personen und das Ticket bereits angefangen ist
                if (current.quantity == 0) {
                    iterator.remove();
                }
                //Die Anzahl Personen ohne Tickets muss erhöht werden
                int numPersonsOnTicket;
                if (current.ticket instanceof TimeTicket) {
                    numPersonsOnTicket = ((TimeTicket) current.ticket).getNumPersons();
                } else {
                    numPersonsOnTicket = quantity;
                }
                //Beim freisetzen, muss beachtet werden, dass es nicht mehr Personen kein Ticket haben, als
                //vom Nutzer angegeben
                int value = Math.min(numPersonsOnTicket + usersWithoutTicket.get(type), numUserClasses.get(type));
                usersWithoutTicket.put(type, value);
                break;
            }
        }
    }

    /**
     * Überprüft, ob mindestens ein Ticket der Fahrt zugeordnet ist
     * <p>
     * Dafür wird für jede Nutzerklasse geprüft, ob diese mindestens ein Ticket besitzt
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

    /**
     * Aktualisiert die Verbindung des TripItems <br/>
     *
     * @param trip Neue Verbindung
     * @preconditions Der Nutzer hat in der Detailansicht auf Aktualisieren geklickt und
     * anschließend auf zurück
     */
    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Trip getTrip() {
        return trip;
    }

    public String getPreisstufe() {
        return preisstufe;
    }

    ArrayList<JourneyItem> getJourneyItems() {
        return UtilsList.journeyItems(trip.legs);
    }

    public HashMap<Fare.Type, Integer> getNumUserClasses() {
        return numUserClasses;
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

    public MyURLParameter getMyURLParameter() {
        return myURLParameter;
    }

    /**
     * Liefert die Anzahl Personen in der jeweiligen Personenklasse
     *
     * @param type Nutzerklasse
     * @return Anzahl Personen in der Nutzerklasse
     */
    public int getNumUserClass(Fare.Type type) {
        try {
            return numUserClasses.get(type);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Wie viele Personen der Nutzerklasse noch kein Ticket besitzen
     *
     * @param type Nutzerklasse
     * @return #Personen ohne Ticket in der Nutzerklasse
     */
    public int getUserClassWithoutTicket(Fare.Type type) {
        return usersWithoutTicket.get(type);
    }

    /**
     * Liefert alle Ticket-IDs, die der Fahrt für die jeweilige Nutzerklasse zugeordnet sind
     *
     * @param type Nutzerklasse
     * @return Ticket-IDs der zugeordneten Fahrten in der Nutzerklasse
     */
    public ArrayList<UUID> getTicketIDs(Fare.Type type) {
        ArrayList<UUID> ticketIDs = new ArrayList<>();
        if (allTicketInformations.containsKey(type)) {
            for (TripTicketInformationHolder t : allTicketInformations.get(type)) {
                ticketIDs.add(t.ticketIdentifier);
            }
        }
        return ticketIDs;
    }

    /**
     * Liefert die erste Abfahrtszeit <br/>
     */
    public Date getFirstDepartureTime() {
        return trip.getFirstDepartureTime();
    }

    public String getTripID() {
        return trip.getId();
    }

    /**
     * Liefert die letzte Ankunftszeit
     */
    public Date getLastArrivalTime() {
        return trip.getLastArrivalTime();
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
        Triplet<ArrayList<Ticket>, ArrayList<Integer>, ArrayList<String>> allInformations = Utils.shortTicketInformationNumTicket(allTicketInformations);
        return UtilsString.buildStringTicketList(allInformations.getValue0(), allInformations.getValue1(), allInformations.getValue2()).toString();
    }

    /**
     * String in dem die Tickets, ihre Häufigkeit sowie Preisstufe enthalten ist. Zusätzlich ist auch der Ort des Entwertens angegeben <br/>
     * <p>
     * Hierbei werden nur Fahrscheine mit einer festen Anzahl an Fahrten zusammengefasst
     *
     * @return Preisstufe, Ticket, Häufigkeit, Entwerten des Tickets als String
     */
    public String getDetailedTicketListAsString(Activity activity) {
        Triplet<ArrayList<Ticket>, ArrayList<Integer>, ArrayList<String>> allInformations = Utils.shortTicketInformationNumTicket(allTicketInformations);
        String numTicketInformation = MainMenu.myProvider.getTicketInformationNumTicket(allInformations.getValue0(), allInformations.getValue1(), allInformations.getValue2(), this);
        String timeTicketInformation = MainMenu.myProvider.getTicketInformationTimeTicket(UtilsList.shortTicketInformationTimeTicket(activity, allTicketInformations));
        return numTicketInformation + "\n" + timeTicketInformation;
    }

    /**
     * Konstruktor für JUnit Tests <br/>
     * <p>
     * JUnit Tests bei denen die Anzahl der reisenden Personen berücksichtigt werden soll
     */
    public TripItem(HashMap<Fare.Type, Integer> numUserClasses) {
        trip = null;
        preisstufe = "";
        this.numUserClasses = new HashMap(numUserClasses);
        allTicketInformations = new HashMap<>();
        usersWithoutTicket = new HashMap(numUserClasses);
    }

    /**
     * Konstruktor für JUnit Tests <br/>
     * <p>
     * <p>
     * JUnit Tests, bei denen die Regionen berücksichtigt werden sollen
     *
     * @param crossedFarezones Tarifgebiete, die durchfahren werden
     * @param startID          Startwabe
     */
    public TripItem(Set<Integer> crossedFarezones, int startID) {
        this(new HashMap<>());
        this.crossedFarezones = crossedFarezones;
        this.startID = startID;
    }
}