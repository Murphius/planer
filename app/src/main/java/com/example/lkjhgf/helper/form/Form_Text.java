package com.example.lkjhgf.helper.form;

import android.content.Context;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lkjhgf.Adapter.MyArrayAdapter;
import com.example.lkjhgf.R;

class Form_Text {

    private View view;
    private Context context;
    private Form form;

    TextView date_view, arrival_departure_view;
    AutoCompleteTextView start_view, destination_view, stopover_view;
    TextView numChildren, numChildrenView;
    TextView numAdult, numAdultView;



    Form_Text(View view, Context context, Form form){
        this.view = view;
        this.context = context;
        this.form = form;
        findTextViews();
    }

    private void findTextViews(){
        date_view = view.findViewById(R.id.editText1);
        arrival_departure_view = view.findViewById(R.id.editText2);
        start_view = view.findViewById(R.id.editText3);
        stopover_view = view.findViewById(R.id.editText4);
        destination_view = view.findViewById(R.id.editText5);

        numChildren = view.findViewById(R.id.textView75);
        numAdult = view.findViewById(R.id.textView74);
        numChildrenView = view.findViewById(R.id.editText14);
        numAdultView = view.findViewById(R.id.editText13);
    }

    void setAdapter(){
        start_view.setAdapter(new MyArrayAdapter(context, start_view.getThreshold()));
        start_view.setOnItemClickListener((parent, view, position, id) -> form.startLocation = ((MyArrayAdapter.LocationHolder) parent.getItemAtPosition(
                position)).location);
        destination_view.setAdapter(new MyArrayAdapter(context, destination_view.getThreshold()));
        destination_view.setOnItemClickListener((parent, view, position, id) -> form.destinationLocation = ((MyArrayAdapter.LocationHolder) parent.getItemAtPosition(
                position)).location);
        stopover_view.setAdapter(new MyArrayAdapter(context, stopover_view.getThreshold()));
        stopover_view.setOnItemClickListener((parent, view, position, id) -> form.stopoverLocation = ((MyArrayAdapter.LocationHolder) parent.getItemAtPosition(
                position)).location);
    }

}
