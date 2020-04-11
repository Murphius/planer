package com.example.lkjhgf.activites.futureTrips.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.closeUp.FutureIncompleteCloseUp;

import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.futureTrip.MyTrip.EXTRA_TRIP;

/**
 * Mehrere Fahrten <br/>
 * Öffnet die Detailansicht für eine Fahrt, die bei der Optimierung berücksichtigt werden soll <br/>
 * <p>
 * @preconditions Der Nutzer hat auf eine Fahrt geklickt, welche bei der Optimierung
 * berücksichtigt werden soll, entweder bei der Planung mehrerer solcher Fahrten, oder in der
 * allgemeinen Fahrtenübersicht
 * <br/>
 * Diese Fahrt wird mittels Intent übergeben <br/>
 * Für diese Fahrt erhält der Nutzer nun eine Detaillierte Ansicht <br/>
 * Für jeden Schritt: Startzeit, -ort, Weg, Ankunftszeit, Zielort, Anzahl reisender Personen <br/>
 *
 * Im Unterschied zur normalen Ansicht, kann der Nutzer hier nur zurück, und die Fahrt nicht erneut
 * zu den geplanten Fahrten hinzufügen
 * <br/>
 * Das Füllen der Ansicht erfolgt in der Klasse {@link FutureIncompleteCloseUp}.
 */

public class Incomplete extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connection_view_detail);

        Intent intent = getIntent();

        Trip trip = (Trip) intent.getSerializableExtra(EXTRA_TRIP);
        ConstraintLayout layout = findViewById(R.id.constraintLayout2);

        new FutureIncompleteCloseUp(this, layout, trip);
    }

}
