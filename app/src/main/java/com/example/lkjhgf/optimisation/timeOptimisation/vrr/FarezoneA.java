package com.example.lkjhgf.optimisation.timeOptimisation.vrr;

import android.util.Pair;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.TicketToBuyTimeComporator;
import com.example.lkjhgf.helper.util.TripItemTimeComparator;
import com.example.lkjhgf.helper.util.TripQuantitiesTimeComparator;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.optimisation.timeOptimisation.FarezoneUtil;
import com.example.lkjhgf.optimisation.timeOptimisation.Util;
import com.example.lkjhgf.publicTransport.provider.Farezone;
import com.example.lkjhgf.publicTransport.provider.MyVRRprovider;
import com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.MyVrrTimeOptimisationHelper;
import com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation.VRR_Farezones;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

/**
 * Optimierung für die Preisstufe A <br/>
 * <p>
 * Enthält die Optimierung für die Preisstufen A1, A2 und A3
 */
public class FarezoneA {

    /**
     * Optimierung der Preisstufe A <br/>
     * <p>
     * Optimiert jede der drei Abstufungen (A1, A2, A3) einzeln <br/>
     * Für jede Abstufung wird die Liste an möglichen Tickets angepasst.
     *
     * @param allTrips        Zu optimierende Fahrten
     * @param possibleTickets Tickets die für die Preisstufe A genutzt werden können
     * @return Liste aller Fahrscheine für die Preisstufe A
     */
    public static ArrayList<TicketToBuy> optimierungPreisstufeA(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets) {
        ArrayList<TimeTicket> zweiWabenTicketList = new ArrayList<>(possibleTickets);
        ArrayList<TimeTicket> tarifgebietTicketList = new ArrayList<>(possibleTickets);

        //A3
        int preisstufenIndex = MainMenu.myProvider.getPreisstufenSize() - 4;
        int indexOfVierStundenTicket_2 = zweiWabenTicketList.indexOf(MyVRRprovider.vierStundenTicket);
        if (indexOfVierStundenTicket_2 > -1) {
            zweiWabenTicketList.remove(indexOfVierStundenTicket_2);
            tarifgebietTicketList.remove(indexOfVierStundenTicket_2);
        }
        ArrayList<TicketToBuy> result = new ArrayList<>(optimierungPreisstufeA(allTrips, tarifgebietTicketList, zweiWabenTicketList, preisstufenIndex));

        //A2
        preisstufenIndex--;
        if (indexOfVierStundenTicket_2 > -1) {
            tarifgebietTicketList.add(indexOfVierStundenTicket_2, MyVRRprovider.vierStundenTicket);
        }
        result.addAll(optimierungPreisstufeA(allTrips, tarifgebietTicketList, zweiWabenTicketList, preisstufenIndex));

        //A1
        preisstufenIndex--;
        result.addAll(optimierungPreisstufeA(allTrips, tarifgebietTicketList, zweiWabenTicketList, preisstufenIndex));
        return result;
    }

    /**
     * Optimierung für die einzelne Preisstufe von A <br/>
     * <p>
     * Teilt die Fahrten auf in Zweiwabentarif und Tarifgebiete <br/>
     * Optimiert beide Typen von Fahrten. <br/>
     *
     * @param allTrips                   Zu optimierende Fahrten
     * @param possibleTicketsTarifgebiet Tickets die für die Fahrten innerhalb eines Tarifgebiets nutzt werden können
     * @param possibleTicketsZweiWaben   Tickets die für Fahrten zwischen zwei Waben genutzt werden können
     * @param preisstufenIndex           betrachtete Preisstufe
     * @return Liste an Fahrscheinen die für die Preisstufe benötigit werden
     */
    private static ArrayList<TicketToBuy> optimierungPreisstufeA(ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTicketsTarifgebiet, ArrayList<TimeTicket> possibleTicketsZweiWaben, int preisstufenIndex) {
        ArrayList<TicketToBuy> result = new ArrayList<>();
        ArrayList<TripItem> tripsPreisstufe = Util.collectTripsPreisstufe(allTrips, preisstufenIndex);
        if (tripsPreisstufe.isEmpty()) {
            return result;
        }
        HashMap<HashSet<Integer>, ArrayList<TripItem>> zweiWabenTarif = collectZweiWabenTrips(tripsPreisstufe);
        //Zwei Waben Optimierung
        result.addAll(zweiWabenOptimierung(zweiWabenTarif, allTrips, possibleTicketsZweiWaben, preisstufenIndex));
        tripsPreisstufe = Util.collectTripsPreisstufe(allTrips, preisstufenIndex);
        HashMap<Farezone, ArrayList<TripItem>> tarifgebietTrips = collectFarezoneTrips(tripsPreisstufe);

        //Tarifgebiet Optimierung
        result.addAll(tarifgebietOptimierung(tarifgebietTrips, allTrips, possibleTicketsTarifgebiet, preisstufenIndex));
        return result;
    }

