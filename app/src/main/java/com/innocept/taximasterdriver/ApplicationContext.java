package com.innocept.taximasterdriver;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.innocept.taximasterdriver.model.foundation.Location;
import com.innocept.taximasterdriver.model.foundation.Order;
import com.innocept.taximasterdriver.ui.activity.NewOrderActivity;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

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

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new NotificationHandler())
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                ApplicationPreferences.setOneSignalUserId(userId);
            }
        });

        OneSignal.sendTag("userType", "taxiDriver");
    }

    public static Context getContext(){
        return context;
    }

//    This fires when a notification is opened by tapping on it or one is received while the app is running.
    private class NotificationHandler implements OneSignal.NotificationOpenedHandler {

        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            try {
                if (additionalData != null) {
                   if(additionalData.getString("notificationType").equals("newOrder")){
                       Intent intent = new Intent(context, NewOrderActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

                       Order order = new Order();
                       order.setId(additionalData.getInt("id"));
                       order.setOrigin(additionalData.getString("origin"));
                       order.setOriginCoordinates(new Location(Double.parseDouble(additionalData.getString("originLatitude")),Double.parseDouble(additionalData.getString("originLongitude"))));
                       order.setDestination(additionalData.getString("destination"));
                       order.setDestinationCoordinates(new Location(Double.parseDouble(additionalData.getString("destinationLatitude")),Double.parseDouble(additionalData.getString("destinationLongitude"))));
                       order.setNote(additionalData.getString("note"));
                       order.setContact(additionalData.getString("contact"));
                       order.setTime(new SimpleDateFormat("yyyy-MM-dd HH-mm").parse(additionalData.getString("time")));

                       intent.putExtra("order", order);
                       startActivity(intent);
                   }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

        }
    }
}
