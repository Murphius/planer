package com.example.lkjhgf;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Connection_item {
    private String time_of_arrival, time_of_departure, preisstufe;
    private ArrayList<Journey_item> journey_items;
    private int duration, num_changes;

    public Connection_item(String Time_of_arrival, String Time_of_departure, int Num_changes, String Preisstufe, ArrayList<Journey_item> Journey_items){
        time_of_arrival = Time_of_arrival;
        time_of_departure = Time_of_departure;
        //TODO Dauer ergibt sich aus Ankunftszeit-Startzeit
        duration = 0;
        journey_items = Journey_items;
        num_changes = Num_changes;
        preisstufe = Preisstufe;
    }

    public String get_time_of_arrival(){
        return time_of_arrival;
    }
    public String get_time_of_departure(){
        return time_of_departure;
    }
    public String get_preisstufe(){
        return preisstufe;
    }
    public int get_duration(){
        return duration;
    }
    public int get_num_changes(){
        return num_changes;
    }
    public ArrayList<Journey_item> get_journey_items(){
        return journey_items;
    }
}
