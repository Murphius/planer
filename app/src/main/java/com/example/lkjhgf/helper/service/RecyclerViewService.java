package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.publicTransport.QueryMoreParameter;
import com.example.lkjhgf.publicTransport.QueryMoreTask;
import com.example.lkjhgf.recyclerView.possibleConnections.ConnectionAdapter;
import com.example.lkjhgf.recyclerView.possibleConnections.ConnectionItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.Trip;

/**
 * Handhabung des RecyclerViews, welcher die möglichen Verbindungen beeinhaltet <br/>
 * <p>
 * Die Ansicht der möglichen Verbindungen erfolgt im groben Überblick
 */
class RecyclerViewService {

    private ArrayList<ConnectionItem> connection_items;

    private RecyclerView recyclerView;
    private Context context;
    private NetworkProvider provider;

    private PossibleConnections possibleConnections;

    private ConnectionAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Initialisierung der Attribute <br/>
     * <p>
     * RecyclerView im Layout finden, <br/>
     * Hinzufügen von Adaopter und Layoutmanager für diesen in {@link #buildRecyclerView()} <br/>
     * <p>
     * Für das Füllen der Liste möglicher Verbindungen wird die Funktion {@link UtilsList#fillConnectionList(List Trips)}
     *
     * @param view                - Layout
     * @param activity            - benötigt, um ggf. eine Nachricht anzuzeigen, wenn keine passenden Verbindungen gefunden wurden
     * @param possibleConnections - enthält das QueryTripsResult
     * @param provider            - benötigt für das Suchen nach weiteren Verbindungen
     */
    RecyclerViewService(View view,
                        Activity activity,
                        PossibleConnections possibleConnections,
                        NetworkProvider provider) {
        //Attribut <-> ID
        recyclerView = view.findViewById(R.id.recyclerView1);

        context = activity.getApplicationContext();

        // enthält QueryTripsResult
        this.possibleConnections = possibleConnections;
        this.provider = provider;

        //Keine passenden Verbindugnen gefunden -> Nachricht an den Nutzer
        if (possibleConnections.result == null) {
            Toast.makeText(activity, "Keine passenden Verbindungen gefunden", Toast.LENGTH_SHORT).show();
            adapter = new ConnectionAdapter(new ArrayList<>());
        } else {
            // Liste mit den möglichen Verbindungen füllen
            connection_items = UtilsList.fillConnectionList(possibleConnections.result.trips);
            adapter = new ConnectionAdapter(connection_items);
        }
        buildRecyclerView();
    }

    /**
     * Setzt den Adapter und Layout Manager für den RecyclerView, sowie einen OnItemClickListerner <br/>
     * <p>
     * Wenn auf das Item geklickt wird, wird der zugehörige Trip genommen und anschließend die
     * Detaillierte Ansicht geöffnet.
     */
    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            ConnectionItem connection = connection_items.get(position);
            possibleConnections.changeViewConnectionDetail(connection.getTrip());
        });
    }

    /**
     * Suche nach späteren Verbindungen <br/>
     * <p>
     * Dazu wird eine Anfrage an den Provider gestartet ({@link QueryMoreTask}),
     * und anschließend die Liste möglicher
     * Verbindungen aktuallisiert.
     * <br/>
     * Damit der Nutzer nicht selbst an den Punkt der neuen
     * Verbindungen scrollen muss, wird automatisch auf das letzte Element der alten Liste
     * gescrollt
     *
     * @preconditions der Nutzer hat den Button "später" in {@link ButtonClass} gedrückt
     * @postconditions die Liste der möglichen Verbindungen enthält weitere Verbindungen oder der Nutzer
     * wird darüber informiert, dass keine weiteren Verbindungen gefunden werden konnten
     */
    void newConnectionsLater() {
        // alte Größe, um später an diese Position zu scrollen
        int pos = connection_items.size();

        //Provider -> suche nach weiteren Verbindungen
        QueryMoreParameter query = new QueryMoreParameter(possibleConnections.result.context, true, provider);
        AsyncTask<QueryMoreParameter, Void, QueryTripsResult> execute = new QueryMoreTask().execute(query);

        QueryTripsResult resultLater = null;

        try {
            //Neue Ergebnisse holen
            resultLater = execute.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (resultLater == null) {
            Toast.makeText(context,
                    "Es konnten keine späteren Verbindungen gefunden werden",
                    Toast.LENGTH_SHORT).show();
        } else {
            //Liste mit möglichen Verbindungen umwandeln und anschließen in den Adapter packen
            possibleConnections.result = resultLater;
            List<Trip> trips = possibleConnections.result.trips;
            ArrayList<ConnectionItem> newConnections = UtilsList.fillConnectionList(trips);

            connection_items.clear();
            connection_items.addAll(newConnections);

            adapter.notifyDataSetChanged();
            // an das erste neue Element scrollen
            layoutManager.smoothScrollToPosition(recyclerView, null, pos);
        }
    }

    /**
     * Suche nach früheren Verbindungen <br/>
     * <p>
     * Dazu wird eine Anfrage an den Provider gestartet ({@link QueryMoreTask}),
     * und anschließend die Liste möglicher
     * Verbindungen aktuallisiert.
     * <br/>
     * Damit der Nutzer nicht selbst an den Punkt der neuen
     * Verbindungen scrollen muss, wird automatisch auf das erste Element der neuen Liste
     * gescrollt
     *
     * @preconditions der Nutzer hat den Button "früher" in {@link ButtonClass} gedrückt
     * @postconditions die Liste der möglichen Verbindungen enthält weitere Verbindungen oder der Nutzer
     * wird darüber informiert, dass keine weiteren Verbindungen gefunden werden konnten
     */
    void newConnectionsEarlier() {
        // Speichern der alten Listengröße um später auf das erste neue Element zu scrollen
        int length = connection_items.size();

        // Suche nach neuen Verbindungen
        QueryMoreParameter query = new QueryMoreParameter(possibleConnections.result.context, false, provider);
        AsyncTask<QueryMoreParameter, Void, QueryTripsResult> execute = new QueryMoreTask().execute(
                query);

        QueryTripsResult resultEarlier = null;

        try {
            // Neue Ergebnisse holen
            resultEarlier = execute.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (resultEarlier == null) { // Keine weiteren Verbindungen gefunen
            Toast.makeText(context,
                    "Es konnten keine früheren Verbindungen gefunden werden",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Anpassen der aktuellen Liste -> hinzufügen der neuen Elemente
            possibleConnections.result = resultEarlier;
            List<Trip> trips = resultEarlier.trips;
            connection_items.clear();
            connection_items.addAll(UtilsList.fillConnectionList(trips));

            adapter.notifyDataSetChanged();
            // Scrollen an die Position des ersten neuen Elements
            layoutManager.smoothScrollToPosition(recyclerView, null, connection_items.size() - length);
        }
    }
}
