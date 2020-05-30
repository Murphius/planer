package com.example.lkjhgf.publicTransport.query;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.optimisation.Optimisation;
import com.example.lkjhgf.optimisation.OptimisationSplit;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.Trip;
import de.schildbach.pte.dto.TripOptions;

public class SplitTripTask extends AsyncTask<QueryParameter, Void, ArrayList<Trip>> {

    private NetworkProvider provider;

    public SplitTripTask(){
        provider = MainMenu.myProvider.getNetworkProvider();
    }

    @Override
    protected ArrayList<Trip> doInBackground(QueryParameter... queryParameters) {
        ArrayList<QueryTripsResult> zwischenergebnis = new ArrayList<>();
        for(QueryParameter queryParameter : queryParameters){
            QueryTripsResult queryTripsResult = null;
            try {
                queryTripsResult = queryTrips(queryParameter.getFrom(),
                        queryParameter.getVia(),
                        queryParameter.getTo(),
                        queryParameter.getDate(),
                        queryParameter.isDeparture(),
                        queryParameter.getOptions());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(queryTripsResult !=  null){
                zwischenergebnis.add(queryTripsResult);
            }
        }
        ArrayList<Trip> result = new ArrayList<>();
        for(int i = 0; i < queryParameters.length; i++){
            if(zwischenergebnis.get(i).from.equals(queryParameters[i].getFrom())){
                for(Trip newQueryTrip : zwischenergebnis.get(i).trips){
                    if(newQueryTrip.getFirstDepartureTime().equals(queryParameters[i].getDate())){
                        result.add(newQueryTrip);
                        break;
                    }
                }
            }
        }
        return result;
    }


    /**
     * Anfrage an den Provider mit den Eingaben des Nutzers
     *
     * @return Mögliche Verbindungen, die zu den Parametern passen
     */
    private QueryTripsResult queryTrips(final Location from,
                                        final @Nullable Location via,
                                        final Location to,
                                        final Date date,
                                        final boolean dep,
                                        final @Nullable TripOptions options) throws IOException {
        return provider.queryTrips(from, via, to, date, dep, options);
    }

    @Override
    protected void onPostExecute(ArrayList<Trip> trips) {
        super.onPostExecute(trips);
        OptimisationSplit.blah(trips);
    }
}
