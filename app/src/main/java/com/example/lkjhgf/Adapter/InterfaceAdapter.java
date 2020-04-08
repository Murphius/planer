package com.example.lkjhgf.Adapter;

import com.example.lkjhgf.futureTrips.recyclerView.TripItem;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.schildbach.pte.dto.Line;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Point;
import de.schildbach.pte.dto.Stop;
import de.schildbach.pte.dto.Trip;

public class InterfaceAdapter implements JsonSerializer, JsonDeserializer {

    private static String CLASSNAME = "CLASSNAME";
    private static String DATA = "DATA";

    //Public Transport
    private static String EXTRA_LINE = "com.example.lkjhgf.EXTRA_LINE";
    private static String EXTRA_DESTINATION = "com.example.lkjhgf.EXTRA_DESTINATION";
    private static String EXTRA_DEPARTURE_STOP = "com.example.lkjhgf.EXTRA_DEPARTURE_STOP";
    private static String EXTRA_ARRIVAL_STOP = "com.example.lkjhgf.EXTRA_ARRIVAL_STOP";
    private static String EXTRA_INTERMEDIATE_STOPS = "com.example.lkjhgf.EXTRA_INTERMEDIATE_STOPS";
    private static String EXTRA_PATH = "com.example.lkjhgf.EXTRA_PATH";
    private static String EXTRA_MESSAGE = "com.example.lkjhgf.EXTRA_MESSAGE";

    //Individual
    private static final String EXTRA_TYPE = "com.example.lkjhgf.EXTRA_TYPE";
    private static final String EXTRA_DEPARTURE = "com.example.lkjhgf.EXTRA_DEPARTURE";
    private static final String EXTRA_DEPARTURE_TIME = "com.example.lkjhgf.EXTRA_DEPARTURE_TIME";
    private static final String EXTRA_ARRIVAL = "com.example.lkjhgf.EXTRA_ARRIVAL";
    private static final String EXTRA_ARRIVAL_TIME = "com.example.lkjhgf.EXTRA_ARRIVAL_TIME";
    private static final String EXTRA_DISTANCE = "com.example.lkjhgf.EXTRA_DISTANCE";

    /****** Helper method to get the className of the object to be deserialized *****/
    public Class getObjectClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
    }

    @Override
    public Object deserialize(JsonElement json,
                              java.lang.reflect.Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String className = jsonObject.get(CLASSNAME).getAsString();

        if (className.equals(Trip.Public.class.getName())) {
            Line line = context.deserialize(jsonObject.get(EXTRA_LINE), Line.class);
            Location destination = context.deserialize(jsonObject.get(EXTRA_DESTINATION), Location.class);
            Stop departureStop = context.deserialize(jsonObject.get(EXTRA_DEPARTURE_STOP), Stop.class);
            Stop arrivalStop = context.deserialize(jsonObject.get(EXTRA_ARRIVAL_STOP), Stop.class);
            Type intermediateStopType =  new TypeToken<List<Stop>>() {}.getType();
            List<Stop> intermediateStops = context.deserialize(jsonObject.get(EXTRA_INTERMEDIATE_STOPS), intermediateStopType);
            Type listType = new TypeToken<List<Point>>() {}.getType();
            List<Point> path = context.deserialize(jsonObject.get(EXTRA_PATH), listType);
            String message = context.deserialize(jsonObject.get(EXTRA_MESSAGE), String.class);
            return new Trip.Public(line, destination, departureStop, arrivalStop, intermediateStops, path, message);
        }else if (className.equals(Trip.Individual.class.getName())){
            Trip.Individual.Type type = context.deserialize(jsonObject.get(EXTRA_TYPE),
                    Trip.Individual.Type.class);
            Location departure = context.deserialize(jsonObject.get(EXTRA_DEPARTURE),
                    Location.class);
            Date departureTime = context.deserialize(jsonObject.get(EXTRA_DEPARTURE_TIME),
                    Date.class);
            Location arrival = context.deserialize(jsonObject.get(EXTRA_ARRIVAL), Location.class);
            Date arrivalTime = context.deserialize(jsonObject.get(EXTRA_ARRIVAL_TIME), Date.class);
            Type listType = new TypeToken<List<Point>>() {}.getType();
            List<Point> path = context.deserialize(jsonObject.get(EXTRA_PATH), listType);
            int distance = context.deserialize(jsonObject.get(EXTRA_DISTANCE), Integer.class);
            return new Trip.Individual(type,
                    departure,
                    departureTime,
                    arrival,
                    arrivalTime,
                    path,
                    distance);
        }
        return null;
    }

    @Override
    public JsonElement serialize(Object src,
                                 java.lang.reflect.Type typeOfSrc,
                                 JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASSNAME, src.getClass().getName());
        if(src instanceof Trip.Leg){
            if (src instanceof Trip.Public) {
                Trip.Public publicTrip = (Trip.Public) src;
                jsonObject.add(EXTRA_LINE, context.serialize(publicTrip.line));
                jsonObject.add(EXTRA_DESTINATION, context.serialize(publicTrip.destination));
                jsonObject.add(EXTRA_DEPARTURE_STOP, context.serialize(publicTrip.departureStop));
                jsonObject.add(EXTRA_ARRIVAL_STOP, context.serialize(publicTrip.arrivalStop));
                jsonObject.add(EXTRA_INTERMEDIATE_STOPS,
                        context.serialize(publicTrip.intermediateStops));
                jsonObject.add(EXTRA_PATH, context.serialize(publicTrip.path));
                jsonObject.add(EXTRA_MESSAGE, context.serialize(publicTrip.message));
            }else if (src instanceof Trip.Individual){
                Trip.Individual individualTrip = (Trip.Individual) src;
                jsonObject.add(EXTRA_TYPE,context.serialize(individualTrip.type));
                jsonObject.add(EXTRA_DEPARTURE,context.serialize(individualTrip.departure));
                jsonObject.add(EXTRA_DEPARTURE_TIME, context.serialize(individualTrip.departureTime));
                jsonObject.add(EXTRA_ARRIVAL,context.serialize(individualTrip.arrival));
                jsonObject.add(EXTRA_ARRIVAL_TIME, context.serialize(individualTrip.arrivalTime));
                jsonObject.add(EXTRA_PATH, context.serialize(individualTrip.path));
                jsonObject.add(EXTRA_DISTANCE, context.serialize(individualTrip.distance));
        }
        }else{
            JsonElement element = context.serialize(src);
            jsonObject.add(DATA, element);
        }
        return jsonObject;
    }
}
