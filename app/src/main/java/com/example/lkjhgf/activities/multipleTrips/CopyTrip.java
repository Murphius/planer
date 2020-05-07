package com.example.lkjhgf.activities.multipleTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Complete;
import com.example.lkjhgf.activities.futureTrips.Incomplete;
import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.MultipleTrip;
import com.example.lkjhgf.helper.futureTrip.MyTripList;

import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Trip;

/**
 * Kopieren einer Fahrt, die bei der Ermittlung der günstigsten Fahrscheine
 * berücksichtigt werden soll <br/>
 * <p>
 *
 * Der Nutzer hat für eine zukünftige Fahrt den Button "Fahrt kopieren" gedürckt <br/>
 * Event in der Klasse {@link MyTripList} <br/>
 * Dazugehörige Aktivitäten:
 * {@link Complete}
 * oder
 * {@link Incomplete}
 * <br/>
 * <p>
 * Mittels Intent wurden die Angaben der vorherigen Fahrt übergeben, um das Formular für den Nutzer auszufüllen,
 * so dass dieser anschließend nur einen neuen Zeitpunkt angeben muss, und die anderen Angaben ändern kann
 * <p>
 *  Das Füllen der Ansicht, sowie das Handling von Klicks erfolgt in der Klasse
 *  {@link MultipleTrip}
 */
public class CopyTrip extends Activity {
    Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();

        Trip trip = (Trip) intent.getSerializableExtra(MainMenu.EXTRA_TRIP);
        int numChildren = intent.getIntExtra(MainMenu.NUM_CHILDREN, 0);
        int numAdult = intent.getIntExtra(MainMenu.NUM_ADULT, 0);
        int numTrip = intent.getIntExtra(MainMenu.EXTRA_NUM_TRIP, 1);

        form = new MultipleTrip(this, layout, new VrrProvider(), trip, numChildren, numAdult, numTrip);

        form.setOnClickListener();

        form.setAdapter();

        form.copy();
    }
}
