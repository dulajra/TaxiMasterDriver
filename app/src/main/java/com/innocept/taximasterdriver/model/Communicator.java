package com.innocept.taximasterdriver.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.innocept.taximasterdriver.ApplicationContext;
import com.innocept.taximasterdriver.ApplicationPreferences;
import com.innocept.taximasterdriver.model.foundation.Driver;
import com.innocept.taximasterdriver.model.foundation.Location;
import com.innocept.taximasterdriver.model.foundation.Order;
import com.innocept.taximasterdriver.model.foundation.State;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Dulaj on 14-Apr-16.
 */

public class Communicator {

    private final String DEBUG_TAG = Communicator.class.getSimpleName();

    private final String URL_ROOT = "http://taximaster.herokuapp.com";
//    private final String URL_ROOT = "http://92893741.ngrok.io";

    private final String URL_UPDATE_STATE = URL_ROOT + "/driver/update/state";
    private final String URL_UPDATE_LOCATION = URL_ROOT + "/driver/update/location";
    private final String URL_LOGIN = URL_ROOT + "/driver/login";
    private final String URL_LOGOUT = URL_ROOT + "/driver/logout";
    private final String URL_UPDATE_PASSWORD = URL_ROOT + "/driver/update/password";
    private final String URL_RESPOND_TO_NEW_ORDER = URL_ROOT + "/driver/order/respond";
    private final String URL_GET_ORDERS = URL_ROOT + "/driver/orders";
    private final String URL_FINISH_ORDER = URL_ROOT + "/driver/order/finish";

    public Communicator() {
    }

    public boolean updateState(State state) {
        ContentValues values = new ContentValues();
        values.put("id", ApplicationPreferences.getDriver().getId());
        values.put("stateId", state.getValue());
        String response = HTTPHandler.sendPOST(URL_UPDATE_STATE, values);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean result = jsonObject.getBoolean("success");
                if (result) {
                    ApplicationPreferences.setCurrentState(state);
                    Log.v(DEBUG_TAG, "Driver state update success");
                } else {
                    Log.w(DEBUG_TAG, "Driver state update failed");
                }
                return result;
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return false;
    }

    public boolean updateState(State state, int orderId) {
        ContentValues values = new ContentValues();
        values.put("id", ApplicationPreferences.getDriver().getId());
        values.put("stateId", state.getValue());
        values.put("orderId", orderId);
        String response = HTTPHandler.sendPOST(URL_UPDATE_STATE, values);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean result = jsonObject.getBoolean("success");
                if (result) {
                    ApplicationPreferences.setCurrentState(state);
                    Log.v(DEBUG_TAG, "Driver state update success");
                } else {
                    Log.w(DEBUG_TAG, "Driver state update failed");
                }
                return result;
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return false;
    }

    public boolean updateLocation(Location location) {
        ContentValues values = new ContentValues();
        values.put("id", ApplicationPreferences.getDriver().getId());
        values.put("latitude", location.getLatitude());
        values.put("longitude", location.getLongitude());
        String response = HTTPHandler.sendPOST(URL_UPDATE_LOCATION, values);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean result = jsonObject.getBoolean("success");
                if (!result) {
                    Log.v(DEBUG_TAG, "Driver location update success");
                } else {
                    Log.w(DEBUG_TAG, "Driver location update failed");
                }
                return result;
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return false;
    }

    public int login(String username, String password) {
        Driver driver = null;
        int resultCode = -1;
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("oneSignalUserId", ApplicationPreferences.getOneSignalUserId());
        String response = HTTPHandler.sendPOST(URL_LOGIN, values);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int result = jsonObject.getInt("success");
                switch (result) {
                    case 0:
                        driver = new Gson().fromJson(jsonObject.getJSONObject("driver").toString(), Driver.class);
                        ApplicationPreferences.saveDriver(driver);
                        resultCode = 0;
                        Log.v(DEBUG_TAG, "Login success");
                        break;
                    case 1:
                        resultCode = 1;
                        Log.w(DEBUG_TAG, "Username or password is incorrect");
                        break;
                }
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return resultCode;
    }

