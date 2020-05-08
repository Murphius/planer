package com.example.lkjhgf.publicTransport.query;

import android.os.AsyncTask;

import com.example.lkjhgf.activities.MainMenu;

import java.io.IOException;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.QueryTripsContext;
import de.schildbach.pte.dto.QueryTripsResult;

/**
 * Anfrage an den Server nach weiteren Fahrten (früher oder späterer Zeitpunkt) <br/>
 */
public class QueryMoreTask extends AsyncTask<QueryMoreParameter, Void, QueryTripsResult> {

    private NetworkProvider provider;

    /**
     * Nach späteren / früheren Verbindungen suchen <br/>
     * <p>
     * Ruft die Funktion {@link #queryMoreTrips(QueryTripsContext context, boolean isLater)} auf
     *
     * @param queryMoreParameters nur die Parameter für eine Verbindung hinterlegt -> [0]
     * @return alte Verbindungen und weitere Verbindungen
     */
    @Override
    protected QueryTripsResult doInBackground(QueryMoreParameter... queryMoreParameters) {
        provider = MainMenu.myProvider.getNetworkProvider();
        QueryMoreParameter parameter = queryMoreParameters[0];
        QueryTripsResult result = null;

        try {
            result = queryMoreTrips(parameter.getContext(), parameter.getLater());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Anfrage an den Provider mit dem Kontext der ursprünglichen Anfrage
     *
     * @param context - Kontext der Fahrt
     * @param later - ob der Zeitpunkt früher oder später liegen soll
     * @return (neue) Verbindungen
     */
    private QueryTripsResult queryMoreTrips(final QueryTripsContext context, final boolean later)
            throws IOException {
        return provider.queryMoreTrips(context, later);
    }
}
