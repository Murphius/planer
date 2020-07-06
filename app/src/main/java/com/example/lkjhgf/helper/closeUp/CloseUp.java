package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.Settings;
import com.example.lkjhgf.helper.MyURLParameter;
import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.publicTransport.query.QueryParameter;
import com.example.lkjhgf.publicTransport.query.QueryRefresh;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.concurrent.ExecutionException;

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

    Trip trip;

    Activity activity;
    View view;
    boolean changed;
    MyURLParameter myURLParameter;

    /**
     * Initialisierung der Attribute <br/>
     * <p>
     * Mittels Intent wird die Fahrt geladen, welche aktuell betrachtet werden soll
     *
     * @param activity - wird für die Textfarbe von Verspätungen benötigt, sowie für das Starten von Aktivitäten
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

    CloseUp(TripItem trip, Activity activity, View view) {
        this.trip = trip.getTrip();
        this.activity = activity;
        myURLParameter = trip.getMyURLParameter();
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
        newIntent.putExtra(EXTRA_MYURLPARAMETER, myURLParameter);
        activity.startActivity(newIntent);
    }

    void refreshTrip() {
        setChanged(false);
        QueryParameter q = new QueryParameter(myURLParameter);
        refreshTrip(q);

    }

    void refreshTrip(QueryParameter q) {
        new QueryRefresh(activity, this::findTripOuter).execute(q);
    }

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

    private void findTripOuter2(QueryTripsResult result) {
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

    private void findTripOuter(QueryTripsResult result) {
        findTrip(result);
        if (!changed) {
            myURLParameter.changeDate(trip.getFirstDepartureTime());
            QueryParameter q = new QueryParameter(myURLParameter);
            new QueryRefresh(activity, this::findTripOuter2).execute(q);
        }
    }

    public void onBackPressed(Intent intent) {
        activity.startActivity(intent);
    }

    private void setChanged(boolean b) {
        changed = b;
    }

    public Trip getTrip(){
        return trip;
    }
}