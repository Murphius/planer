package com.example.lkjhgf.individual_trip.secondView_service.secondView_components;

public class Journey_item {
    private int image_resource;
    String time_line;

    public Journey_item(int image_resource, String Time_line){
        this.image_resource = image_resource;
        this.time_line = Time_line;
    }

    public int get_image_resource(){
        return image_resource;
    }
    public String get_time(){
        return time_line;
    }
}
