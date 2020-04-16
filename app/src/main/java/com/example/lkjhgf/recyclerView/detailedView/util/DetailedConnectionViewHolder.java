package com.example.lkjhgf.recyclerView.detailedView.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.font.Typicon;
import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPrivateItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPublicItem;
import com.example.lkjhgf.recyclerView.detailedView.OnItemClickListener;
import com.example.lkjhgf.recyclerView.detailedView.components.Stopover_adapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Locale;

public class DetailedConnectionViewHolder extends RecyclerView.ViewHolder {

    private Activity activity;

    private TextViewClass textViewClass;
    private ButtonClass buttonClass;
    public ImageView icon;
    private View horizontalLine;

    public RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public DetailedConnectionViewHolder(Activity activity,
                                        View itemView,
                                        OnItemClickListener listener) {
        super(itemView);

        this.activity = activity;

        textViewClass = new TextViewClass(itemView);

        buttonClass = new ButtonClass(this, itemView, listener);

        recyclerView = itemView.findViewById(R.id.recyclerView4);
        recyclerView.setHasFixedSize(true);

        icon = itemView.findViewById(R.id.imageView);

        horizontalLine = itemView.findViewById(R.id.view4);

        layoutManager = new LinearLayoutManager(itemView.getContext());
    }

    public void publicItemSetUpView(CloseUpPublicItem closeUpPublicItem) {
        buttonClass.showOnMap.setVisibility(View.GONE);

        textViewClass.start_platform_view.setText(closeUpPublicItem.getDeparturePlatform());
        textViewClass.destination_platform_view.setText(closeUpPublicItem.getDestinationPlatform());

        textViewClass.destination_of_number_view.setText(closeUpPublicItem.getDestinationOfNumber());
        textViewClass.number_view.setText(closeUpPublicItem.getNumber());

        Utils.setDelayView(textViewClass.delay_departure,
                closeUpPublicItem.getDelayDeparture(),
                activity.getResources());
        Utils.setDelayView(textViewClass.delay_arrival,
                closeUpPublicItem.getDelayArrival(),
                activity.getResources());

        if (closeUpPublicItem.isShowDetails()) {
            recyclerView.setVisibility(View.VISIBLE);
            buttonClass.stopoverLocationsShow.setTypicon(Typicon.TY_ARROW_UP);
            horizontalLine.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            buttonClass.stopoverLocationsShow.setTypicon(Typicon.TY_ARROW_DOWN);
            horizontalLine.setVisibility(View.GONE);
        }

        if (closeUpPublicItem.getStopoverItems().isEmpty()) {
            buttonClass.stopoverLocationsShow.setVisibility(View.INVISIBLE);
        }
        adapter = new Stopover_adapter(closeUpPublicItem.getStopoverItems());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void privateItemSetUpView(CloseUpPrivateItem closeUpPrivateItem) {
        buttonClass.stopoverLocationsShow.setVisibility(View.INVISIBLE);
        textViewClass.destination_of_number_view.setVisibility(View.GONE);
        textViewClass.number_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        LatLng departure = closeUpPrivateItem.getDepartureLocation();
        LatLng destination = closeUpPrivateItem.getDestinationLocation();
        String uri = String.format(Locale.ENGLISH,
                "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                departure.latitude,
                departure.longitude,
                destination.latitude,
                destination.longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                activity.startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(activity.getApplicationContext(),
                        "Please install a maps application",
                        Toast.LENGTH_LONG).show();
            }
        }
        adapter = new Stopover_adapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void closeUpItemSetUpView(CloseUpItem item) {
        textViewClass.start_view.setText(item.getDeparture());
        textViewClass.destination_view.setText(item.getDestination());

        icon.setImageResource(item.getImage_resource());

        textViewClass.time_of_arrival_view.setText(item.getTime_of_arrival());
        textViewClass.time_of_departure_view.setText(item.getTime_of_departure());
    }

}