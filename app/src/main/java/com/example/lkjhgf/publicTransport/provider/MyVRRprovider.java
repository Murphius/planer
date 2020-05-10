package com.example.lkjhgf.publicTransport.provider;

import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.VrrProvider;

public class MyVRRprovider extends MyProvider {


    public MyVRRprovider(){
        super();

        String[] preisstufen = {"K", "A1", "A2", "A3", "B", "C", "D"};

        ArrayList<Ticket> adultTickets = new ArrayList<>();
        adultTickets.add(new NumTicket(1, new int[] { 170, 280, 280, 290, 600, 1280, 1570 }, "Einzelticket E"));
        adultTickets.add(new NumTicket(4, new int[] { 610, 1070, 1070, 1070, 2250, 4690, 5710 },
                "4er-Ticket E"));
       adultTickets.add(new NumTicket(10, new int[] { 1420, 2290, 2290, 2290, 4600, 9315, 10485 },
                "10er-Ticket E"));

        ArrayList<Ticket> childrenTickets = new ArrayList<>();
        childrenTickets.add(new NumTicket(1, new int[]{170,170,170,170, 170, 170, 170}, "Einzelticket K"));
        childrenTickets.add(new NumTicket(4, new int[]{610, 610, 610, 610, 610, 610, 610}, "4er-Ticket K"));

        ArrayList<ArrayList<Ticket>> allTickets = new ArrayList<>();
        allTickets.add(adultTickets);
        allTickets.add(childrenTickets);

        NetworkProvider provider = new VrrProvider();

        initialise(preisstufen, allTickets, provider);
    }

    @Override
    public ArrayList<ArrayList<TripItem>> createUserClassTripLists(ArrayList<TripItem> allTrips) {
        ArrayList<TripItem> adultList = new ArrayList<>();
        ArrayList<TripItem> chidrenList = new ArrayList<>();
        for (TripItem tripItem : allTrips) {
            for (int i = 1; i <= tripItem.getNumAdult(); i++) {
                adultList.add(tripItem);
            }
            for(int i = 1; i <= tripItem.getNumChildren(); i++){
                chidrenList.add(tripItem);
            }
        }
        ArrayList<ArrayList<TripItem>> sortedTripList = new ArrayList<>();
        sortedTripList.add(adultList);
        sortedTripList.add(chidrenList);
        return sortedTripList;
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
