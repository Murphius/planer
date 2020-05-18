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
import com.example.lkjhgf.optimisation.OptimisationUtil;
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
     * @see MyTripList#startEdit(int position) <br/>
     * <p>
     * Wenn der Nutzer eine nicht zu optimierende Fahrt editiert, kommt der Nutzer direkt in das Formular, in
     * welchem er diese Fahrt ändern kann <br/>
     * Bei einer zu optimierenden Fahrt hingegen wird erst geprüft, ob die zugeordneten Fahrscheine ebenfalls
     * für andere, bereits vergangene Fahrten genutzt wurden <br/>
     * Falls ja, wird der Nutzer darauf hingewiesen, dass durch das editieren die Fahrscheine eventuell nicht
     * mehr optimal sind <br/>
     * -> Option zum Abbrechen
     * <br/>
     * Falls die Fahrt keine angefangenen Fahrscheine nutzt, wird die Fahrt aus der Liste der zu optimierenden
     * Fahrten gelöscht und die Liste aller Fahrten wird optimiert. Anschließend wird die Ansicht mit dem
     * bereits ausgefüllten Formular ({@link EditIncompleteTripFromCompleteList}) geöffnet. <br/>
     * <br/>
     * Falls die Fahrt mindestens ein angefangenes Ticket nutzt, werden die entsprechenden Fahrten freigegeben
     * und anschließend die Fahrt aus der Liste der Fahrten gelöscht. Dann werden die verbliebenen zukünftigen
     * Fahrten erneut optimiert. Anschließend wird das vorausgefüllte
     * Formular ({@link EditIncompleteTripFromCompleteList}) geöffnet.
     */
    @Override
    void startEdit(int position) {
        TripItem current = tripItems.get(position);
        if (current.isComplete()) {
            tripItems.remove(position);
            adapter.notifyItemRemoved(position);
            Intent newIntent = new Intent(activity.getApplicationContext(), EditTrip.class);
            newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
            startNextActivity(newIntent);
        } else {
            if (checkTicketsIfFuture(position)) {
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

    /**
     * @see MyTripList#onDeleteClicked(int position) <br/>
     * <p>
     * Wenn der Nutzer eine nicht optimierte Fahrt löscht, wird diese einfach aus der Liste der Fahrten entfernt <br/>
     * <p>
     * Entfernt er hingegen eine optimierte Fahrt, wird unterschieden, ob diese Fahrt mindestens ein angefangenes Ticket
     * nutzt oder nicht. <br/>
     * Falls kein Ticket angefangen ist, wird die Fahrt aus der Liste aller Fahrten entfernt und alle zukünftigen Fahrten
     * werden erneut optimiert. <br/>
     * <br/>
     * Ist jedoch hingegen mindestens ein Ticket angefangen, so wird der Nutzer darüber informiert, und hat die Option
     * abzubrechen. <br/>
     * Bestätigt der Nutzer diese Abfrage, werden die Fahrten auf den angefangenen Fahrscheinen freigegeben
     * und die neuen Fahrscheine entfernt und die Fahrt aus der Liste der Fahrten entfernt und die zukünftigen Fahrten
     * erneut optimiert.
     */
    @Override
    void onDeleteClicked(int position) {
        AlertDialog.Builder firstBuilder = new AlertDialog.Builder(activity);
        firstBuilder.setMessage("Diese Fahrt wirklich löschen?");
        firstBuilder.setCancelable(false);
        firstBuilder.setPositiveButton("Ja", (dialog, which) -> {
            if (tripItems.get(position).isComplete()) {
                removeItemAtPosition(position);
            } else {
                if (this.checkTicketsIfFuture(position)) {
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

    /**
     * Überprüft alle gespeicherten Fahrscheine, ob diese Fahrt diese nutzt und wenn ja, ob alle in
     * der Zukunft liegen.
     *
     * @param position Listenindex des zu prüfenden Elements
     * @return false - mindestens ein Ticket hat eine Fahrt, die nicht in der Zukunft liegt <br/>
     * true - für alle zugeordneten Fahrscheine gilt, dass alle diesem zugeordneten Fahrten in der
     * Zukunft liegen
     */
    private boolean checkTicketsIfFuture(int position) {
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

    /**
     * Entfernt die Fahrt an der jeweiligen Position und die zugehörigen Fahrscheine <br/>
     * <p>
     * Optimiert anschließend alle zukünftigen Fahrten neu
     *
     * @param position Die Postion des zu löschenden Elements
     * @preconditions Alle der Fahrt zugeordneten Fahrten sind zukünftige Tickets ({@link #checkTicketsIfFuture(int position)} = true)
     * @postconditions Die Fahrt & ihre Fahrscheine sind nicht in der neuen Ticket- und Fahrtenliste enthalten
     */
    private void removeTripAndTicket(int position) {
        //Optimierung wie gehabt, ohne die gelöschte Fahrt
        tripItems.remove(position);
        HashMap<Fare.Type, ArrayList<TicketToBuy>> newTicketList = OptimisationUtil.startOptimisation(tripItems, activity);
        adapter.notifyDataSetChanged();
        AllTickets.saveData(newTicketList, activity);
        saveData();
    }

    /**
     * Löscht die Fahrt aus der Liste der Fahrten und gibt die entsprechende Anzahl an Fahrten bei angefangenen Fahrscheinen frei und
     * löscht Tickets, wenn diese nur bei dieser Verbindung genutzt werden und optimiert anschließend die zukünftigen Fahrten erneut.
     *
     * @param position des zu löschenden Listenelements
     * @preconditions Mindestens eins der Tickets besitzt eine zugeordnete Fahrt, deren Abfahrtszeit in der Vergangenheit liegt
     * ({@link #checkTicketsIfFuture(int position)} = false)
     * @postconditions Die Fahrt ist nicht mehr in der Fahrtenliste enthalten und auch keinem Fahrschein mehr zugeordnet.
     * Wenn ein Fahrschein dadurch keine Fahrten mehr enthält, wird er ebenfalls aus der Liste der Fahrscheine entfernt.
     * Andere Fahrten, die in der Zukunft liegen, werden anschließend erneut optimiert.
     */
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
        //TODO überprüfen
        AllTickets.saveData(allSavedTickets, activity);
        HashMap<Fare.Type, ArrayList<TicketToBuy>> newTicketList = OptimisationUtil.startOptimisation(tripItems, activity);
        AllTickets.saveData(newTicketList, activity);
        saveData();
    }

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
