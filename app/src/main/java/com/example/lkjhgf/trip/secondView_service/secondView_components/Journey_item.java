package com.example.lkjhgf.trip.secondView_service.secondView_components;

import java.io.Serializable;

public class Journey_item implements Serializable {
    private int image_resource;
    String time_line;

    public Journey_item(int image_resource, String Time_line){
        this.image_resource = image_resource;
        this.time_line = Time_line;
    }

    public int get_image_resource(){
        return image_resource;
    }
    public String getTimeOrLine(){
        return time_line;
    }
}
