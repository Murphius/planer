package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.view.View;

/**
 * Ansicht einer fertig geplanten "optimierten" Fahrt <br/>
 * <p>
 * Im Gegensatz zur "normalen" detaillierten Ansicht einer zu optimierenden Fahrt, kann der Nutzer
 * nicht "weiter klicken, und die Fahrt erneut hinzufügen"
 * <br/>
 *
 * @preconditions Der Nutzer klickt die zu optimierende Fahrt in der Liste geplanter Fahrten an
 * @postconditions Keine Veränderung an der Liste; der Nutzer kann nur die App schließen oder
 * zurück klicken
 */
public class FutureIncompleteCloseUp extends MultipleCloseUp {
    /**
     * @see MultipleCloseUp#MultipleCloseUp(Activity, View)
     * <p>
     * Unterschied ist der fehlende "weiter" Button <br/>
     * <p>
     * Unterschied zu {@link FutureIncompleteAllTripsCloseUp}: Aus welcher Ansicht in diese Ansicht gewechselt wird;
     * Hier: Liste der aktuell geplanten Fahrten -> optimierung
     * {@link FutureIncompleteAllTripsCloseUp}: aus der Liste ALLER geplanten Fahrten <br/>
     * <p>
     * Des weiteren wird in {@link FutureIncompleteAllTripsCloseUp} der zu verwendende Fahrschein angezeigt
     */
    public FutureIncompleteCloseUp(Activity activity, View view) {
        super(activity, view);
        buttons.button_accept.setVisibility(View.INVISIBLE);
    }

}
