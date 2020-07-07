package com.example.lkjhgf.recyclerView.tickets.groupedTicketView;

import com.example.lkjhgf.helper.ticketOverview.groupedOverview.RecyclerViewGroupedTicketOverview;

/**
 * Enthält die Funktionen, die ausgeführt werden sollen, für klicks <br/>
 * Implementierung in {@link RecyclerViewGroupedTicketOverview#buildRecyclerView()}
 */
public interface OnItemClickListener {
    void showDetails(int position);
}
