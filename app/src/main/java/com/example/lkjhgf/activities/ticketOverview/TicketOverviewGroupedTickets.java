package com.example.lkjhgf.activities.ticketOverview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;

/**
 * Anzeigen der benötigten Fahrscheine <br/>
 *
 * Die Tickets werden dabei möglichst groupiert. <br/>
 * D.h. für Fahrscheine mit gleichem Namen sowie gleicher Preisstufe werden die
 * jeweiligen Fahrten zusammen angezeigt.
 *
 * Das Handling der Ansicht erfolgt in der Klasse {@link AllTickets}
 */
public class TicketOverviewGroupedTickets extends Activity {

    private AllTickets myGroupedTickets;

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);

        setContentView(R.layout.ticket_generalview);

        ConstraintLayout layout = findViewById(R.id.constraintLayout4);

        myGroupedTickets = new AllTickets(this, layout);
    }

    @Override
    public void onBackPressed(){
        myGroupedTickets.saveData();
        Intent intent = new Intent(this, TicketOverview.class);
        startActivity(intent);
    }
}
