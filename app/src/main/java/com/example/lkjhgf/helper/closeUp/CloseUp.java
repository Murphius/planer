package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.MyURLParameter;
import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.publicTransport.query.QueryParameter;
import com.example.lkjhgf.publicTransport.query.QueryRefresh;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.form.Form.EXTRA_MYURLPARAMETER;

/**
 * Handhabung von der detaillierten Ansicht einer Fahrt
 */
public abstract class CloseUp {

    TextViewClass textViewClass;
    ButtonClass buttons;
    CloseUpRecyclerView recyclerView;
    /**
     * Fahrt die betrachtet wird
     */
    Trip trip;

    Activity activity;
    View view;
    /**
     * Ob die Fahrt bei der Verbindungsabfrage gefunden wurde
     */
    boolean changed;
    /**
     * Parameter, mit denen nach der Verbindung gesucht werden soll
     */
    MyURLParameter myURLParameter;

    /**
     * Initialisierung der Attribute <br/>
     * <p>
     * Mittels Intent wird die Fahrt geladen, welche aktuell betrachtet werden soll
     *
     * @param activity - wird für die Textfarbe von Verspätungen benötigt, sowie für das Starten von Aktivitäten,
     *                 enthält den Informationen aus der vorherigen Aktivität
     * @param view     - Layout
     */
    CloseUp(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
        // Fahrt die betrachtet werden soll
        this.trip = (Trip) activity.getIntent().getSerializableExtra(MainMenu.EXTRA_TRIP);
        myURLParameter = (MyURLParameter) activity.getIntent().getSerializableExtra(EXTRA_MYURLPARAMETER);
        textViewClass = new TextViewClass(view, activity.getResources(), this);
        buttons = new ButtonClass(activity, view, this);
        recyclerView = new CloseUpRecyclerView(activity, view, this);
    }

    /**
     * Initialisierung der Attribute <br/>
     * <p>
     * Wenn eine bereits optimierte Fahrt betrachtet wird, so werden die Informationen nicht mittels Intent übergeben,
     * sondern sind im TripItem enthalten.
     *
     * @param tripItem Fahrt, die betrachtet wird
     * @param activity aufrufende Aktivität - für Farben und das Starten weiterer Aktivitäten benötigt
     * @param view     - Layout
     */
    CloseUp(TripItem tripItem, Activity activity, View view) {
        this.trip = tripItem.getTrip();
        this.activity = activity;
        myURLParameter = tripItem.getMyURLParameter();
        textViewClass = new TextViewClass(view, activity.getResources(), this);
        buttons = new ButtonClass(activity, view, this);
        recyclerView = new CloseUpRecyclerView(activity, view, this);
    }

    /**
     * Der Nutzer will die Fahrt speichern  <br/>
     *
     * @preconditions Klick auf die "Pinnnadel"
     */
    public abstract void onAcceptClicked();

    /**
     * Ansicht von geplanten Fahrten <br/>
     * <p>
     * Abhängig davon, ob der Nutzer gerade mehrere zu optimierende Fahrten plant, oder eine
     * einzelne Fahrt, wird eine andere Ansicht gestartet. Welche, ist in dem Intent der als
     * Parameter übergeben wird enthalten. Unabhängig von der zu startenden Aktivität, wird
     * die aktuell betrachtete Fahrt in den Intent gepackt. <br/>
     *
     * @param newIntent enthält die Informationen über die neue Aktivität, so wie Informationen, die
     *                  an diese übergeben werden sollen
     * @preconditions Der Nutzer hat "Fahrt merken" geklickt, der Zeitpunkt der Fahrt liegt in
     * der Zukunft
     * @postconditions Ansichtswechsel, in die Übersicht mit geplanten Fahrten; wenn diese
     * Fahrt noch nicht enthalten ist, so soll diese in der Liste enthalten sein
     */
    void onAcceptClicked(Intent newIntent) {
        newIntent.putExtra(MainMenu.EXTRA_TRIP, trip);
        //Merken, mit welchen Informationen nach der Fahrt gesucht wurde
        newIntent.putExtra(EXTRA_MYURLPARAMETER, myURLParameter);
        activity.startActivity(newIntent);
    }

