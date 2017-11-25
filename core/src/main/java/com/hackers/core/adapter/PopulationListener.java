package com.hackers.core.adapter;

import android.view.View;

public interface PopulationListener<T> {
    void populateFrom(View var1, int var2, T var3, View[] var4);

    void onRowCreate(View[] var1);
}