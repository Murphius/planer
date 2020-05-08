package com.example.lkjhgf.publicTransport.query;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

/**
 * Verbindungsanfrage an den Provider <br/>
 *
 * Während der Anfrage selbst, wird ein Ladebildschirm angezeigt, anschließend wird die nächste
 * Aktivität gestartet ({@link com.example.lkjhgf.helper.service.SinglePossibleConnection} oder
 * {@link com.example.lkjhgf.helper.service.MultiplePossibleConnections})
 */
public class QueryTask extends AsyncTask<QueryParameter, Void, QueryTripsResult> {

    // Konstanten fuer Intent
    public static final String EXTRA_QUERY_TRIPS_RESULT = "com.example.lkjhgf.public_transport.EXTRA_QUERYTRIPSRESULT";

    private NetworkProvider provider;
    private Context context;
    private AlertDialog dialog;
    private Intent intent;

    /**
     * Konstruktor mit allen im Verlauf benötigten Werte
     *
     * @param context  - benötigt zum Anzeigen des Dialogs während des Warten auf die Antwort vom Provider
     * @param intent   - zum Wechseln der Aktivität benötigt
     */
    public QueryTask(Context context, Intent intent) {
        this.provider = MainMenu.myProvider.getNetworkProvider();
        this.context = context;
        this.intent = intent;
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

    /**
     * Startet die nächste Aktivität <br/>
     * <p>
     * Startet die nächste Aktivität (welche das ist, ist im Intent hinterlegt) <br/>
     * dabei wird das Ergebnis der Anfrage an die
     * nächste Ansicht übergeben (Verwendung in {@link com.example.lkjhgf.helper.service.PossibleConnections}<br/>
     * Anschließend wird der Dialog geschlossen <br/>
     *
     * @param result Ergebnis der Provider Anfrage
     */
    @Override
    protected void onPostExecute(QueryTripsResult result) {
        if (result != null) {
            intent.putExtra(EXTRA_QUERY_TRIPS_RESULT, result);
            context.startActivity(intent);
        }
        dialog.dismiss();
    }

}
