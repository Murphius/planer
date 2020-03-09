package com.example.lkjhgf.public_transport;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Date;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.QueryTripsContext;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.TripOptions;

public class QueryTask extends AsyncTask<QueryParameter, Void, QueryTripsResult> {

    private NetworkProvider provider;

    @Override
    protected QueryTripsResult doInBackground(QueryParameter... queryParameters) {
        provider = new VrrProvider();
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

    protected QueryTripsResult queryMoreTrips(final QueryTripsContext context, final boolean later)
            throws IOException {
        return provider.queryMoreTrips(context, later);
    }
}
