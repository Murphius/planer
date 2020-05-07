package com.example.lkjhgf.activities.ticketOverview;

import android.app.Activity;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.ticketOverview.AllTickets;

public class TicketOverview extends Activity {

    AllTickets myAllTickets;
    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);

        setContentView(R.layout.ticket_generalview);

        ConstraintLayout layout = findViewById(R.id.constraintLayout4);

        myAllTickets = new AllTickets(this, layout);
    }

    @Override
    public void onBackPressed(){
        myAllTickets.saveData();
        super.onBackPressed();
    }
}
