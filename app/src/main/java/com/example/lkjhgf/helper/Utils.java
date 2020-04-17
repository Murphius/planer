package com.example.lkjhgf.helper;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.possibleConnections.ConnectionItem;
import com.example.lkjhgf.recyclerView.possibleConnections.components.JourneyItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPrivateItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPublicItem;
import com.example.lkjhgf.recyclerView.detailedView.components.Stopover_item;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Product;
import de.schildbach.pte.dto.Stop;
import de.schildbach.pte.dto.Trip;

public final class Utils {

    public static String arrivalDepartureTime(boolean isArrivalTime) {
        if (isArrivalTime) {
            return "Ankunftszeit";
        } else {
            return "Abfahrtszeit:";
        }
    }

    public static void setArrivalDepartureButton(BootstrapButton button, boolean isArrivalTime) {
        Context context = button.getContext();
        BootstrapText.Builder builder = new BootstrapText.Builder(context);
        builder.addText(arrivalDepartureTime(isArrivalTime));
        button.setBootstrapText(builder.build());
    }

    public static String setLocationName(Location location) {
        if (location.name != null && location.place != null) {
            String[] splits = location.name.split(" ");
            StringBuilder placeName = new StringBuilder(location.place);
            for (String split : splits) {
                if (!location.place.contains(split)) {
                    placeName.append(" ").append(split);
                }
            }
            return placeName.toString();
        }
        if (location.place == null && location.name != null) {
            return location.name;
        }
        if (location.place != null) {
            return location.place + " ?";
        }
        return "?";
    }

    public static String setLocationName(String place, String name) {
        if (name.contains(place)) {
            return name;
        } else {
            return place + " " + name;
        }
    }

