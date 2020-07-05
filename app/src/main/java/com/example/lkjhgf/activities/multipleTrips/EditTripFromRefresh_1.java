package com.example.lkjhgf.activities.multipleTrips;

import android.content.Intent;

import com.example.lkjhgf.activities.futureTrips.IncompleteAfterRefresh;

/**
 * Editieren einer Fahrt, die noch gar nicht gespeichert wurde
 */
public class EditTripFromRefresh_1 extends EditIncompleteTripFromIncompleteList{

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, IncompleteAfterRefresh.class);
        startActivity(intent);
    }

}
