package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.Comparator;

/**
 * Sortierung der Preisstufen für den VRR, sowie überprüfung ob ein String eine für diesen
 * Verkehrsverbund legitime Preisstufe ist.
 */
public class VRRpreisstufenComparator implements Comparator<TripItem> {

    /**
     * Alle Preisstufen des VRR
     */
    private static final String[] preisstufen = {"K", "A1", "A2", "A3", "B", "C", "D"};

    /**
     * Sortiert die Fahrten nach ihrer Preisstufe <br/>
     * <p>
     * Es kann nicht anhand der Strings sortiert werden, da K die kleinste Preisstufe ist; <br/>
     * Deshalb wird erst dieser Sonderfall betrachtet, und anschließend die Compare Methode für
     * Strings aufgerufen.
     *
     * @param tripItem1 erste zu vergleichende Fahrt
     * @param tripItem2 zweite zu vergelichende Fahrt
     * @return 0 bei gleichheit, -1 wenn die erste Preisstufe < zweite Preisstufe, 1 wenn Preisstufe 2 < Preisstufe 1
     */
    @Override
    public int compare(TripItem tripItem1, TripItem tripItem2) {
        String preisstufe1 = tripItem1.getTrip().fares.get(0).units;
        String preisstufe2 = tripItem2.getTrip().fares.get(0).units;

        if (preisstufe1 == preisstufen[0] && preisstufe2 == preisstufen[0]) {
            return 0;
        } else if (preisstufe1 == preisstufen[0] && preisstufe2 != preisstufen[0]) {
            return -1;
        } else if (preisstufe1 != preisstufen[0] && preisstufe2 == preisstufen[0]) {
            return 1;
        } else {
            return preisstufe1.compareTo(preisstufe2);
        }
    }

    /**
     * Überprüft, ob ein String eine Preisstufe dieses Verkehrsverbundes ist
     * @param preisstufe zu überprüfende Zeichenfolge
     * @return true, wenn die Zeichenfolge in dem Array enthalten ist <br/>
     * false, falls nicht
     */
    static boolean checkContains(String preisstufe){
        for(String p :preisstufen){
            if(preisstufe.contains(p)){
                return true;
            }
        }
        return false;
    }

    /**
     * @preconditions die Preisstufe ist gültig, oder es gilt die Postcondition
     * @postconditions es wird abgefangen, dass die Preisstufe nicht gültig ist
     * @param preisstufe
     * @return
     */
    public static int getPreisstufenIndex(String preisstufe){
        for (int i = 0; i < preisstufen.length; i++){
            if (preisstufe.contains(preisstufen[i])){
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * @preconditions Index innerhalb der Größe
     * @param index
     * @return
     */
    public static String getPreisstufe(int index){
        return preisstufen[index];
    }
}
