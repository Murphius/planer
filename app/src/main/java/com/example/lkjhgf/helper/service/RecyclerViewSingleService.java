package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.view.View;

import com.example.lkjhgf.helper.util.Utils;
import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.recyclerView.possibleConnections.ConnectionItem;

import java.util.ArrayList;
import java.util.List;

import de.schildbach.pte.dto.Trip;

/**
 * Handhabung des RecyclerViews, welcher die möglichen Verbindungen beeinhaltet für eine einzelne, nicht
 * zu optimierende Fahrt<br/>
 * <p>
 * Die Ansicht der möglichen Verbindungen erfolgt im groben Überblick
 */
public class RecyclerViewSingleService extends RecyclerViewService {

    RecyclerViewSingleService(View view,
                        Activity activity,
                        PossibleConnections possibleConnections, ButtonClass buttons) {
        super(view,activity,possibleConnections, buttons);
    }

    @Override
    ArrayList<ConnectionItem> fillConnectionList(List<Trip> trips){
        return UtilsList.fillConnectionList(trips);
    }
}
