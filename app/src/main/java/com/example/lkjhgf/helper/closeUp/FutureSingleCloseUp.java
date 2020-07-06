package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Complete;

import static com.example.lkjhgf.helper.form.Form.EXTRA_MYURLPARAMETER;

/**
 * Ansicht einer fertig geplanten " nicht optimierten" Fahrt <br/>
 * <p>
 * Im Gegensatz zur "normalen" detaillierten Ansicht einer Fahrt, kann der Nutzer in dieser Ansicht
 * nicht "weiter" klicken und die Fahrt erneut zur Liste zukünftiger Fahrten hinzufügen. <br/>
 *
 * @preconditions Der Nutzer klickt auf eine "normale" Fahrt in der Liste geplanter Fahrten
 * @postconditions Keine Veränderung an der Liste; der Nutzer kann nur die App schließen oder
 * zurück klicken
 */
public class FutureSingleCloseUp extends SingleCloseUp {

    /**
     * @see SingleCloseUp#SingleCloseUp(Activity, View)
     * <p>
     * Der Button "weiter" wird nicht sichtbar gesetzt <br/>
     * Auf zurück -> Ansicht aller geplanten Fahrten
     */
    public FutureSingleCloseUp(Activity activity,
                               View view) {
        super(activity, view);
        buttons.button_accept.setVisibility(View.INVISIBLE);
        buttons.button_back.setOnClickListener(v -> {
            Intent intent = new Intent(activity.getApplicationContext(), Complete.class);
            intent.putExtra(MainMenu.EXTRA_TRIP, trip);
            intent.putExtra(EXTRA_MYURLPARAMETER, myURLParameter);
            activity.startActivity(intent);
        });
    }
}
