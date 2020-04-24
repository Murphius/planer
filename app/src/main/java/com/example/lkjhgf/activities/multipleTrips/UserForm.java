package com.example.lkjhgf.activities.multipleTrips;

import android.app.Activity;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.form.MultipleTrip;

import de.schildbach.pte.VrrProvider;

/**
 * Planung mehrerer Fahrten <br/>
 * Ansicht eines Formulars, dass der Nutzer ausfüllen muss <br/>
 * Dabei muss der Nutzer Angaben zu seiner geplanten Fahrt machen (Datum, Uhrzeit, Start-, [Zwischenhalt], Zielpunkt, Anzahl reisende
 * Personen) <br/>
 * <p>
 * Das Managen dieser Ansicht erfolgt in der Klasse Form
 * {@link MultipleTrip}
 */

public class UserForm extends Activity {

    com.example.lkjhgf.helper.form.Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        form = new MultipleTrip(this, layout, new VrrProvider());

        form.setOnClickListener();

        form.setAdapter();
    }
}
