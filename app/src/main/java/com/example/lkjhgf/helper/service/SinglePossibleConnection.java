package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activites.singleTrip.ThirdView_CloseUp;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Trip;

public class SinglePossibleConnection extends PossibleConnections {

    public SinglePossibleConnection (Activity activity, View view, Intent intent, NetworkProvider provider){
        super(activity, view, intent, provider);
        TextViews text = getText();
        text.numChildren.setVisibility(View.GONE);
        text.numAdult.setVisibility(View.GONE);
        text.numChildrenView.setVisibility(View.GONE);
        text.numAdultView.setVisibility(View.GONE);
    }

    @Override
    public void change_view_connection_detail(Trip trip) {
        Intent newIntent = new Intent(context, ThirdView_CloseUp.class);
        super.change_view_connection_detail(trip, newIntent);
    }
}
