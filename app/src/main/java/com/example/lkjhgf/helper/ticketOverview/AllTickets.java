package com.example.lkjhgf.helper.ticketOverview;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.adapter.SerializeAdapter;
import com.example.lkjhgf.helper.ticketOverview.groupedOverview.RecyclerViewGroupedTicketOverview;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
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
     * Lädt die gespeicherten Fahrten und lagert das Management des RecyclerViews an die Klasse {@link RecyclerViewGroupedTicketOverview} aus.
     * <br/>
     * Berechnung des Gesamtpreises
     *
     * @param activity aufrufende Aktivität zum Laden & Speichern benötigt
     * @param view     Layout
     */
    public AllTickets(Activity activity, View view) {
        this.activity = activity;

        loadData(activity, 24 * 60 * 60 * 1000);

        TextView fullpriceHolder = view.findViewById(R.id.textView99);

        new RecyclerViewGroupedTicketOverview(activity, view, this);
        fullpriceHolder.setText(UtilsString.centToString(fullPrice));
    }

    /**
     * Lädt die gespeicherten Fahrscheine <br/>
     *
     * Die Fahrten, welche länger als der Offset her sind, werden nicht in die Liste
     *      * aufgenommen
     *
     * @param activity zum laden benötigt
     * @param offset   gibt an, wie viel Zeit verstrichen sein soll, zwischen aktuellem Zeitpunkt und
     *                 letzter Fahrt, bis ein Ticket gelöscht werden soll
     */
    public static void loadData(Activity activity, long offset) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dataPath,
                MODE_PRIVATE);

        String json = sharedPreferences.getString(SAVED_TICKETS_TASK, null);
        //Manuelles laden der Klassen -> abstrakte Oberklasse -> beim Laden nicht klar, welche
        //Klasse die Objekte haben
        Type type = new TypeToken<HashMap<Fare.Type, ArrayList<TicketToBuy>>>() {
        }.getType();
        Gson gson = generateGson();
        allTickets = gson.fromJson(json, type);

        fullPrice = 0;

        if (allTickets == null) {
            allTickets = new HashMap<>();
        } else {
            //Entfernen von "abgelaufenen" Tickets und Berechnung des aktuellen Gesamtpreises
            for (Fare.Type type1 : allTickets.keySet()) {
                ArrayList<TicketToBuy> currentList = allTickets.get(type1);
                if(currentList == null){
                    currentList = new ArrayList<>();
                }
                for (Iterator<TicketToBuy> it = currentList.iterator(); it.hasNext(); ) {
                    TicketToBuy current = it.next();
                    if (current.isPastTicket(offset)) {
                        it.remove();
                    } else {
                        fullPrice += MainMenu.myProvider.getTicketPrice(current.getTicket(), current.getPreisstufe());
                    }
                }
            }
        }
    }

    /**
     * Speichern der Tickets <br/>
     *
     * Ruft {@link #saveData(Activity)} auf
     */
    public void saveData() {
        saveData(activity);
    }

    /**
     * Speichern der Daten
     *
     * @param activity zum Speichern benötigt
     * @postconditions Die Fahrten sind gespeichert
     */
    private static void saveData(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dataPath,
                MODE_PRIVATE);

        //Manuelles Speichern
        SharedPreferences.Editor editor = sharedPreferences.edit();
        GsonBuilder builder = new GsonBuilder();
        SerializeAdapter adapter = new SerializeAdapter();
        builder.registerTypeAdapter(Trip.Public.class, adapter);
        builder.registerTypeAdapter(Trip.Individual.class, adapter);
        builder.registerTypeAdapter(NumTicket.class, adapter);
        builder.registerTypeAdapter(TimeTicket.class, adapter);
        Gson gson = builder.create();

        for (ArrayList<TicketToBuy> ticketList : allTickets.values()) {
            ticketList.sort(TicketToBuy::compareTo);
        }

        String json = gson.toJson(allTickets);
        editor.putString(SAVED_TICKETS_TASK, json);
        editor.apply();
    }

    /**
     * Die bei der Optimierung ermittelten optimalen Fahrscheine speichern <br/>
     *
     * ({@link com.example.lkjhgf.publicTransport.provider.MyProvider#optimise(ArrayList trips, HashMap savedTickets, Activity)})
     *
     * @param newTickets neue Fahrscheine die gespeichert werden sollen
     * @param activity zum Speichern benötigt
     */
    public static void saveData(HashMap<Fare.Type, ArrayList<TicketToBuy>> newTickets, Activity activity) {
        allTickets = newTickets;
        saveData(activity);
    }

    public HashMap<Fare.Type, ArrayList<TicketToBuy>> getTickets() {
        return allTickets;
    }

    /**
     * Lädt die aktuell gespeicherten Daten <br/>
     *
     * Bei der Optimierung werden nur aktuelle Fahrscheine benötigt, also wird der Offset für
     * {@link #loadData(Activity, long offset)} 0 gewählt.
     *
     * @param activity zum Laden benötigt
     * @return die gespeicherten Fahrten
     */
    public static HashMap<Fare.Type, ArrayList<TicketToBuy>> loadTickets(Activity activity) {
        loadData(activity, 0);
        return allTickets;
    }

    /**
     * Lädt die gespeicherten Fahrten und gibt diese zurück <br/>
     *
     * Laden der Fahrten in das Attribut, Rückgabe des Attributs
     *
     * @param activity zum Laden benötigt
     * @param offset Gibt den zeitlichen Abstand zwischen akutellem Zeitpunkt und letzter Fahrt an,
     *               die maximal vergangen sein darf, bis die Fahrt nicht mehr angezeigt wird
     * @return Liste aller Fahrscheine, deren letzte Fahrt kürzer als der offset vorbei ist
     */
    public static HashMap<Fare.Type, ArrayList<TicketToBuy>> loadTickets(Activity activity, long offset){
        loadData(activity, offset);
        return allTickets;
    }

    public static int getFullPrice() {
        return fullPrice;
    }

    /**
     * Für die manuelle deserialisierung
     */
    private static Gson generateGson(){
        SerializeAdapter adapter = new SerializeAdapter();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Trip.Public.class, adapter);
        builder.registerTypeAdapter(Trip.Individual.class, adapter);
        builder.registerTypeAdapter(Trip.Leg.class, adapter);
        builder.registerTypeAdapter(Ticket.class, adapter);
        builder.registerTypeAdapter(NumTicket.class, adapter);
        builder.registerTypeAdapter(TimeTicket.class, adapter);
        return builder.create();
    }

}
