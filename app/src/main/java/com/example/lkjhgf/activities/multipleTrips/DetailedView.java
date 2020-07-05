package com.example.lkjhgf.activities.multipleTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.MyURLParameter;
import com.example.lkjhgf.helper.closeUp.MultipleCloseUp;
import com.example.lkjhgf.publicTransport.query.QueryParameter;
import com.example.lkjhgf.publicTransport.query.QueryTask;

import static com.example.lkjhgf.helper.form.Form.EXTRA_MYURLPARAMETER;

/**
 * Planung mehrerer Fahrten <br/>
 * Detaillierte Ansicht einer Fahrt <br/>
 * <p>
 * Vorbedingung: Der Nutzer hat eine Fahrt ausgewählt {@link ShowAllPossibleConnections} <br/>
 * Die vom Nutzer ausgewählte Fahrt wird mittels Intent an diese Aktivität übergeben (EXTRA_TRIP) <br/>
 * <p>
 * Die Ansicht wird mittels {@link MultipleCloseUp} gefüllt
 */

public class DetailedView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);

        RelativeLayout layout = findViewById(R.id.constraintLayout2);

        new MultipleCloseUp(this, layout);
    }

    @Override
    public void onBackPressed(){
        Intent intent = getIntent();
        MyURLParameter myURLParameter = (MyURLParameter) intent.getSerializableExtra(EXTRA_MYURLPARAMETER);
        QueryParameter queryParameter = new QueryParameter(myURLParameter.getStartLocation(), myURLParameter.getVia(), myURLParameter.getDestinationLocation(), myURLParameter.getStartDate() ,myURLParameter.isDepartureTime(), myURLParameter.getTripOptions());
        Intent newIntent = new Intent(this, ShowAllPossibleConnections.class);
        newIntent.putExtra(EXTRA_MYURLPARAMETER, myURLParameter);
        newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, intent.getIntArrayExtra(MainMenu.EXTRA_NUM_TRIP));
        newIntent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, intent.getSerializableExtra(MainMenu.NUM_PERSONS_PER_CLASS));
        new QueryTask(this, newIntent, true).execute(queryParameter);
    }

}
