package com.example.lkjhgf.helper.form;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.singleTrip.ShowAllPossibleConnections;

import de.schildbach.pte.dto.Trip;

/**
 * Handhabung des Formulars für Fahrten, die bei der Optimierung von Fahrtkosten nicht
 * berücksichtigt werden sollen
 */
public class SingleTrip extends Form {

    /**
     * Erzeugen einer neuen Fahrt <br/>
     *
     * Da keine #Personen von Interesse ist, wird dies für den Nutzer nicht angezeigt
     * @see Form#Form(Activity, View)
     */
    public SingleTrip(Activity activity, View view){
        super(activity, view);
        setVisibility(view);
    }

    /**
     * Kopieren oder Editieren einer Fahrt <br/>
     *
     * Das Layout anpassen -> {@link #setVisibility(View)}
     * @see Form#Form(Activity, View, Trip)
     */
    public SingleTrip(Activity activity, View view, Trip trip){
        super(activity, view, trip);
        setVisibility(view);
    }

    /**
     * Passt das Layout an <br/>
     *
     * Alles was mit der Anzahl der reisenden Personen zu tun hat, wird auf unsichtbar gesetzt
     * @param view - Layot
     */
    private void setVisibility(View view){
        TextViewClass text = this.getText();
        text.numAdult.setVisibility(View.GONE);
        text.numAdultView.setVisibility(View.GONE);
        text.numChildren.setVisibility(View.GONE);
        text.numChildrenView.setVisibility(View.GONE);
        view.findViewById(R.id.view2).setVisibility(View.GONE);
        view.findViewById(R.id.view1).setVisibility(View.GONE);
    }

    /**
     * Festlegen, welche Aktivität als nächstes gestartet werden soll
     * <br/>
     * @see Form#changeViewToPossibleConnections()
     */
    void changeViewToPossibleConnections(){
        intent = new Intent(context, ShowAllPossibleConnections.class);
        super.changeViewToPossibleConnections();
    }

}
