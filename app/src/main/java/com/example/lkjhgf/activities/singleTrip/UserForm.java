package com.example.lkjhgf.activities.singleTrip;

import android.app.Activity;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.SingleTrip;
import com.example.lkjhgf.R;

import de.schildbach.pte.VrrProvider;

/**
 * Planung einer einzelnen Fahrt <br/>
 * Ansicht eines Formulars, dass der Nutzer ausfüllen muss <br/>
 * Dabei muss der Nutzer Angaben zu seiner geplanten Fahrt machen (Datum, Uhrzeit, Start-, [Zwischenhalt], Zielpunkt) <br/>
 * <p>
 * Das Managen dieser Ansicht erfolgt in der Klasse Form
 * {@link SingleTrip}
 */

public class UserForm extends Activity {

    Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        form = new SingleTrip(this, layout, new VrrProvider());

        form.setOnClickListener();
        
        form.setAdapter();
    }

}
