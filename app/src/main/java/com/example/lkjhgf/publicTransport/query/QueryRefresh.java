package com.example.lkjhgf.publicTransport.query;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

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

    /**
     * Ermöglicht es, das Ergebnis zu setzen & einen Ladebildschirm zu erhalten
     */
    public interface RefreshAction {
        void refreshAction(QueryTripsResult result);
    }

    private Activity activity;
    private RefreshAction action;
    private NetworkProvider provider;
    private AlertDialog dialog;

    /**
     * Konstruktor mit allen im Verlauf benötigten Werte
     *
     * @param activity - benötigt zum Anzeigen des Dialogs während des Warten auf die Antwort vom Provider
     */
    public QueryRefresh(Activity activity, RefreshAction action) {
        this.provider = MainMenu.myProvider.getNetworkProvider();
        this.activity = activity;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(R.layout.fragment, null);
        TextView textView = mView.findViewById(R.id.textView80);
        String text = "Aktualisieren der Verbindung...";
        textView.setText(text);
        builder.setView(mView);
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
        action.refreshAction(result);
        dialog.dismiss();
    }


}
