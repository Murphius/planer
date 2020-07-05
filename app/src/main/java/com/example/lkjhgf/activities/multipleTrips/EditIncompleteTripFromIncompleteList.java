package com.example.lkjhgf.activities.multipleTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Incomplete;
import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.MultipleTrip;

import java.util.HashMap;

import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Fare;
import de.schildbach.pte.dto.Trip;

/**
 * Ansicht des Formulars, mit den Angaben einer geplanten Fahrt, die optimiert werden soll </br>
 *
 * Die Handhabung erfolgt in der Klasse {@link MultipleTrip}. Dabei werden jedoch die Informationen des
 * Intents übergeben, um das Formular vorzubereiten. <br/>
 */
public class EditIncompleteTripFromIncompleteList extends Activity {

    Form form;
    Trip trip;
    HashMap<Fare.Type, Integer> numPersonsPerClass;
    int numTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();

        trip = (Trip) intent.getSerializableExtra(MainMenu.EXTRA_TRIP);

        numPersonsPerClass =  (HashMap<Fare.Type, Integer>) intent.getSerializableExtra(MainMenu.NUM_PERSONS_PER_CLASS);

        numTrip = intent.getIntExtra(MainMenu.EXTRA_NUM_TRIP, 1);


        form = new MultipleTrip(this, layout, trip, numPersonsPerClass, numTrip);

        form.setOnClickListener();

        form.setAdapter();
    }

    /**
     * Bricht der Nutzer das Editieren der Fahrt ab, muss diese wieder in die Liste
     * eingefügt werden.
     *
     * @preconditions Der Nutzer hat auf "zurück" geklickt
     * @postconditions Die Fahrt ist unverändert in der Liste der zu optimierenden Fahrten eingetragen
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Incomplete.class);

        intent.putExtra(MainMenu.EXTRA_TRIP, trip);
        intent.putExtra(MainMenu.EXTRA_NUM_TRIP, numTrip);
        intent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, numPersonsPerClass);

        startActivity(intent);
    }
}
