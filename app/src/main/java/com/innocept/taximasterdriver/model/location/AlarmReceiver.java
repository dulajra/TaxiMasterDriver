package com.innocept.taximasterdriver.model.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Dulaj on 16-Apr-16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, GPSLocationUpdateService.class);
        context.startService(service);
    }
}
