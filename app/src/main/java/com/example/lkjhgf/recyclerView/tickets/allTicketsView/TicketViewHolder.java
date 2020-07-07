package com.example.lkjhgf.recyclerView.tickets.allTicketsView;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.publicTransport.provider.Farezone;

import java.util.ArrayList;

/**
 * Anzeige eines einzelnen Tickets mit allen wesentlichen Informationen
 */
class TicketViewHolder extends RecyclerView.ViewHolder {

    private TextView ticketName;
    private TextView price;
    private TextView preisstufe;
    private TextView validFarezones;
    private TextView validFarezones_view;
    private TextView validTimeInterval;
    private TextView validTimeInterval_view;
    private TextView usedNumTrips;
    private TextView usedNumTrips_view;

    /**
     * Initialisierung der Attribute <br/>
     *
     * @param view Layout
     */
    public TicketViewHolder(View view) {
        super(view);
        ticketName = view.findViewById(R.id.textView108);
        price = view.findViewById(R.id.textView115);
        preisstufe = view.findViewById(R.id.textView116);
        validFarezones_view = view.findViewById(R.id.textView109);
        validFarezones = view.findViewById(R.id.textView110);
        validTimeInterval = view.findViewById(R.id.textView112);
        validTimeInterval_view = view.findViewById(R.id.textView111);
        usedNumTrips = view.findViewById(R.id.textView114);
        usedNumTrips_view = view.findViewById(R.id.textView113);
    }

    /**
     * Ansicht mit den Angaben der übergebenen Variable füllen
     *
     * @param current Ticket, dessen Informationen angezeigt werden sollen
     */
    public void fillView(TicketToBuy current) {
        ticketName.setText(current.getTicket().getName());
        String v = "Preisstufe: " + current.getPreisstufe();
        preisstufe.setText(v);
        price.setText(UtilsString.centToString(current.getTicket().getPrice(MainMenu.myProvider.getPreisstufenIndex(current.getPreisstufe()))));
        if (current.getTicket() instanceof NumTicket) {
            fillViewForNumTicket(current);
        } else {
            fillViewForTimeTicket(current);
        }
    }

    /**
     * Passt das Layout an ein NumTicket an <br/>
     * <p>
     * Es werden die nicht benötigten Felder nicht angezeigt und die benötigten Felder
     * ausgefüllt.
     *
     * @param current Ticket das angezeigt werden soll
     */
    private void fillViewForNumTicket(TicketToBuy current) {
        String text;
        validTimeInterval_view.setVisibility(View.GONE);
        validTimeInterval.setVisibility(View.GONE);
        validFarezones.setVisibility(View.GONE);
        validFarezones_view.setVisibility(View.GONE);
        usedNumTrips.setVisibility(View.VISIBLE);
        usedNumTrips_view.setVisibility(View.VISIBLE);
        text = "(" + (((NumTicket) current.getTicket()).getNumTrips() - current.getFreeTrips()) + " / " + ((NumTicket) current.getTicket()).getNumTrips() + ")";
        usedNumTrips.setText(text);
    }

    /**
     * Passt das Layout an ein TimeTicket an <br/>
     * <p>
     * Füllt die Textfelder mit den Informationen zu Geltungszeit und -bereich. <br/>
     * Setzt die Felder für die Anzahl freier Fahrten auf unsichtbar
     *
     * @param current Ticket dessen Informationen angezeigt werden sollen
     */
    private void fillViewForTimeTicket(TicketToBuy current) {
        String text;
        usedNumTrips.setVisibility(View.GONE);
        usedNumTrips_view.setVisibility(View.GONE);
        validTimeInterval_view.setVisibility(View.VISIBLE);
        validTimeInterval.setVisibility(View.VISIBLE);
        validFarezones.setVisibility(View.VISIBLE);
        validFarezones_view.setVisibility(View.VISIBLE);
        text = "";
        if (current.isZweiWabenTarif()) {
            ArrayList<Farezone> f = new ArrayList<>(current.getValidFarezones());
            text += f.get(0).getId() + " - " + current.getTripList().get(0).getTrip().from.place + ", \n";
            text += f.get(1).getId() + " - " + current.getTripList().get(1).getTrip().from.place;
        } else {
            if (current.getPreisstufe().equals(MainMenu.myProvider.getPreisstufe(MainMenu.myProvider.getPreisstufenSize() - 1))) {
                text = "Gesamtes VRR-Gebiet";
            } else {
                ArrayList<Farezone> f = new ArrayList<>(current.getValidFarezones());
                for (int i = 0; i < f.size(); i++) {
                    text += f.get(i).getId() + " - " + f.get(i).getName();
                    if (i + 1 < f.size()) {
                        text += ", \n";
                    }
                }
            }
        }
        validFarezones.setText(text);
        text = UtilsString.setDate(current.getFirstDepartureTime()) + " " + UtilsString.setTime(current.getFirstDepartureTime());
        text += " bis \n";
        text += UtilsString.endDate(current);
        validTimeInterval.setText(text);
    }
}
