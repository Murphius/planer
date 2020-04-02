package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.trip.thirdView_DetailedView.CloseUp_adapter;
import com.example.lkjhgf.trip.thirdView_DetailedView.CloseUp_publicItem;
import com.example.lkjhgf.trip.thirdView_DetailedView.CloseUp_item;

import java.util.ArrayList;

class CloseUpRecyclerView {

    private CloseUp_adapter adapter;

    private ArrayList<CloseUp_item> items;

    CloseUpRecyclerView(Activity activity, View view, CloseUp closeUp) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView3);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());

        items = Utils.fillDetailedConnectonList(closeUp.trip.legs);

        adapter = new CloseUp_adapter(items, activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CloseUp_adapter.OnItemClickListener() {
            @Override
            public void onShowDetails(int position) {
                if (items.get(position) instanceof CloseUp_publicItem) {
                    System.out.println("Hier eigentlich nicht -------------------------------------------");
                    items.get(position).setShowDetails();
                    adapter.notifyDataSetChanged();
                } else {
                    items.get(position).setShowDetails();
                    adapter.notifyDataSetChanged();
                    activity.recreate();
                }


            }
        });
        recyclerView.setFocusable(false);
    }
}
