package com.example.lkjhgf.recyclerView.tickets.allTicketsView;

import android.app.Activity;
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

class TicketViewHolder extends RecyclerView.ViewHolder {

    private Activity activity;

    private TextView ticketName;
    private TextView price;
    private TextView preisstufe;
    private TextView validFarezones;
    private TextView validFarezones_view;
    private TextView validTimeInterval;
    private TextView validTimeInterval_view;
    private TextView usedNumTrips;
    private TextView usedNumTrips_view;

    public TicketViewHolder(View view, Activity activity) {
        super(view);
        this.activity = activity;
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

    public void fillView(TicketToBuy current) {
        ticketName.setText(current.getTicket().getName());
        String v = "Preisstufe: " + current.getPreisstufe();
        preisstufe.setText(v);
        price.setText(UtilsString.centToString(current.getTicket().getPrice(MainMenu.myProvider.getPreisstufenIndex(current.getPreisstufe()))));
        if (current.getTicket() instanceof NumTicket) {
            validTimeInterval_view.setVisibility(View.GONE);
            validTimeInterval.setVisibility(View.GONE);
            validFarezones.setVisibility(View.GONE);
            validFarezones_view.setVisibility(View.GONE);
            usedNumTrips.setVisibility(View.VISIBLE);
            usedNumTrips_view.setVisibility(View.VISIBLE);
            v = "(" + (((NumTicket) current.getTicket()).getNumTrips() - current.getFreeTrips()) + " / " + ((NumTicket) current.getTicket()).getNumTrips() + ")";
            usedNumTrips.setText(v);
        }else{
            usedNumTrips.setVisibility(View.GONE);
            usedNumTrips_view.setVisibility(View.GONE);
            validTimeInterval_view.setVisibility(View.VISIBLE);
            validTimeInterval.setVisibility(View.VISIBLE);
            validFarezones.setVisibility(View.VISIBLE);
            validFarezones_view.setVisibility(View.VISIBLE);
            v = "";
            if(current.isZweiWabenTarif()){
                ArrayList<Farezone> f = new ArrayList<>(current.getValidFarezones());
                v+= f.get(0).getId() + " - " + current.getTripList().get(0).getTrip().from.place + ", \n";
                v += f.get(0).getId() + " - " + current.getTripList().get(1).getTrip().to.place;
            }else{
                for(Farezone f : current.getValidFarezones()){
                    v += f.getId() + " - " + f.getName() + ", \n";
                }
            }

            validFarezones.setText(v);
            v = UtilsString.setDate(current.getFirstDepartureTime()) + " " + UtilsString.setTime(current.getFirstDepartureTime());
            v += " bis \n";
            v += UtilsString.endDate(current);
            validTimeInterval.setText(v);
        }
    }
}
