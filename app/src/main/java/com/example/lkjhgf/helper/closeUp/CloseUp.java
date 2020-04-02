package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import de.schildbach.pte.dto.Trip;

public abstract class CloseUp {

    public static String EXTRA_TRIP = "com.example.lkjhgf.helper.closeUp.EXTRA_TRIP";

    TextViews textViews;
    Buttons buttons;

    Trip trip;

    Activity activity;

    CloseUp(Activity activity, View view, Trip trip) {
        this.activity = activity;
        this.trip = trip;

        textViews = new TextViews(view, activity.getResources(), this);
        buttons = new Buttons(activity, view, this);
        new CloseUpRecyclerView(activity, view, this);
    }

    public abstract void onAcceptClicked();

    public void onAcceptClicked(Intent newIntent){
        newIntent.putExtra(EXTRA_TRIP, trip);
        activity.startActivity(newIntent);
    }


}
