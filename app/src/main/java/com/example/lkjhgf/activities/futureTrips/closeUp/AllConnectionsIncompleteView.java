package com.example.lkjhgf.activities.futureTrips.closeUp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.closeUp.FutureIncompleteAllTripsCloseUp;

/**
 * Alle geplanten Fahrten <br/>
 * Öffnet die Detailansicht für eine Fahrt, die bei der Optimierung berücksichtigt werden soll <br/>
 * <p>
 * @preconditions Der Nutzer hat auf eine Fahrt geklickt, welche bei der Optimierung
 * berücksichtigt werden soll in der allgemeinen Fahrtenübersicht
 * <br/>
 * Diese Fahrt wird mittels Intent übergeben <br/>
 * Für diese Fahrt erhält der Nutzer nun eine Detaillierte Ansicht <br/>
 * Für jeden Schritt: Startzeit, -ort, Weg, Ankunftszeit, Zielort, Anzahl reisender Personen <br/>
 *
 * Im Unterschied zur normalen Ansicht, kann der Nutzer hier nur zurück, und die Fahrt nicht erneut
 * zu den geplanten Fahrten hinzufügen
 * <br/>
 * Das Füllen der Ansicht erfolgt in der Klasse {@link FutureIncompleteAllTripsCloseUp}.
 */

public class AllConnectionsIncompleteView extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detailed_view);

        RelativeLayout layout = findViewById(R.id.constraintLayout2);

        new FutureIncompleteAllTripsCloseUp(this, layout);
    }

}
