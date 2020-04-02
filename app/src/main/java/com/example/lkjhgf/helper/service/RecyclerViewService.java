package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.public_transport.QueryMoreParameter;
import com.example.lkjhgf.public_transport.QueryMoreTask;
import com.example.lkjhgf.trip.secondView_service.Connection_adapter;
import com.example.lkjhgf.trip.secondView_service.Connection_item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.Trip;

class RecyclerViewService {

    private ArrayList<Connection_item> connection_items;

    private RecyclerView recyclerView;
    private Context context;
    private NetworkProvider provider;

    private PossibleConnections possibleConnections;

    private Connection_adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    RecyclerViewService(View view,
                        Activity activity,
                        PossibleConnections possibleConnections,
                        NetworkProvider provider) {

        recyclerView = view.findViewById(R.id.recyclerView1);

        context = activity.getApplicationContext();

        this.possibleConnections = possibleConnections;
        this.provider = provider;

        if (possibleConnections.result == null) {
            Toast.makeText(activity, "Keine passenden Verbindungen gefunden", Toast.LENGTH_SHORT).show();
            adapter = new Connection_adapter(new ArrayList<>());
        } else {
            connection_items = Utils.fillConnectionList(possibleConnections.result.trips);
            adapter = new Connection_adapter(connection_items);
        }
        buildRecyclerView();
    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.set_on_item_click_listener(position -> {
            Connection_item connection = connection_items.get(position);
            possibleConnections.change_view_connection_detail(connection.getTrip());
        });
    }

    void newConnectionsLater() {
        int pos = connection_items.size();

        QueryMoreParameter query = new QueryMoreParameter(possibleConnections.result.context, true, provider);
        AsyncTask<QueryMoreParameter, Void, QueryTripsResult> execute = new QueryMoreTask().execute(query);

        try {
            possibleConnections.result = execute.get();
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (possibleConnections.result == null) {
            Toast.makeText(context,
                    "Es konnten keine späteren Verbindungen gefunden werden",
                    Toast.LENGTH_SHORT).show();
        }else{
            List<Trip> trips = possibleConnections.result.trips;
            ArrayList<Connection_item> newConnections = Utils.fillConnectionList(trips);

            connection_items.clear();
            connection_items.addAll(newConnections);

            adapter.notifyDataSetChanged();
            layoutManager.smoothScrollToPosition(recyclerView, null, pos);
        }
    }

    void newConnectionsEarlier() {
        QueryMoreParameter query = new QueryMoreParameter(possibleConnections.result.context, false, provider);

        AsyncTask<QueryMoreParameter, Void, QueryTripsResult> execute = new QueryMoreTask().execute(
                query);

        QueryTripsResult resultEarlier = null;

        int length = connection_items.size();

        try {
            resultEarlier = execute.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (resultEarlier == null) {
            Toast.makeText(context,
                    "Es konnten keine früheren Verbindungen gefunden werden",
                    Toast.LENGTH_SHORT).show();
        }else{
            possibleConnections.result = resultEarlier;
            List<Trip> trips = resultEarlier.trips;
            connection_items.clear();
            connection_items.addAll(Utils.fillConnectionList(trips));

            adapter.notifyDataSetChanged();
            layoutManager.smoothScrollToPosition(recyclerView, null, connection_items.size()-length);
        }
    }
}
