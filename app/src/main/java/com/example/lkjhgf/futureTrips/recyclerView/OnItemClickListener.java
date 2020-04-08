package com.example.lkjhgf.futureTrips.recyclerView;

public interface OnItemClickListener {
    void onItemClick(int position);
    void onDeleteClicked(int position);
    void onCopyClicked(int position);
    void onEditClicked(int position);
}
