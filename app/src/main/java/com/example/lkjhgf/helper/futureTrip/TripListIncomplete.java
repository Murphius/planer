package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.app.AlertDialog;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

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
        newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, current.getNumUserClasses());
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

        //TODO Kalkulierung der Tickets
        /**
         * Wichtig hierbei: Nutzerklassenindex Ticketliste = Nutzerklassenindex Fahrten!
         */
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
            //Fahrten ohne Preisstufe sowie nicht zu optimierende Fahrten aussortieren
            copy = Optimisation.removeTrips(tripItems);
            // Fahrten nach den Preisstufen sortieren
            Collections.sort(copy, MainMenu.myProvider);
            //Fahrten auf Nutzerklassen aufteilen
            HashMap<Fare.Type, ArrayList<TripItem>> userClassTripLists = MainMenu.myProvider.createUserClassTripLists(copy);

            //Fahrscheine der letzten Optimierung
            HashMap<Fare.Type, ArrayList<TicketToBuy>> activeTickets = new HashMap(AllTickets.loadTickets(activity));
            //Allen benötigte Fahrscheine
            HashMap<Fare.Type, ArrayList<TicketToBuy>> allTicketLists = new HashMap<>();
            //Fahrscheine, mit mindestens einer freien Fahrt
            HashMap<Fare.Type, ArrayList<TicketToBuy>> freeTickets = new HashMap<>();

            for (Iterator<Fare.Type> it = activeTickets.keySet().iterator(); it.hasNext(); ) {
                //Die gespeicherten Fahrscheine der aktuellen Nutzerklasse
                Fare.Type currentType = it.next();
                ArrayList<TicketToBuy> userClassActiveTickets = activeTickets.get(currentType);
                //Fahrscheine mit freien Fahrten dieser Nutzerklasse
                ArrayList<TicketToBuy> freeUserClassTickets = new ArrayList<>();
                //Alle neuen Fahrscheine dieser Nutzerklasse
                ArrayList<TicketToBuy> allUserClassTickets = new ArrayList<>();
                //Fahrten dieser Nutzerklasse
                ArrayList<TripItem> userClassTrips = userClassTripLists.get(currentType);
                //Für jedes Ticket der Nutzerklasse:
                for (Iterator<TicketToBuy> it2 = userClassActiveTickets.iterator(); it2.hasNext(); ) {
                    TicketToBuy currentTicket = it2.next();
                    //Prüfen, ob es sich um ein zukünftiges Ticket handelt
                    if (currentTicket.isFutureTicket()) {
                        //Falls ja, müssen zu erst die Fahrten des Tickets die Fahrscheine freigegeben bekommen

                        //Über alle zugeordneten Fahrten iterieren
                        for(Iterator<TripItem> tripItemIterator = currentTicket.getTripList().iterator(); tripItemIterator.hasNext();){
                            TripItem currentTrip = tripItemIterator.next();
                            //Entfernen der zugeordneten Tickets
                            //TODO
                            if (userClassTrips != null && !userClassTrips.isEmpty()) {
                                int index = userClassTrips.indexOf(currentTrip);
                                if(index != -1){
                                    userClassTrips.get(index).removeTickets(currentType, currentTicket.getTicketID());
                                }
                            }

                        }
                        //Entfernen des zukünftigen Fahrscheins
                        it2.remove();
                    } else {
                        //Kein zukünftiges Ticket
                        //Ticket merken
                        allUserClassTickets.add(currentTicket);
                        //Die zugeordneten Fahrten dieses Tickets, aus der Liste der zu optimierenden Fahrten entfernen
                        for (TicketToBuy.TripQuantity currentTripItem : currentTicket.getTripQuantities()) {
                            int quantity = currentTripItem.getQuantity();
                            while (quantity != 0){
                                userClassTrips.remove(currentTripItem.getTrip());
                                quantity--;
                            }
                        }
                        //Wenn noch freie Fahrten vorhanden sind -> diese zur Liste der Fahrscheine mit freien Fahrten
                        //hinzufügen
                        if (currentTicket.getFreeTrips() > 0) {
                            freeUserClassTickets.add(currentTicket);
                        }
                    }
                }
                allTicketLists.put(currentType, allUserClassTickets);
                freeTickets.put(currentType, freeUserClassTickets);
            }

            //Optimieren
            //TODO dieser Teil funktioniert nur für NumTicket
            //Optimierung mit alten Fahrscheinen
            if (!freeTickets.isEmpty()) {
                for (Fare.Type type : freeTickets.keySet()) {
                    if (!freeTickets.get(type).isEmpty()) {
                        Optimisation.optimisationWithOldTickets(freeTickets.get(type), userClassTripLists.get(type));
                    }
                }
            }

            //Optimieren mit neuen Fahrscheinen
            HashMap<Fare.Type, TicketInformationHolder> lastBestTickets = new HashMap<>();
            HashMap<Fare.Type, ArrayList<Ticket>> allTicketsMap = MainMenu.myProvider.getAllTickets();
            for (Iterator<Fare.Type> iterator = userClassTripLists.keySet().iterator(); iterator.hasNext(); ) {
                Fare.Type type = iterator.next();
                if (allTicketsMap.containsKey(type)) {
                    lastBestTickets.put(type, Optimisation.optimisationBuyNewTickets(allTicketsMap.get(type), userClassTripLists.get(type)));
                } else {
                    System.err.println("Fehler bei der Optimierung neuer Fahrscheine - unbekannter Tickettyp: " + type);
                }
            }
            //TicketListe Bauen
            for (Fare.Type type : lastBestTickets.keySet()) {
                ArrayList<TicketToBuy> ticketList = Optimisation.createTicketList(lastBestTickets.get(type));
                if (allTicketLists.containsKey(type)) {
                    allTicketLists.get(type).addAll(ticketList);
                } else {
                    allTicketLists.put(type, ticketList);
                }

            }
            AllTickets.saveData(allTicketLists, activity);

            //TODO eigentlich anzeigen der Tickets statt aller Fahrten
            Intent newIntent = new Intent(activity.getApplicationContext(), Complete.class);
            startNextActivity(newIntent);
        });
    }

}
