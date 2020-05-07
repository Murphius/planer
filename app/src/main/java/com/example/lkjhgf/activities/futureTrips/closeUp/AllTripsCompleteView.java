package com.example.lkjhgf.activities.futureTrips.closeUp;

import android.app.Activity;
import android.os.Bundle;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.closeUp.FutureSingleCloseUp;

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

public class AllTripsCompleteView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detailed_view);

        new FutureSingleCloseUp(this, findViewById(R.id.constraintLayout2));
    }
}