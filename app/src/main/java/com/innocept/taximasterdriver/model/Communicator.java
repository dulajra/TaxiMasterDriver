package com.innocept.taximasterdriver.model;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.drive.Drive;
import com.google.gson.Gson;
import com.innocept.taximasterdriver.ApplicationContext;
import com.innocept.taximasterdriver.ApplicationPreferences;
import com.innocept.taximasterdriver.model.foundation.Driver;
import com.innocept.taximasterdriver.model.foundation.Location;
import com.innocept.taximasterdriver.model.foundation.State;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Dulaj on 14-Apr-16.
 */

public class Communicator{

    private final String TAG = Communicator.class.getSimpleName();

    private final String URL_ROOT = "http://be963632.ngrok.io";
    private final String URL_UPDATE_STATE = URL_ROOT + "/driver/update/state";
    private final String URL_UPDATE_LOCATION = URL_ROOT + "/driver/update/location";
    private final String URL_LOGIN = URL_ROOT + "/driver/login";

    public Communicator() {
    }

    public boolean updateState(State state) {
        ContentValues values = new ContentValues();
        values.put("id", ApplicationPreferences.getDriver().getId());
        values.put("stateId", state.getValue());
        JSONObject response = HTTPHandler.sendPOST(URL_UPDATE_STATE, values);
        if (response != null) {
            try {
                boolean result = response.getBoolean("success");
                if(result){
                    ApplicationPreferences.setCurrentState(state);
                    Log.i(TAG, "Driver state update success");
                }
                else{
                    Log.i(TAG, "Driver state update failed");
                }
                return result;
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean updateLocation(Location location) {
        ContentValues values = new ContentValues();
        values.put("id", ApplicationPreferences.getDriver().getId());
        values.put("latitude", location.getLatitude());
        values.put("longitude", location.getLongitude());
        JSONObject response = HTTPHandler.sendPOST(URL_UPDATE_LOCATION, values);
        if (response != null) {
            try {
                boolean result = response.getBoolean("success");
                if(result){
                    Log.i(TAG, "Driver location update success");
                }
                else{
                    Log.i(TAG, "Driver location update failed");
                }
                return result;
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        } else {
            return false;
        }
    }

    public int login(String username, String password) {
        Driver driver = null;
        int resultCode = -1;
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        JSONObject response = HTTPHandler.sendPOST(URL_LOGIN, values);
        if (response != null) {
            try {
                int result = response.getInt("success");
                switch (result){
                    case 0:
                        driver = new Gson().fromJson(response.getJSONObject("driver").toString(), Driver.class);
                        ApplicationPreferences.saveDriver(driver);
                        resultCode = 0;
                        Log.i(TAG, "Login success");
                        break;
                    case 1:
                        resultCode = 1;
                        Log.i(TAG, "Username or password is incorrect");
                        break;
                    case 2:
                        resultCode = 2;
                        Log.i(TAG, "Username not exists");
                        break;
                }
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
            }
        }
        return resultCode;
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) ApplicationContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}

