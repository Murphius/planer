package com.example.lkjhgf.publicTransport.query;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;

import java.io.IOException;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.QueryTripsContext;
import de.schildbach.pte.dto.QueryTripsResult;

/**
 * Anfrage an den Server nach weiteren Fahrten (früher oder späterer Zeitpunkt) <br/>
 */
public class QueryMoreTask extends AsyncTask<QueryMoreParameter, Void, QueryTripsResult> {

    private Context context;
    private NetworkProvider provider;
    private AlertDialog dialog;

    /**
     * Konstruktor mit allen im Verlauf benötigten Werte
     *
     * @param context  - benötigt zum Anzeigen des Dialogs während des Warten auf die Antwort vom Provider
     */
    public QueryMoreTask(Context context) {
        this.provider = MainMenu.myProvider.getNetworkProvider();
        this.context = context;
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

    /**
     *Schließt die Anzeige
     * @param result Ergebnis der Provider Anfrage
     */
    @Override
    protected void onPostExecute(QueryTripsResult result) {
        dialog.dismiss();
    }
}
