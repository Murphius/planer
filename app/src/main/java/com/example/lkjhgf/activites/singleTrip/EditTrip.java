package com.example.lkjhgf.activites.singleTrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activites.futureTrips.Complete;
import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.SingleTrip;

import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.futureTrip.MyTrip.EXTRA_TRIP;

/**
 * Editieren einer einzelnen geplanten Fahrt, die nicht bei der Ermittlung der günstigsten
 * Fahrscheine berücksichtigt werden soll
 * <p>
 *
 * Vorbedingung: Der Nutzer hat unter den zukünftigen Fahrten bei einer Fahrt den editieren-Button gedrückt
 * und bestätigt, dass er die Fahrt bearbeiten will <br/>
 * Aufruf in {@link com.example.lkjhgf.helper.futureTrip.MyTrip} <br/>
 * Zugehörige Aktivität:
 * {@link Complete}<br/>
 * <p>
 * Mittels Intent wurden die Angaben der vorherigen Fahrt übergeben, um das Formular für den Nutzer auszufüllen,
 * so dass dieser anschließend nur die Angaben, die er bearbeiten wollte, ändern muss
 * <p>
 * Das Füllen der Ansicht, sowie das Handling von Klicks erfolgt in der Klasse {@link SingleTrip}
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

        form = new SingleTrip(this, layout, new VrrProvider(), trip);

        form.setOnClickListener();

        form.setAdapter();
    }



}
