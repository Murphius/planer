package com.example.lkjhgf.recyclerView.tickets;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketViewHolder> {

    private Activity activity;

    private ArrayList<TicketItem> ticketItems;
    private OnItemClickListener onItemClickListener;

    public TicketAdapter (Activity activity, ArrayList<TicketItem> ticketItems){
        this.activity = activity;
        this.ticketItems = ticketItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    @Override
    @NonNull
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_view, parent, false);
        return new TicketViewHolder(activity, view, onItemClickListener);
    }

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
