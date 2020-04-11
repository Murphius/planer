package com.example.lkjhgf.activites.futureTrips.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.closeUp.FutureSingleCloseUp;

import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.futureTrip.MyTrip.EXTRA_TRIP;

/**
 * Öffnet die Detailansicht für eine einzelne Fahrt <br/>
 * <p>
 * @preconditions Der Nutzer hat auf eine Fahrt geklickt, welche nicht bei der Optimierung
 * berücksichtigt werden soll
 * <br/>
 * Diese Fahrt wird mittels Intent übergeben <br/>
 * Für diese Fahrt erhält der Nutzer nun eine Detaillierte Ansicht <br/>
 * Für jeden Schritt: Startzeit, -ort, Weg, Ankunftszeit, Zielort <br/>
 *
 * Im Unterschied zur normalen Ansicht, kann der Nutzer hier nur zurück, und die Fahrt nicht erneut
 * zu den geplanten Fahrten hinzufügen
 * <br/>
 * Das Füllen der Ansicht erfolgt in der Klasse {@link FutureSingleCloseUp}.
 */

public class Complete extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connection_view_detail);

        Intent intent = getIntent();

        Trip trip = (Trip) intent.getSerializableExtra(EXTRA_TRIP);

        new FutureSingleCloseUp(this, findViewById(R.id.constraintLayout2), trip);
    }
}
