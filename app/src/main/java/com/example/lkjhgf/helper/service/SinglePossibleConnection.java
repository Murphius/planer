package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activities.singleTrip.CloseUp;

import de.schildbach.pte.dto.Trip;

/**
 * Anzeigen möglicher Verbindungen wenn der Nutzer nur eine Fahrt plant (keine Optimierung der
 * Fahrscheine) <br/>
 * @preconditions  siehe {@link PossibleConnections}
 */
public class SinglePossibleConnection extends PossibleConnections {

    /**
     * Ruft den Konstruktor der Superklasse auf <br/>
     * <p>
     * Anschließend wird die Sichtbarkeit der Textfelder für die Personenanzahl auf unsichtbar gestellt
     * Siehe {@link PossibleConnections#PossibleConnections(Activity, View, Intent)} für
     * eine Erläuterung der Parameter.
     */
    public SinglePossibleConnection(Activity activity, View view) {
        super(activity, view, activity.getIntent());
        TextViewClass text = getTextViews();
        text.numChildren.setVisibility(View.GONE);
        text.numAdult.setVisibility(View.GONE);
        text.numChildrenView.setVisibility(View.GONE);
        text.numAdultView.setVisibility(View.GONE);

        RecyclerViewService recyclerViewService = new RecyclerViewService(view, activity, this, super.getButtons());
        super.getButtons().setRecyclerView(recyclerViewService);
    }

    /**
     * Wechsel in die Detailansicht <br/>
     * <p>
     * Hierbei werden keine zusätzlichen Informationen an die nächste Ansicht übergeben,
     * nur der Trip und die zu startende Aktivität <br/>
     *
     * @param trip Verbindung die der Nutzer genauer betrachten will
     */
    @Override
    public void changeViewConnectionDetail(Trip trip) {
        Intent newIntent = new Intent(context, CloseUp.class);
        super.changeViewConnectionDetail(trip, newIntent);
    }
}
