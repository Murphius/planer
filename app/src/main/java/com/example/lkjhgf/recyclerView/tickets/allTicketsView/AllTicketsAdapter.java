package com.example.lkjhgf.recyclerView.tickets.allTicketsView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.optimisation.TicketToBuy;

import java.util.ArrayList;

/**
 * Adapter für die Anzeige der Informationen aller Tickets <br/>
 * <p>
 * Verbindung zwischen {@link TicketToBuy} und {@link TicketViewHolder}
 */
public class AllTicketsAdapter extends RecyclerView.Adapter<TicketViewHolder> {

    private Activity activity;

    /**
     * Liste der anzuzeigenden Elemente
     */
    private ArrayList<TicketToBuy> ticketItems;

    public AllTicketsAdapter(Activity activity, ArrayList<TicketToBuy> ticketItems) {
        this.ticketItems = ticketItems;
        this.activity = activity;
    }

    /**
     * Erzeugt neue Ansicht
     *
     * @return Weitere Ansicht
     */
    @Override
    @NonNull
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_allticket_item, parent, false);
        return new TicketViewHolder(view);
    }

    /**
     * Füllt da Layout mit den Informationen des Elements an der entsprechenden Position <p>
     * <p>
     * Füllen der Ansicht erfolgt in
     *
     * @param holder   gibt an, welches Layout Objekt gefüllt werden soll
     * @param position gibt an, welche Element genutzt werden soll, um die Ansicht zu füllen
     */
    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position) {
        TicketToBuy current = ticketItems.get(position);
        holder.fillView(current);
    }

    @Override
    public int getItemCount() {
        return ticketItems.size();
    }
}
