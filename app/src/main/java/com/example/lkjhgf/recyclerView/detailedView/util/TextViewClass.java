package com.example.lkjhgf.recyclerView.detailedView.util;

import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;

class TextViewClass {
    TextView time_of_departure_view, start_view, start_platform_view;
    TextView number_view, destination_of_number_view;
    TextView time_of_arrival_view, destination_view, destination_platform_view;
    TextView delay_departure, delay_arrival;
    TextView ticketView, useTicket;

    TextViewClass(View view) {
        time_of_departure_view = view.findViewById(R.id.textView5);
        start_view = view.findViewById(R.id.textView57);
        start_platform_view = view.findViewById(R.id.textView58);
        number_view = view.findViewById(R.id.textView61);
        destination_of_number_view = view.findViewById(R.id.textView62);
        time_of_arrival_view = view.findViewById(R.id.textView60);
        destination_view = view.findViewById(R.id.textView63);
        destination_platform_view = view.findViewById(R.id.textView59);

        delay_departure = view.findViewById(R.id.textView68);
        delay_arrival = view.findViewById(R.id.textView69);

        ticketView = view.findViewById(R.id.textView89);
        useTicket = view.findViewById(R.id.textView90);
    }
}
