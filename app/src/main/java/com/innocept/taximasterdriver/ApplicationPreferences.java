package com.innocept.taximasterdriver;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.innocept.taximasterdriver.model.foundation.State;

/**
 * Created by Dulaj on 16-Apr-16.
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

    public static int getDriverID(){
        init();
        return sharedPreferences.getInt("driver_id", 1);
    }

//    Interval is in seconds
    public static int getUpdateInterval(State state){
        init();
        return sharedPreferences.getInt(state.toString(), 10);
    }
}
