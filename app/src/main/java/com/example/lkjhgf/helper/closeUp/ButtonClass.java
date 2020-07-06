package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;
import com.example.lkjhgf.publicTransport.query.QueryParameter;

/**
 * Handhabung der Buttons in der detaillierten Ansicht einer Fahrt
 */
class ButtonClass {

    private Activity activity;

    private CloseUp closeUp;

    private BootstrapButton button_refresh;
    BootstrapButton button_accept, button_back;

    /**
     * Initialisierung der Attribute <br/>
     * <p>
     * Initialisierung -> {@link #findButtons(View)} <br/>
     * Layout der Buttons anpassen -> {@link #designButtons()} <br/>
     * Buttons mit Funktion versehen -> {@link #setOnClickListener()} <br/>
     *
     * @param activity -> auf Button "zurück" klicken = onBackPressed
     * @param view     Layout
     * @param closeUp  Auslagerung der Methoden die auferufen werden, wenn ein Nutzer einen Button
     *                 klickt
     */
    ButtonClass(Activity activity, View view, CloseUp closeUp) {
        this.closeUp = closeUp;
        this.activity = activity;
        findButtons(view);
        designButtons();
        setOnClickListener();
    }

    /**
     * Initialiserung der Attribute <br/>
     * <p>
     * Attribut <-> ID -> einfachere Handhabung
     *
     * @param view Layout
     */
    private void findButtons(View view) {
        button_accept = view.findViewById(R.id.BootstrapButton31);
        button_back = view.findViewById(R.id.BootstrapButton30);
        button_refresh = view.findViewById(R.id.BootstrapButton32);
    }

    /**
     * Buttons mit dem gleichen "Anstrich" versehen
     */
    private void designButtons() {
        button_back.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        button_accept.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        button_refresh.setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }

    /**
     * OnClickListener für die einzelnen Buttons <br/>
     * <p>
     * zurück -> aktivität.onBackPressed <br/>
     * refresh -> Aktualisiert die Fahrt - Siehe {@link CloseUp#refreshTrip()} <br/>
     * accept -> {@link CloseUp#onAcceptClicked()}
     */
    private void setOnClickListener() {
        button_accept.setOnClickListener(v -> closeUp.onAcceptClicked());

        button_back.setOnClickListener(v -> activity.onBackPressed());

        button_refresh.setOnClickListener(v -> closeUp.refreshTrip());
    }
}
