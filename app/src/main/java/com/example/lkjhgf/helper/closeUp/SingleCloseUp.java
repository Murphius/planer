package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.lkjhgf.activities.futureTrips.Complete;

import java.util.Calendar;

/**
 * Detaillierte Ansicht einer einzelnen "normalen" Fahrt <br/>
 * <p>
 * Anpassung des Layouts: Anzahl der reisenden Personen, sowie die Fahrscheine -> unsichtbar <br/>
 * Im Gegensatz zu den geplanten Fahrten, für die Fahrscheinberatung, kann sich der Nutzer auch
 * Verbindungen für vergangene Zeitpunkte anzeigen lassen, jedoch können diese Fahrten nicht
 * gespeichert werden
 */
public class SingleCloseUp extends CloseUp {
    /**
     * Anpassung des Layouts: keine Fahrscheine, keine reisenden Personen <br/>
     * Speicherung nur von Fahrten, die in der Zukunft liegen <br/>
     *
     * @see CloseUp#CloseUp(Activity, View)
     */
    public SingleCloseUp(Activity activity,
                         View view) {
        super(activity, view);
        textViewClass.numChildrenView.setVisibility(View.GONE);
        textViewClass.numChildren.setVisibility(View.GONE);
        textViewClass.numAdultView.setVisibility(View.GONE);
        textViewClass.numAdult.setVisibility(View.GONE);
        textViewClass.view.setVisibility(View.GONE);
        textViewClass.useTicket.setVisibility(View.GONE);
        textViewClass.ticketView.setVisibility(View.GONE);

        //Nur Fahrten, die in der Zukunft liegen, können "gespeichert" werden
        buttons.button_accept.setOnClickListener(v -> {
            if (trip.getFirstDepartureTime().before(Calendar.getInstance().getTime())) {
                Toast.makeText(activity.getApplicationContext(), "Nur zukünftige Fahrten können gespeichert werden", Toast.LENGTH_SHORT).show();
            } else {
                onAcceptClicked();
            }
        });
    }

    /**
     * Wechselt in die Ansicht aller geplanten Fahrten <br/>
     *
     * @preconditions Der Nutzer hat den Button "pinnen" gedrückt; die Fahrt liegt in der Zukunft
     * @postconditions Die Fahrt wird in die Liste zukünftiger Fahrten eingefügt (außer sie ist bereits
     * enthalten); Anzeigen aller zukünftigen Fahrten inklusive der aktuell betrachteten Fahrt
     */
    @Override
    public void onAcceptClicked() {
        Intent newIntent = new Intent(activity, Complete.class);
        super.onAcceptClicked(newIntent);
    }


}
