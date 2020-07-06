package com.example.lkjhgf.activities.ticketOverview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;

public class TicketOverview extends Activity {

    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.ticketsubmenu);

        BootstrapButton groupedTickets = this.findViewById(R.id.BootstrapButton39);
        groupedTickets.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        groupedTickets.setOnClickListener(v -> {
                Intent intent = new Intent(this, TicketOverviewGroupedTickets.class);
                startActivity(intent);
        });

        BootstrapButton allTicketsList = this.findViewById(R.id.BootstrapButton38);
        allTicketsList.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        allTicketsList.setOnClickListener(v ->{
            Intent intent = new Intent(this, AllTicketList.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
