package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.closeUp.AllConnectionsIncompleteView;
import com.example.lkjhgf.activities.futureTrips.closeUp.AllTripsCompleteView;
import com.example.lkjhgf.activities.multipleTrips.EditIncompleteTripFromCompleteList;
import com.example.lkjhgf.activities.singleTrip.EditTrip;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;
import com.example.lkjhgf.helper.util.UtilsOptimisation;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.activities.singleTrip.UserForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import de.schildbach.pte.dto.Fare;

/**
 * Ansicht nach der Planung einer Fahrt, die nicht Optimiert werden muss <br/>
 * Ansicht, wenn aus dem Hauptmenü aufgerufen
 */
public class TripListComplete extends MyTripList {

    public static String EXTRA_TICKET = "com.example.lkjhgf.helper.futureTrip.EXTRA_TICKET";

    /**
     * Layout der Ansicht modifizieren <br/>
     * <p>
     * In dieser Ansicht können keine Fahrscheine berechnet werden -> dieser Button ist für
     * den Nutzer nicht sichrbar <br/>
     * Auch  kann der Nutzer nicht abbrechen, sondern nur zurück ins Hauptmenü <br/>
     *
     * @see MyTripList#MyTripList(Activity activity, View view, TripItem tripItem, String DataPath)
     */
    public TripListComplete(Activity activity, View view, TripItem tripItem) {
        super(activity, view, tripItem, MyTripList.ALL_SAVED_TRIPS);

        abort.setVisibility(View.GONE);
        calculateTickets.setVisibility(View.GONE);

        setOnClickListener();
        setRecyclerView();
    }

