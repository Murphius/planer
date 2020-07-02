package com.example.lkjhgf.activities.ticketOverview;

import android.app.Activity;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.ticketOverview.groupedOverview.AllTickets;

/**
 * Anzeigen aller benötigten Fahrscheine <br/>
 *
 * Das Handling der Ansicht erfolgt in der Klasse {@link AllTickets}
 */
public class TicketOverviewGroupedTickets extends Activity {

    AllTickets myGroupedTickets;
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
        super.onBackPressed();
    }
}
