package com.example.lkjhgf.recyclerView.detailedView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.detailedView.util.DetailedConnectionViewHolder;

import java.util.ArrayList;

/**
 * Adapter für die detaillierte Ansicht einer einzelnen Fahrt <br/>
 * Verbindung zwischen Ansicht und CloseUpItem (Trip)
 */
public class CloseUpAdapter extends RecyclerView.Adapter<DetailedConnectionViewHolder> {

    /**
     * Liste aller Verbindungsabschnitte
     */
    private ArrayList<CloseUpItem> closeUp_items;
    private OnItemClickListener listener;
    private Activity activity;

    public CloseUpAdapter(ArrayList<CloseUpItem> items, Activity activity) {
        closeUp_items = items;
        this.activity = activity;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Erzeugt neuen View
     *
     * @return neue Ansicht
     */
    @NonNull
    @Override
    public DetailedConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_journey,
                parent,
                false);

        return new DetailedConnectionViewHolder(activity,
                view, listener);
    }

    /**
     * Ein einzelnes Layoutfragment wird mit den jeweiligen Informationen gefüllt
     *
     * @param holder   Layout, welches gefüllt werden soll
     * @param position gibt die Position des Items an, dessen Informationen verwendet
     *                 werden sollen, um das Layout zu füllen
     *                 Das füllen der Ansicht erfolgt in der Klasse
     *                 {@link DetailedConnectionViewHolder#closeUpItemSetUpView(CloseUpItem)}
     *                 abhängig von der Art des Abschnitts, wird eine andere Ansicht erzeugt
     *                 ({@link DetailedConnectionViewHolder#publicItemSetUpView(CloseUpPublicItem)}
     *                 oder
     *                 {@link DetailedConnectionViewHolder#privateItemSetUpView(CloseUpPrivateItem)}.
     *                 <br/>
     *                 <p>
     *  Verbindung zwischen Ansicht und Daten
     */
    @Override
    public void onBindViewHolder(DetailedConnectionViewHolder holder, int position) {
        CloseUpItem current = closeUp_items.get(position);

        holder.closeUpItemSetUpView(current);

        if (current.getTicket() == null) {
            //TODO
            //holder.useTicket.setVisibility(View.GONE);
            //holder.ticketView.setVisibility(View.GONE);
        } else {
            //TODO
            //holder.useTicket.setText(current.getTicket.toString());
        }

        if (current instanceof CloseUpPublicItem) {
            holder.publicItemSetUpView((CloseUpPublicItem) current);
        } else {// instance of IndividualItem
            holder.privateItemSetUpView((CloseUpPrivateItem) current);
        }
    }

    @Override
    public int getItemCount() {
        return closeUp_items.size();
    }

}
