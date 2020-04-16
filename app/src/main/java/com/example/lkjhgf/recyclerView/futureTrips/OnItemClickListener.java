package com.example.lkjhgf.recyclerView.futureTrips;

import com.example.lkjhgf.helper.futureTrip.MyTrip;

/**
 * Interface mit den Methoden, die bei dem jeweiligen Click auf das Item beziehungsweise den Button
 * ausgeführt werden soll<br/>
 * <p>
 *  Implementierung in {@link MyTrip#recyclerViewBlah()}
 * </p>
 */
public interface OnItemClickListener {
    void onItemClick(int position);
    void onDeleteClicked(int position);
    void onCopyClicked(int position);
    void onEditClicked(int position);
}
