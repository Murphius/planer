package com.example.lkjhgf.color;

import android.content.Context;

import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;

/**
 * Färbt den BootstrapButton in blaugrau -> für ein einheitliches Farbschema
 * <br/>
 * Rand gleich gefärbt, Schrift in weiß <br/>
 * Klickt der Nutzer einen Button wird dieser etwas dunkler eingefärbt, um den Klick zu kennzeichnen
 */
public class ButtonBootstrapBrandVisible implements BootstrapBrand {
    @Override
    public int defaultFill(Context context) {
        return 0xff6C829A;
    }

    @Override
    public int defaultEdge(Context context) {
        return 0xff6C829A;
    }

    @Override
    public int defaultTextColor(Context context) {
        return 0xffffffff;
    }

    @Override
    public int activeFill(Context context) {
        return 0xff48596a;
    }

    @Override
    public int activeEdge(Context context) {
        return 0xff6C829A;
    }

    @Override
    public int activeTextColor(Context context) {
        return 0xffffffff;
    }

    @Override
    public int disabledFill(Context context) {
        return 0xffa8b5c3;
    }

    @Override
    public int disabledEdge(Context context) {
        return 0xffa8b5c3;
    }

    @Override
    public int disabledTextColor(Context context) {
        return 0xffffffff;
    }

    @Override
    public int getColor() {
        return 0xff6C829A;
    }
}
