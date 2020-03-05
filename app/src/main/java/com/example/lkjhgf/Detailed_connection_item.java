package com.example.lkjhgf;

public class Detailed_connection_item {

    private String time_of_departure, time_of_arrival, start, destination, start_platform, destination_platform, number, destination_of_number;
    private int image_resource;

    public Detailed_connection_item(String time_of_departure, String time_of_arrival, String start, String destination, String start_platform, String destination_platform, String number, String destination_of_number, int image_resource){
        this.destination = destination;
        this.destination_of_number = destination_of_number;
        this.destination_platform = destination_platform;
        this.image_resource = image_resource;
        this.number = number;
        this.start = start;
        this.start_platform = start_platform;
        this.time_of_arrival = time_of_arrival;
        this.time_of_departure = time_of_departure;
    }

    public String getStart() {
        return start;
    }

    public String getDestination() {
        return destination;
    }

    public int getImage_resource() {
        return image_resource;
    }

    public String getDestination_of_number() {
        return destination_of_number;
    }

    public String getDestination_platform() {
        return destination_platform;
    }

    public String getNumber() {
        return number;
    }

    public String getStart_platform() {
        return start_platform;
    }

    public String getTime_of_arrival() {
        return time_of_arrival;
    }

    public String getTime_of_departure() {
        return time_of_departure;
    }

}
