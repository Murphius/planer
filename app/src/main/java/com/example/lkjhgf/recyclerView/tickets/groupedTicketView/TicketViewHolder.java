package com.example.lkjhgf.recyclerView.tickets.groupedTicketView;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.font.Typicon;
import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.futureTrips.closeUp.AllConnectionsIncompleteView;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;

/**
 * Gruppierte Annzeige von Fahrscheinen <br/>
 * <p>
 * Dabei sind NumTickets mit gleicher Preisstufe zusammengefasst und
 * TimeTickets mit gleichem Geltungsbereich
 * <br/><
 * Zeigt auch die zugehörigen Verbindungen an
 */
class TicketViewHolder extends RecyclerView.ViewHolder {

    private Activity activity;

    private TextView quantity;
    private TextView ticketName;
    private TextView farezoneView;
    private TextView costs;
    private TextView freeTrips;
    private BootstrapButton showDetails;
    private RecyclerView recyclerView;

    /**
     * Initalisierung der Attribute <br/>
     *
     * @param activity zum Starten der Detailansicht benötigt
     * @param view     Layout
     * @param listener - zum Anzeigen / Verbergen der Fahrten
     */
    TicketViewHolder(Activity activity, View view, OnItemClickListener listener) {
        super(view);
        this.activity = activity;

        quantity = view.findViewById(R.id.textView96);
        ticketName = view.findViewById(R.id.textView98);
        farezoneView = view.findViewById(R.id.textView100);
        costs = view.findViewById(R.id.textView101);
        freeTrips = view.findViewById(R.id.textView103);

        showDetails = view.findViewById(R.id.BootstrapButton37);
        showDetails.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        showDetails.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.showDetails(position);
                }
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView8);
    }

    /**
     * Füllt die Ansicht mit den Werten des Parameters
     *
     * @param currentItem enthält die Informationen, welche in der Ansicht angezeigt werden sollen
     */
    void fillView(TicketItem currentItem) {
        String text = currentItem.getQuantity() + "x";
        quantity.setText(text);
        ticketName.setText(currentItem.getTicket().getName());
        farezoneView.setText(currentItem.getPreisstufe());
        int costsValue = currentItem.getQuantity() * currentItem.getTicket().getPrice(MainMenu.myProvider.getPreisstufenIndex(currentItem.getPreisstufe()));
        text = currentItem.getQuantity() + " * "
                + UtilsString.centToString(currentItem.getTicket().getPrice(MainMenu.myProvider.getPreisstufenIndex(currentItem.getPreisstufe())))
                + " = " + UtilsString.centToString(costsValue);
        costs.setText(text);
        if (currentItem.getTicket() instanceof NumTicket) {
            int allTrips = currentItem.getQuantity() * ((NumTicket) currentItem.getTicket()).getNumTrips();
            int usedTrips = allTrips - currentItem.getFreeTrips();
            text = "(" + usedTrips + " / " + allTrips + ")";
            freeTrips.setText(text);
        }
        if (currentItem.getShowDetails()) {
            recyclerView.setVisibility(View.VISIBLE);
            ArrayList<TripItem> tripItems = currentItem.getTripItems();
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
            TripAdapterTicketList adapter = new TripAdapterTicketList(tripItems, activity);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            showDetails.setTypicon(Typicon.TY_ARROW_UP);
            setAdapterOnClickListener(adapter,tripItems);
        } else {
            recyclerView.setVisibility(View.GONE);
            showDetails.setTypicon(Typicon.TY_ARROW_DOWN);
        }
    }

    /**
     * Bei dem Klick auf das Element an der jeweiligen Position, wird die Detailansicht geöffnet
     *
     * @param currentItem Item, dessen Detailansicht geöffnet werden soll
     */
    private void onItemClick(TripItem currentItem) {
        Intent intent = new Intent(activity, AllConnectionsIncompleteView.class);
        intent.putExtra(MainMenu.EXTRA_TRIP, currentItem);
        intent.putExtra(MainMenu.EXTRA_CLASS, activity.getClass());
        activity.startActivity(intent);
    }

    /**
     * OnClickListener für die einzelnen Buttons <br/>
     *
     * In dieser Ansicht kann der Nutzer die Fahrt nicht kopieren, editieren oder löschen <br/>
     * @param adapter für den RecyclerView mit den Fahrten
     * @param tripItems zugeordnete Fahrten
     */
    private void setAdapterOnClickListener(TripAdapterTicketList adapter, ArrayList<TripItem> tripItems){
        //Keine Buttons die geklickt werden können -> keine Funktionen die aufgerufen werden müssen
        adapter.setOnItemClickListener(new com.example.lkjhgf.recyclerView.futureTrips.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TicketViewHolder.this.onItemClick(tripItems.get(position));
            }

            @Override
            public void onDeleteClicked(int position) {
            }

            @Override
            public void onCopyClicked(int position) {
            }

            @Override
            public void onEditClicked(int position) {
            }
        });
    }
}
