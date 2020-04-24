package com.example.lkjhgf.activities.singleTrip;

import android.app.Activity;
import android.os.Bundle;

import com.example.lkjhgf.helper.closeUp.SingleCloseUp;
import com.example.lkjhgf.R;

public class ThirdView_CloseUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);

        new SingleCloseUp(this, findViewById(R.id.constraintLayout2));
    }





}
