package com.example.lkjhgf.public_transport;

import android.os.AsyncTask;

import java.io.IOException;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.QueryTripsContext;
import de.schildbach.pte.dto.QueryTripsResult;

public class QueryMoreTask extends AsyncTask<QueryMoreParameter, Void, QueryTripsResult> {

    NetworkProvider provider;

    @Override
    protected QueryTripsResult doInBackground(QueryMoreParameter... queryMoreParameters) {
        provider = queryMoreParameters[0].getProvider();
        QueryMoreParameter parameter = queryMoreParameters[0];
        QueryTripsResult result = null;

        try {
            result = queryMoreTrips(parameter.getContext(), parameter.getLater());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected QueryTripsResult queryMoreTrips(final QueryTripsContext context, final boolean later)
            throws IOException {
        return provider.queryMoreTrips(context, later);
    }
}
