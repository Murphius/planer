package com.example.lkjhgf.recyclerView.tickets;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;

import java.util.ArrayList;
/**
 * Adapter für die Anzeige der wesentlichen Informationen eines Tickets <br/>
 *
 * Verbindung zwischen {@link TicketItem} und {@link TicketViewHolder}
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketViewHolder> {

    private Activity activity;
    /**
     * Liste der anzuzeigenden Elemente
     */
    private ArrayList<TicketItem> ticketItems;
    private OnItemClickListener onItemClickListener;

    public TicketAdapter (Activity activity, ArrayList<TicketItem> ticketItems){
        this.activity = activity;
        this.ticketItems = ticketItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    /**
     * Erzeugt neue Ansicht
     *
     * @return Weiteres "Exemplar" der Ansicht
     */
    @Override
    @NonNull
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_view, parent, false);
        return new TicketViewHolder(activity, view, onItemClickListener);
    }

    /**
     * Füllt das Layout mit den Informationen des Elements an der entsprechenden Position <br/>
     * <p>
     * Füllen der Ansicht erfolgt in {@link TicketViewHolder#fillView(TicketItem)}
     *
     * @param holder   gibt an, welches Layout gefüllt werden soll
     * @param position gibt an, welches Element genutzt werden soll, um die Ansicht zu füllen
     */
    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position){
        TicketItem currentItem = ticketItems.get(position);
        holder.fillView(currentItem);
    }

    @Override
    public int getItemCount(){
        return ticketItems.size();
    }

}
