package com.hackers.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackers.core.network.DataManager;
import com.hackers.core.network.callbacks.ApiCallbacks;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by SR on 25/11/17.
 */

public abstract class BaseFragment extends Fragment implements ApiCallbacks {


    private Unbinder unbinder;

    protected DataManager dataManager;

    protected abstract int initViews();

    protected abstract void initialize();

    public View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {

//        setUserVisibleHint(true);

            rootView = inflater.inflate(initViews(), container, false);
            unbinder = ButterKnife.bind(this, rootView);

            dataManager = new DataManager(this);
            initialize();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

