package com.innocept.taximasterdriver;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.innocept.taximasterdriver.model.foundation.Driver;
import com.innocept.taximasterdriver.model.foundation.State;

/**
 * Created by Dulaj on 16-Apr-16.
 */

/**
 * Actions regarding all the user preferences of the application.
 */
public class ApplicationPreferences {

    private static ApplicationPreferences instance = null;
    private static SharedPreferences sharedPreferences;

    private ApplicationPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.getContext());
    }

    private static ApplicationPreferences init(){
        if(instance==null){
            instance=new ApplicationPreferences();
        }
        return instance;
    }

//    All getters go here
    public static Driver getDriver(){
        init();
        return new Gson().fromJson(sharedPreferences.getString("driver", null), Driver.class);
    }

//    Interval is in seconds
    public static int getUpdateInterval(State state){
        init();
        return sharedPreferences.getInt(state.toString(), 10);
    }

    public static String getCurrentState(){
        init();
        return sharedPreferences.getString("state", State.NOT_IN_SERVICE.toString());
    }

//    All setters go here

    public static void saveDriver(Driver driver){
        init();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("driver", new Gson().toJson(driver).toString());
        editor.commit();
    }

    public static void setCurrentState(State state){
        init();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("state", state.toString());
        editor.commit();
    }

    public static void setOneSignalUserId(String id){
        init();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("oneSignalUserId", id);
        editor.commit();
    }

    public static String getOneSignalUserId(){
        init();
        return sharedPreferences.getString("oneSignalUserId", null);
    }

}
