package com.example.lkjhgf.activities.multipleTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.Incomplete;
import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.MultipleTrip;

import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Trip;

public class EditIncompleteTripFromIncompleteList extends Activity {

    Form form;
    Trip trip;
    int numChildren, numAdult, numTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();

        trip = (Trip) intent.getSerializableExtra(MainMenu.EXTRA_TRIP);
        numChildren = intent.getIntExtra(MainMenu.NUM_CHILDREN, 0);
        numAdult = intent.getIntExtra(MainMenu.NUM_ADULT, 0);
        numTrip = intent.getIntExtra(MainMenu.EXTRA_NUM_TRIP, 1);


        form = new MultipleTrip(this, layout, new VrrProvider(), trip, numChildren, numAdult, numTrip);

        form.setOnClickListener();

        form.setAdapter();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Incomplete.class);

        intent.putExtra(MainMenu.EXTRA_TRIP, trip);
        intent.putExtra(MainMenu.EXTRA_NUM_TRIP, numTrip);
        intent.putExtra(MainMenu.NUM_ADULT, numAdult);
        intent.putExtra(MainMenu.NUM_CHILDREN, numChildren);

        startActivity(intent);
    }
}
