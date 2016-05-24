package com.innocept.taximasterdriver;

import android.app.Application;
import android.content.Context;

/**
 * Created by Dulaj on 14-Apr-16.
 */

/**
 * ApplicationContext is used to get the context of the application from any where.
 */
public class ApplicationContext extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
