package com.example.lkjhgf.activities.multipleTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Complete;
import com.example.lkjhgf.activities.futureTrips.CompleteAbortEditingIncompleteTrip;
import com.example.lkjhgf.activities.futureTrips.Incomplete;
import com.example.lkjhgf.helper.MyURLParameter;
import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.MultipleTrip;
import com.example.lkjhgf.helper.futureTrip.MyTripList;

import java.util.HashMap;

import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Fare;
import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.form.Form.EXTRA_MYURLPARAMETER;

/**
 * Editieren einer geplanten Fahrt, die bei der Ermittlung der günstigsten
 * Fahrscheine berücksichtigt werden soll
 * <p>
 * <p>
 * Vorbedingung: Der Nutzer hat unter den zukünftigen Fahrten bei einer Fahrt den editieren-Button gedrückt
 * und bestätigt, dass er die Fahrt bearbeiten will <br/>
 * Aufruf in {@link MyTripList} <br/>
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
public class EditIncompleteTripFromCompleteList extends Activity {

    private Trip trip;
    private HashMap<Fare.Type, Integer> numPersonsPerClass;
    private int numTrip;
    private MyURLParameter myURLParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();

        trip = (Trip) intent.getSerializableExtra(MainMenu.EXTRA_TRIP);
        numPersonsPerClass = (HashMap<Fare.Type, Integer>) intent.getSerializableExtra(MainMenu.NUM_PERSONS_PER_CLASS);
        numTrip = intent.getIntExtra(MainMenu.EXTRA_NUM_TRIP, 1);
        myURLParameter = (MyURLParameter) intent.getSerializableExtra(EXTRA_MYURLPARAMETER);

        Form form = new MultipleTrip(this, layout, trip, numPersonsPerClass, numTrip);

        form.setOnClickListener();

        form.setAdapter();
    }

    /**
     * Wenn der Nutzer das Editieren abbricht, soll die Fahrt und ihre Fahrscheine wieder in die jeweiligen Listen eingefügt werden sollen. <br/>
     * <p>
     * Dies geschieht in {@link CompleteAbortEditingIncompleteTrip}
     * @preconditions Der Nutzer hat auf "zurück" geklickt
     * @postconditions Die Fahrt ist wieder in der Liste der optimierten Fahrten eingetragen & besitzt die gleichen Fahrscheine wie vor dem Editieren
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CompleteAbortEditingIncompleteTrip.class);
        intent.putExtra(MainMenu.EXTRA_TRIP, trip);
        intent.putExtra(MainMenu.EXTRA_NUM_TRIP, numTrip);
        intent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, numPersonsPerClass);
        intent.putExtra(EXTRA_MYURLPARAMETER, myURLParameter);
        startActivity(intent);
    }
}
