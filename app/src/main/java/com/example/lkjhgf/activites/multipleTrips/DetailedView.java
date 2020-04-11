package com.example.lkjhgf.activites.multipleTrips;

import android.app.Activity;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.closeUp.MultipleCloseUp;
import com.example.lkjhgf.helper.service.MultiplePossibleConnections;

import de.schildbach.pte.dto.Trip;

/**
 * Planung mehrerer Fahrten <br/>
 * Detaillierte Ansicht einer Fahrt <br/>
 * <p>
 * Vorbedingung: Der Nutzer hat eine Fahrt ausgewählt {@link ShowAllPossibleConnections} <br/>
 * Die vom Nutzer ausgewählte Fahrt wird mittels Intent an diese Aktivität übergeben (EXTRA_TRIP) <br/>
 * <p>
 * Die Ansicht wird mittels {@link MultipleCloseUp} gefüllt
 */

public class DetailedView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_view_detail);

        Trip trip = (Trip) getIntent().getSerializableExtra(MultiplePossibleConnections.EXTRA_TRIP);

        ConstraintLayout layout = findViewById(R.id.constraintLayout2);

        new MultipleCloseUp(this, layout, trip);
    }

}
