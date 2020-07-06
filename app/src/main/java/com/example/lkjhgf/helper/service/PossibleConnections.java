package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.MyURLParameter;
import com.example.lkjhgf.publicTransport.query.QueryTask;
import com.example.lkjhgf.helper.form.Form;

import java.util.Date;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.form.Form.EXTRA_MYURLPARAMETER;

/**
 * Handhabung des Layouts für mögliche Verbindungen <br/>
 * <p>
 * Die Ansicht wurde in drei Teile unterteilt: <br/>
 * - Textfelder: {@link TextViewClass} <br/>
 * - Buttons: {@link ButtonClass} <br/>
 * - RecyclerView: {@link RecyclerViewService} <br/>
 * Das Füllen / Handhaben dieser ist in die jeweilige Klasse ausgelagert
 *
 * @preconditions Der Nutzer hat das Formular ({@link Form}) ausgefüllt, und es wurden passende
 * Verbindungen gesucht ({@link QueryTask})
 */
public abstract class PossibleConnections {

    protected Context context;
    protected Activity activity;
    protected Intent intent;
    protected MyURLParameter myURLParameter;

    private TextViewClass textViews;
    private ButtonClass buttons;

    // Aus dem Formular
    Date user_date_time;
    Location start, destination, stopover;
    boolean isArrivalTime;
    QueryTripsResult result;

    /**
     * Initialisiert die Attribute, welche in beiden Formularen enthalten sind <br/>
     * <p>
     * Datum, Start, Ziel, Via, Abfahrts / Ankunftszeit sind Nutzereingaben aus dem Formular.
     * Result ist hingegen die passende Provider Antwort auf diese. <br/>
     * <p>
     * Initialisierung der Attribute <br/>
     * Initialisierung des Attributs der Klasse TextViewClass;
     * anlegen des RecyclerViews sowie der Buttons.
     *
     * @param activity - aufrufende Aktivität
     * @param view     - Layout das gefüllt wird
     * @param intent   - Werte aus der vorherigen Aktivität
     */
    PossibleConnections(Activity activity, View view, Intent intent) {
        this.activity = activity;
        this.myURLParameter = (MyURLParameter) intent.getSerializableExtra(EXTRA_MYURLPARAMETER);
        context = activity.getApplicationContext();

        user_date_time = myURLParameter.getStartDate();
        start = myURLParameter.getStartLocation();
        stopover = myURLParameter.getVia();
        destination = myURLParameter.getDestinationLocation();
        isArrivalTime = ! myURLParameter.isDepartureTime();
        result = (QueryTripsResult) intent.getSerializableExtra(QueryTask.EXTRA_QUERY_TRIPS_RESULT);

        textViews = new TextViewClass(view, this);

        buttons = new ButtonClass(view, activity);
    }

    /**
     * Wechsel in die Detailansicht <br/>
     *
     * @param trip Verbindung die der Nutzer genauer betrachten will
     */
    public abstract void changeViewConnectionDetail(Trip trip);

    /**
     * Wechsel in die Detailansicht <br/>
     * <p>
     * @param trip Verbindung die detaillierter betrachtet werden soll
     * @param newIntent Informationen die an die nächste Ansicht übergeben werden sollen, ebenfalls ist 
     *                  enthalten, welche Aktivität als nächstes gestartet werden soll
     */
    void changeViewConnectionDetail(Trip trip, Intent newIntent) {
        newIntent.putExtra(MainMenu.EXTRA_TRIP, trip);
        newIntent.putExtra(EXTRA_MYURLPARAMETER, myURLParameter);
        activity.startActivity(newIntent);
        activity.finish();
    }

    TextViewClass getTextViews() {
        return textViews;
    }

    ButtonClass getButtons(){return buttons;}
}
