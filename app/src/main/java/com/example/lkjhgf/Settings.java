package com.example.lkjhgf;

import android.app.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.LinkedHashSet;
import java.util.Set;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Product;
import de.schildbach.pte.dto.TripOptions;

public class Settings extends Activity {

    private RadioButton notBarrierfree, barrierfree, limitedBarrierfree;
    private RadioButton walkFast, walkNormal, walkSlowly;
    private RadioButton fastConnection, fewChanges, shortWalk;
    private CheckBox nonRegionalTraffic, regionalTraffic, suburbantraffic, undergroundtrain, tram, bus, ast, ferry, gondola;

    private static boolean notBarrierfreeSettings, barrierfreeSettings, limitedBarrierfreeSettings;
    private static boolean walkSlowlySettings, walkFastSettings, walkNormalSettings;
    private static boolean fastConnectionSettings, fewChangesSettings, shortWalkSettings;

    private static boolean nonRegionalTrafficB, regionalTrafficB, suburbantrafficB, undergroundtrainB, tramB, busB, astB, ferryB, gondolaB;

    private static TripOptions tripOptions;

    public static final String SETTINGS = "com.example.lkjhf.settings";

    public static final String BARRIERFREE = "com.example.lkjhf.barrierfree";
    public static final String LIMITEDBARRIERFREE = "com.example.lkjhf.limitedbarrierfree";
    public static final String NOTBARRIERFREE = "com.example.lkjhf.notbarrierfree";

    public static final String WALKSLOWLY = "com.example.lkjhf.walkslowly";
    public static final String WALKFAST = "com.example.lkjhf.walkfast";
    public static final String WALKNORMAL = "com.example.lkjhf.walknormal";

    public static final String FASTCONNECTION = "com.example.lkjhf.fastconnection";
    public static final String FEWCHANGES = "com.example.lkjhf.fewchanges";
    public static final String SHORTWALK = "com.example.lkjhf.shortwalk";

    public static final String NONREGIONALTRAFFIC = "com.example.lkjhf.nonregionaltraffic";
    public static final String REGIONALTRAFFIC = "com.example.lkjhf.regionaltraffic";
    public static final String SUBURBANTRAFFIC = "com.example.lkjhf.suburbantraffic";
    public static final String UNDERGROUNDTRAIN = "com.example.lkjhf.undergroundtrain";
    public static final String TRAM = "com.example.lkjhf.tram";
    public static final String BUS = "com.example.lkjhf.bus";
    public static final String AST = "com.example.lkjhf.ast";
    public static final String FERRY = "com.example.lkjhf.ferry";
    public static final String GONDOLA = "com.example.lkjhf.gondola";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
        loadData(this);
        updateView();
        tripOptions = tripOptions();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tripOptions = tripOptions();
        saveData();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.putBoolean(BARRIERFREE, barrierfree.isChecked());
        editor.putBoolean(NOTBARRIERFREE, notBarrierfree.isChecked());
        editor.putBoolean(LIMITEDBARRIERFREE, limitedBarrierfree.isChecked());


        editor.putBoolean(WALKFAST, walkFast.isChecked());
        editor.putBoolean(WALKNORMAL, walkNormal.isChecked());
        editor.putBoolean(WALKSLOWLY, walkSlowly.isChecked());

        editor.putBoolean(FEWCHANGES, fewChanges.isChecked());
        editor.putBoolean(SHORTWALK, shortWalk.isChecked());
        editor.putBoolean(FASTCONNECTION, fastConnection.isChecked());

        editor.putBoolean(NONREGIONALTRAFFIC, nonRegionalTraffic.isChecked());
        editor.putBoolean(REGIONALTRAFFIC, regionalTraffic.isChecked());
        editor.putBoolean(SUBURBANTRAFFIC, suburbantraffic.isChecked());
        editor.putBoolean(UNDERGROUNDTRAIN, undergroundtrain.isChecked());
        editor.putBoolean(TRAM, tram.isChecked());
        editor.putBoolean(BUS, bus.isChecked());
        editor.putBoolean(AST, ast.isChecked());
        editor.putBoolean(FERRY, ferry.isChecked());
        editor.putBoolean(GONDOLA, gondola.isChecked());

        editor.apply();

