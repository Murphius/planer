package com.example.lkjhgf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Connection_item {
    private String time_of_arrival, time_of_departure, preisstufe, id;
    private ArrayList<Journey_item> journey_items;
    private int num_changes;
    private long duration_hours, duration_minutes;


    public Connection_item(Date Time_of_departure, Date Time_of_arrival, int Num_changes, long d, String Preisstufe, String ID, ArrayList<Journey_item> Journey_items){
        DateFormat dateFormat = new SimpleDateFormat("HH : mm");
        time_of_arrival = dateFormat.format(Time_of_arrival);
        time_of_departure = dateFormat.format(Time_of_departure);
        //TODO Dauer ergibt sich aus Ankunftszeit-Startzeit
        duration_hours = TimeUnit.HOURS.convert(d, TimeUnit.MILLISECONDS) % 24;
        duration_minutes = TimeUnit.MINUTES.convert(d, TimeUnit.MILLISECONDS) % 60;
        journey_items = Journey_items;
        num_changes = Num_changes;
        preisstufe = Preisstufe;
        id = ID;
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
    public String get_duration_string(){
        String result = "";
        if(duration_hours >= 1){
            result += duration_hours + "h ";
        }
        if(duration_minutes < 1){
            result += "00";
        }else if(duration_minutes < 10){
            result += "0" + duration_minutes;
        }else{
            result += duration_minutes;
        }

        return result + "min";
    }
    public int get_num_changes(){
        return num_changes;
    }
    public String get_id(){ return id;}
    public ArrayList<Journey_item> get_journey_items(){
        return journey_items;
    }
    public long getDuration(){
        return duration_hours*60+duration_minutes;
    }
}
