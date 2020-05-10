package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Complete;
import com.example.lkjhgf.activities.futureTrips.closeUp.PlanIncompleteView;
import com.example.lkjhgf.activities.multipleTrips.EditIncompleteTripFromIncompleteList;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.optimisation.Optimisation;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketInformationHolder;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.activities.multipleTrips.UserForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

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
        newIntent.putExtra(MainMenu.NUM_CHILDREN, current.getNumChildren());
        newIntent.putExtra(MainMenu.NUM_ADULT, current.getNumAdult());
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
        newIntent.putExtra(MainMenu.NUM_ADULT, current.getNumAdult());
        newIntent.putExtra(MainMenu.NUM_CHILDREN, current.getNumChildren());
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

        //TODO Kalkulierung der Tickets
        /**
         * Wichtig hierbei: Nutzerklassenindex Ticketliste = Nutzerklassenindex Fahrten!
         */
        calculateTickets.setOnClickListener(v -> {


            //PreisProvider vrrAdultPreisProvider = new VRRadultPreisProvider(MainMenu.myProvider.getTicketList);

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
            //Fahrten ohne Preisstufe sowie nicht zu optimierende Fahrten aussortieren
            copy = Optimisation.removeTrips(tripItems);
            // Fahrten nach den Preisstufen sortieren
            Collections.sort(copy, MainMenu.myProvider);
            //Fahrten auf Nutzerklassen aufteilen
            ArrayList<ArrayList<TripItem>> userClassTripLists = MainMenu.myProvider.createUserClassTripLists(copy);

            //Alte Fahrscheine
            ArrayList<ArrayList<TicketToBuy>> activeTickets = new ArrayList<>(AllTickets.loadTickets(activity));
            //TODO überprüfen
            ArrayList<ArrayList<TicketToBuy>> freeTickets = new ArrayList<>();
            for(int i = 0; i < activeTickets.size(); i++){
                ArrayList<TicketToBuy> userClassOldTickets = activeTickets.get(i);
                ArrayList<TicketToBuy> freeUserClassTickets = new ArrayList<>();
                for(TicketToBuy currentTicket : userClassOldTickets){
                    if(currentTicket.getFreeTrips() > 0 && ! currentTicket.isFutureTicket()){
                        freeUserClassTickets.add(currentTicket);
                    }
                    for(TripItem t : currentTicket.getTripList()){
                        t.addTicket(currentTicket.getTicket(), currentTicket.getPreisstufe());

                    }
                }
                freeTickets.add(freeUserClassTickets);
            }

            //Optimieren
            //TODO dieser Teil funktioniert nur für NumTicket
            //Optimierung mit alten Fahrscheinen
            if(!freeTickets.isEmpty()){
                for(int i = 0; i < userClassTripLists.size(); i++){
                    if(!freeTickets.get(i).isEmpty() || freeTickets == null){
                        Optimisation.optimisationWithOldTickets(freeTickets.get(i),userClassTripLists.get(i));
                    }
                }
            }

            if(freeTickets.size() != 0){
                AllTickets.saveData(new ArrayList<>(), activity);
            }




            //Optimieren mit neuen Fahrscheinen
            ArrayList<TicketInformationHolder> lastBestTickets = new ArrayList<>();
            ArrayList<ArrayList<Ticket>> allTicketsList = MainMenu.myProvider.getAllTickets();
            for (int i = 0; i < allTicketsList.size(); i++) {
                lastBestTickets.add(Optimisation.optimisationBuyNewTickets(allTicketsList.get(i), userClassTripLists.get(i)));
            }


            //TicketListe Bauen
            ArrayList<ArrayList<TicketToBuy>> allTicketLists = new ArrayList<>();
            for (TicketInformationHolder currentLastBestTicket : lastBestTickets) {
                ArrayList<TicketToBuy> ticketList = Optimisation.createTicketList(currentLastBestTicket);
                allTicketLists.add(ticketList);
            }
            AllTickets.saveData(allTicketLists, activity);

            //TODO eigentlich anzeigen der Tickets statt aller Fahrten
            Intent newIntent = new Intent(activity.getApplicationContext(), Complete.class);
            startNextActivity(newIntent);
        });
    }
}