    public static String setDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd. MM. YYYY", Locale.GERMANY);
        return dateFormat.format(date);
    }

    public static String setTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("HH : mm", Locale.GERMANY);
        return dateFormat.format(date);
    }

    public static String durationString(long durationHours, long durationMinutes) {
        String result = "";
        if (durationHours >= 1) {
            result += durationHours + "h ";
        }
        if (durationMinutes < 1) {
            result += "00";
        } else if (durationMinutes < 10 & durationHours >= 1) {
            result += "0" + durationMinutes;
        } else if (durationMinutes < 10) {
            result += durationMinutes + "min";
        } else {
            result += durationMinutes + "min";
        }
        return result;
    }

    public static int longToMinutes(long l) {
        return (int) (l / (1000 * 60) % 60);
    }

    public static long durationToHour(long duration) {
        return TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS) % 24;
    }

    public static long durationToMinutes(long duration) {
        return TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS) % 60;
    }

    public static void setDelayView(TextView textView, int delay, Resources resources) {
        String text = "+ " + delay;
        textView.setText(text);
        if (delay == 0) {
            textView.setVisibility(View.INVISIBLE);
        } else if (delay <= 5) {
            textView.setTextColor(resources.getColor(R.color.my_green, null));
        } else {
            textView.setTextColor(resources.getColor(R.color.maroon, null));
        }
    }

    //TODO Verbindungsarten -> andere Farben
    // versuchen Umstiege anzuzeigen
    public static ArrayList<ConnectionItem> fillConnectionList(List<Trip> trips) {
        ArrayList<ConnectionItem> connection_items = new ArrayList<>();
        if (trips == null) {
            return connection_items;
        }
        for (Trip trip : trips) {
            ArrayList<JourneyItem> list_of_journey_elements = new ArrayList<>();
            if (trip.isTravelable()) {
                List<Trip.Leg> legs = trip.legs;
                list_of_journey_elements = journeyItems(legs);
            }
            connection_items.add(new ConnectionItem(trip, list_of_journey_elements));
        }
        return connection_items;
    }

    public static ArrayList<JourneyItem> journeyItems(List<Trip.Leg> legs) {
        ArrayList<JourneyItem> journeyItems = new ArrayList<>();
        for (Trip.Leg leg : legs) {
            if (leg instanceof Trip.Public) {
                journeyItems.add(publicItems((Trip.Public) leg));
            } else {
                journeyItems.add(individualItems((Trip.Individual) leg));
            }
        }
        return journeyItems;
    }

    public static ArrayList<CloseUpItem> fillDetailedConnectonList(List<Trip.Leg> legs) {
        ArrayList<CloseUpItem> items = new ArrayList<>();
        if (legs == null) {
            return items;
        }

        for (Trip.Leg leg : legs) {
            if (leg instanceof Trip.Public) {
                items.add(new CloseUpPublicItem((Trip.Public) leg));
            } else {
                items.add(new CloseUpPrivateItem((Trip.Individual) leg));
            }
        }


        return items;
    }

    private static JourneyItem publicItems(Trip.Public publicTrip) {
        String name = publicTrip.line.label;
        int icon = R.drawable.ic_android;
        Product product = publicTrip.line.product;
        if (product == null) {
            return new JourneyItem(icon, name);
        }
        icon = iconPublic(publicTrip.line.product);
        return new JourneyItem(icon, name);
    }

    private static JourneyItem individualItems(Trip.Individual individualTrip) {
        String time = individualTrip.min + " min";
        int icon = iconIndividual(individualTrip.type);
        return new JourneyItem(icon, time);
    }


    public static ArrayList<Stopover_item> createStopoverList(Trip.Public publicTrip) {
        ArrayList<Stopover_item> stopoverItems = new ArrayList<>();

        List<Stop> stops = publicTrip.intermediateStops;

        if (stops != null) {
            for (Stop stop : stops) {
                //TODO für den ersten Zwischenhalt prüfen
                int delay = 0;
                if (stop.getDepartureDelay() != null) {
                    delay = longToMinutes(stop.getDepartureDelay());
                    stopoverItems.add(new Stopover_item(setTime(stop.plannedDepartureTime),
                            setLocationName(stop.location),
                            delay));
                } else if (stop.plannedDepartureTime != null) {
                    stopoverItems.add(new Stopover_item(setTime(stop.plannedDepartureTime),
                            setLocationName(stop.location),
                            0));
                } else if (stop.predictedDepartureTime != null) {
                    stopoverItems.add(new Stopover_item(setTime(stop.predictedDepartureTime),
                            setLocationName(stop.location),
                            0));
                } else if (stop.plannedArrivalTime != null) {
                    stopoverItems.add(new Stopover_item(setTime(stop.plannedArrivalTime),
                            setLocationName(stop.location),
                            0));
                } else if (stop.predictedArrivalTime != null) {
                    stopoverItems.add(new Stopover_item(setTime(stop.predictedArrivalTime),
                            setLocationName(stop.location),
                            0));
                } else {
                    stopoverItems.add(new Stopover_item("", setLocationName(stop.location), 0));
                }
            }
        }

        return stopoverItems;
    }

    public static int iconPublic(Product product) {
        if (product == null) {
            return R.drawable.ic_android;
        }
        switch (product) {
            case HIGH_SPEED_TRAIN:
                return R.drawable.ic_non_regional_traffic;
            case SUBWAY:
                return R.drawable.ic_underground_train;
            case SUBURBAN_TRAIN:
                return R.drawable.ic_s_bahn;
            case TRAM:
                return R.drawable.ic_tram;
            case CABLECAR:
                return R.drawable.ic_gondola;
            case FERRY:
                return R.drawable.ic_ship;
            case REGIONAL_TRAIN:
                return R.drawable.ic_regionaltrain;
            case BUS:
                return R.drawable.ic_bus;
            case ON_DEMAND:
                return R.drawable.ic_taxi;
            default:
                return R.drawable.ic_android;
        }
    }

    public static int iconIndividual(Trip.Individual.Type individualType) {
        switch (individualType) {
            case WALK:
                return R.drawable.ic_walk;
            case TRANSFER:
                return R.drawable.ic_time;
            case CAR:
                return R.drawable.ic_taxi;
            case BIKE:
                return R.drawable.ic_bike;
            default:
                return R.drawable.ic_android;
        }
    }

    public static String platform(Stop stop, boolean isArrival) {
        String platform = "Gleis ";
        if (isArrival) {
            if (stop.predictedArrivalPosition != null) {
                return platform + stop.predictedArrivalPosition.toString();
            } else if (stop.plannedArrivalPosition != null) {
                return platform + stop.plannedArrivalPosition.toString();
            }
        } else {
            if (stop.predictedDeparturePosition != null) {
                return platform + stop.predictedDeparturePosition.toString();
            } else if (stop.plannedDeparturePosition != null) {
                return platform + stop.plannedDeparturePosition.toString();
            }
        }
        return "?";
    }

    public static String setNumChanges(Trip trip) {
        if (trip.getNumChanges() != null) {
            return setNumChanges(trip.getNumChanges());
        } else {
            return setNumChanges(0);
        }
    }

    public static String setNumChanges(int num_changes) {
        return num_changes + "x ";
    }
}
