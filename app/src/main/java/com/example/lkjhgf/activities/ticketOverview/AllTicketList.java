package com.example.lkjhgf.activities.ticketOverview;

import android.app.Activity;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.ticketOverview.allTicketsOverview.RecyclerViewAllTicketsOverview;

/**
 * Aktivität zum anzeigen aller Tickets <br/>
 * <p>
 * Dabei wird jedes Ticket einzeln angezeigt (keine Gruppierung).
 * Zu jedem Ticket wird bei einem NumTicket die Anzahl genutzer Fahrten angezeigt
 * und bei einem TimeTicket das Zeitfenster, in dem das Ticket genutzt werden soll,
 * sowie in welchen Bereichen das Ticket gültig ist. <br/>
 * <p>
 * Das Füllen der Ansicht findet in der Klasse {@link RecyclerViewAllTicketsOverview} statt.
 *
 * @preconditions Der Nutzer hat im Hauptmenü auf den Button "Fahrscheinübersicht" und anschließend
 * "alle Tickets" ausgewählt.
 * @postconditions Fahrscheine, deren letzte Fahrt mehr als 24h in der Vergangenheit liegen, werden
 * gelöscht
 */
public class AllTicketList extends Activity {

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);

        setContentView(R.layout.ticket_allticket_list);

        ConstraintLayout layout = findViewById(R.id.constraintLayout5);

        new RecyclerViewAllTicketsOverview(this, layout);
    }

}
