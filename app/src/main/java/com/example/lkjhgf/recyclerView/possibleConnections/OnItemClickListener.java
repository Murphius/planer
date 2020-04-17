package com.example.lkjhgf.recyclerView.possibleConnections;

/**
 * Interface mit der Methode, die ausgeführt werden soll, wenn auf das Item geklickt wird <br/>
 *
 * Implementierung in {@link RecyclerViewService#buildRecyclerView()}
 */
public interface OnItemClickListener {
    void onItemClick(int position);
}
