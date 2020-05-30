package com.example.lkjhgf.publicTransport.provider;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.Utils;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.helper.util.testAsyncTaskextends;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Fare;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Trip;

public class MyVRRprovider extends MyProvider {

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

        NetworkProvider provider = new VrrProvider();

        initialise(preisstufen, allTickets, provider);
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
    public HashMap<Fare.Type, ArrayList<TripItem>> createUserClassTripLists(ArrayList<TripItem> allTrips) {
        HashMap<Fare.Type, ArrayList<TripItem>> userClassTrips = new HashMap<>();
        for (TripItem tripItem : allTrips) {
            for (Fare.Type type : getAllTickets().keySet()) {
                for (int i = 1; i <= tripItem.getNumUserClass(type); i++) {
                    ArrayList<TripItem> oldTripItems = userClassTrips.get(type);
                    if (oldTripItems == null) {
                        oldTripItems = new ArrayList<>();
                    }
                    oldTripItems.add(tripItem);
                    userClassTrips.put(type, oldTripItems);
                }
            }
        }
        return userClassTrips;
    }

    @Override
    public String getTicketInformation(ArrayList<Ticket> tickets, ArrayList<Integer> quantity, ArrayList<String> preisstufe, TripItem tripItem) {
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
            }

            if (preisstufe.get(i).equals(preisstufen[0])) {
                value.append("\n \tEntwerten für die Starthaltestelle: ").append(UtilsString.setLocationName(tripItem.getTrip().from));
            }else if(preisstufe.get(i).equals(preisstufen[1]) || preisstufe.get(i).equals(preisstufen[2]) || preisstufe.get(i).equals(preisstufen[3])){
                Location startLocation = tripItem.getTrip().from;
                Location destinationLocation = tripItem.getTrip().to;
                String startID = startLocation.id.substring(startLocation.id.length()-4);
                String destinationID = destinationLocation.id.substring(destinationLocation.id.length()-4);
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
                if(waben.size() == 2){
                    if(waben.get(0) /10 == waben.get(1) / 10){
                        value.append("\n \tEntwerten für das Tarifgebiet: ").append(waben.get(0));
                    }else{
                        value.append("\n \tEntwerten für die zwei Waben mit der Starthaltestelle: ").append(UtilsString.setLocationName(tripItem.getTrip().from));
                        value.append(" und der Zielhaltestelle ").append(UtilsString.setLocationName(tripItem.getTrip().to));
                    }
                }
            }else{
                Location startLocation = tripItem.getTrip().from;
                String startID = startLocation.id.substring(startLocation.id.length()-4);
                int startIDint = Integer.parseInt(startID);
                testAsyncTaskextends wabenTask = new testAsyncTaskextends();
                wabenTask.execute(startIDint);
                ArrayList<Integer> waben = new ArrayList<>();
                try {
                    waben = wabenTask.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                if(! waben.isEmpty()){
                    value.append("\n\tEntwerten für das Tarifgebiet: ").append(waben.get(0)/10);
                }

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
        String preisstufe1 = tripItem1.getTrip().fares.get(0).units;
        String preisstufe2 = tripItem2.getTrip().fares.get(0).units;

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
