package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.view.View;

import de.schildbach.pte.dto.Trip;

public abstract class CloseUp {

    TextViews textViews;
    Buttons buttons;

    Trip trip;

    CloseUp(Activity activity, View view, Trip trip) {
        this.trip = trip;

        textViews = new TextViews(view, activity.getResources(), this);
        buttons = new Buttons(activity, view);
        new CloseUpRecyclerView(activity, view, this);
    }

}
