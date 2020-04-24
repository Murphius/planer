package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Incomplete;

/**
 * Detaillierte Ansicht einer einzelnen "zu optimierenden" Fahrt <br/>
 * <p>
 * Die Fahrscheine, die für diese Fahrt benötigt werden sind noch unbekannt -> keine Anzeige <br/>
 * <p>
 * Zusätzlich zu den Informationen in {@link CloseUp} ist in dieser Ansicht noch die Fahrtennummer
 * und die Anzahl reisender Personen enthalten
 */
public class MultipleCloseUp extends CloseUp {

    protected int numTrip;
    protected int numAdult, numChildren;

    /**
     * Layout mit weiteren Informationen füllen <br/>
     * <p>
     * Zusätzliche Informationen sind die "Fahrtennummer" und die anzahl der reisenden Personen <br/>
     * In dieser Ansicht ist die Anzeige der Fahrscheine unsichtbar, denn die benötigten Fahrscheine
     * sind noch unbekannt
     *
     * @see CloseUp#CloseUp(Activity, View)
     */
    public MultipleCloseUp(Activity activity,
                           View view) {
        super(activity, view);

        textViewClass.useTicket.setVisibility(View.GONE);
        textViewClass.ticketView.setVisibility(View.GONE);

        Intent intent = activity.getIntent();
        numTrip = intent.getIntExtra(MainMenu.EXTRA_NUM_TRIP, 1);
        numAdult = intent.getIntExtra(MainMenu.NUM_ADULT, 0);
        numChildren = intent.getIntExtra(MainMenu.NUM_CHILDREN, 0);

        String setText = numTrip + ". Fahrt \n Detaillierte Fahrt";

        TextView viewTitle = view.findViewById(R.id.textView);
        viewTitle.setText(setText);

        setText = numAdult + "";
        textViewClass.numAdult.setText(setText);

        setText = numChildren + "";
        textViewClass.numChildren.setText(setText);
    }

    /**
     * Wechselt in die Ansicht aller geplanten Fahrten <br/>
     * <p>
     * Zusätzlich zu der Fahrt, die übergeben wird, wird bei mehreren Fahrten auch die
     * Fahrtennummer, die #reisende Personen übergeben
     *
     * @preconditions Der Nutzer hat den Button "pinnen" gedrückt
     * @postconditions Die Fahrt wird in die Liste der aktuell geplanten Fahrten eingefügt (außer sie ist bereits
     * enthalten); Anzeigen aller aktuell geplanten Fahrten inklusive der aktuell betrachteten Fahrt
     */
    @Override
    public void onAcceptClicked() {
        Intent newIntent = new Intent(activity, Incomplete.class);
        newIntent.putExtra(MainMenu.NUM_ADULT, numAdult);
        newIntent.putExtra(MainMenu.NUM_CHILDREN, numChildren);
        newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, numTrip);
        super.onAcceptClicked(newIntent);
    }


}
