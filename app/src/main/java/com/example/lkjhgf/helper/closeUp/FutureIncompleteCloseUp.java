package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activites.futureTrips.Incomplete;

import de.schildbach.pte.dto.Trip;

/**
 * Ansicht einer fertig geplanten "optimierten" Fahrt <br/>
 *
 * Im Gegensatz zur "normalen" detaillierten Ansicht einer zu optimierenden Fahrt, werden in
 * dieser Ansicht die zu nutzenden Fahrscheine angezeigt. <br/>
 * Desweiteren kann der Nutzer die Fahrt nicht erneut zur Liste zukünftiger Fahrten hinzufügen <br/>
 *
 * @preconditions Der Nutzer klickt die zu optimierende Fahrt in der Liste geplanter Fahrten an
 * @postconditions Keine Veränderung an der Liste; der Nutzer kann nur die App schließen oder
 * zurück klicken
 */
public class FutureIncompleteCloseUp extends MultipleCloseUp {
    /**
     * @see MultipleCloseUp#MultipleCloseUp(Activity, View, Trip)
     *
     * Unterschiede sind das Anzeigen von Fahrscheinen sowie die unterschiedliche Funktion
     * beim zurückklicken und das es keinen "weiter" Button gibt
     */
    public FutureIncompleteCloseUp(Activity activity,
                                   View view,
                                   Trip trip) {
        super(activity, view, trip);
        // Layout verändern
        textViewClass.ticket.setVisibility(View.VISIBLE);
        textViewClass.useTicket.setVisibility(View.VISIBLE);
        buttons.button_accept.setVisibility(View.INVISIBLE);
        //TODO die Tickets müssen im View angezeigt werden
        buttons.button_back.setOnClickListener(v -> {
            Intent intent = new Intent(activity.getApplicationContext(), Incomplete.class);
            activity.startActivity(intent);
        });
    }
}