    /**
     * Klickt der Nutzer auf die Fahrt, wird die Detailansicht dieser Fahrt geöffnet <br/>
     * <p>
     *
     * @see MyTripList#onItemClick(int position)
     * <p>
     * Wenn der Nutzer in der Übersicht aller gespeicherten Fahrten ist, muss zwischen den Fahrtypen
     * unterschieden werden, da davon abhängig das Layout anders gestaltet werden muss. <br/>
     * Ein zusammenlegen mit {@link TripListIncomplete#onItemClick(int position)} ist nicht möglich,
     * da in der Klasse {@link TripListIncomplete} das Ticket für die jeweilige Fahrt noch nicht bekannt ist.
     */
    @Override
    void onItemClick(int position) {
        TripItem current = tripItems.get(position);
        Intent newIntent;
        //Abhängig von der Fahrt wird eine andere Aktivität gestartet
        if (current.isComplete()) {
            newIntent = new Intent(activity.getApplicationContext(), AllTripsCompleteView.class);
        } else {
            //TODO #Trip??
            newIntent = new Intent(activity.getApplicationContext(), AllConnectionsIncompleteView.class);
            //#Fahrt und #reisende Personen ebenfalls übergeben
            newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, position + 1);
            newIntent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, current.getNumUserClasses());
        }
        newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
        startNextActivity(newIntent);
    }

    /**
     * @see MyTripList#startEdit(int position)
     */
    @Override
    void startEdit(int position) {
        TripItem current = tripItems.get(position);
        if (current.isComplete()) {
            System.out.println("--------------------------------------------------------------------+++");
            tripItems.remove(position);
            adapter.notifyItemRemoved(position);
            Intent newIntent = new Intent(activity.getApplicationContext(), EditTrip.class);
            newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
            startNextActivity(newIntent);
        } else {
            System.out.println("--------------------------------------------------------------------++++");
            if (checkTickets(position)) {
                System.out.println("-------------------------------------------------------------+++++");
                //Nur freie Fahrten
                removeTripAndTicket(position);
                Intent newIntent = new Intent(activity.getApplicationContext(),
                        EditIncompleteTripFromCompleteList.class);
                newIntent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, current.getNumUserClasses());
                newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, position + 1);
                newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
                startNextActivity(newIntent);
            } else {
                //Fahrt nutzt angefangene Tickets
                AlertDialog.Builder secondBuilder = new AlertDialog.Builder(activity);
                secondBuilder.setMessage("Diese Fahrt besitzt mindestens ein angefangenes Ticket, durch das editieren dieser Fahrt könnten die " +
                        "Kosten nicht mehr minimal sein. \n Trotzdem editieren?");
                secondBuilder.setCancelable(false);
                secondBuilder.setPositiveButton("Ja", (secondDialog, which1) -> {
                    //falls ja: bei den Fahrscheinen die Fahrt löschen & ggf. anzahl der freien Fahrten korrigieren
                    removeTripSetTicketFree(position);
                    Intent newIntent = new Intent(activity.getApplicationContext(),
                            EditIncompleteTripFromCompleteList.class);
                    newIntent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, current.getNumUserClasses());
                    newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, position + 1);
                    newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
                    startNextActivity(newIntent);
                });
                secondBuilder.setNegativeButton("Nein", (secondDialog, which1) -> secondDialog.cancel());
                AlertDialog secondDialog = secondBuilder.create();
                secondDialog.show();
            }

        }

    }


    @Override
    void onDeleteClicked(int position) {
        AlertDialog.Builder firstBuilder = new AlertDialog.Builder(activity);
        firstBuilder.setMessage("Diese Fahrt wirklich löschen?");
        firstBuilder.setCancelable(false);
        firstBuilder.setPositiveButton("Ja", (dialog, which) -> {
            if (tripItems.get(position).isComplete()) {
                removeItemAtPosition(position);
            } else {
                if (this.checkTickets(position)) {
                    //keine angefangenen Tickets
                    removeTripAndTicket(position);
                } else {
                    //mindestens ein angefangenes Ticket
                    //Nutzer abfrage -> eventuell Kosten nicht mehr minimal
                    AlertDialog.Builder secondBuilder = new AlertDialog.Builder(activity);
                    secondBuilder.setMessage("Diese Fahrt besitzt mindestens ein angefangenes Ticket, durch das löschen dieser Fahrt könnten die " +
                            "Kosten nicht mehr minimal sein. \n Trotzdem löschen?");
                    secondBuilder.setCancelable(false);
                    secondBuilder.setPositiveButton("Ja", (secondDialog, which1) -> {
                        //falls ja: bei den Fahrscheinen die Fahrt löschen & ggf. anzahl der freien Fahrten korrigieren
                        removeTripSetTicketFree(position);
                    });
                    secondBuilder.setNegativeButton("Nein", (secondDialog, which1) -> dialog.cancel());
                    AlertDialog secondDialog = secondBuilder.create();
                    secondDialog.show();
                }
            }
        });
        firstBuilder.setNegativeButton("Nein", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = firstBuilder.create();
        dialog.show();
    }

    private void removeTripAndTicket(int position) {
        //Optimierung wie gehabt, ohne die gelöschte Fahrt
        tripItems.remove(position);
        HashMap<Fare.Type, ArrayList<TicketToBuy>> newTicketList = UtilsOptimisation.brauchtEinenTollenNamen(tripItems, activity);
        adapter.notifyDataSetChanged();
        AllTickets.saveData(newTicketList, activity);
        saveData();
    }

    private void removeTripSetTicketFree(int position) {
        TripItem currentTrip = tripItems.get(position);
        HashMap<Fare.Type, ArrayList<TicketToBuy>> allSavedTickets = AllTickets.loadTickets(activity);
        Set<Fare.Type> keys = currentTrip.getNumUserClasses().keySet();
        for (Fare.Type type : keys) {
            ArrayList<UUID> ticketUUIDs = currentTrip.getTicketIDs(type);
            ArrayList<TicketToBuy> tickets = allSavedTickets.get(type);
            for (UUID currentTicketUUID : ticketUUIDs) {
                for (Iterator<TicketToBuy> iteratorTicketToBuy = tickets.iterator(); iteratorTicketToBuy.hasNext(); ) {
                    TicketToBuy currentTicketToBuy = iteratorTicketToBuy.next();
                    if (currentTicketUUID.equals(currentTicketToBuy.getTicketID())) {
                        if (currentTicketToBuy.removeTrip(currentTrip.getTrip().getId())) {
                            //Ticket entfernen, da dies keine weitenen Fahrten belegt hat -> kann gelöscht werden
                            iteratorTicketToBuy.remove();
                        }
                    }
                }
            }
        }
        tripItems.remove(position);
        adapter.notifyDataSetChanged();
        AllTickets.saveData(allSavedTickets, activity);
        saveData();
    }

    private boolean checkTickets(int position) {
        TripItem currentTrip = tripItems.get(position);
        HashMap<Fare.Type, ArrayList<TicketToBuy>> allSavedTickets = AllTickets.loadTickets(activity);
        Set<Fare.Type> keys = currentTrip.getNumUserClasses().keySet();
        //prüfen, ob alle Tickets dieser Fahrt in der Fahrt noch nicht angefangen sind
        for (Fare.Type type : keys) {
            ArrayList<UUID> ticketUUIDs = currentTrip.getTicketIDs(type);
            ArrayList<TicketToBuy> tickets = allSavedTickets.get(type);
            for (UUID currentTicketUUID : ticketUUIDs) {
                for (TicketToBuy currentTicketToBuy : tickets) {
                    if (currentTicketUUID.equals(currentTicketToBuy.getTicketID())) {
                        if (!currentTicketToBuy.isFutureTicket()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /*
    private ArrayList<UUID> usedTickets(int position){
        ArrayList<UUID> usedTickets = new ArrayList<>();
        TripItem currentTrip = tripItems.get(position);

        for(Fare.Type type : currentTrip.getNumUserClasses().keySet()){
            usedTickets.addAll(currentTrip.getTicketIDs(type));
        }

        return usedTickets;
    }

    private void searchForTickets(ArrayList<UUID> uuids, int position){
        //Laden der Fahrscheine
        HashMap<Fare.Type, ArrayList<TicketToBuy>> savedTickets = AllTickets.loadTickets(activity);
        ArrayList<TicketToBuy> usedTickets = new ArrayList<>();
        for(Fare.Type type : savedTickets.keySet()){
            //gespeicherte Fahrscheine der Nutzerklasse
            ArrayList<TicketToBuy> userClassSavedTickets = savedTickets.get(type);
            //über dessen zugeordnete Fahrscheine iterieren
            for(Iterator<TicketToBuy> ticketToBuyIterator = userClassSavedTickets.iterator(); ticketToBuyIterator.hasNext();){
                TicketToBuy currentTicketToBuy = ticketToBuyIterator.next();
                for(Iterator<UUID> uuidIterator = uuids.iterator(); uuidIterator.hasNext();){
                    //Prüfen, ob die aktuelle TicketID in der Liste der Fahrscheine dieser Fahrt enthalten ist
                    if(currentTicketToBuy.getTicketID().equals(uuidIterator.next())){
                        //wenn ja -> speichern dieser Fahrt
                        usedTickets.add(currentTicketToBuy);
                        uuidIterator.remove();
                        if(currentTicketToBuy.removeTrip(tripItems.get(position).getTrip().getId())){
                            ticketToBuyIterator.remove();
                        }
                    }
                }
            }
        }
    }*/

    /**
     * Neue Fahrt planen, die nicht in bei der Optimierung berücksichtigt wird <br/>
     *
     * @preconditions der Nutzer hat den Button Plus gedrückt
     * @postconditions Öffnen des Formulars für eine neue Fahrt, welche nicht optimiert wird
     */
    private void setOnClickListener() {
        addTrip.setOnClickListener(v -> {
            Intent newIntent = new Intent(activity, UserForm.class);
            super.startNextActivity(newIntent);
        });
    }
}
