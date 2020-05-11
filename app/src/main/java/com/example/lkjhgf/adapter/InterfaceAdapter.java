package com.example.lkjhgf.adapter;

import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import de.schildbach.pte.dto.Fare;
import de.schildbach.pte.dto.Line;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Point;
import de.schildbach.pte.dto.Stop;
import de.schildbach.pte.dto.Trip;

/**
 * Zur Speicherung von geplanten Fahrten, müssen die {@link Trip.Leg} manuell serialisiert und
 * deserialisert werden. Dies erfolgt in dieser Klasse. <br/>
 * <p>
 * Die statischen Attribute werden für das serialisieren / deserialisieren genutzt, zur identifikation. <br/>
 * <p>
 * Wichtig ist es, die abstrakte Klasse, sowie die erbenden Klassen hinzuzufügen. <br/>
 * <p>
 * Bei der Serialisierung ist die Klasse bekannt, beim Deserialisieren hingegen nicht, deshalb muss
 * der Klassenname ebenfalls gespeichert werden, um das Objekt korrekt zu "rekonstruieren"
 */
public class InterfaceAdapter implements JsonSerializer, JsonDeserializer {

    private static String CLASSNAME = "CLASSNAME";
    private static String DATA = "DATA";

    //Leg
    private static String EXTRA_PATH = "com.example.lkjhgf.EXTRA_PATH";

    //Public Transport
    private static String EXTRA_LINE = "com.example.lkjhgf.EXTRA_LINE";
    private static String EXTRA_DESTINATION = "com.example.lkjhgf.EXTRA_DESTINATION";
    private static String EXTRA_DEPARTURE_STOP = "com.example.lkjhgf.EXTRA_DEPARTURE_STOP";
    private static String EXTRA_ARRIVAL_STOP = "com.example.lkjhgf.EXTRA_ARRIVAL_STOP";
    private static String EXTRA_INTERMEDIATE_STOPS = "com.example.lkjhgf.EXTRA_INTERMEDIATE_STOPS";
    private static String EXTRA_MESSAGE = "com.example.lkjhgf.EXTRA_MESSAGE";

    //Individual
    private static final String EXTRA_TYPE = "com.example.lkjhgf.EXTRA_TYPE";
    private static final String EXTRA_DEPARTURE = "com.example.lkjhgf.EXTRA_DEPARTURE";
    private static final String EXTRA_DEPARTURE_TIME = "com.example.lkjhgf.EXTRA_DEPARTURE_TIME";
    private static final String EXTRA_ARRIVAL = "com.example.lkjhgf.EXTRA_ARRIVAL";
    private static final String EXTRA_ARRIVAL_TIME = "com.example.lkjhgf.EXTRA_ARRIVAL_TIME";
    private static final String EXTRA_DISTANCE = "com.example.lkjhgf.EXTRA_DISTANCE";

    //Ticket
    private static final String EXTRA_NAME_TICKET = "com.example.lkjhgf.EXTRA_NAME_TICKET";
    private static final String EXTRA_ARRAY_PRICE = "com.example.lkjhgf.EXTRA_ARRAY_PRICE";
    private static final String EXTRA_FARE_TYPE = "com.example.lkjhgf.EXTRA_FARE_TYPE";
    
    //NumTicket
    private static final String EXTRA_INT_NUM_TRIPS = "com.example.lkjhgf.EXTRA_INT_NUM_TRIPS";


