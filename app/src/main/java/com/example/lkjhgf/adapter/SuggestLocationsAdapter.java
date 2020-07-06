package com.example.lkjhgf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.UtilsString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.SuggestLocationsResult;

/**
 * Persönlicher Adapter für Ortsvorschläge <br/>
 * <p>
 * <p>
 * Die Ortsvorschläge basieren auf den Nutzereingaben und stammen vom Provider, der
 * Nutzer muss aus den Vorschlägen einen Ort wählen, um eine Verbindungsanfrage starten zu können.
 * <br/>
 * Verwendung in {@link com.example.lkjhgf.helper.form.Form_Text}
 *
 * @preconditions Der Nutzer gibt in ein Autcomplete Textfeld mit diesem Adapter etwas ein
 */
public class SuggestLocationsAdapter extends ArrayAdapter<SuggestLocationsAdapter.LocationHolder> {

    private Context mContext;
    private List<SuggestLocationsAdapter.LocationHolder> mList;
    private ArrayFilter mFilter;
    private int threshhold;

    /**
     * Öffentlicher Konstruktor -> ruft privaten Konstruktor auf
     *
     * @param context    - für das Erzeugen vom Layout in {@link #getView(int, View, ViewGroup)}
     * @param threshhold - ab wie vielen Zeichen die Suche nach möglichen Zielen starten soll
     */
    public SuggestLocationsAdapter(@NonNull Context context, int threshhold) {
        this(context, new ArrayList<>(), threshhold);
    }

    /**
     * Privater Konstruktor, in welcher die Liste an Vorschlägen initialisiert wird (mit einer leeren Liste) <br/>
     * generelle initialisierung der Attribute
     *
     * @param list - Vorschlagsliste
     */
    private SuggestLocationsAdapter(@NonNull Context context,
                                    ArrayList<SuggestLocationsAdapter.LocationHolder> list,
                                    int threshhold) {
        super(context, 0, list);
        mContext = context;
        mList = list;
        this.threshhold = threshhold;
    }

    /**
     * Füllt / Erzeugt ein gefülltes Layout Element für die jeweilige Position <br/>
     * <p>
     * Erzeugung des Layouts für die Postition, <br/>
     * anschließendes füllen der Ansicht mit den Informationen aus der Liste an Vorschlägen, <br/>
     *
     * @param position des Elements in der Liste aller Vorschläge, dessen Informationen in der Liste
     *                 eingefügt werden sollen
     * @return eine gefüllte Layout Ansicht, die in der Liste der Vorschläge angezeigt wird
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Das Layout für die Vorschläge
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }

        // Variablen für die Bestandteile des Layouts -> einfacher zugänglich
        TextView textView = listItem.findViewById(R.id.textView64);
        ImageView imageView = listItem.findViewById(R.id.imageView2);

        // Füllen des Layouts mit den Informationen des Elements an der jeweiligen Position
        String currentString = mList.get(position).toString();
        textView.setText(currentString);
        // Abhängig von dem Ort-Typ, soll ein anderes Symbol angezeigt werden, damit der Nutzer
        // diese unterscheiden kann
        switch (mList.get(position).location.type) {
            case POI:
                imageView.setImageResource(R.drawable.ic_point_of_interest);
                break;
            case ADDRESS:
                imageView.setImageResource(R.drawable.ic_address);
                break;
            case STATION:
                imageView.setImageResource(R.drawable.ic_architecture_and_city);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_bookmark_black_24dp);
        }

        return listItem;
    }

    /**
     * Die Ergebnisse sollen "manuell" gefiltert werden
     * {@link ArrayFilter} <br/>
     * <p>
     * Filterung nicht nach dem Inhalt des AutoCompleteTextViews
     *
     * @return einen (neuen) ArrayFilter
     */
    @Override
    public @NonNull
    Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter(this);
        }
        return mFilter;
    }

    /**
     * Filterung der Ergebnisse nach "eigenen" Regeln <br/>
     * <p>
     * Da bei einem klassischen AutoCompleteTextView die Vorschläge gemäß der Nutzereingabe gefiltert
     * werden, werden bei weiteren Eingaben nicht erneuet nach neuen Eingaben gesucht. <br/>
     * Desweiteren erfolgt die Filterung direkt beim Provider.
     */
    private class ArrayFilter extends Filter {

        private NetworkProvider provider;
        private ArrayAdapter<SuggestLocationsAdapter.LocationHolder> adapter;

        ArrayFilter(ArrayAdapter<SuggestLocationsAdapter.LocationHolder> adapter) {
            this.provider = new VrrProvider();
            this.adapter = adapter;
        }

        /**
         * Sucht passende Ergebnisse für die Eingabe des Nutzers<br/>
         * <p>
         * Die Suche nach passenden Orten erfolgt in der Funktion {@link #suggestLocations(CharSequence prefix)}
         *
         * @param prefix bisherige Nutzereingabe
         * @return Ergebnisse der Anfrage an den Provider / mögliche Orte, die auf die Eingabe des Nutzers passen
         */
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            SuggestLocationsResult suggestedLocationsResult = null;
            //Anfrage an den Provider
            try {
                suggestedLocationsResult = suggestLocations(prefix);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Umformung SuggestLocationResult -> Location / LocationHolder
            List<SuggestLocationsAdapter.LocationHolder> suggestedLocations = new ArrayList<>();
            if (suggestedLocationsResult != null) {
                for (Location location : suggestedLocationsResult.getLocations()) {
                    suggestedLocations.add(new LocationHolder(location));
                }
            }
            //Ergebnisse der Anfrage in Form eines FilterResult umwandeln & zurückgeben
            FilterResults results = new FilterResults();
            results.count = suggestedLocations.size();
            results.values = suggestedLocations;

            return results;
        }

        /**
         * Schlägt mögliche Orte vor
         *
         * @param constraint eingegebene Zeichen vom Nutzer
         * @return null - wenn der Nutzer noch nichts eingegeben hat, oder die Anzahl eingegebener
         * Zeichen kleiner als der gewählte / festgelegte Threshhold ist
         * @throws IOException in {@link NetworkProvider#suggestLocations(CharSequence Nutzereingabe, Set mögliche Ortstypen, int maximale Anzahl an Vorschlägen)}
         */
        final SuggestLocationsResult suggestLocations(final CharSequence constraint)
                throws
                IOException {
            if (constraint == null) { //leere Eingabe
                return null;
            } else if (constraint.length() < threshhold) { // zu kurze Eingabe
                return null;
            } else {
                return provider.suggestLocations(constraint, null, 5); // Anfrage an den Provider
            }
        }

        /**
         * Packt die Vorschläge in die Liste
         *
         * @param results Suchergebnisse des Providers auf die Eingabe des Nutzers, erzeugt in
         * {@link #performFiltering(CharSequence)}
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //Vorherige Vorschläge löschen
            adapter.clear();
            if (results.count > 0) {
                //Alle neuen Vorschläge hinzufügen
                adapter.addAll((ArrayList<SuggestLocationsAdapter.LocationHolder>) results.values);
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    /**
     * Eine "Kopie" der Klasse {@link Location}, um eine eigene toString Methode zu nutzen
     */
    public class LocationHolder {
        public Location location;

        LocationHolder(Location location) {
            this.location = location;
        }

        @Override
        @NonNull
        public String toString() {
            return UtilsString.setLocationName(location);
        }
    }
}