    public boolean logout() {
        int resultCode = -1;
        ContentValues values = new ContentValues();
        values.put("id", ApplicationPreferences.getDriver().getId());
        values.put("stateId", State.NOT_IN_SERVICE.getValue());
        values.put("latitude", 0);
        values.put("longitude", 0);
        String response = HTTPHandler.sendPOST(URL_LOGOUT, values);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean result = jsonObject.getBoolean("success");
                if (result) {
                    ApplicationPreferences.saveDriver(null);
                    Log.v(DEBUG_TAG, "Logout success");
                } else {
                    Log.w(DEBUG_TAG, "Logout failed");
                }
                return result;
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return false;
    }

    public boolean respondToNewOrder(int orderId, boolean isAccepted) {
        ContentValues values = new ContentValues();
        values.put("orderId", orderId);
        values.put("isAccepted", isAccepted);
        String response = HTTPHandler.sendGET(URL_RESPOND_TO_NEW_ORDER, values);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject.getBoolean("success");
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return false;
    }

    public List<Order> getOrders(int driverId, int type) {
        ContentValues values = new ContentValues();
        values.put("taxiDriverId", driverId);
        values.put("type", type);
        String response = HTTPHandler.sendGET(URL_GET_ORDERS, values);
        List<Order> orderList = new ArrayList<Order>();

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Order order = new Order();
                order.setId(jsonObject.getInt("id"));
                order.setOrigin(jsonObject.getString("origin"));
                order.setDestination(jsonObject.getString("destination"));
                try {
                    if (type == 0) {
                        order.setOriginCoordinates(new Location(jsonObject.getDouble("originLatitude"), jsonObject.getDouble("originLongitude")));
                        order.setDestinationCoordinates(new Location(jsonObject.getDouble("destinationLatitude"), jsonObject.getDouble("destinationLongitude")));
                        order.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(jsonObject.getString("time")));
                        order.setOrderState(Order.OrderState.valueOf(jsonObject.getString("state")));
                        order.setNote(jsonObject.getString("note"));
                        order.setCustomerId(jsonObject.getInt("customerId"));
                    } else if (type == 1) {
                        order.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(jsonObject.getString("startTime")));
                    }
                } catch (ParseException e) {
                    Log.e(DEBUG_TAG, "Error parsing date: " + e.toString());
                }
                order.setContact(jsonObject.getString("contact"));
                orderList.add(order);
            }
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "Error converting to json array " + e.toString());
        } catch (NullPointerException e) {
            Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
        }
        return orderList;
    }

    public boolean finishOrder(Order order) {
        ContentValues values = new ContentValues();
        values.put("id", order.getId());
        values.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(order.getTime()));
        values.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(order.getEndTime()));
        values.put("origin", order.getOrigin());
        values.put("originLatitude", order.getOriginCoordinates().getLatitude());
        values.put("originLongitude", order.getOriginCoordinates().getLongitude());
        values.put("destination", order.getDestination());
        values.put("destinationLatitude", order.getDestinationCoordinates().getLatitude());
        values.put("destinationLongitude", order.getDestinationCoordinates().getLongitude());
        values.put("customerId", order.getCustomerId());
        values.put("distance", order.getDistance());
        values.put("contact", order.getContact());
        values.put("fare", order.getFare());
        values.put("taxiDriverId", ApplicationPreferences.getDriver().getId());
        String response = HTTPHandler.sendGET(URL_FINISH_ORDER, values);

        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getBoolean("success");
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, e.toString());
        } catch (NullPointerException e) {
            Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
        }

        return false;
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) ApplicationContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public int changePassword(String username, String oldPassword, String newPassword) {
        int resultCode = -1;
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("oldPassword", oldPassword);
        values.put("newPassword", newPassword);
        String response = HTTPHandler.sendPOST(URL_UPDATE_PASSWORD, values);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                resultCode = jsonObject.getInt("success");
                switch (resultCode) {
                    case 0:
                        Log.v(DEBUG_TAG, "Password change success");
                        break;
                    case 1:
                        Log.w(DEBUG_TAG, "Old password incorrect");
                        break;
                }
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return resultCode;
    }
}

