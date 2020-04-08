package com.example.lkjhgf.helper.form;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.closeUp.SingleCloseUp;
import com.example.lkjhgf.trip.singleTrip.SecondView_AllPossibleConnections;
import com.example.lkjhgf.main_menu.Settings;
import com.example.lkjhgf.public_transport.QueryParameter;
import com.example.lkjhgf.public_transport.QueryTask;

import java.util.Calendar;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Trip;
import de.schildbach.pte.dto.TripOptions;


public class SingleTrip extends Form {

    public SingleTrip(Activity activity, View view, NetworkProvider provider, Trip trip){
        super(activity, view, provider, trip);
        setVisibility(view);
    }

    public SingleTrip(Activity activity, View view, NetworkProvider provider){
        super(activity, view, provider);
        setVisibility(view);
    }

    private void setVisibility(View view){
        Form_Text text = this.getText();
        text.numAdult.setVisibility(View.GONE);
        text.numAdultView.setVisibility(View.GONE);
        text.numChildren.setVisibility(View.GONE);
        text.numChildrenView.setVisibility(View.GONE);
        view.findViewById(R.id.view2).setVisibility(View.GONE);
        view.findViewById(R.id.view1).setVisibility(View.GONE);
    }

    void changeViewToPossibleConnections(){
        intent = new Intent(context, SecondView_AllPossibleConnections.class);
        super.changeViewToPossibleConnections();
    }

    @Override
    public void copy(){
        text.date_view.setText("");
        text.arrival_departure_view.setText("");
        selectedDate = Calendar.getInstance();
    }
}
