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
import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.activities.multipleTrips.UserForm;

import java.util.ArrayList;
import java.util.HashMap;

import de.schildbach.pte.dto.Fare;

import static com.example.lkjhgf.helper.form.Form.EXTRA_MYURLPARAMETER;

/**
 * Ansicht bei der Planung mehrerer Fahrten, deren Fahrscheine optimiert werden sollen
 */
public class TripListIncomplete extends MyTripList {

    /**
     * Arbeitet nur auf den in einem "Durchlauf" geplanten Fahrten
     *
     * @see MyTripList#MyTripList(Activity, View, TripItem, String Data Path)
     */
    public TripListIncomplete(Activity activity, View view, TripItem tripItem) {
        super(activity, view, tripItem, MyTripList.NEW_SAVED_TRIPS);

        setOnClickListener();
        setRecyclerView();
    }

    /**
     * Ansicht füllen, jedoch keine weitere Fahrt hinzugekommen <br/>
     *
     * @param activity zum Laden benötigt
     * @param view     Layout das gefüllt werden soll
     */
    public TripListIncomplete(Activity activity, View view) {
        super(activity, view, null, MyTripList.NEW_SAVED_TRIPS);

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
        newIntent.putExtra(EXTRA_MYURLPARAMETER, current.getMyURLParameter());
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
        saveData(NEW_SAVED_TRIPS);
        Intent newIntent = new Intent(activity, EditIncompleteTripFromIncompleteList.class);
        newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, position + 1);
        newIntent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, current.getNumUserClasses());
        newIntent.putExtra(EXTRA_MYURLPARAMETER, current.getMyURLParameter());
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
            newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, tripItems.size() + 1);
            startNextActivity(newIntent);
        });

        abort.setOnClickListener(v -> activity.onBackPressed());

        calculateTickets.setOnClickListener(v -> {
            prepareOptimisation();
        });
    }

    /**
     * Vorbereitung der Optimierung <br/>
     * <p>
     * Die Fahrten werden in die Liste der zu optimierenden Fahrten gepackt, dabei wird überprüft,
     * ob sich Fahrten überlappen, falls ja, werden diese entfernt. <br/>
     * Anschließend wird die Liste der zu optimierenden Fahrten als leer gespeichert. <br/>
     * Sollten nicht alle Fahrten hinzugefügt werden, wird der Nutzer darüber informiert. <br/>
     * Anschließend startet die Optimierung ({@link #startOptimisation()})
     *
     * @preconditions Der Nutzer hat auf "weiter/Optimieren" geklickt
     * @postconditions Die Fahrten werden in die Liste aller gespeicherter Fahrten gepackt (sofern
     * sie optimierbar sind & sich nicht überlappen). Die Liste der zu optimierenden Fahrten wird
     * als leer gespeichert.
     */
    private void prepareOptimisation() {
        //Kopieren aller geplanten Fahrten
        ArrayList<TripItem> copy = new ArrayList<>(tripItems);
        //Liste leeren
        tripItems.clear();
        //Speichern, dass keine Fahrten geplant sind
        saveData(NEW_SAVED_TRIPS);
        //Laden aller gespeicherten Fahrten
        tripItems = loadData(activity, ALL_SAVED_TRIPS);
        //Neue Fahrten hinzufügen
        int size = tripItems.size();
        for (TripItem item : copy) {
            insertTrip(item);
        }
        UtilsList.removeOverlappingTrips(tripItems);
        if (tripItems.size() != size + copy.size()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Nicht alle Fahrten konnten hinzugefügt werden \n" +
                    "Mögliche Ursachen: Fahrten sind bereits in der Liste enthalten, Fahrten haben sich überlappt, ...");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", (dialog, which) -> startOptimisation());
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            startOptimisation();
        }
    }

    /**
     * Die Optimierung der Fahrten <br/>
     * <p>
     * Jede zu optimierende Fahrt (mit dem Attribut isComplete = false) erhält so viele Fahrscheine, wie sie benötigt.
     *
     * @preconditions Alle überlappenden Fahrten wurden gelöscht <br/>
     * @postconditions Die Fahrten und ihre zugewiesenen Fahrscheine werden gespeichert.
     */
    private void startOptimisation() {
        //Optimieren der Fahrten
        HashMap<Fare.Type, ArrayList<TicketToBuy>> savedTickets = AllTickets.loadTickets(activity);
        HashMap<Fare.Type, ArrayList<TicketToBuy>> newTicketList = MainMenu.myProvider.optimise(tripItems, savedTickets, activity);
        adapter.notifyDataSetChanged();
        AllTickets.saveData(newTicketList, activity);
        dataPath = ALL_SAVED_TRIPS;

        //TODO eigentlich anzeigen der Tickets statt aller Fahrten
        Intent newIntent = new Intent(activity.getApplicationContext(), Complete.class);
        startNextActivity(newIntent);
    }

}
