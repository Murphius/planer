package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.publicTransport.query.QueryMoreParameter;
import com.example.lkjhgf.publicTransport.query.QueryMoreTask;
import com.example.lkjhgf.recyclerView.possibleConnections.ConnectionItem;

import java.util.ArrayList;
import java.util.List;

import de.schildbach.pte.dto.Trip;


public class RecyclerViewMultipleService extends RecyclerViewService {

    RecyclerViewMultipleService(View view,
                        Activity activity,
                        PossibleConnections possibleConnections, ButtonClass buttons) {
        super(view, activity, possibleConnections, buttons);
    }

    @Override
    ArrayList<ConnectionItem> fillConnectionList(List<Trip> trips){
        return  UtilsList.fillFutureConnectionList(trips);
    }

}
