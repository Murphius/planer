package com.example.lkjhgf.helper.ticketOverview;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.adapter.InterfaceAdapter;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketInformationHolder;
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
    private View view;

    private TextView fullpriceView;
    private TextView fullpriceHolder;
    private View separatorLine;

    private static ArrayList<TicketInformationHolder> allTickets;
    private int fullPrice;


    public AllTickets(Activity activity, View view) {
        this.activity = activity;
        this.view = view;

        loadData();

        separatorLine = view.findViewById(R.id.view7);
        fullpriceView = view.findViewById(R.id.textView97);
        fullpriceHolder = view.findViewById(R.id.textView99);

        if (allTickets.isEmpty()) {
            separatorLine.setVisibility(View.GONE);
            fullpriceHolder.setVisibility(View.GONE);
            fullpriceView.setVisibility(View.GONE);
        } else {
            RecyclerViewTicketOverview recyclerViewTicketOverview = new RecyclerViewTicketOverview(activity, view, this);
            fullPrice = allTickets.get(0).getCosts();
            fullpriceHolder.setText(UtilsString.centToString(fullPrice));
        }
    }

    private void loadData() {
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

        Type type = new TypeToken<ArrayList<TicketInformationHolder>>() {
        }.getType();

        allTickets = gson.fromJson(json, type);

        if (allTickets == null) {
            allTickets = new ArrayList<>();
        } else {
            for(Iterator<TicketInformationHolder> it = allTickets.iterator(); it.hasNext();){
                TicketInformationHolder current = it.next();
                if (current.isPastTicket()) {
                    it.remove();
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
        String json = gson.toJson(allTickets);
        editor.putString(SAVED_TICKETS_TASK, json);
        editor.apply();
    }

    public static void saveData(ArrayList<TicketInformationHolder> newTickets, Activity activity) {
        allTickets = newTickets;
        saveData(activity);
    }

    public ArrayList<TicketInformationHolder> getTickets() {
        return allTickets;
    }
}
