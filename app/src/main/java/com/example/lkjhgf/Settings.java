package com.example.lkjhgf;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;

public class Settings extends Activity {

    private static boolean regional_traffic_only = true;
    private static boolean long_distance_traffic = false;
    private static boolean regional_traffic, sbahn, ubahn, bus, ast, ferry, gondel = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



    }
}
