package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Complete;
import com.example.lkjhgf.activities.futureTrips.closeUp.PlanIncompleteView;
import com.example.lkjhgf.activities.multipleTrips.EditIncompleteTripFromIncompleteList;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Optimisation;
import com.example.lkjhgf.optimisation.PreisProvider;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketInformationHolder;
import com.example.lkjhgf.optimisation.VRRadultPreisProvider;
import com.example.lkjhgf.optimisation.VRRpreisstufenComparator;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.activities.multipleTrips.UserForm;

import java.util.ArrayList;
import java.util.Collections;

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
     * @see MyTripList#onItemClick(int position)
     *
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
     *
     * In der Liste finden sich nur zu optimierende Fahrten -> keine Unterscheidung nötig
     */
    @Override
    void startEdit(int position){
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
        calculateTickets.setOnClickListener(v -> {
            //Preise hinterlegen
            Ticket einzelTicketE = new NumTicket(1, new int[] { 170, 280, 280, 290, 600, 1280, 1570 }, "Einzelticket");
            Ticket viererTicketE = new NumTicket(4, new int[] { 610, 1070, 1070, 1070, 2250, 4690, 5710 },
                    "4er-Ticket");
            Ticket zehnerTicketE = new NumTicket(10, new int[] { 1420, 2290, 2290, 2290, 4600, 9315, 10485 },
                    "10er-Ticket");
            ArrayList<Ticket> adultTickets = new ArrayList<>();
            adultTickets.add(einzelTicketE);
            adultTickets.add(viererTicketE);
            adultTickets.add(zehnerTicketE);

            PreisProvider vrrAdultPreisProvider = new VRRadultPreisProvider(adultTickets);

            //Kopieren aller geplanten Fahrten
            ArrayList<TripItem> copy = new ArrayList<>(tripItems);

            //Liste leeren
            tripItems.clear();
            //Speichern, dass keine Fahrten geplant sind
            saveData();
            //Laden aller gespeicherten Fahrten
            dataPath = ALL_SAVED_TRIPS;
            loadData();
            for (TripItem item : copy) {
                insertTrip(item);
            }
            // Holen aller Fahrten
            copy = new ArrayList<>(tripItems);
            //TODO
            //Fahrten ohne Preisstufe sowie nicht zu optimierende Fahrten aussortieren
            Optimisation.removeTrips(copy);
            //TODO
            //Fahrscheinliste laden; entfernen von Fahrten eines angefangen Tickets


            // Fahrten nach den Preisstufen sortieren
            Collections.sort(copy, new VRRpreisstufenComparator());
            //Erwachsenen Fahrt:
            ArrayList<TripItem> tripsToOptimiseAdult = Optimisation.createAdultTripList(copy, vrrAdultPreisProvider);
            //TODO Kinder


            //Optimierungszauber
            TicketInformationHolder lastBestTicket = Optimisation.optimisation(vrrAdultPreisProvider, tripsToOptimiseAdult);
            //TODO Zusammenfügen von Fahrten
            /*for(int i = 0; i < tripsToOptimiseAdult.size() - 1; i++){

                TripItem currentTripItem = tripsToOptimiseAdult.get(i);
                System.out.println(currentTripItem.getTicketList().size());
                TripItem nextTripItem = tripsToOptimiseAdult.get(i+1);
                if(currentTripItem.equals(nextTripItem)){
                    currentTripItem.addTicket(nextTripItem.getTicketList().get(0), nextTripItem.getTicketPreisstufenList().get(0));
                    tripsToOptimiseAdult.remove(i+1);
                }

            }*/
            //TicketListe Bauen
            //TODO
            ArrayList<TicketInformationHolder> ticketList = Optimisation.createTicketList(lastBestTicket);
            AllTickets.saveData(ticketList,activity);
            // Fahrten zu den Tickets packen
            /*for (TicketInformationHolder ticketHolder : ticketList) {
                for (TripItem tripHolder : tripsToOptimiseAdult) {
                    if (tripHolder.getTicketList() == ticketHolder.getTicket() && tripHolder.getTicketPreisstufenList() == ticketHolder.getPreisstufe()) {
                        ticketHolder.addTripItem(tripHolder);
                    }
                }
            }*/

            //TODO
            for (TicketInformationHolder ticketHolder : ticketList) {
                System.out.print(ticketHolder.toString());
            }



            //
            //TODO eigentlich anzeigen der Tickets statt aller Fahrten
            Intent newIntent = new Intent(activity.getApplicationContext(), Complete.class);
            startNextActivity(newIntent);
        });
    }
}