    /**
     * Aktualisierung der Informationen zum Trip <br/>
     *
     * @preconditions Der Nutzer hat auf Aktualisieren geklickt
     * @postconditions Die aktualisierten Informationen werden angezeigt und gegebenenfalls gespeichert
     */
    void refreshTrip() {
        setChanged(false);
        QueryParameter q = new QueryParameter(myURLParameter);
        refreshTrip(q);
    }

    /**
     * Akutalisierung der Informationen zum Trip
     *
     * @param q Parameter, die benötigt werden, um die Fahrt zu suchen
     */
    private void refreshTrip(QueryParameter q) {
        new QueryRefresh(activity, this::findTripOuter).execute(q);
    }

    /**
     * Sucht die Verbindung in der ersten Anfrage an den Server <br/>
     *
     * @param result Ergebnis der Serveranfrage
     * @postconditions Falls die gesuchte Verbindung nicht in der Liste der Fahrten enthalten ist,
     * wird die Zeit des MyURLParameters nicht auf die gleiche Zeit wie bei der Suche gestellt, sondern
     * auf die Abfahrtszeit der Fahrt und es folgt eine erneute Anfrage {@link #findTripInner} <br/>
     * @preconditions Sollte die Fahrt in der Verbindungsliste enthalten sein, ist die Ansicht an
     * diese aktualisiert worden {@link #findTrip(QueryTripsResult)}
     */
    private void findTripOuter(QueryTripsResult result) {
        findTrip(result);
        if (!changed) {
            myURLParameter.changeDate(trip.getFirstDepartureTime());
            QueryParameter q = new QueryParameter(myURLParameter);
            new QueryRefresh(activity, this::findTripInner).execute(q);
        }
    }

    /**
     * Prüft, ob eine Fahrt in der Liste an Verbindungen enthalten ist <br/>
     *
     * @param result Liste mit möglichen Verbindungen
     * @preconditions Der Nutzer hat auf Aktualiseren geklickt
     * @postconditions Wenn die Fahrt in der Liste der Verbindungen enthalten ist, wird die Ansicht
     * an die neuen Informationen angepasst. <br/>
     * Wenn die Fahrt in der Liste enthalten ist, wird dies in der Variablen changed vermerkt.
     */
    private void findTrip(QueryTripsResult result) {
        if (result == null || result.status != QueryTripsResult.Status.OK) {
            Toast.makeText(activity.getApplicationContext(), "Keine Verbindung gefunden", Toast.LENGTH_SHORT).show();
        } else {
            for (Trip t : result.trips) {
                if (t.getId().equals(trip.getId())) {
                    trip = t;
                    textViewClass.fillTextView();
                    recyclerView.update(UtilsList.fillDetailedConnectionList(trip.legs));
                    setChanged(true);
                    break;
                }
            }
        }
    }

    /**
     * Prüft, ob eine Fahrt in der Liste an Verbindungen enthalten ist <br/>
     *
     * @param result Ergebnis der zweiten Anfrage an den Server mit der Startzeit der Fahrt als
     *               Abfahrts- bzw. Ankunftszeit.
     * @preconditions Die Originalanfrage enthält nicht die Fahrt
     * @postconditions Wenn die Fahrt auch in der erneuten Anfrage nicht enthalten ist, wird der
     * Nutzer informiert, dass er sich eine andere Lösung suchen sollte (zB löschen der Fahrt und
     * eine alternative Verbindung suchen) <br/>
     * Ist die Fahrt hingegen enthalten gewesen, so ist die Ansicht aktualisiert.
     */
    private void findTripInner(QueryTripsResult result) {
        findTrip(result);
        if (!changed) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Diese Fahrt konnte nicht mehr gefunden werden.\n" +
                    "Eventuell ist die Fahrt nicht mehr möglich. \n" +
                    "Unter Umständen muss diese bearbeitet werden.");
            builder.setCancelable(false);
            builder.setNegativeButton("Okay", (dialog, which) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void onBackPressed(Intent intent) {
        activity.startActivity(intent);
    }

    private void setChanged(boolean b) {
        changed = b;
    }

    public Trip getTrip() {
        return trip;
    }
}