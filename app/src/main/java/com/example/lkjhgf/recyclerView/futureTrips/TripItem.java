package com.example.lkjhgf.recyclerView.futureTrips;

import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.possibleConnections.components.JourneyItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import de.schildbach.pte.dto.Fare;
import de.schildbach.pte.dto.Trip;

/**
 * Enthält die Informationen für eine Reise
 */
public class TripItem implements Serializable {
    private Trip trip;
    private String preisstufe;
    private boolean isComplete;

    private class TripTicketInformationHolder {
        Ticket ticket;
        String ticketPreisstufe;
        UUID ticketIdentifier;

        private TripTicketInformationHolder(Ticket ticket, String ticketPreisstufe, UUID ticketIdentifier) {
            this.ticket = ticket;
            this.ticketPreisstufe = ticketPreisstufe;
            this.ticketIdentifier = ticketIdentifier;
        }
    }

    private HashMap<Fare.Type, Integer> numUserClasses;
    private HashMap<Fare.Type, ArrayList<TripTicketInformationHolder>> allTicketInformations;


    /**
     * Kurze Zusammenfassung der Abfolge von Verkehrsmitteln
     */
    private ArrayList<JourneyItem> journeyItems;

    /**
     * Konstruktor für Fahrten, deren Fahrkosten nicht optimiert werden sollen<br/>
     *
     * @param trip       - enthält alle Informationen zur Reise, die unabhängig von der Optimierung sind
     * @param isComplete - gibt an, ob die Fahrt bei der Optimierung berücksichtigt werden soll <br/>
     *                   false - keine Berücksichtigung
     */
    public TripItem(Trip trip, boolean isComplete) {
        this.trip = trip;
        this.isComplete = isComplete;
        if (trip.fares != null) {
            preisstufe = trip.fares.get(0).units;
        } else {
            preisstufe = "?";
        }
        numUserClasses = new HashMap<>();
        journeyItems = UtilsList.journeyItems(trip.legs);

        allTicketInformations = new HashMap<>();
    }

    /**
     * Konstruktor für Fahrten, deren Fahrkosten optimiert wrden sollen <br/>
     *
     * @param trip           - enthält alle Informationen zur Reise, die unabhängig von der Optimierung sind
     * @param isComplete     - gibt an, ob die Fahrt bei der Optimierung berücksichtigt werdne soll <br/>
     *                       true - soll berücksichtigt werden
     * @param numUserClasses gibt an, wie viele Personen einer Nutzerklasse reisen
     */
    public TripItem(Trip trip, boolean isComplete, HashMap<Fare.Type, Integer> numUserClasses) {
        this(trip, isComplete);
        this.numUserClasses = numUserClasses;
    }

    public Trip getTrip() {
        return trip;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public int getNumUserClass(Fare.Type type) {
        try {
            return numUserClasses.get(type);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    ArrayList<JourneyItem> getJourneyItems() {
        return journeyItems;
    }

    public String getPreisstufe() {
        return preisstufe;
    }

    public void addTicket(TicketToBuy ticket) {
        ArrayList<TripTicketInformationHolder> c = allTicketInformations.get(ticket.getTicket().getType());
        if (c == null) {
            c = new ArrayList<>();
            allTicketInformations.put(ticket.getTicket().getType(), c);
        }
        c.add(new TripTicketInformationHolder(ticket.getTicket(), ticket.getPreisstufe(), ticket.getTicketID()));
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
        ArrayList<Ticket> ticketsToUse = new ArrayList<>();
        ArrayList<String> preisstufeToUse = new ArrayList<>();
        ArrayList<Integer> num = new ArrayList<>();

        for (Fare.Type type : allTicketInformations.keySet()) {
            ArrayList<TripTicketInformationHolder> ticketList = allTicketInformations.get(type);


            if (ticketList != null && !ticketList.isEmpty()) {
                ticketsToUse.add(ticketList.get(0).ticket);
                preisstufeToUse.add(ticketList.get(0).ticketPreisstufe);
                num.add(1);

                for (int i = 1; i < ticketList.size(); i++) {
                    boolean contains = false;
                    for (int j = 0; j < ticketsToUse.size() && !contains; j++) {
                        if (ticketList.get(i).ticket.getName().equals(ticketsToUse.get(j).getName())) {
                            if (ticketList.get(i).ticketPreisstufe.equals(preisstufeToUse.get(j))) {
                                num.set(j, num.get(j) + 1);
                                contains = true;
                            } else {
                                ticketList.add(ticketList.get(i));
                                preisstufeToUse.add(ticketList.get(i).ticketPreisstufe);
                                num.add(1);
                                contains = true;
                            }
                        }
                    }
                    if (!contains) {
                        ticketsToUse.add(ticketList.get(i).ticket);
                        preisstufeToUse.add(ticketList.get(i).ticketPreisstufe);
                        num.add(1);
                    }
                }
            }


        }

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
            }

            if (i != ticketsToUse.size() - 1) {
                value.append("\n");
            }
        }

        return value.toString();
    }

    public ArrayList<UUID> getTicketIDs(Fare.Type type) {
        ArrayList<UUID> ticketIDs = new ArrayList<>();
        for (TripTicketInformationHolder t : allTicketInformations.get(type)) {
            ticketIDs.add(t.ticketIdentifier);
        }
        return ticketIDs;
    }

    public void removeTickets(Fare.Type type, UUID uuid) {
        ArrayList<TripTicketInformationHolder> d = allTicketInformations.get(type);
        for (Iterator<TripTicketInformationHolder> iterator = d.iterator(); iterator.hasNext(); ) {
            TripTicketInformationHolder current = iterator.next();
            if (current.ticketIdentifier.equals(uuid)) {
                iterator.remove();
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
        return otherTripItem.trip.getId().equals(this.trip.getId());
    }

    boolean hasNoTicket() {
        for (Fare.Type key : allTicketInformations.keySet()) {
            if (!allTicketInformations.get(key).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public HashMap<Fare.Type, Integer> getNumUserClasses() {
        return numUserClasses;
    }

}
