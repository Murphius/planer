package com.example.lkjhgf.activites.multipleTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activites.futureTrips.Complete;
import com.example.lkjhgf.activites.futureTrips.Incomplete;
import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.MultipleTrip;

import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.futureTrip.MyTrip.EXTRA_NUM_ADULT;
import static com.example.lkjhgf.helper.futureTrip.MyTrip.EXTRA_NUM_CHILDREN;
import static com.example.lkjhgf.helper.futureTrip.MyTrip.EXTRA_TRIP;

/**
 * Editieren einer geplanten Fahrt, die bei der Ermittlung der günstigsten
 * Fahrscheine berücksichtigt werden soll
 * <p>
 *
 * Vorbedingung: Der Nutzer hat unter den zukünftigen Fahrten bei einer Fahrt den editieren-Button gedrückt
 * und bestätigt, dass er die Fahrt bearbeiten will <br/>
 * Aufruf in {@link com.example.lkjhgf.helper.futureTrip.MyTrip} <br/>
 * Zugehörige Aktivitäten:
 * {@link Complete} oder
 * {@link Incomplete} <br/>
 * <p>
 * Mittels Intent wurden die Angaben der vorherigen Fahrt übergeben, um das Formular für den Nutzer auszufüllen,
 * so dass dieser anschließend nur die Angaben, die er bearbeiten wollte, ändern muss
 * <p>
 * Das Füllen der Ansicht, sowie das Handling von Klicks erfolgt in der Klasse {@link MultipleTrip}
 * </p>
 */

public class EditTrip extends Activity {

    Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();

        Trip trip = (Trip) intent.getSerializableExtra(EXTRA_TRIP);
        int numChildren = intent.getIntExtra(EXTRA_NUM_CHILDREN, 0);
        int numAdult = intent.getIntExtra(EXTRA_NUM_ADULT, 0);

        form = new MultipleTrip(this, layout, new VrrProvider(), trip, numChildren, numAdult);

        form.setOnClickListener();

        form.setAdapter();
    }
}
