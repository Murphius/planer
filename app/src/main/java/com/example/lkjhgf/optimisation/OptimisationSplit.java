package com.example.lkjhgf.optimisation;

import android.app.Activity;

import com.example.lkjhgf.activities.Settings;
import com.example.lkjhgf.publicTransport.query.QueryParameter;
import com.example.lkjhgf.publicTransport.query.SplitTripTask;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Trip;
import de.schildbach.pte.dto.TripOptions;

public class OptimisationSplit {

    public static void createTripPublicLegList(Activity activity, TripItem tripItem){
        ArrayList<TripItem> publicLegList = new ArrayList<>();
        ArrayList<QueryParameter> queryParameters = new ArrayList<>();
        Trip trip = tripItem.getTrip();
        for(Trip.Leg leg : trip.legs){
            if(leg instanceof Trip.Public){
                Trip.Public current = (Trip.Public) leg;
                TripOptions tripOptions = Settings.getTripOptions(activity);
                Location startLocation = current.departure;
                Location destinationLocation = current.destination;
                Date date = current.getDepartureTime();
                QueryParameter queryParameter = new QueryParameter(startLocation, null, destinationLocation, date, false, tripOptions);
                queryParameters.add(queryParameter);
            }
        }
        SplitTripTask t = new SplitTripTask();
        t.execute((Runnable) queryParameters);
        try {
            t.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Trip> blah(ArrayList<Trip> trips){
        return trips;
    }
}
