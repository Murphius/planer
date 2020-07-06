package com.example.lkjhgf.activities.singleTrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Complete;
import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.SingleTrip;
import com.example.lkjhgf.helper.futureTrip.MyTripList;

import de.schildbach.pte.dto.Trip;

/**
 * Kopieren einer einzelnen Fahrt, welche nicht bei der Optimierung berücksichtigt wird <br/>
 * <p>
 *
 * Der Nutzer hat für eine Fahrt den Button "Fahrt kopieren" gedürckt <br/>
 * Event in der Klasse {@link MyTripList} <br/>
 * Dazugehörige Aktivität:
 * {@link Complete}
 * <br/>
 * <p>
 * Mittels Intent wurden die Angaben der vorherigen Fahrt übergeben, um das Formular für den Nutzer auszufüllen,
 * so dass dieser anschließend nur einen neuen Zeitpunkt angeben muss, und die anderen Angaben ändern kann
 * <p>
 *  Das Füllen der Ansicht, sowie das Handling von Klicks erfolgt in der Klasse
 *  {@link SingleTrip}
 */
public class CopyTrip extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();

        Trip trip = (Trip) intent.getSerializableExtra(MainMenu.EXTRA_TRIP);

        Form form = new SingleTrip(this, layout, trip);

        form.setOnClickListener();

        form.setAdapter();

        form.copy();
    }

}
