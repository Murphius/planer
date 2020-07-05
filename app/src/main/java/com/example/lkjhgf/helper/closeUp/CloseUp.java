package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.Settings;
import com.example.lkjhgf.publicTransport.query.QueryParameter;
import com.example.lkjhgf.publicTransport.query.QueryRefresh;

import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.Trip;

/**
 * Handhabung von der detaillierten Ansicht einer Fahrt
 */
public abstract class CloseUp {

    TextViewClass textViewClass;
    ButtonClass buttons;

    Trip trip;

    Activity activity;
    View view;

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

        textViewClass = new TextViewClass(view, activity.getResources(), this);
        buttons = new ButtonClass(activity, view, this);
        new CloseUpRecyclerView(activity, view, this);
    }

    CloseUp(Trip trip, Activity activity, View view) {
        this.trip = trip;
        this.activity = activity;
        textViewClass = new TextViewClass(view, activity.getResources(), this);
        buttons = new ButtonClass(activity, view, this);
        new CloseUpRecyclerView(activity, view, this);
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
        activity.startActivity(newIntent);
    }

    void refreshTrip() {
        QueryParameter q = new QueryParameter(trip.from, null, trip.to, trip.getFirstDepartureTime(), true, Settings.getTripOptions(activity));
        new QueryRefresh(activity, this::findTrip).execute(q);
    }

    private void findTrip(QueryTripsResult result) {
        if (result == null || result.status != QueryTripsResult.Status.OK) {
            Toast.makeText(activity.getApplicationContext(), "Keine Verbindung gefunden", Toast.LENGTH_SHORT).show();
        } else {
            boolean contains = false;
            for (Trip t : result.trips) {
                if (t.getId().equals(trip.getId())) {
                    trip = t;
                    contains = true;
                    textViewClass.fillTextView();
                    break;
                }
            }
            //TODO in !contains ändern
            if(contains){
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Diese Fahrt ist nicht mehr möglich.\n" +
                        "Soll diese Fahrt editiert werden?");
                builder.setCancelable(false);
                builder.setPositiveButton("Ja", (dialog, which) -> startEditing());
                builder.setNegativeButton("Nein", (dialog, which) -> dialog.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    public void onBackPressed(Intent intent){
        //intent.putExtra(MainMenu.EXTRA_TRIP, trip);
        activity.startActivity(intent);
    }


    public abstract void startEditing();


}