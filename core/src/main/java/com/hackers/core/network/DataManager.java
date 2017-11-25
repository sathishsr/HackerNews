package com.hackers.core.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hackers.core.network.callbacks.ApiCallbacks;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by SR on 25/11/17.
 */

public class DataManager {

    private ApiCallbacks apiCallbacks= null;

    public DataManager(ApiCallbacks apiCallbacks) {
        this.apiCallbacks = apiCallbacks;
    }

    public void sendJsonArrayRequest(Context context, String url) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                apiCallbacks.handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                apiCallbacks.handleError(error);
            }
        });
        NetworkCall.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public void sendJsonObjectRequest(Context context,String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                apiCallbacks.handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                apiCallbacks.handleError(error);
            }
        });

        NetworkCall.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
