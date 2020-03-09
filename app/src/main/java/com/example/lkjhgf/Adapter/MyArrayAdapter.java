package com.example.lkjhgf.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lkjhgf.R;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.SuggestLocationsResult;

public class MyArrayAdapter extends ArrayAdapter<MyArrayAdapter.LocationHolder>{

    private Context mContext;
    private List<MyArrayAdapter.LocationHolder> mList = new ArrayList<>();
    private ArrayFilter mFilter;
    private int threshhold;

    public MyArrayAdapter(@NonNull Context context, int threshhold){
        this(context, new ArrayList<>(), threshhold);
    }

    private MyArrayAdapter(@NonNull Context context, ArrayList<MyArrayAdapter.LocationHolder> list, int threshhold)
    {
        super(context, 0, list);
        mContext = context;
        mList = list;
        this.threshhold = threshhold;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }
        String currentString = mList.get(position).toString();
        TextView textView = listItem.findViewById(R.id.textView64);
        ImageView imageView = listItem.findViewById(R.id.imageView2);
        textView.setText(currentString);
        switch (mList.get(position).location.type){
            case POI: imageView.setImageResource(R.drawable.ic_point_of_interest);break;
            case ADDRESS: imageView.setImageResource(R.drawable.ic_address); break;
            case STATION: imageView.setImageResource(R.drawable.ic_architecture_and_city); break;
            default: imageView.setImageResource(R.drawable.ic_bookmark_black_24dp);
        }
        return  listItem;
    }

    @Override
    public @NonNull
    Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter(this);
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {

        private NetworkProvider provider;
        private ArrayAdapter<MyArrayAdapter.LocationHolder> adapter;

        public ArrayFilter(ArrayAdapter<MyArrayAdapter.LocationHolder> adapter)
        {
            this.provider = new VrrProvider();
            this.adapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            SuggestLocationsResult suggestedLocationsResult = null;
            try {
                suggestedLocationsResult = suggestLocations(prefix);
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<MyArrayAdapter.LocationHolder> suggestedLocations = new ArrayList<>();
            FilterResults results = new FilterResults();
            if (suggestedLocationsResult != null) {
                for (Location location :suggestedLocationsResult.getLocations())
                {
                    suggestedLocations.add(new LocationHolder(location));
                }
            }

            results.count = suggestedLocations.size();
            results.values = suggestedLocations;

            return results;
        }

        protected final SuggestLocationsResult suggestLocations(final CharSequence constraint) throws
                IOException {
            if (constraint == null)
                return null;
            if (constraint.length() < threshhold)
                return null;
            return provider.suggestLocations(constraint, null, 5);
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.clear();
            if (results.count > 0) {
                adapter.addAll((ArrayList<MyArrayAdapter.LocationHolder>) results.values);
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public class LocationHolder
    {
        public Location location;

        public LocationHolder(Location location)
        {
            this.location = location;
        }

        public String toString()
        {
            return location.place + " " + location.name;
        }
    }
}