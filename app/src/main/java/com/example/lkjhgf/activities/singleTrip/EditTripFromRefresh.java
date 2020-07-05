package com.example.lkjhgf.activities.singleTrip;

import android.content.Intent;

import com.example.lkjhgf.activities.MainMenu;

public class EditTripFromRefresh extends EditTripFromComplete {

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finishAffinity();
    }
}
