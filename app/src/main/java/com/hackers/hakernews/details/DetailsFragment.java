package com.hackers.hakernews.details;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hackers.core.BaseFragment;
import com.hackers.core.HackersConstant.HackerNewsConstant;
import com.hackers.core.adapter.CommonRecyclerAdapter;
import com.hackers.core.adapter.PopulationListener;
import com.hackers.core.models.TopStories;
import com.hackers.hakernews.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by SR on 25/11/17.
 */

public class DetailsFragment extends BaseFragment {


    Unbinder unbinder;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    CommonRecyclerAdapter<TopStories> adapter;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.toolBarTitle)
    TextView toolBarTitle;
    private ArrayList<TopStories> kidStoriesList = new ArrayList<>();


    @Override
    protected int initViews() {
        return R.layout.fragment_details;
    }

    @Override
    protected void initialize() {
        toolBarTitle.setText(getString(R.string.TITLE_DETAILS));
        setUpAdapter();
        if (getArguments() != null) {
            TopStories topStories = (TopStories) getArguments().getSerializable(HackerNewsConstant.TOP_STORY);
            assert topStories != null;
            String url = HackerNewsConstant.DETAIL_URL + topStories.getId() + HackerNewsConstant.APPEND_JSON;
            dataManager.sendJsonObjectRequest(getActivity(), url);
        }

    }

    private void setUpAdapter() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CommonRecyclerAdapter<>(getActivity(), new ArrayList<TopStories>());

        int[] ids = {R.id.key};


        adapter.setLayoutTextViews(R.layout.item_list_home, ids);

        adapter.setPopulationListener(new PopulationListener<TopStories>() {
            @Override
            public void populateFrom(View var1, int var2, TopStories var3, View[] views) {

                ((TextView) views[0]).setText(var3.getId());
            }

            @Override
            public void onRowCreate(View[] var1) {

            }
        });


        recyclerView.setAdapter(adapter);
    }

    @Override
    public void handleResponse(Object o) {
        if (progress != null)
            progress.setVisibility(View.GONE);

        if (o instanceof JSONObject) {

            parseJsonObjectResponse(o);

        }
    }


    private void parseJsonObjectResponse(Object o) {
        JSONObject jsonObject = (JSONObject) o;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(HackerNewsConstant.KIDS);



                kidStoriesList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    int id = jsonArray.getInt(i);

                    TopStories topStories = new TopStories(id + "");
                    kidStoriesList.add(topStories);
                }
                adapter.clear();
                adapter.addAll(kidStoriesList);
                adapter.notifyDataSetChanged();



        } catch (JSONException e) {
            Toast.makeText(getActivity(), "No value for the attribute Kid", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void handleError(Object o) {
        if (progress != null)
            progress.setVisibility(View.GONE);

        Toast.makeText(getActivity(), "Network error!!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