    /**
     * Sucht die Fahrten die zwei Waben nutzen raus <br/>
     * <p>
     * Überprüfung, ob die jeweilige Fahrt zwei Waben nutzt über die IDs der durchfahrenen Waben.
     * Wenn eine gefunden wird, die /10 abweicht, handelt es sich um den zwei Waben Tarif.
     *
     * @param tripList Liste aller Fahrten einer Preisstufe
     * @return Alle Fahrten die zwei Waben nutzen
     */
    private static HashMap<HashSet<Integer>, ArrayList<TripItem>> collectZweiWabenTrips(ArrayList<TripItem> tripList) {
        HashMap<HashSet<Integer>, ArrayList<TripItem>> zweiWabenTarif = new HashMap<>();
        for (TripItem item : tripList) {
            ArrayList<Integer> crossedFarezones = new ArrayList<>(item.getCrossedFarezones());
            int startID = crossedFarezones.get(0);
            for (Integer crossedFarezone : crossedFarezones) {
                if (crossedFarezone / 10 != startID / 10) {
                    HashSet<Integer> zweiWaben = new HashSet<>();
                    zweiWaben.add(crossedFarezone);
                    zweiWaben.add(startID);
                    ArrayList<TripItem> oldTrips = zweiWabenTarif.get(zweiWaben);
                    if (oldTrips == null) {
                        oldTrips = new ArrayList<>();
                        zweiWabenTarif.put(zweiWaben, oldTrips);
                    }
                    oldTrips.add(item);
                    break;
                }
            }
        }
        return zweiWabenTarif;
    }

    /**
     * Sucht die Fahrten raus, die innerhalb eines Tarifgebiets sind <br/>
     * <p>
     * Überpüfung, ob eine Fahrt im zwei Waben Tarif liegt, wenn nein -> in die Liste packen <br/>
     * Beachtet auch die fünf Sonderfall Städte.
     *
     * @param tripList Alle Fahrten
     * @return Alle Fahrten, die innerhalb eines Tarifgebiets sind
     * @preconditions Fahrten die innerhalb einer Wabe bleiben, und diese Wabe von einem zwei Waben Ticket abgedeckt wird,
     * sind diesem Ticket zugewiesen
     * @postconditions Bei der Setzung der Geltungsbereiche, müssen die Sonderfälle erneut beachetet werden
     */
    private static HashMap<Farezone, ArrayList<TripItem>> collectFarezoneTrips(ArrayList<TripItem> tripList) {
        Set<Farezone> farezones = VRR_Farezones.createVRRFarezone();
        HashMap<Farezone, ArrayList<TripItem>> tarifgebietTrips = new HashMap<>();
        for (TripItem tripItem : tripList) {
            if (!MyVrrTimeOptimisationHelper.checkIsZweiWaben(tripItem.getCrossedFarezones())) {
                Farezone farezone = farezones.stream().filter(f -> f.getId() == tripItem.getStartID() / 10).findFirst().get();
                farezone = checkSpecialFarezone(farezone);
                ArrayList<TripItem> oldTrips = tarifgebietTrips.get(farezone);
                if (oldTrips == null) {
                    oldTrips = new ArrayList<>();
                    tarifgebietTrips.put(farezone, oldTrips);
                }
                oldTrips.add(tripItem);
            }
        }
        return tarifgebietTrips;
    }

