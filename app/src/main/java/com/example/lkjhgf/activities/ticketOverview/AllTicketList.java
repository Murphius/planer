package com.example.lkjhgf.activities.ticketOverview;

import android.app.Activity;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.ticketOverview.allTicketsOverview.RecyclerViewAllTicketsOverview;

public class AllTicketList extends Activity {

    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);

        setContentView(R.layout.ticket_allticket_list);

        ConstraintLayout layout = findViewById(R.id.constraintLayout5);

        new RecyclerViewAllTicketsOverview(this, layout);
    }

}
