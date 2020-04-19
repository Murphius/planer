package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import de.schildbach.pte.dto.Trip;

/**
 * Handhabung von der detaillierten Ansicht einer Fahrt
 */
public abstract class CloseUp {

    public static String EXTRA_TRIP = "com.example.lkjhgf.helper.closeUp.EXTRA_TRIP";

    TextViewClass textViewClass;
    ButtonClass buttons;

    Trip trip;

    Activity activity;

    /**
     * Initialisierung der Attribute <br/>
     *
     * @param activity - wird für die Textfarbe von Verspätungen benötigt, sowie für das Starten von Aktivitäten
     * @param view     - Layout
     * @param trip     - Fahrt die detailliert betrachtet werden soll
     */
    CloseUp(Activity activity, View view, Trip trip) {
        this.activity = activity;
        this.trip = trip;

        textViewClass = new TextViewClass(view, activity.getResources(), this);
        buttons = new ButtonClass(activity, view, this);
        new CloseUpRecyclerView(activity, view, this);
    }

    /**
     * Der Nutzer will die Fahrt speichern  <br/>
     *
     * @preconditions Klick auf die "Pinnnadel"
     */
    public abstract void onAcceptClicked();

    /**
     * Ansicht von geplanten Fahrten <br/>
     * <p>
     * Abhängig davon, ob der Nutzer gerade mehrere zu optimierende Fahrten plant, oder eine
     * einzelne Fahrt, wird eine andere Ansicht gestartet. Welche, ist in dem Intent der als
     * Parameter übergeben wird enthalten. Unabhängig von der zu startenden Aktivität, wird
     * die aktuell betrachtete Fahrt in den Intent gepackt. <br/>
     *
     * @param newIntent enthält die Informationen über die neue Aktivität, so wie Informationen, die
     *                  an diese übergeben werden sollen
     * @preconditions Der Nutzer hat "Fahrt merken" geklickt, der Zeitpunkt der Fahrt liegt in
     * der Zukunft
     * @postconditions Ansichtswechsel, in die Übersicht mit geplanten Fahrten; wenn diese
     * Fahrt noch nicht enthalten ist, so soll diese in der Liste enthalten sein
     */
    void onAcceptClicked(Intent newIntent) {
        newIntent.putExtra(EXTRA_TRIP, trip);
        activity.startActivity(newIntent);
    }


}