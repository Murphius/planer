package com.example.lkjhgf.publicTransport.query;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;

import java.io.IOException;
import java.util.Date;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.TripOptions;

public class QueryRefresh extends AsyncTask<QueryParameter, Void, QueryTripsResult> {

    public interface Action
    {
        void DoSomeThing(QueryTripsResult result);
    }

    private Context context;
    private Action action;
    private NetworkProvider provider;
    private AlertDialog dialog;

    /**
     * Konstruktor mit allen im Verlauf benötigten Werte
     *
     * @param context  - benötigt zum Anzeigen des Dialogs während des Warten auf die Antwort vom Provider
     */
    public QueryRefresh(Context context, Action action) {
        this.provider = MainMenu.myProvider.getNetworkProvider();
        this.context = context;
        this.action = action;
    }

    /**
     * Während / vor der Ausführung, soll der Nutzer informiert werden, dass nach möglichen Verbindungen
     * gesucht wird. <br/>
     * <p>
     * Der Vorgang kann nicht unterbrochen werden <br/>
     * Anzeigen eines Ladebalkens (sich drehender Kreis) und zugehöriger Text <br/>
     * So gelöst, da ProgressDialog deprecated ist <br/>
     * <p>
     * Nutzer über das Suchen der Verbindung informieren mittels Fragment
     */
    @Override
    protected void onPreExecute() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.fragment);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    /**
     * Nach Verbindungen suchen <br/>
     * <p>
     * Ruft die Funktion {@link #queryTrips(Location From, Location via, Location To, Date Zeitpunkt, boolean isDepartureTime, TripOptions Einstellungen)}
     * auf, für die Provider Anfrage
     *
     * @param queryParameters nur die Parameter einer Fahrt hinterlegt -> queryParameter[0]
     * @return QueryTripResult - zu den Parametern passende Verbindungen
     */
    @Override
    protected QueryTripsResult doInBackground(QueryParameter... queryParameters) {
        QueryParameter queryParameter = queryParameters[0];
        QueryTripsResult result = null;
        try {
            result = queryTrips(queryParameter.getFrom(),
                    queryParameter.getVia(),
                    queryParameter.getTo(),
                    queryParameter.getDate(),
                    queryParameter.isDeparture(),
                    queryParameter.getOptions());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Anfrage an den Provider mit den Eingaben des Nutzers
     *
     * @return Mögliche Verbindungen, die zu den Parametern passen
     */
    private QueryTripsResult queryTrips(final Location from,
                                        final @Nullable Location via,
                                        final Location to,
                                        final Date date,
                                        final boolean dep,
                                        final @Nullable TripOptions options) throws IOException {
        return provider.queryTrips(from, via, to, date, dep, options);
    }

    @Override
    protected void onPostExecute(QueryTripsResult result) {
        action.DoSomeThing(result);
        dialog.dismiss();
    }


}
