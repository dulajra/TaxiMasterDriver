package com.innocept.taximasterdriver.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.innocept.taximasterdriver.ApplicationPreferences;
import com.innocept.taximasterdriver.model.Communicator;
import com.innocept.taximasterdriver.model.foundation.Order;
import com.innocept.taximasterdriver.ui.activity.OrderListActivity;
import com.innocept.taximasterdriver.ui.fragment.OrderFragment;

import java.util.List;

/**
 * Created by dulaj on 5/27/16.
 */

/**
 * Presenter if OrderListActivity
 */
public class OrderListPresenter {

    private final String DEBUG_TAG = OrderListPresenter.class.getSimpleName();

    private static OrderListPresenter instance = null;
    private OrderListActivity view;

    private OrderFragment onGoingFragment;
    private OrderFragment finishedOrderFragment;

    private OrderListPresenter() {
    }

    public static OrderListPresenter getInstance(){
        if(instance==null){
            instance = new OrderListPresenter();
        }
        return instance;
    }

    public void setView(OrderListActivity view) {
        this.view = view;
        this.onGoingFragment = view.onGoingOrderFragment;
        this.finishedOrderFragment = view.finishedOrderFragment;
    }

    public void getOrderList(final int type){

        new AsyncTask<Void, Void, Void>(){

            Communicator communicator;
            List<Order> orderList;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                communicator = new Communicator();

                view.lockUI();

                if(type == 0){
                    onGoingFragment.lockUI();
                }
                else if(type == 1){
                    finishedOrderFragment.lockUI();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                orderList = communicator.getOrders(ApplicationPreferences.getDriver().getId(), type);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                view.releaseUI();

                if(type == 0){
                    onGoingFragment.setData(orderList);
                    onGoingFragment.releaseUI();
                }
                else if(type == 1){
                    finishedOrderFragment.setData(orderList);
                    finishedOrderFragment.releaseUI();
                }
            }

        }.execute();
    }

}
