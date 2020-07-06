package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.adapter.SerializeAdapter;
import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.TripItemTimeComparator;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.recyclerView.futureTrips.OnItemClickListener;
import com.example.lkjhgf.recyclerView.futureTrips.TripAdapter;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.activities.singleTrip.CopyTrip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import de.schildbach.pte.dto.Trip;

import static android.content.Context.MODE_PRIVATE;

/**
 * Übersicht über alle (bisher) geplanten Fahrten
 */
public abstract class MyTripList {

    public static String ALL_SAVED_TRIPS = "com.example.lkjhgf.futureTrips.ALL_SAVED_TRIPS";
    public static String NEW_SAVED_TRIPS = "com.example.lkjhgf.futureTrips.SAVED_TRIPS";
    private static String SAVED_TRIPS_TASK = "com.example.lkjhgf.futureTrips.SAVED_TRIPS_TASK";

    Activity activity;
    ArrayList<TripItem> tripItems;

    private RecyclerView recyclerView;

    TripAdapter adapter;

    BootstrapButton abort, addTrip, calculateTickets;

    String dataPath;

    /**
     * Laden der alten Daten, einfügen der neuen Daten, Ansicht füllen <br/>
     * <p>
     * Lädt die entsprechenden Fahrten, abhängig vom Datenpfad ({@link #loadData(Activity, String dataPath)}<br/>
     * Fügt die übergebene Fahrt der Liste aller Fahrten hinzu ({@link #insertTrip(TripItem)}
     *
     * @param activity aufrufende Aktivität, zB zum starten der nächsten Aktivität
     * @param view     Layout
     * @param tripItem neue Fahrt, die in die Liste von zukünftigen Fahrten eingefügt werden soll
     * @param dataPath gibt an, welche Fahrten geladen werden sollen
     */
    MyTripList(Activity activity, View view, TripItem tripItem, String dataPath) {
        this.activity = activity;
        this.dataPath = dataPath;
        loadData();

        insertTrip(tripItem);
        //TODO kann das hier stehen?
        saveData(dataPath);

        findID(view);
    }

    /**
     * Fügt die Fahrt der Liste hinzu, wenn sie noch nicht in der Liste enthalten ist <br/>
     * <p>
     * Wenn die Fahrt noch nicht enthalten ist, soll sie chronologisch eingefügt werden <br/>
     * Dies geschieht in der Funktion {@link #sortTrips()}
     *
     * @param tripItem Fahrt, die hinzugefügt werden soll
     */
    void insertTrip(TripItem tripItem) {
        if (tripItem != null) {
            if (!tripItems.contains(tripItem)) {
                tripItems.add(tripItem);
                Collections.sort(tripItems, new TripItemTimeComparator());
            }
        }
    }

