package com.example.lkjhgf.activities.singleTrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.lkjhgf.helper.MyURLParameter;
import com.example.lkjhgf.helper.closeUp.SingleCloseUp;
import com.example.lkjhgf.R;
import com.example.lkjhgf.publicTransport.query.QueryParameter;
import com.example.lkjhgf.publicTransport.query.QueryTask;

import static com.example.lkjhgf.helper.form.Form.EXTRA_MYURLPARAMETER;

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

    @Override
    public void onBackPressed(){
        Intent intent = getIntent();
        MyURLParameter myURLParameter = (MyURLParameter) intent.getSerializableExtra(EXTRA_MYURLPARAMETER);
        QueryParameter queryParameter = new QueryParameter(myURLParameter.getStartLocation(), myURLParameter.getVia(), myURLParameter.getDestinationLocation(), myURLParameter.getStartDate() ,myURLParameter.isDepartureTime(), myURLParameter.getTripOptions());
        Intent newIntent = new Intent(this, ShowAllPossibleConnections.class);
        newIntent.putExtra(EXTRA_MYURLPARAMETER, myURLParameter);
        new QueryTask(this, newIntent, true).execute(queryParameter);
    }


}
