package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.util.Pair;
import android.view.View;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Complete;
import com.example.lkjhgf.activities.futureTrips.closeUp.PlanIncompleteView;
import com.example.lkjhgf.activities.multipleTrips.EditIncompleteTripFromIncompleteList;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.optimisation.OptimisationUtil;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeOptimisation;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.activities.multipleTrips.UserForm;

import java.util.ArrayList;
import java.util.HashMap;

import de.schildbach.pte.dto.Fare;

/**
 * Ansicht bei der Planung mehrerer Fahrten, deren Fahrscheine optimiert werden sollen
 */
public class TripListIncomplete extends MyTripList {

    private int numTrip;

    /**
     * Arbeitet nur auf den in einem "Durchlauf" geplanten Fahrten
     *
     * @see MyTripList#MyTripList(Activity, View, TripItem, String Data Path)
     */
    public TripListIncomplete(Activity activity, View view, TripItem tripItem) {
        super(activity, view, tripItem, MyTripList.SAVED_TRIPS);

        numTrip = activity.getIntent().getIntExtra(MainMenu.EXTRA_NUM_TRIP, 1);

        setOnClickListener();
        setRecyclerView();
    }

    /**
     * Klickt der Nutzer auf die Fahrt, wird die Detailansicht dieser Fahrt geöffnet <br/>
     * <p>
     *
     * @see MyTripList#onItemClick(int position)
     * <p>
     * Wenn der Nutzer in der Übersicht der aktuell geplanten Fahrten ist, muss nicht zwischen den Fahrtypen
     * unterschieden werden, da nur zu optimierende Fahrten angezeigt werden. <br/>
     * Ein zusammenlegen mit {@link TripListComplete#onItemClick(int position)} ist nicht möglich,
     * da in der Klasse {@link TripListComplete} das Ticket für die jeweilige Fahrt bereits bekannt ist.
     */
    @Override
    void onItemClick(int position) {
        TripItem current = tripItems.get(position);
        Intent newIntent = new Intent(activity, PlanIncompleteView.class);
        newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, current.getTrip());
        //TODO erweitern für weitere Personenklassen
        newIntent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, current.getNumUserClasses());
        newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, position + 1);
        newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
        startNextActivity(newIntent);
    }

    /**
     * @see MyTripList#startEdit(int position)
     * <p>
     * In der Liste finden sich nur zu optimierende Fahrten -> keine Unterscheidung nötig
     */
    @Override
    void startEdit(int position) {
        TripItem current = tripItems.get(position);
        tripItems.remove(position);
        Intent newIntent = new Intent(activity, EditIncompleteTripFromIncompleteList.class);
        newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, position + 1);
        newIntent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, current.getNumUserClasses());
        newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
        activity.startActivity(newIntent);
    }

    /**
     * Hinzufügen von OnClickListenern für die Buttons <br/>
     * <p>
     * addTrip -> hinzufügen einer neuen zu optimierenden Fahrt <br/>
     * abort -> entspricht zurück -> Sicherheitsabrfrage <br/>
     * calculateTickets -> weiter & keine weiteren Fahrten mehr planen; optimiereung der Fahrscheine
     */
    private void setOnClickListener() {
        addTrip.setOnClickListener(v -> {
            Intent newIntent = new Intent(activity, UserForm.class);
            newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, numTrip + 1);
            startNextActivity(newIntent);
        });

        abort.setOnClickListener(v -> activity.onBackPressed());


        calculateTickets.setOnClickListener(v -> {
            //Kopieren aller geplanten Fahrten
            ArrayList<TripItem> copy = new ArrayList<>(tripItems);

            //Liste leeren
            tripItems.clear();
            //Speichern, dass keine Fahrten geplant sind
            saveData();
            //Laden aller gespeicherten Fahrten
            dataPath = ALL_SAVED_TRIPS;
            loadData();
            //Neue Fahrten hinzufügen
            for (TripItem item : copy) {
                insertTrip(item);
            }
            //Optimieren der Fahrten
            //Pair<Integer, Integer> test = TimeOptimisation.findBest24hInterval(tripItems, new TimeTicket(new int[]{720, 1470, 2530, 3040}, "24hTicket-1", Fare.Type.ADULT, 24*60*60*1000,2));
            ArrayList<TimeTicket> timeTickets = new ArrayList<>();
            timeTickets.add(new TimeTicket(new int[]{720, 720, 720, 720, 1470, 2530, 3040}, "24-StundenTicket-1", Fare.Type.ADULT, 24 * 60 * 60 * 1000, 2));
            timeTickets.add(new TimeTicket(new int[]{1370, 1370, 1370, 1370, 2790, 4810, 5780}, "48-StundenTicket-1", Fare.Type.ADULT, 48 * 60 * 60 * 1000, 5));
            timeTickets.add(new TimeTicket(new int[]{2295, 2295, 2815, 2950, 4275, 5730, 7240}, "7-TageTicket", Fare.Type.ADULT, 7 * 24 * 60 * 60 * 1000, 5));
            timeTickets.add(new TimeTicket(new int[]{7120, 7120, 7560, 7920, 11355, 15355, 19390}, "30-TageTicket", Fare.Type.ADULT, 30 * 24 * 60 * 60 * 1000, 16));
            ArrayList<TicketToBuy> ticketToBuyArrayList = TimeOptimisation.optimierungPreisstufeD(tripItems, timeTickets);
            System.out.println("-------------------------------------------------------------------------------------------------");
            System.out.println("#Tickets: " + ticketToBuyArrayList.size());
            for (TicketToBuy t : ticketToBuyArrayList) {
                System.out.println("\tTicketname:" + t.getTicket().getName() + " zugeordnete #Fahrten:" + t.getTripList().size());
            }
            System.out.println("-------------------------------------------------------------------------------------------------");
            HashMap<Fare.Type, ArrayList<TicketToBuy>> newTicketList = OptimisationUtil.startOptimisation(tripItems, activity);
            adapter.notifyDataSetChanged();
            AllTickets.saveData(newTicketList, activity);
            saveData();

            //TODO eigentlich anzeigen der Tickets statt aller Fahrten
            Intent newIntent = new Intent(activity.getApplicationContext(), Complete.class);
            startNextActivity(newIntent);
        });
    }

}
