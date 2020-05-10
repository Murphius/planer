package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.multipleTrips.DetailedView;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Trip;

/**
 * Anzeigen möglicher Verbindungen, wenn der Nutzer mehrere Fahrten plant (mit Optimierung der Fahrscheine) <br/>
 *
 * @preconditions siehe {@link PossibleConnections}
 */
public class MultiplePossibleConnections extends PossibleConnections {

    private int numTrip;
    private int numAdult, numChildren;

    /**
     * Neben den Attributen der Oberklasse gibt es noch weitere Attribute welche initialisiert werden <br/>
     * <p>
     * Aufruf des Konstruktors der Oberklasse -> Initialisierung der Attribute <br/>
     * In dem Intent sind zusätzlich noch weitere Informationen enthalten: <br/>
     * die wievielte geplante Fahrt es ist, Anzahl reisender Personen <br/>
     * <p>
     * Zusätzlich zu den Attributen der Oberklasse gibt es noch die Attribute #Fahrt, #reisende Personen.
     * Diese werden mit den Werten aus dem Intent initialisiert. <br/>
     * <p>
     * Die Erläuterung der Parameter ist in {@link PossibleConnections#PossibleConnections(Activity, View, Intent, NetworkProvider)}
     * zu finden
     */
    public MultiplePossibleConnections(Activity activity,
                                       View view,
                                       Intent intent,
                                       NetworkProvider provider) {
        super(activity, view, intent, provider);

        //#Fahrt
        numTrip = intent.getIntExtra(MainMenu.EXTRA_NUM_TRIP, 1);
        String numTripString = numTrip + ". Fahrt \n mögliche Verbindungen";
        TextView numTripView = view.findViewById(R.id.app_name_3);
        numTripView.setText(numTripString);
        //#reisende Personen
        TextViewClass textViews = getTextViews();
        numAdult = intent.getIntExtra(MainMenu.NUM_ADULT, 0);
        numChildren = intent.getIntExtra(MainMenu.NUM_CHILDREN, 0);
        String putText = numAdult + "";
        textViews.numAdult.setText(putText);
        putText = numChildren + "";
        textViews.numChildren.setText(putText);

        RecyclerViewService recyclerViewService = new RecyclerViewMultipleService(view, activity, this, super.getButtons());
        super.getButtons().setRecyclerView(recyclerViewService);
    }

    /**
     * Wechsel in die Detailansicht <br/>
     * <p>
     * Hierbei werden noch weitere Informationen übergeben: #Fahrt, #reisende Personen<br/>
     * Des Weiteren wird die zu startende Aktivität festgelegt
     *
     * @param trip Verbindung die der Nutzer genauer betrachten will
     */
    @Override
    public void changeViewConnectionDetail(Trip trip) {
        Intent newIntent = new Intent(context, DetailedView.class);
        newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, numTrip);
        newIntent.putExtra(MainMenu.NUM_ADULT, numAdult);
        newIntent.putExtra(MainMenu.NUM_CHILDREN, numChildren);
        super.changeViewConnectionDetail(trip, newIntent);
    }
}
