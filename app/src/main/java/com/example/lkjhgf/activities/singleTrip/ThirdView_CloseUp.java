package com.example.lkjhgf.activities.singleTrip;

import android.app.Activity;
import android.os.Bundle;

import com.example.lkjhgf.helper.closeUp.SingleCloseUp;
import com.example.lkjhgf.R;

/**
 * Detailansicht einer einzelnen Fahrt, welche nicht optimiert werden soll <br/>
 *
 * @preconditions Der Nutzer hat eine Fahrt in der Ansicht {@link ShowAllPossibleConnections} angeklickt
 * <p>
 * Das Füllen der Ansicht, sowie die Handhabung von Klicks, erfolgt in der Klasse {@link SingleCloseUp}
 */
public class ThirdView_CloseUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);

        new SingleCloseUp(this, findViewById(R.id.constraintLayout2));
    }


}