        Toast.makeText(this, "Einstellungen gespeichert", Toast.LENGTH_SHORT).show();
    }

    public static void loadData(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SETTINGS, MODE_PRIVATE);

        barrierfreeSettings = sharedPreferences.getBoolean(BARRIERFREE, true);
        notBarrierfreeSettings = sharedPreferences.getBoolean(NOTBARRIERFREE, false);
        limitedBarrierfreeSettings = sharedPreferences.getBoolean(LIMITEDBARRIERFREE, false);

        walkSlowlySettings = sharedPreferences.getBoolean(WALKSLOWLY, false);
        walkNormalSettings = sharedPreferences.getBoolean(WALKNORMAL, true);
        walkFastSettings = sharedPreferences.getBoolean(WALKFAST, false);

        fewChangesSettings = sharedPreferences.getBoolean(FEWCHANGES, false);
        fastConnectionSettings = sharedPreferences.getBoolean(FASTCONNECTION, true);
        shortWalkSettings = sharedPreferences.getBoolean(SHORTWALK, false);

        nonRegionalTrafficB = sharedPreferences.getBoolean(NONREGIONALTRAFFIC, false);
        regionalTrafficB = sharedPreferences.getBoolean(REGIONALTRAFFIC, true);
        suburbantrafficB = sharedPreferences.getBoolean(SUBURBANTRAFFIC, true);
        undergroundtrainB = sharedPreferences.getBoolean(UNDERGROUNDTRAIN, true);
        tramB = sharedPreferences.getBoolean(TRAM, true);
        busB = sharedPreferences.getBoolean(BUS, true);
        astB = sharedPreferences.getBoolean(AST, true);
        ferryB = sharedPreferences.getBoolean(FERRY, true);
        gondolaB = sharedPreferences.getBoolean(GONDOLA, true);
    }

    private void updateView() {
        barrierfree.setChecked(barrierfreeSettings);
        limitedBarrierfree.setChecked(limitedBarrierfreeSettings);
        notBarrierfree.setChecked(notBarrierfreeSettings);

        walkFast.setChecked(walkFastSettings);
        walkNormal.setChecked(walkNormalSettings);
        walkSlowly.setChecked(walkSlowlySettings);

        fewChanges.setChecked(fewChangesSettings);
        fastConnection.setChecked(fastConnectionSettings);
        shortWalk.setChecked(shortWalkSettings);

        nonRegionalTraffic.setChecked(nonRegionalTrafficB);
        regionalTraffic.setChecked(regionalTrafficB);
        suburbantraffic.setChecked(suburbantrafficB);
        undergroundtrain.setChecked(undergroundtrainB);
        tram.setChecked(tramB);
        bus.setChecked(busB);
        ast.setChecked(astB);
        ferry.setChecked(ferryB);
        gondola.setChecked(gondolaB);
    }

    private void init() {
        notBarrierfree = findViewById(R.id.radioButton1);
        barrierfree = findViewById(R.id.radioButton3);
        limitedBarrierfree = findViewById(R.id.radioButton2);

        walkFast = findViewById(R.id.radioButton4);
        walkNormal = findViewById(R.id.radioButton5);
        walkSlowly = findViewById(R.id.radioButton6);

        fastConnection = findViewById(R.id.radioButton7);
        fewChanges = findViewById(R.id.radioButton8);
        shortWalk = findViewById(R.id.radioButton9);

        nonRegionalTraffic = findViewById(R.id.checkBox);
        regionalTraffic = findViewById(R.id.checkBox1);
        suburbantraffic = findViewById(R.id.checkBox3);
        undergroundtrain = findViewById(R.id.checkBox4);
        tram = findViewById(R.id.checkBox5);
        bus = findViewById(R.id.checkBox6);
        ast = findViewById(R.id.checkBox7);
        ferry = findViewById(R.id.checkBox8);
        gondola = findViewById(R.id.checkBox9);
    }

    private static TripOptions tripOptions() {

        Set<Product> products = new LinkedHashSet<Product>();
        if (busB) {
            products.add(Product.BUS);
        }
        if (tramB) {
            products.add(Product.TRAM);
        }
        if (regionalTrafficB) {
            products.add(Product.REGIONAL_TRAIN);
        }
        if(suburbantrafficB){
            products.add(Product.SUBURBAN_TRAIN);
        }
        if (nonRegionalTrafficB) {
            products.add(Product.HIGH_SPEED_TRAIN);
        }
        if (undergroundtrainB) {
            products.add(Product.SUBWAY);
        }
        if (ferryB) {
            products.add(Product.FERRY);
        }
        if (gondolaB) {
            products.add(Product.CABLECAR);
        }
        if (astB) {
            products.add(Product.ON_DEMAND);
        }

        NetworkProvider.Optimize optimize = null;
        if (fewChangesSettings) {
            optimize = NetworkProvider.Optimize.LEAST_CHANGES;
        } else if (fastConnectionSettings) {
            optimize = NetworkProvider.Optimize.LEAST_DURATION;
        } else {
            optimize = NetworkProvider.Optimize.LEAST_WALKING;
        }

        NetworkProvider.WalkSpeed walkSpeed = null;
        if (walkFastSettings) {
            walkSpeed = NetworkProvider.WalkSpeed.FAST;
        } else if (walkNormalSettings) {
            walkSpeed = NetworkProvider.WalkSpeed.NORMAL;
        } else {
            walkSpeed = NetworkProvider.WalkSpeed.SLOW;
        }

        NetworkProvider.Accessibility accessibility = null;
        if (notBarrierfreeSettings) {
            accessibility = NetworkProvider.Accessibility.NEUTRAL;
        } else if (limitedBarrierfreeSettings) {
            accessibility = NetworkProvider.Accessibility.LIMITED;
        } else {
            accessibility = NetworkProvider.Accessibility.BARRIER_FREE;
        }
        return new TripOptions(products, optimize, walkSpeed, accessibility, null);
    }

    public static TripOptions getTripOptions(Activity activity){
        loadData(activity);
        tripOptions = tripOptions();
        return tripOptions;
    }

}
