package com.example.lkjhgf.helper.ticketOverview;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.adapter.InterfaceAdapter;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.schildbach.pte.dto.Fare;
import de.schildbach.pte.dto.Trip;

import static android.content.Context.MODE_PRIVATE;

/**
 * Handhabung aller Fahrscheine
 */
public class AllTickets {

    private static String dataPath = "com.example.lkjhgf.helper.ticketOverview.dataPath";
    private static String SAVED_TICKETS_TASK = "com.example.lkjhgf.helper.ticketOverview.SAVED_TICKETS_TASK";

    private Activity activity;

    /**
     * Enthält alle Fahrscheine, gruppiert auf die Nutzerklassen
     */
    private static HashMap<Fare.Type, ArrayList<TicketToBuy>> allTickets;
    /**
     * Gesamtpreis für die Preise
     */
    private static int fullPrice;


    /**
     * Lädt die gespeicherten Fahrten und lagert das Management des RecyclerViews an die Klasse {@link RecyclerViewTicketOverview} aus.
     * <br/>
     * Berechnung des Gesamtpreises
     *
     * @param activity aufrufende Aktivität zum Laden & Speichern benötigt
     * @param view Layout
     */
    public AllTickets(Activity activity, View view) {
        this.activity = activity;

        loadData(activity);

        TextView fullpriceHolder = view.findViewById(R.id.textView99);

        new RecyclerViewTicketOverview(activity, view, this);
        fullpriceHolder.setText(UtilsString.centToString(fullPrice));
    }

    /**
     * Lädt die gespeicherten Fahrscheine
     * @param activity zum laden benötigt
     */
    private static void loadData(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dataPath,
                MODE_PRIVATE);

        String json = sharedPreferences.getString(SAVED_TICKETS_TASK, null);
        //Manuelles laden der Klassen -> abstrakte Oberklasse -> beim Laden nicht klar, welche
        //Klasse die Objekte haben
        InterfaceAdapter adapter = new InterfaceAdapter();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Trip.Public.class, adapter);
        builder.registerTypeAdapter(Trip.Individual.class, adapter);
        builder.registerTypeAdapter(Trip.Leg.class, adapter);
        builder.registerTypeAdapter(Ticket.class, adapter);
        builder.registerTypeAdapter(NumTicket.class, adapter);
        //TODO Zeitfahrscheine
        Gson gson = builder.create();

        Type type = new TypeToken<HashMap<Fare.Type, ArrayList<TicketToBuy>>>() {
        }.getType();

        allTickets = gson.fromJson(json, type);

        fullPrice = 0;

        if (allTickets == null) {
            allTickets = new HashMap<>();
        } else {
            //Entfernen von "abgelaufenen" Tickets und Berechnung des aktuellen Gesamtpreises
            for (Fare.Type type1 : allTickets.keySet()) {
                for (Iterator<TicketToBuy> it = allTickets.get(type1).iterator(); it.hasNext(); ) {
                    TicketToBuy current = it.next();
                    if (current.isPastTicket()) {
                        it.remove();
                    } else {
                        fullPrice += MainMenu.myProvider.getTicketPrice(current.getTicket(), current.getPreisstufe());
                    }
                }
            }
        }
    }

    public void saveData() {
        saveData(activity);
    }

    /**
     * Speichern der Daten
     * @param activity zum Speichern benötigt
     */
    private static void saveData(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dataPath,
                MODE_PRIVATE);

        //Manuelles Speichern
        SharedPreferences.Editor editor = sharedPreferences.edit();
        GsonBuilder builder = new GsonBuilder();
        InterfaceAdapter adapter = new InterfaceAdapter();
        builder.registerTypeAdapter(Trip.Public.class, adapter);
        builder.registerTypeAdapter(Trip.Individual.class, adapter);
        builder.registerTypeAdapter(NumTicket.class, adapter);
        //TODO Zeitticket
        Gson gson = builder.create();

        for (ArrayList<TicketToBuy> ticketList : allTickets.values()) {
            ticketList.sort((o1, o2) -> o1.compareTo(o2));
        }

        String json = gson.toJson(allTickets);
        editor.putString(SAVED_TICKETS_TASK, json);
        editor.apply();
    }

    /**
     * Die bei der Optimierung ermittelten optimalen Fahrscheine speichern ({@link com.example.lkjhgf.helper.util.UtilsOptimisation#brauchtEinenTollenNamen(ArrayList zu optimierende Fahrten, Activity)}
     * @param newTickets neue Fahrscheine die gespeichert werden sollen
     * @param activity
     */
    public static void saveData(HashMap<Fare.Type, ArrayList<TicketToBuy>> newTickets, Activity activity) {
        allTickets = newTickets;
        saveData(activity);
    }

    public HashMap<Fare.Type, ArrayList<TicketToBuy>> getTickets() {
        return allTickets;
    }

    /**
     * Lädt die aktuell gespeicherten Daten
     * @param activity zum Laden benötigt
     * @return die gespeicherten Fahrten
     */
    public static HashMap<Fare.Type, ArrayList<TicketToBuy>> loadTickets(Activity activity) {
        loadData(activity);
        return allTickets;
    }

}
