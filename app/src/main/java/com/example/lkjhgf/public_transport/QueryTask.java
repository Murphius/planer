package com.example.lkjhgf.public_transport;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.recyclerView.possibleConnections.ConnectionItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.TripOptions;

public class QueryTask extends AsyncTask<QueryParameter, Void, QueryTripsResult> {

    // Konstante fuer Intent
    public static final String EXTRA_CONNECTION_ITEMS = "com.example.lkjhgf.public_transport.EXTRA_CONNECTION_ITEMS";
    public static final String EXTRA_TRIP_CONTEXT = "com.example.lkjhgf.public_transport.EXTRA_TRIP_CONTEXT";
    public static final String EXTRA_TEST = "com.example.lkjhgf.public_transport.EXTRA_TEST";

    private NetworkProvider provider;
    private Context context;
    private AlertDialog dialog;
    private Intent intent;

    public QueryTask(NetworkProvider provider, Context context, Intent intent) {
        this.provider = provider;
        this.context = context;
        this.intent = intent;
    }

    @Override
    protected void onPreExecute() {
        //Nutzer über das Suchen der Verbindung informieren mittels Fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.fragment);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    protected QueryTripsResult doInBackground(QueryParameter... queryParameters) {
        QueryParameter queryParameter = queryParameters[0];
        QueryTripsResult result = null;
        try {
            result = queryTrips(queryParameter.getFrom(),
                    queryParameter.getVia(),
                    queryParameter.getTo(),
                    queryParameter.getDate(),
                    queryParameter.isDep(),
                    queryParameter.getOptions());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    protected QueryTripsResult queryTrips(final Location from,
                                          final @Nullable Location via,
                                          final Location to,
                                          final Date date,
                                          final boolean dep,
                                          final @Nullable TripOptions options) throws IOException {
        return provider.queryTrips(from, via, to, date, dep, options);
    }

    @Override
    protected void onPostExecute(QueryTripsResult result) {
        if (result != null) {
            ArrayList<ConnectionItem> connection_items = Utils.fillConnectionList(result.trips);
            intent.putExtra(EXTRA_TRIP_CONTEXT, result.context);
            intent.putExtra(EXTRA_TEST, result);
            intent.putExtra(EXTRA_CONNECTION_ITEMS, connection_items);
            context.startActivity(intent);
        }
        dialog.dismiss();
    }


}