    /**
     * Recycler View Adapter und Manager, sowie OnClickListener <br/>
     * <p>
     * Bei den OnClick-Methoden wird je eine Methode dieser Klasse aufgerufen <br/>
     * <p>
     * -> onItemClick -> {@link #onItemClick(int position)} <br/>
     * -> löschen -> {@link #onDeleteClicked(int position)} <br/>
     * -> kopieren -> {@link #myOnCopyClicked(int position)} <br/>
     * -> editieren -> {@link #editClickMethod(int position)}
     */
    void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        adapter = new TripAdapter(tripItems, activity);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MyTripList.this.onItemClick(position);
            }

            @Override
            public void onDeleteClicked(int position) {
                MyTripList.this.onDeleteClicked(position);
            }

            public void onCopyClicked(int position) {
                myOnCopyClicked(position);
            }


            public void onEditClicked(int position) {
                editClickMethod(position);
            }
        });
    }

    /**
     * Öffnet die entsprechende Ansicht, abhängig davon, ob die Fahrt optimiert werden soll oder nicht
     * <br/>
     * Als Intent wird der aktuelle Trip übergeben -> dessen Parameter werden zum Füllen der
     * Ansicht genutzt
     *
     * @param position der Fahrt in der Liste, die übergeben werden soll
     * @preconditions der Nutzer hat den Button kopieren geklickt
     * @postconditions die Ansicht für eine neue Fahrt ist mit den Informationen dieser Fahrt gefüllt,
     * die Felder für den Zeitpunkt sind hingegen leer
     */
    private void myOnCopyClicked(int position) {
        TripItem current = tripItems.get(position);

        Intent newIntent;
        // Festlegen, welche Aktivität gestartet werden soll
        if (current.isComplete()) {
            newIntent = new Intent(activity.getApplicationContext(), CopyTrip.class);
        } else {
            newIntent = new Intent(activity.getApplicationContext(),
                    com.example.lkjhgf.activities.multipleTrips.CopyTrip.class);
            // bei einer zu optimierenden Fahrt, müssen noch die #Fahrt und #reisende Personen
            // mit übergeben werden
            newIntent.putExtra(MainMenu.EXTRA_NUM_TRIP, position + 1);
            newIntent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, current.getNumUserClasses());
        }
        newIntent.putExtra(MainMenu.EXTRA_TRIP, current.getTrip());
        startNextActivity(newIntent);
    }

    /**
     * Sicherheitsabfrage an den Nutzer, ob dieser die Fahrt wirklich löschen will<br/>
     *
     * @param position des zu löschenden Elements in der Liste
     * @preconditions der Nutzer hat auf den Button löschen geklickt
     * @postconditions wenn der Nutzer bestätigt, wird das Element gelöscht, lehnt der Nutzer
     * das Löschen hingegen ab, wird die Fahrt nicht gelöscht
     */
    void onDeleteClicked(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Diese Fahrt wirklich löschen?");
        builder.setCancelable(false);
        builder.setPositiveButton("Ja", (dialog, which) -> removeItemAtPosition(position));
        builder.setNegativeButton("Nein", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Klickt der Nutzer auf die Fahrt, wird die Detailansicht dieser Fahrt geöffnet <br/>
     * <p>
     * Abhöngig von der Art der Fahrt, wird die entsprechende Detailansicht geöffnet, in welcher
     * der Nutzer nur zurück kann, nicht die Fahrt erneut hinzugefügt werden <br/>
     *
     * @param position gibt die Position in der Liste an, auf die der Nutzer geklickt hat
     * @preconditions Der Nutzer hat eine Fahrt angetippt
     * @postconditions Detaillierte Anzeige der gewählten Fahrt, jedoch ist der hinzufügen Knopf nicht
     * sichtbar
     */
    abstract void onItemClick(int position);

    /**
     * Sicherheitsabfrage an den Nutzer, ob dieser wirklich die Fahrt editieren will <br/>
     *
     * @param position gibt die Postion des Elements in der Liste an, das editiert werden soll
     * @postconditions Wenn der Nutzer bestätigt die Fahrt editieren zu wollen, wird die Fahrt aus
     * der Liste der gespeicherten Fahrten gelöscht, anschließend wird das entsprechende Formular
     * mit den Daten geladen {@link #startEdit(int position)}
     * @preconditions Der Nutzer hat den Button editieren gedrückt
     */
    private void editClickMethod(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Diese Fahrt editieren?");
        builder.setCancelable(false);
        builder.setNegativeButton("Nein", ((dialog, which) -> dialog.cancel()));
        builder.setPositiveButton("Ja", (dialog, which) -> startEdit(position));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Öffnet das Formular mit den Informationen dieser Fahrt <br/>
     *
     * @param position des zu editierenden Elements in der Liste
     * @preconditions Der Nutzer hat die Abfrage, ob er die Fahrt wirklich editieren will, bestötigt
     * @postconditions Die Fahrt ist nicht mehr in der Liste enthalten, Öffnung der entsprechenden Ansicht
     * mit allen Angaben der gewählten Fahrt
     */
    abstract void startEdit(int position);

    /**
     * Initialiseriung der Attribute <br/>
     * <p>
     * Zuordnung Attribut <-> ID, Design der Buttons <br/>
     *
     * @param view Layout / Ansicht
     */
    private void findID(View view) {
        recyclerView = view.findViewById(R.id.recyclerView5);

        abort = view.findViewById(R.id.BootstrapButton15);
        addTrip = view.findViewById(R.id.BootstrapButton16);
        calculateTickets = view.findViewById(R.id.BootstrapButton17);

        abort.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        addTrip.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        calculateTickets.setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }

    /**
     * Löscht das Element an der jeweiligen Position
     *
     * @param position gibt die Position des zu löschenden Elements in der Liste an
     * @preconditions Der Nutzer hat auf den Button löschen geklickt, und im folgenden Dialog bestätigt,
     * dass er die Fahrt wirklich löschen will
     * @postconditions die Fahrt ist aus der Liste gelöscht
     */
    void removeItemAtPosition(int position) {
        tripItems.remove(position);
        adapter.notifyItemRemoved(position);
        saveData(dataPath);
    }

    /**
     * Startet die im Intent enhaltene Aktivität <br/>
     * <p>
     * bevor die nächste Aktivität gestartet wird, wird die aktuelle Fahrtenliste gespeichert
     *
     * @param newIntent enthält die Informationen, die an die nächste Aktivität übergeben werden sollen
     */
    void startNextActivity(Intent newIntent) {
        saveData(dataPath);
        activity.startActivity(newIntent);
    }

    /**
     * Speichern der Daten <br/>
     * <p>
     * Aufruf der Methode {@link #saveTrips(ArrayList tripItems, String dataPath, Activity)}
     */
    public void saveData(String dataPath) {
        saveTrips(tripItems, dataPath, activity);
    }

    /**
     * Laden der Daten <br/>
     * <p>
     * Aufruf der Methode {@link #loadData(Activity, String dataPath)}
     */
    private void loadData() {
        tripItems = loadData(activity, dataPath);
    }

    /**
     * Speichern der übergebenen Liste <br/>
     * <p>
     * Speichern mittels Shared Preferences und GSON <br/>
     * Manuelle Serialisierung mittels {@link SerializeAdapter} für Trip (Individual und Public) <br/>
     *
     * @param newTripItems - Liste der zu speichernden Elemente
     * @param dataPath     - unter welchem String die Liste gespeichert werden soll
     * @param activity     - zum Speichern benötigt
     */
    public static void saveTrips(ArrayList<TripItem> newTripItems, String dataPath, Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dataPath,
                MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        GsonBuilder builder = new GsonBuilder();
        SerializeAdapter adapter = new SerializeAdapter();
        builder.registerTypeAdapter(Trip.Public.class, adapter);
        builder.registerTypeAdapter(Trip.Individual.class, adapter);
        builder.registerTypeAdapter(NumTicket.class, adapter);
        builder.registerTypeAdapter(TimeTicket.class, adapter);
        Gson gson = builder.create();
        String json = gson.toJson(newTripItems);
        editor.putString(SAVED_TRIPS_TASK, json);
        editor.apply();
    }

    /**
     * Laden der Daten <br/>
     * <p>
     * Manuelle deserialisierung von Trip.leg -> Public und Individual
     * <p>
     * Aus den gespeicherten Verbindungen, werden die Fahrten gelöscht, deren Ankunftszeitpunkt mehr als 24h
     * in der Vergangenheit liegen.
     *
     * @param dataPath Abhängig vom Datenpfad, werden entweder alle Fahrten geladen, oder nur die zu Optimierenden
     */
    static ArrayList<TripItem> loadData(Activity activity, String dataPath) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dataPath,
                MODE_PRIVATE);

        String json = sharedPreferences.getString(SAVED_TRIPS_TASK, null);
        SerializeAdapter adapter = new SerializeAdapter();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Trip.Public.class, adapter);
        builder.registerTypeAdapter(Trip.Individual.class, adapter);
        builder.registerTypeAdapter(Trip.Leg.class, adapter);
        builder.registerTypeAdapter(Ticket.class, adapter);
        builder.registerTypeAdapter(NumTicket.class, adapter);
        builder.registerTypeAdapter(TimeTicket.class, adapter);
        Gson gson = builder.create();
        Type type = new TypeToken<ArrayList<TripItem>>() {
        }.getType();
        ArrayList<TripItem> tripItems = gson.fromJson(json, type);
        if (tripItems == null) {
            tripItems = new ArrayList<>();
        } else {
            Date today = Calendar.getInstance().getTime();
            long oneDay = 24 * 60 * 60 * 1000;
            for (Iterator<TripItem> it = tripItems.iterator(); it.hasNext(); ) {
                TripItem item = it.next();
                if (item.getTrip().getLastArrivalTime().getTime() + oneDay - today.getTime() <= 0) {
                    it.remove();
                }
            }
        }
        return tripItems;
    }

    /**
     * Laden der gespeicherten Fahrten <br/>
     *
     * Ruft {@link #loadData(Activity, String dataPath)} auf.
     * @param activity zum Laden benötigt
     * @param dataPath legt fest, welche Fahrten geladen werden
     * @return Liste, der unter dem Datenpfad gespeicherten Fahrten
     */
    public static ArrayList<TripItem> loadTripList(Activity activity, String dataPath) {
        return loadData(activity, dataPath);
    }

}