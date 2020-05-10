package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;

/**
 * Handhabung der Buttons in der Ansicht möglicher Verbindungen <br/>
 * <p>
 * Der Nutzer kann die Fahrt editieren (neue Anfrage), nach frühereren und späteren Verbindungen
 * suchen
 */
class ButtonClass {

    private Activity activity;

    private RecyclerViewService recyclerViewService;

    BootstrapButton earlierButton;
    private BootstrapButton editButton, laterButton;

    /**
     * Ruft die Funktionen zur Initialisierung auf <br/>
     * <p>
     * Initialisiert die Buttons ({@link #findButtons(View)}), verpasst ihnen den richtigen
     * Anstrich ({@link #designButtons()}) und setzt OnClickListener ({@link #setOnClickListener()}).
     *
     * @param view     Layout
     * @param activity wird für das Editieren benötigt {@link #setOnClickListener()}
     */
    ButtonClass(View view, Activity activity) {
        this.activity = activity;
        findButtons(view);
        designButtons();
        setOnClickListener();
    }

    /**
     * Setzt den RecyclerView, in dem die möglichen Verbindungen enthalten sind <br/>
     * <p>
     * Da bei der Suche nach weiteren Verbindungen die Liste des RecyclerViews sich ändert, werden die
     * Funktionen dieser Klasse aufgerufen <br/>
     * <p>
     * Sollten keine Verbindungen gefunden werden, kann der Nutzer auch keine früheren oder späteren
     * Verbindungen suchen, deshalb werden diese Buttons für den Nutzer nicht nutzbar <br/>
     *
     * @param recyclerViewService stellt die Funktionen für frühere / spätere Fahrten zur Verfügung
     * @see #setOnClickListener()
     */
    void setRecyclerView(RecyclerViewService recyclerViewService) {
        this.recyclerViewService = recyclerViewService;
        //Falls keine Verbindungen gefunden wurden
        if (recyclerViewService.isConnectionListEmpty()) {
            earlierButton.setClickable(false);
            laterButton.setClickable(false);
        }
    }

    /**
     * Initialisierung der Attribute
     * ID -> Attribut --> einfachere Handhabung
     *
     * @param view Layout in dem die Buttons liegen sollen
     */
    private void findButtons(View view) {
        earlierButton = view.findViewById(R.id.BootstrapButton23);
        editButton = view.findViewById(R.id.BootstrapButton24);
        laterButton = view.findViewById(R.id.BootstrapButton25);
    }

    /**
     * Button Layout / Design einheitlich gestalten
     */
    private void designButtons() {
        earlierButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        laterButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        editButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }

    /**
     * onClickListener für die einzelnen Buttons <br/>
     * <p>
     * Editieren: zurück zum Formular ({@link com.example.lkjhgf.helper.form.Form}) <br/>
     * Frühere Verbindungen: {@link RecyclerViewService#newConnectionsEarlier()} <br/>
     * Spätere Verbindungen: {@link RecyclerViewService#newConnectionsLater()}
     */
    private void setOnClickListener() {
        editButton.setOnClickListener(v -> activity.onBackPressed());
        laterButton.setOnClickListener(view -> recyclerViewService.newConnectionsLater());
        earlierButton.setOnClickListener(v -> recyclerViewService.newConnectionsEarlier());
    }

}