    /**
     * Deserialisierung eines JsonElements
     *
     * @param json    Element, dass deserialisiert werden soll
     * @param context zum deserialisieren benötigt
     * @return Abhängig von der Klasse des jsonElements, entweder ein rekonstruiertes {@link Trip.Public}
     * oder {@link Trip.Individual} Objekt.
     * @throws JsonParseException Fehler beim Parsen
     */
    @Override
    public Object deserialize(JsonElement json,
                              java.lang.reflect.Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String className = jsonObject.get(CLASSNAME).getAsString();

        if (className.equals(Trip.Public.class.getName())) { // Erzeugen eines neuen Trip.Public Objekts
            Line line = context.deserialize(jsonObject.get(EXTRA_LINE), Line.class);
            Location destination = context.deserialize(jsonObject.get(EXTRA_DESTINATION),
                    Location.class);
            Stop departureStop = context.deserialize(jsonObject.get(EXTRA_DEPARTURE_STOP),
                    Stop.class);
            Stop arrivalStop = context.deserialize(jsonObject.get(EXTRA_ARRIVAL_STOP), Stop.class);
            // Deserialisierung der Zwischenhalte-Liste
            Type intermediateStopType = new TypeToken<List<Stop>>() {
            }.getType();
            List<Stop> intermediateStops = context.deserialize(jsonObject.get(
                    EXTRA_INTERMEDIATE_STOPS), intermediateStopType);
            // Deserialisierung der GPS-Koordinaten-Liste
            Type listType = new TypeToken<List<Point>>() {
            }.getType();
            List<Point> path = context.deserialize(jsonObject.get(EXTRA_PATH), listType);

            String message = context.deserialize(jsonObject.get(EXTRA_MESSAGE), String.class);
            return new Trip.Public(line,
                    destination,
                    departureStop,
                    arrivalStop,
                    intermediateStops,
                    path,
                    message);
        } else if (className.equals(Trip.Individual.class.getName())) { // Erzeugen eines neuen Trip.Individual Objekts
            Trip.Individual.Type type = context.deserialize(jsonObject.get(EXTRA_TYPE),
                    Trip.Individual.Type.class);
            Location departure = context.deserialize(jsonObject.get(EXTRA_DEPARTURE),
                    Location.class);
            Date departureTime = context.deserialize(jsonObject.get(EXTRA_DEPARTURE_TIME),
                    Date.class);
            Location arrival = context.deserialize(jsonObject.get(EXTRA_ARRIVAL), Location.class);
            Date arrivalTime = context.deserialize(jsonObject.get(EXTRA_ARRIVAL_TIME), Date.class);
            //Deserialisierung der GPS-Koordinaten-Liste
            Type listType = new TypeToken<List<Point>>() {
            }.getType();
            List<Point> path = context.deserialize(jsonObject.get(EXTRA_PATH), listType);

            int distance = context.deserialize(jsonObject.get(EXTRA_DISTANCE), Integer.class);
            return new Trip.Individual(type,
                    departure,
                    departureTime,
                    arrival,
                    arrivalTime,
                    path,
                    distance);
        }else if(className.equals(NumTicket.class.getName())){ //Erzeugen eines neuen NumTickets
            String name = context.deserialize(jsonObject.get(EXTRA_NAME_TICKET), String.class);
            int numTrips = context.deserialize(jsonObject.get(EXTRA_INT_NUM_TRIPS), Integer.class);
            int[] prices = context.deserialize(jsonObject.getAsJsonArray(EXTRA_ARRAY_PRICE), int[].class);
            Fare.Type type = context.deserialize(jsonObject.get(EXTRA_FARE_TYPE), Fare.Type.class);
            return new NumTicket(numTrips, prices, name,type);
        }
        return null;
    }

    /**
     * Serialisierung des Objekts, mit manueller serialisierung für die Klassen Trip.Public,
     * Trip.Individual und Ticket <br/>
     * <p>
     * Serialisiert die einzelnen Attribute der Klassen mit "Speicheradressen"
     *
     * @param src      Objekt das serialisiert wird
     * @param context, zum Serialisieren benötigt
     * @return das Objekt serialisiert als JsonObject
     */
    @Override
    public JsonElement serialize(Object src,
                                 java.lang.reflect.Type typeOfSrc,
                                 JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        //Klassenname serialisieren
        jsonObject.addProperty(CLASSNAME, src.getClass().getName());
        if (src instanceof Trip.Leg) {
            if (src instanceof Trip.Public) { // Serialisierung von Trip.Public
                Trip.Public publicTrip = (Trip.Public) src;
                jsonObject.add(EXTRA_LINE, context.serialize(publicTrip.line));
                jsonObject.add(EXTRA_DESTINATION, context.serialize(publicTrip.destination));
                jsonObject.add(EXTRA_DEPARTURE_STOP, context.serialize(publicTrip.departureStop));
                jsonObject.add(EXTRA_ARRIVAL_STOP, context.serialize(publicTrip.arrivalStop));
                jsonObject.add(EXTRA_INTERMEDIATE_STOPS,
                        context.serialize(publicTrip.intermediateStops));
                jsonObject.add(EXTRA_PATH, context.serialize(publicTrip.path));
                jsonObject.add(EXTRA_MESSAGE, context.serialize(publicTrip.message));
            } else if (src instanceof Trip.Individual) { // Serialisierung von Trip.Individual
                Trip.Individual individualTrip = (Trip.Individual) src;
                jsonObject.add(EXTRA_TYPE, context.serialize(individualTrip.type));
                jsonObject.add(EXTRA_DEPARTURE, context.serialize(individualTrip.departure));
                jsonObject.add(EXTRA_DEPARTURE_TIME,
                        context.serialize(individualTrip.departureTime));
                jsonObject.add(EXTRA_ARRIVAL, context.serialize(individualTrip.arrival));
                jsonObject.add(EXTRA_ARRIVAL_TIME, context.serialize(individualTrip.arrivalTime));
                jsonObject.add(EXTRA_PATH, context.serialize(individualTrip.path));
                jsonObject.add(EXTRA_DISTANCE, context.serialize(individualTrip.distance));
            }
        }else if(src instanceof Ticket){
            Ticket ticket = (Ticket) src;
            jsonObject.add(EXTRA_FARE_TYPE, context.serialize(ticket.getType()));
            jsonObject.add(EXTRA_NAME_TICKET, context.serialize(ticket.getName()));
            jsonObject.add(EXTRA_ARRAY_PRICE, context.serialize(ticket.getPrices()));
            if(src instanceof NumTicket){
                NumTicket numTicket = (NumTicket) src;
                jsonObject.add(EXTRA_INT_NUM_TRIPS, context.serialize(numTicket.getNumTrips()));
            }
            //TODO Zeitticket
        }else {
            JsonElement element = context.serialize(src);
            jsonObject.add(DATA, element);
        }
        return jsonObject;
    }
}
