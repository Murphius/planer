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
import java.util.Iterator;

import de.schildbach.pte.dto.Trip;

import static android.content.Context.MODE_PRIVATE;

public class AllTickets {

    private static String dataPath = "com.example.lkjhgf.helper.ticketOverview.dataPath";
    private static String SAVED_TICKETS_TASK = "com.example.lkjhgf.helper.ticketOverview.SAVED_TICKETS_TASK";

    private Activity activity;

    private TextView fullpriceView;
    private TextView fullpriceHolder;
    private View separatorLine;

    private static ArrayList<ArrayList<TicketToBuy>> allTickets;
    private static int fullPrice;


    public AllTickets(Activity activity, View view) {
        this.activity = activity;

        loadData(activity);

        separatorLine = view.findViewById(R.id.view7);
        fullpriceView = view.findViewById(R.id.textView97);
        fullpriceHolder = view.findViewById(R.id.textView99);

        new RecyclerViewTicketOverview(activity, view, this);
        fullpriceHolder.setText(UtilsString.centToString(fullPrice));

    }

    private static void loadData(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dataPath,
                MODE_PRIVATE);

        String json = sharedPreferences.getString(SAVED_TICKETS_TASK, null);
        InterfaceAdapter adapter = new InterfaceAdapter();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Trip.Public.class, adapter);
        builder.registerTypeAdapter(Trip.Individual.class, adapter);
        builder.registerTypeAdapter(Trip.Leg.class, adapter);
        builder.registerTypeAdapter(Ticket.class, adapter);
        builder.registerTypeAdapter(NumTicket.class, adapter);
        Gson gson = builder.create();

        Type type = new TypeToken<ArrayList<ArrayList<TicketToBuy>>>() {
        }.getType();

        allTickets = gson.fromJson(json, type);

        fullPrice = 0;

        if (allTickets == null) {
            allTickets = new ArrayList<>();
        } else {
            for (Iterator<ArrayList<TicketToBuy>> it1 = allTickets.iterator(); it1.hasNext(); ) {
                for (Iterator<TicketToBuy> it = it1.next().iterator(); it.hasNext(); ) {
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

    private static void saveData(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dataPath,
                MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        GsonBuilder builder = new GsonBuilder();
        InterfaceAdapter adapter = new InterfaceAdapter();
        builder.registerTypeAdapter(Trip.Public.class, adapter);
        builder.registerTypeAdapter(Trip.Individual.class, adapter);
        builder.registerTypeAdapter(NumTicket.class, adapter);
        //TODO Zeitticket
        Gson gson = builder.create();

        for (ArrayList<TicketToBuy> ticketList : allTickets) {
            ticketList.sort((o1, o2) -> o1.compareTo(o2));
        }

        String json = gson.toJson(allTickets);
        editor.putString(SAVED_TICKETS_TASK, json);
        editor.apply();
    }

    public static void saveData(ArrayList<ArrayList<TicketToBuy>> newTickets, Activity activity) {
        if (allTickets == null) {
            allTickets = new ArrayList<>();
        }
        int i = 0;
        if (allTickets.isEmpty()) {
            allTickets.add(newTickets.get(0));
            i++;
        }
        for (; i < newTickets.size(); i++) {
            if (i >= allTickets.size()) {
                allTickets.add(newTickets.get(i));
            } else {
                allTickets.get(i).addAll(newTickets.get(i));
            }
        }


        saveData(activity);
    }

    public ArrayList<ArrayList<TicketToBuy>> getTickets() {
        return allTickets;
    }

    public static ArrayList<ArrayList<TicketToBuy>> loadTickets(Activity activity) {
        loadData(activity);
        return allTickets;
    }

}
