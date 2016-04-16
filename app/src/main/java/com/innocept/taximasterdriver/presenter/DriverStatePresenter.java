package com.innocept.taximasterdriver.presenter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.innocept.taximasterdriver.ApplicationPreferences;
import com.innocept.taximasterdriver.model.Communicator;
import com.innocept.taximasterdriver.model.foundation.State;
import com.innocept.taximasterdriver.model.location.AlarmReceiver;
import com.innocept.taximasterdriver.ui.activities.DriverStateActivity;

/**
 * Created by Dulaj on 14-Apr-16.
 */
public class DriverStatePresenter {

    private static DriverStatePresenter instance = null;
    private DriverStateActivity view;

    private DriverStatePresenter() {
    }

    public static DriverStatePresenter getInstance(){
        if(instance==null){
            instance = new DriverStatePresenter();
        }
        return instance;
    }

    public void setView(DriverStateActivity view) {
        this.view = view;
    }

    public void changeState(final State state){
        new AsyncTask<Void, Void, Boolean>(){

            Communicator communicator;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                communicator = new Communicator();
                view.lockUI();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                boolean response = communicator.updateState(state);
                return response;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                super.onPostExecute(b);
                view.releaseUI(state, b);
                setLocationUpdates(state);
            }
        }.execute();
    }

    public void setLocationUpdates(State state){
        int interval = ApplicationPreferences.getUpdateInterval(state);
        switch (state){
            case NOT_IN_SERVICE:
                stopLocationAlarm();
                view.setLocationMode(false);
                break;
            default:
                startLocationAlarm(interval);
                view.setLocationMode(true);
        }
    }

    private void startLocationAlarm(int interval){
        AlarmManager alarmMgr = (AlarmManager)view.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(view, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(view, 0, intent, 0);
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000 ,1000 * interval, alarmIntent);
    }

    private void stopLocationAlarm(){
        AlarmManager alarmMgr = (AlarmManager)view.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(view, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(view, 0, intent, 0);
        alarmMgr.cancel(alarmIntent);
    }

}
