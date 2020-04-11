package com.example.lkjhgf.activites.multipleTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.service.MultiplePossibleConnections;

import de.schildbach.pte.VrrProvider;

/**
 * Planung mehrerer Fahrten <br/>
 * Anzeigen aller möglichen Verbindungen
 * <p>
 *
 * Vorbedingung: Der Nutzer hat das Formular ausgefüllt
 * {@link UserForm }<br/>
 * Mittels Intent werden vom Provider (hier VRR-Provider) vorgeschlagene Fahrten,
 * sowie die vom Nutzer im Formular gemachten Eingaben an diese Ansicht übergeben.
 *
 * <p>
 * Die Ansicht wird in der Klasse MultiplePossibleConnections gefüllt, sowie Klciks gehandhabt
 * {@link MultiplePossibleConnections}
 */

public class ShowAllPossibleConnections extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_connections_single);

        Intent intent = getIntent();

        ConstraintLayout layout = findViewById(R.id.constraintLayout1);

        new MultiplePossibleConnections(this,
                layout,
                intent,
                new VrrProvider());

    }
}
