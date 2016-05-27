package com.innocept.taximasterdriver.presenter;

import android.os.AsyncTask;

import com.innocept.taximasterdriver.model.Communicator;
import com.innocept.taximasterdriver.ui.activity.NewOrderActivity;

/**
 * Created by dulaj on 5/27/16.
 */
public class NewOrderPresenter {

    private static NewOrderPresenter instance = null;
    private NewOrderActivity view;

    private NewOrderPresenter() {
    }

    public static NewOrderPresenter getInstance(){
        if(instance==null){
            instance = new NewOrderPresenter();
        }
        return instance;
    }

    public void setView(NewOrderActivity view) {
        this.view = view;
    }

    public void respondToNewOrder(final int orderId, final boolean isAccepted){
        new AsyncTask<Void, Void, Boolean>(){

            Communicator communicator;
            boolean response;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                communicator = new Communicator();
                view.showProgressDialog("Please wait...");
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                response = communicator.respondToNewOrder(orderId, isAccepted);
                return response;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                view.closeProgressDialog();
                if(response){
                    view.onSuccess();
                }
                else{
                    view.onError();
                }
            }
        }.execute();
    }
}
