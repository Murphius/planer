package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpAdapter;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPublicItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpItem;

import java.util.ArrayList;

public class CloseUpRecyclerView {

    private CloseUpAdapter adapter;

    private ArrayList<CloseUpItem> items;

    CloseUpRecyclerView(Activity activity, View view, CloseUp closeUp) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView3);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());

        items = Utils.fillDetailedConnectonList(closeUp.trip.legs);

        adapter = new CloseUpAdapter(items, activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            if (items.get(position) instanceof CloseUpPublicItem) {
                items.get(position).setShowDetails();
                adapter.notifyDataSetChanged();
            } else {
                items.get(position).setShowDetails();
                adapter.notifyDataSetChanged();
                activity.recreate();
            }


        });
        recyclerView.setFocusable(false);
    }
}