    /**
     * Optimierung von Fahrten, die in zwei Waben liegen
     *
     * @param zweiWabenTarif   Fahrten die zwei Waben nutzen
     * @param allTrips         Alle zu optimierenden Fahrten
     * @param possibleTickets  zu verfügung stehende Tickets
     * @param preisstufenIndex betrachtete Preisstufe
     * @return Liste der benötigten Tickets
     * @preconditions Die Fahrten im zweiWabenTarif sind nach den genutzten Waben aufgeteilt
     */
    public static ArrayList<TicketToBuy> zweiWabenOptimierung(HashMap<HashSet<Integer>, ArrayList<TripItem>> zweiWabenTarif, ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets, int preisstufenIndex) {
        ArrayList<TicketToBuy> result = new ArrayList<>();
        //Betrachtung von zwei Waben
        for (Set<Integer> zweiWaben : zweiWabenTarif.keySet()) {
            ArrayList<TicketToBuy> currentTicketList = new ArrayList<>();
            ArrayList<TripItem> tarfigebietTrips = zweiWabenTarif.get(zweiWaben);
            Collections.sort(tarfigebietTrips, new TripItemTimeComparator());
            //Alle Tickets ausprobieren
            for (int ticketIndex = 0; ticketIndex < possibleTickets.size(); ticketIndex++) {
                TimeTicket possibleTicket = possibleTickets.get(ticketIndex);
                while (tarfigebietTrips.size() >= possibleTicket.getMinNumTrips(preisstufenIndex)) {//Es gibt genug Fahrten, damit sich das Ticket lohnen kann
                    Pair<Integer, Integer> bestTicketIntervall = Util.findBestTimeInterval(tarfigebietTrips, possibleTicket);
                    if (bestTicketIntervall.second < possibleTicket.getMinNumTrips(preisstufenIndex)) { //Ob sich genug Fahrten zusammenfassen lassen
                        break;
                    }
                    TicketToBuy ticketToBuy = new TicketToBuy(possibleTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                    removeTrips(tarfigebietTrips, allTrips, ticketToBuy, bestTicketIntervall);
                    //Gültigkeitsbereich setzen
                    setFarezone(zweiWaben, ticketToBuy);
                    //Ticket hinzufügen
                    currentTicketList.add(ticketToBuy);
                }
                //Wenn sich mehr als ein Ticket angesammelt hat, prüfen, ob sich dies mit anderen Fahrscheinen günstiger kombinieren lässt
                if (currentTicketList.size() > 1) {
                    Collections.sort(currentTicketList, new TicketToBuyTimeComporator());
                    Util.sumUpTickets(currentTicketList, possibleTickets, ticketIndex + 1, preisstufenIndex);
                }
                //Ticket auch für andere Fahrten anwendbar ?
                checkTicketForOtherTripsZweiWaben(allTrips, currentTicketList);
                for (TicketToBuy ticket : currentTicketList) {//Für diese den Gültigkeitsbereich setzen
                    setFarezone(zweiWaben, ticket);
                }
            }
            result.addAll(currentTicketList);//Tickets der Region, in die Liste aller benötigten Tickets hinzufügen
        }
        return result;
    }

    /**
     * Optimierung von Fahrten, die in einem Tarifgebiet liegen
     *
     * @param tarifgebietTrips zu optimierende Fahrten, auf die Tarifgebiete aufgeteilt
     * @param allTrips         alle zu optimierenden Fahrten
     * @param possibleTickets  mögliche Tickets
     * @param preisstufenIndex Preisstufe
     * @return Liste aller benötigten Tickets
     * @preconditions Die tarifgebietTrips sind entsprechend der Tarifgebiete aufgeteilt
     */
    public static ArrayList<TicketToBuy> tarifgebietOptimierung(HashMap<Farezone, ArrayList<TripItem>> tarifgebietTrips, ArrayList<TripItem> allTrips, ArrayList<TimeTicket> possibleTickets, int preisstufenIndex) {
        ArrayList<TicketToBuy> result = new ArrayList<>();
        //Betrachtung eines Tarifgebiets
        for (Farezone farezone : tarifgebietTrips.keySet()) {
            ArrayList<TicketToBuy> currentTicketList = new ArrayList<>();
            ArrayList<TripItem> tarfigebietTrips = tarifgebietTrips.get(farezone);
            Collections.sort(tarfigebietTrips, new TripItemTimeComparator());
            //Alle Tickets ausprobieren
            for (int ticketIndex = 0; ticketIndex < possibleTickets.size(); ticketIndex++) {
                TimeTicket possibleTicket = possibleTickets.get(ticketIndex);
                //Überprüfen, ob genug Fahrten für das jeweilige Ticket in der Region stattfinden
                while (tarfigebietTrips.size() >= possibleTickets.get(ticketIndex).getMinNumTrips(preisstufenIndex)) {
                    Pair<Integer, Integer> bestTicketIntervall = Util.findBestTimeInterval(tarfigebietTrips, possibleTicket);
                    //Prüfen, ob es genug Fahrten gibt, die zu dem Ticket passen
                    if (bestTicketIntervall.second < possibleTicket.getMinNumTrips(preisstufenIndex)) {
                        break;
                    }
                    TicketToBuy ticketToBuy = new TicketToBuy(possibleTicket, MainMenu.myProvider.getPreisstufe(preisstufenIndex));
                    removeTrips(tarfigebietTrips, allTrips, ticketToBuy, bestTicketIntervall);
                    //Gültigkeitsbereich setzen
                    setFarezone(farezone, ticketToBuy);
                    currentTicketList.add(ticketToBuy);
                }
                //Zusammenfassen der Tickets
                if (currentTicketList.size() > 1) {
                    Collections.sort(currentTicketList, new TicketToBuyTimeComporator());
                    Util.sumUpTickets(currentTicketList, possibleTickets, ticketIndex + 1, preisstufenIndex);
                }
                //Ticket auch für andere Fahrten anwendbar ?
                FarezoneUtil.checkTicketsForOtherTrips(allTrips, currentTicketList);
                for (TicketToBuy ticket : currentTicketList) {
                    setFarezone(farezone, ticket);
                }
            }
            result.addAll(currentTicketList);
        }
        return result;
    }

    /**
     * Überprüft, ob die zwei Waben Tickets noch für weitere Fahrten genutzt werden können
     *
     * @param allTrips     Fahrten, die noch kein Ticket haben
     * @param ticketsToBuy Fahrscheine im zwei Waben Tarif
     */
    public static void checkTicketForOtherTripsZweiWaben(ArrayList<TripItem> allTrips, ArrayList<TicketToBuy> ticketsToBuy) {
        for (TicketToBuy ticketToBuy : ticketsToBuy) {
            TimeTicket timeTicket = (TimeTicket) ticketToBuy.getTicket();
            for (Iterator<TripItem> tripItemIterator = allTrips.iterator(); tripItemIterator.hasNext(); ) {
                TripItem current = tripItemIterator.next();
                //Preisstufe
                if (MainMenu.myProvider.getPreisstufenIndex(current.getPreisstufe()) <= MainMenu.myProvider.getPreisstufenIndex(ticketToBuy.getPreisstufe())) {
                    if (timeTicket.isValidTrip(current)) {
                        //Prüfe, ob alle durchfahrenen Waben, von dem Ticket abgedeckt werden
                        if (ticketToBuy.checkFarezone(current)) {
                            long minStartTime = Math.min(current.getFirstDepartureTime().getTime(), ticketToBuy.getLastArrivalTime().getTime());
                            long maxEndTime = Math.max(current.getLastArrivalTime().getTime(), ticketToBuy.getLastArrivalTime().getTime());
                            if (maxEndTime - minStartTime <= timeTicket.getMaxDuration()) {
                                ticketToBuy.addTripItem(current);
                                tripItemIterator.remove();
                                allTrips.remove(current);
                                Collections.sort(ticketToBuy.getTripQuantities(), new TripQuantitiesTimeComparator());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Prüfung der Sonderfälle <br/>
     * <p>
     * Für diese fünf Städte gilt, dass die Tickets der Preisstufe A in der gesamten Stadt gültig sind
     */
    private static Farezone checkSpecialFarezone(Farezone tarifgebiet) {
        Set<Farezone> farezones = VRR_Farezones.createVRRFarezone();
        //Sonderfälle: Diese Städte müssen insgesamt betrachtet werden, und nicht nur das einzelne Tarifgebiet
        if (tarifgebiet.getId() == 38) {
            tarifgebiet = farezones.stream().filter(f -> f.getId() == 37).findFirst().get();
        } else if (tarifgebiet.getId() == 53) {
            tarifgebiet = farezones.stream().filter(f -> f.getId() == 43).findFirst().get();
        } else if (tarifgebiet.getId() == 33) {
            tarifgebiet = farezones.stream().filter(f -> f.getId() == 23).findFirst().get();
        } else if (tarifgebiet.getId() == 45) {
            tarifgebiet = farezones.stream().filter(f -> f.getId() == 35).findFirst().get();
        } else if (tarifgebiet.getId() == 66) {
            tarifgebiet = farezones.stream().filter(f -> f.getId() == 65).findFirst().get();
        }
        return tarifgebiet;
    }

    /**
     * Für den Geltungsbereich der Tickets, muss das Ersetzen rückgängig gemacht werden <br/>
     * <p>
     * Handelt es sich nicht um einen Sonderfall ist dies nicht schlimm, dass das gleiche Tarifgebiet
     * erneut zurückgegeben wird, da die Tarifgebiete in einem Set gespeichert werden <br/>
     * Für die fünf Sonderfälle ist der Geltungsbereich erweitert.
     *
     * @param farezone muss auf Sonderfall geprüft werden
     * @return Tarifgebiet
     */
    private static Farezone checkSpecialFarezoneReversed(Farezone farezone) {
        Set<Farezone> farezones = VRR_Farezones.createVRRFarezone();
        if (farezone.getId() == 37) {
            return farezones.stream().filter(f -> f.getId() == 38).findFirst().get();
        } else if (farezone.getId() == 43) {
            return farezones.stream().filter(f -> f.getId() == 53).findFirst().get();
        } else if (farezone.getId() == 23) {
            return farezones.stream().filter(f -> f.getId() == 33).findFirst().get();
        } else if (farezone.getId() == 35) {
            return farezones.stream().filter(f -> f.getId() == 45).findFirst().get();
        } else if (farezone.getId() == 65) {
            return farezones.stream().filter(f -> f.getId() == 66).findFirst().get();
        } else {
            return farezone;
        }
    }

    /**
     * Setzt den Gültigkeitsbereich eines Tickets
     */
    private static void setFarezone(Farezone tarifgebiet, TicketToBuy ticketToBuy){
        Set<Farezone> x = new HashSet<>();
        x.add(tarifgebiet);
        //Für diese fünf Tarifgebiete ist der Gültigkeitsbereich nicht nur das Tarifgebiet, sondern die ganze Stadt
        x.add(checkSpecialFarezoneReversed(tarifgebiet));
        ticketToBuy.setValidFarezones(x, tarifgebiet.getId());
    }

    private static void setFarezone(Set<Integer> waben, TicketToBuy ticketToBuy){
        ArrayList<Integer> list = new ArrayList<>(waben);
        Set<Farezone> x = new HashSet<>();
        x.add(new Farezone(list.get(0), ""));
        x.add(new Farezone(list.get(1), ""));
        ticketToBuy.setValidFarezones(x, list.get(0), true);
    }
    /**
     * Löscht die Fahrten, die dem Ticket zugewiesen werden, aus den Listen der zu optimierenden Fahrten <br/>
     * <p>
     * Um auf zwei Listen gleichzeitig mit Index zu arbeiten, obwohl sich die Listen Positionen ändern
     *
     * @param regionalTrips       Liste mit den Fahrten die aktuell optimiert wurden, und die dem Ticket zugewiesen werden sollen
     * @param allTrips            Liste mit allen Fahrten die optimiert werden sollen
     * @param ticketToBuy         Neues Ticket, dem die Fahrten zugewiesen werden sollen
     * @param bestTicketIntervall Gibt an, welche Fahrten aus regionalTrips dem Ticket zugewiesen werden sollen
     */
    private static void removeTrips(ArrayList<TripItem> regionalTrips, ArrayList<TripItem> allTrips, TicketToBuy ticketToBuy, Pair<Integer, Integer> bestTicketIntervall) {
        int i = 0;
        for (ListIterator<TripItem> listIterator = regionalTrips.listIterator(); i < bestTicketIntervall.first + bestTicketIntervall.second; ) {
            TripItem current = listIterator.next();
            if (i >= bestTicketIntervall.first) {
                listIterator.remove();
                ticketToBuy.addTripItem(current);
                allTrips.remove(current);
            }
            i++;
        }
    }
}
