package com.example.lkjhgf.Color;

import android.content.Context;

import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;

/**
 * Färbt einen BootstrapButton weiß (inklusive Rand) mit dunkelblauer Schrift
 * <br/>
 * Bei einem Klick bleiben die Farben gleich
 */
public class ButtonBootstrapBrandInvisible implements BootstrapBrand {
    @Override
    public int defaultFill(Context context) {
        return 0x0;
    }

    @Override
    public int defaultEdge(Context context) {
        return 0x0;
    }

    @Override
    public int defaultTextColor(Context context) {
        return 0xff324856;
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
        return 0xff172b35;
    }

    @Override
    public int disabledFill(Context context) {
        return 0xffffffff;
    }

    @Override
    public int disabledEdge(Context context) {
        return 0xff6C829A;
    }

    @Override
    public int disabledTextColor(Context context) {
        return 0xff000000;
    }

    @Override
    public int getColor() {
        return 0xff6C829A;
    }
}