package com.example.lkjhgf.recyclerView.futureTrips;

import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.recyclerView.possibleConnections.components.JourneyItem;

import java.io.Serializable;
import java.util.ArrayList;

import de.schildbach.pte.dto.Trip;

/**
 * Enthält die Informationen für eine Reise
 */
public class TripItem implements Serializable {
    private Trip trip;
    private String preisstufe;
    private boolean isComplete;
    private int numAdult, numChildren;
    private ArrayList<Ticket> ticketList;
    private ArrayList<String> ticketPreisstufenList;


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
        if(trip.fares != null){
           preisstufe = trip.fares.get(0).units;
        }else{
            preisstufe = "?";
        }
        numAdult = 0;
        numChildren = 0;
        journeyItems = UtilsList.journeyItems(trip.legs);
        ticketList = new ArrayList<>();
        ticketPreisstufenList = new ArrayList<>();
    }

    /**
     * Konstruktor für Fahrten, deren Fahrkosten optimiert wrden sollen <br/>
     *
     * @param trip        - enthält alle Informationen zur Reise, die unabhängig von der Optimierung sind
     * @param isComplete  - gibt an, ob die Fahrt bei der Optimierung berücksichtigt werdne soll <br/>
     *                    true - soll berücksichtigt werden
     * @param numAdult    - gibt die Anzahl reisender Kinder an
     * @param numChildren - gibt die Anzahl reisender Erwachsener an
     */
    public TripItem(Trip trip, boolean isComplete, int numAdult, int numChildren) {
        this(trip, isComplete);
        this.numAdult = numAdult;
        this.numChildren = numChildren;
    }

    public Trip getTrip() {
        return trip;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public int getNumAdult() {
        return numAdult;
    }

    public int getNumChildren() {
        return numChildren;
    }

    ArrayList<JourneyItem> getJourneyItems() {
        return journeyItems;
    }

    public String getPreisstufe(){
        return preisstufe;
    }

    public void addTicket(Ticket ticket, String ticketPreisstufe){
        ticketList.add(ticket);
        ticketPreisstufenList.add(ticketPreisstufe);
    }

    /**
     * Erzeugt einen String zur Ausgabe aller benötigter Fahrscheine für die Fahrt <br/>
     *
     * In diesem String sind gleiche Tickets (d.h. Fahrschein und Preisstufe identisch) zusammengefasst,
     * so dass für den Nutzer direkt ersichtlich ist, welche Fahrscheine er wie oft für diese Fahrt benötigt
     *
     * @return einen String, der alle Informationen zu den Fahrscheinen enthält
     */
    public String getTicketListAsString(){
        ArrayList<Ticket> ticketsToUse = new ArrayList<>();
        ArrayList<String> preisstufeToUse = new ArrayList<>();
        ArrayList<Integer> num = new ArrayList<>();

        ticketsToUse.add(ticketList.get(0));
        preisstufeToUse.add(ticketPreisstufenList.get(0));
        num.add(1);

        for(int i = 1; i < ticketList.size(); i++){
            boolean contains = false;
            for(int j = 0; j < ticketsToUse.size() && !contains; j++){
                if(ticketList.get(i).getName().equals(ticketsToUse.get(j).getName())){
                    if(ticketPreisstufenList.get(i).equals(preisstufeToUse.get(j))){
                        num.set(j, num.get(j)+ 1);
                        contains = true;
                    }else{
                        ticketList.add(ticketList.get(i));
                        preisstufeToUse.add(ticketPreisstufenList.get(i));
                        num.add(1);
                        contains = true;
                    }
                }
            }
            if(!contains){
                ticketsToUse.add(ticketList.get(i));
                preisstufeToUse.add(ticketPreisstufenList.get(i));
                num.add(1);
            }
        }

        String value = "";

        for(int i = 0; i < ticketsToUse.size(); i++){
            if(ticketsToUse.get(i) instanceof NumTicket){
                NumTicket numTicket = (NumTicket) ticketsToUse.get(i);
                int ganzeTickets = num.get(i) / numTicket.getNumTrips();
                int restTicket = num.get(i) % numTicket.getNumTrips();
                if(ganzeTickets != 0){
                    value += ganzeTickets + "x " + numTicket.toString() + "\n \tPreisstufe: " +preisstufeToUse.get(i) + "\n \talle Fahrten entwerten";
                }
                if(restTicket != 0){
                    value +=  "1x " + numTicket.toString() + " \n \tPreisstufe: " + preisstufeToUse.get(i) + "\n \t" + restTicket +"x entwerten";
                }
            }

            if(i != ticketsToUse.size() - 1){
                value += "\n";
            }
        }

        return value;
    }

    public ArrayList<Ticket> getTicketList(){
        return ticketList;
    }

    public void removeTickets(){
        ticketList.clear();
        ticketPreisstufenList.clear();
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

}
