package com.innocept.taximasterdriver.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.innocept.taximasterdriver.R;
import com.innocept.taximasterdriver.model.foundation.Order;
import com.innocept.taximasterdriver.ui.activity.OrderListActivity;
import com.innocept.taximasterdriver.ui.adapter.OrderListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dulaj on 5/24/16.
 */
public class FinishedOrderFragment extends Fragment {

    private final String DEBUG_TAG = FinishedOrderFragment.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressBar progressBar;

    private List<Order> dataSet;

    public FinishedOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataSet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_list, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_order_list);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_order_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OrderListAdapter(getActivity(), dataSet);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((OrderListActivity) getActivity()).submit();
            }
        });

        if (getActivity().getIntent().getBooleanExtra("finish", false)) {
            ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
            viewPager.setCurrentItem(1);
            progressBar.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void initDataSet() {
        this.dataSet = new ArrayList<Order>();
    }

    public void setData(List<Order> dataSet) {
        this.dataSet = dataSet;
        displayData();
    }

    public void displayData() {
        adapter = new OrderListAdapter(getActivity(), this.dataSet);
        recyclerView.setAdapter(adapter);
    }

    public void lockUI() {
        clearViews();
        swipeRefreshLayout.setRefreshing(true);
//        progressBar.setVisibility(View.VISIBLE);
    }

    public void clearViews() {
        initDataSet();
        displayData();
    }

    public void releaseUI() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

}
