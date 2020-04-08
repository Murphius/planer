package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;

class Buttons {

    private Activity activity;

    private CloseUp closeUp;

    private BootstrapButton button_refresh;
    BootstrapButton button_accept,button_back;

    Buttons(Activity activity, View view, CloseUp closeUp){
        this.closeUp = closeUp;
        this.activity = activity;
        findButtons(view);
        designButtons();
        setOnClickListener();
    }

    private void findButtons(View view){
        button_accept = view.findViewById(R.id.BootstrapButton31);
        button_back = view.findViewById(R.id.BootstrapButton30);
        button_refresh = view.findViewById(R.id.BootstrapButton32);
    }

    private void designButtons(){
        button_back.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        button_accept.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        button_refresh.setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }

    private void setOnClickListener(){
        button_accept.setOnClickListener(v -> {
            closeUp.onAcceptClicked();
        });

        button_back.setOnClickListener(v -> activity.onBackPressed());

        button_refresh.setOnClickListener(v -> activity.recreate());
    }




}
