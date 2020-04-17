package com.example.lkjhgf.recyclerView.possibleConnections;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;

import java.util.ArrayList;

/**
 * Adapter für die Anzeige der wesentlichen Informationen einer Verbindung <br/>
 *
 * Verbindung zwischen {@link ConnectionItem} und {@link ConnectionViewHolder}
 */
public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionViewHolder> {

    /**
     * Liste mit allen passenden Verbindungen zur Nutzeranfrage
     */
    private ArrayList<ConnectionItem> connectionList;
    private OnItemClickListener onItemClickListener;

    public ConnectionAdapter(ArrayList<ConnectionItem> ConnectionList){
        connectionList = ConnectionList;
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
    public ConnectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connections, parent, false);
        return new ConnectionViewHolder(view, onItemClickListener);
    }

    /**
     * Füllt das Layout mit den Informationen des Elements an der entsprechenden Position <br/>
     * <p>
     * Füllen der Ansicht erfolgt in {@link ConnectionViewHolder#fillView(ConnectionItem)}
     *
     * @param holder   gibt an, welches Layout gefüllt werden soll
     * @param position gibt an, welches Element genutzt werden soll, um die Ansicht zu füllen
     */
    @Override
    public void onBindViewHolder(ConnectionViewHolder holder, int position) {
        ConnectionItem connectionItem = connectionList.get(position);

        holder.fillView(connectionItem);
    }

    @Override
    public int getItemCount() {
        return connectionList.size();
    }

}