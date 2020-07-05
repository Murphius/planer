package com.example.lkjhgf.activities.futureTrips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.futureTrip.TripListIncomplete;

/**
 * @preconditions Der Nutzer hat das Editieren einer aktualisierten Fahrt abgebrochen
 */
public class IncompleteAfterRefresh extends Activity {

    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);

        setContentView(R.layout.future_trips);

        View view = findViewById(R.id.constraintLayout3);

        new TripListIncomplete(this, view);
    }

    /**
     * Der Nutzer plant weiter Fahrten, oder kehrt ins Hauptmenü zurück<br/>
     * <p>
     *
     * @preconditions Der Nutzer hat auf zurückgeklickt
     * @postconditions Wenn der Nutzer bestätigt, zurück ins Hauptmenü zu wollen, werden die bisher
     * geplanten Fahrten verworfen, sowie der Aktivitätsstack geleert.
     * @postconditions Wenn der Nutzer hingegen ablehnt, folgt keine weitere Handlung
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "Beim Abbrechen werden alle bisher geplanten Fahrten verworfen \n Wirklich abbrechen?");
        builder.setCancelable(false);
        builder.setPositiveButton("Ja", (dialog, which) -> {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            //leert den Aktivitätenstack
            finishAffinity();
            startActivity(intent);
        });
        builder.setNegativeButton("Nein", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
