package com.example.lkjhgf.Color;

import android.content.Context;

import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;

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
        return 0xff6C829A;
    }

    @Override
    public int disabledEdge(Context context) {
        return 0xff6C829A;
    }

    @Override
    public int disabledTextColor(Context context) {
        return 0xff6C829A;
    }

    @Override
    public int getColor() {
        return 0xff6C829A;
    }
}
