package com.example.lkjhgf.activities.singleTrip;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.service.SinglePossibleConnection;

import de.schildbach.pte.VrrProvider;

/**
 * Planung einer einzelnen Fahrt <br/>
 * Anzeigen aller möglichen Verbindungen
 * <p>
 *
 * @preconditions Der Nutzer hat das Formular ausgefüllt
 * {@link com.example.lkjhgf.helper.form.SingleTrip }<br/>
 * Mittels Intent werden vom Provider (hier VRR-Provider) vorgeschlagene Fahrten,
 * sowie die vom Nutzer im Formular gemachten Eingaben an diese Ansicht übergeben.
 *
 * <p>
 * Die Ansicht wird in der Klasse {@link SinglePossibleConnection} gefüllt, sowie Klicks gehandhabt
 */

public class ShowAllPossibleConnections extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_connections_single);

        Intent intent = getIntent();

        ConstraintLayout layout = findViewById(R.id.constraintLayout1);

        new SinglePossibleConnection(this,
                layout,
                intent,
                new VrrProvider());

    }

}






