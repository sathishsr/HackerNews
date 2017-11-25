package com.hackers.hakernews.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hackers.core.BaseFragment;
import com.hackers.core.adapter.CommonRecyclerAdapter;
import com.hackers.core.HackersConstant.HackerNewsConstant;
import com.hackers.core.adapter.PopulationListener;
import com.hackers.core.models.TopStories;
import com.hackers.core.views.RecyclerTouchListener;
import com.hackers.hakernews.MainActivity;
import com.hackers.hakernews.R;
import com.hackers.hakernews.details.DetailsFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by SR on 25/11/17.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private CommonRecyclerAdapter<TopStories> adapter;
    @BindView(R.id.progress)
    ProgressBar progress;
    Unbinder unbinder;
    @BindView(R.id.toolBarTitle)
    TextView toolBarTitle;
    private ArrayList<TopStories> topStoriesList = new ArrayList<>();

    @Override
    protected int initViews() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initialize() {

        toolBarTitle.setText(getString(R.string.TITLE_HOME));
        setUpAdapter();
        dataManager.sendJsonArrayRequest(getActivity(), HackerNewsConstant.HOME_URL);

        recyclerView.addOnItemTouchListener(recyclerTouchListener);

    }

    RecyclerTouchListener recyclerTouchListener = new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
        @Override
        public void onClick(View view, int position) {
            /*
             * Bundle the clicked object and will be sent to ActionWatchListFragment.
             */
            Bundle bundle = new Bundle();
            bundle.putSerializable(HackerNewsConstant.TOP_STORY, topStoriesList.get(position));
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(bundle);
            ((MainActivity) getActivity()).replaceFragment(detailsFragment);

        }

        @Override
        public void onLongClick(View view, int position) {

        }
    });

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
        if (o instanceof JSONArray) {
            Log.d("RESPONSE,", o.toString());
            JSONArray jsonArray = (JSONArray) o;

            try {

                topStoriesList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    int id = jsonArray.getInt(i);

                    TopStories topStories = new TopStories(id + "");
                    topStoriesList.add(topStories);
                }
                adapter.clear();
                adapter.addAll(topStoriesList);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void handleError(Object o) {
        if (progress != null)
            progress.setVisibility(View.GONE);

        Toast.makeText(getActivity(), "Network Error!!", Toast.LENGTH_SHORT).show();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
