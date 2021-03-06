package com.hackers.hakernews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hackers.core.FragmentCallbacks;
import com.hackers.hakernews.home.HomeFragment;

public class MainActivity extends AppCompatActivity implements FragmentCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceFragment(new HomeFragment(), false);

    }


    @Override
    public void replaceFragment(Fragment fragment, boolean isAdd) {

        if (fragment != null) {
            try {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                String backStateName = fragment.getClass().getName();

                if (isAdd) {
                    ft.add(R.id.container, fragment);
                    ft.addToBackStack(backStateName);
                } else {
                    ft.replace(R.id.container, fragment);
                }


                ft.commit();


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error", "Error in creating fragment");
            }
        } else {
            // error in creating fragment
            Log.e("Error", "Error in creating fragment");
        }


    }
}
