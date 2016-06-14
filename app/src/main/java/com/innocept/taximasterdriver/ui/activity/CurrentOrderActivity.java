package com.innocept.taximasterdriver.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.innocept.taximasterdriver.ApplicationPreferences;
import com.innocept.taximasterdriver.R;
import com.innocept.taximasterdriver.model.Communicator;
import com.innocept.taximasterdriver.model.foundation.Order;
import com.innocept.taximasterdriver.model.foundation.State;
import com.innocept.taximasterdriver.presenter.CurrentOrderPresenter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by dulaj on 5/28/16.
 */
public class CurrentOrderActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String DEBUG_TAG = CurrentOrderActivity.class.getSimpleName();

    CurrentOrderPresenter currentOrderPresenter;
    private Toolbar toolbar;

    public LatLng mLastLocationLatLng;
    private GoogleApiClient mGoogleApiClient;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 0;
    private static int FASTEST_INTERVAL = 0;

    //Minimum distance between two location updates in meters.
    private static int DISPLACEMENT = 0;

    private GoogleMap mMap;
    private UiSettings mapUiSettings;
    private Polyline[] polyLines; // 0 - start to end, 1 - driver to start

    private Order order;
    private LatLng startLatLng;
    private LatLng endLatLng;
    private Marker startMarker;
    private Marker endMarker;
    private float DEFAULT_ZOOM_LEVEL = 11f;

    private Menu menu;
    private boolean isPickedUpCustomer = false;
    private boolean isStopVisible = false;

    private TextInputLayout inputLayoutDistance;
    private TextInputLayout inputLayoutFare;
    private EditText editTextDistance;
    private EditText editTexFare;
    private AlertDialog finishedOrderAlertDialog;

    public CurrentOrderActivity() {
        inputLayoutDistance = null;
        inputLayoutFare = null;
        editTextDistance = null;
        editTexFare = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        if (ApplicationPreferences.getKeepScreenOn()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Going for hire");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (currentOrderPresenter == null) {
            currentOrderPresenter = CurrentOrderPresenter.getInstance();
        }
        currentOrderPresenter.setView(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setMenuVisibility(true);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");
        startLatLng = new LatLng(order.getOriginCoordinates().getLatitude(), order.getOriginCoordinates().getLongitude());
        endLatLng = new LatLng(order.getDestinationCoordinates().getLatitude(), order.getDestinationCoordinates().getLongitude());

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        polyLines = new Polyline[2];
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        mMap.setMyLocationEnabled(true);
        mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
        mapUiSettings.setRotateGesturesEnabled(true);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                moveAndAnimateCamera(mLastLocationLatLng, DEFAULT_ZOOM_LEVEL);
                return true;
            }
        });

        if (State.valueOf(ApplicationPreferences.getCurrentState()) == State.IN_HIRE) {
            setEndMarker();
            toolbar.setTitle("On hire");
            isPickedUpCustomer = true;
            isStopVisible = true;
            invalidateOptionsMenu();
            updateRoutes();
        } else {
            setStartMarker();
            setEndMarker();
            moveAndAnimateCamera(startLatLng, DEFAULT_ZOOM_LEVEL);
            plotRoute(startLatLng, endLatLng, getResources().getColor(R.color.colorPrimary), 0);
        }
    }

    private Marker setMarker(LatLng latLng, String title) {
        return mMap.addMarker(new MarkerOptions().position(latLng).title(title));
    }

    private void setStartMarker() {
        startMarker = mMap.addMarker(new MarkerOptions().position(startLatLng).title(order.getOrigin()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_street_view)).snippet("Passenger"));
    }

    private void setEndMarker() {
        endMarker = mMap.addMarker(new MarkerOptions().position(endLatLng).title(order.getDestination()).snippet("Destination"));
    }

    private void moveAndAnimateCamera(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void plotRoute(LatLng startLatLng, LatLng endLatLng, final int routeColor, final int polylineType) {
        String serverKey = "AIzaSyBV39bSkS5_GjTJcrN4Jd_lhEbz1DnOCNU";
        GoogleDirection.withServerKey(serverKey)
                .from(startLatLng)
                .to(endLatLng)
                .transportMode(TransportMode.DRIVING)
                .language(Language.ENGLISH)
                .unit(Unit.METRIC)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.getStatus().equals(RequestResult.OK)) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> pointList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(CurrentOrderActivity.this, pointList, 5, routeColor);
                            polyLines[polylineType] = mMap.addPolyline(polylineOptions);

                        } else if (direction.getStatus().equals(RequestResult.NOT_FOUND)) {
                            Toast.makeText(CurrentOrderActivity.this, "No route cannot be found.", Toast.LENGTH_LONG);
                        } else {
                            Toast.makeText(CurrentOrderActivity.this, "Error occurred. Contact developers if error continues.", Toast.LENGTH_LONG);
                            Log.e(DEBUG_TAG, "Error in while getting directions: " + direction.getStatus());
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.e(DEBUG_TAG, "Error in while getting directions: " + t.toString());
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateLastKnownLocation();
        updateRoutes();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_current_order, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                goBack();
                break;
            case R.id.action_call:
                callCustomer();
                break;
            case R.id.action_pick_customer:
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setMessage("Pick up the customer?");
                alertBuilder.setNegativeButton("No", null);
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AsyncTask<Void, Void, Void>() {

                            boolean result;

                            @Override
                            protected Void doInBackground(Void... params) {
                                result = new Communicator().updateState(State.IN_HIRE);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                if (result) {
                                    toolbar.setTitle("On hire");
                                    menu.findItem(R.id.action_pick_customer).setVisible(false);
                                    isPickedUpCustomer = true;
                                    isStopVisible = true;
                                    invalidateOptionsMenu();
                                    startMarker.remove();
                                    updateRoutes();
                                }
                            }
                        }.execute();
                    }
                });
                alertBuilder.create().show();
                break;

            case R.id.action_update_stop:
                LayoutInflater inflater = LayoutInflater.from(this);
                View alertDialogView = inflater.inflate(R.layout.inflater_alert_dialog_finish_order, null);

                inputLayoutDistance = (TextInputLayout) alertDialogView.findViewById(R.id.input_layout_distance);
                inputLayoutFare = (TextInputLayout) alertDialogView.findViewById(R.id.input_layout_fare);
                editTextDistance = (EditText) alertDialogView.findViewById(R.id.edit_distance);
                editTexFare = (EditText) alertDialogView.findViewById(R.id.edit_fare);

                final AlertDialog.Builder enterDetailsDialogBuilder = new AlertDialog.Builder(this);
                enterDetailsDialogBuilder.setView(alertDialogView);
                enterDetailsDialogBuilder.setCancelable(false);
                enterDetailsDialogBuilder.setTitle("Order summary");
                enterDetailsDialogBuilder.setPositiveButton("Ok", null);
                finishedOrderAlertDialog = enterDetailsDialogBuilder.create();
                finishedOrderAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button positiveButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(submitForm()){
                                    finishOrder();
                                    finishedOrderAlertDialog.dismiss();
                                }
                            }
                        });
                    }
                });

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Finish the hire?");
                alertDialogBuilder.setNegativeButton("No", null);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                new Communicator().updateState(State.AVAILABLE);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                finishedOrderAlertDialog.show();
                            }
                        }.execute();

                    }
                });
                alertDialogBuilder.create().show();
                break;

            case R.id.action_refresh:
                Toast.makeText(CurrentOrderActivity.this, "Updating routes", Toast.LENGTH_SHORT).show();
                updateRoutes();
                break;
        }

        return true;
    }

    private boolean submitForm() {
        if (!validateDistance()) {
            return false;
        }

        if (!validateFare()) {
            return false;
        }

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editTexFare.getWindowToken(), 0);
        return true;
    }

    private boolean validateDistance() {
        if (editTextDistance.getText().toString().trim().isEmpty()) {
            inputLayoutDistance.setError(getString(R.string.err_msg_distance));
            requestFocus(editTextDistance);
            return false;
        } else {
            inputLayoutDistance.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateFare() {
        if (editTexFare.getText().toString().trim().isEmpty()) {
            inputLayoutFare.setError(getString(R.string.err_msg_fare));
            requestFocus(editTexFare);
            return false;
        } else {
            inputLayoutFare.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_distance:
                    validateDistance();
                    break;
                case R.id.edit_fare:
                    validateFare();
                    break;
            }
        }
    }

    public void finishOrder() {
        new AsyncTask<Void, Void, Void>() {

            ProgressDialog progressDialog;
            boolean response;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(CurrentOrderActivity.this);
                progressDialog.setMessage("Finishing hire...");
                progressDialog.show();
                double distance = Double.parseDouble(editTextDistance.getText().toString());
                int fare = Integer.parseInt(editTexFare.getText().toString());

                order.setDistance(distance);
                order.setFare(fare);
                order.setEndTime(Calendar.getInstance().getTime());
            }

            @Override
            protected Void doInBackground(Void... params) {
                response = new Communicator().finishOrder(order);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                if (response) {
                    Intent intent = new Intent(CurrentOrderActivity.this, OrderListActivity.class);
                    intent.putExtra("finish", true);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CurrentOrderActivity.this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                }
            }

        }.execute();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem updateMenu = menu.findItem(R.id.action_pick_customer);
        MenuItem stopMenu = menu.findItem(R.id.action_update_stop);
        updateMenu.setVisible(!isPickedUpCustomer);
        stopMenu.setVisible(isStopVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    private void callCustomer() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order.getContact()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 100);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            callCustomer();
        }
    }

    private void updateRoutes() {
        if (polyLines[1] != null) {
            polyLines[1].remove();
        }
        if (polyLines[0] != null) {
            polyLines[0].remove();
        }
        if (!isPickedUpCustomer) {
            updateLastKnownLocation();
            if (mLastLocationLatLng != null) {
                plotRoute(mLastLocationLatLng, startLatLng, Color.RED, 1);
            }
            plotRoute(startLatLng, endLatLng, getResources().getColor(R.color.colorPrimary), 0);
            moveAndAnimateCamera(mLastLocationLatLng, DEFAULT_ZOOM_LEVEL);
        } else {
            updateLastKnownLocation();
            if (mLastLocationLatLng != null) {
                plotRoute(mLastLocationLatLng, endLatLng, getResources().getColor(R.color.colorPrimary), 0);
                moveAndAnimateCamera(mLastLocationLatLng, DEFAULT_ZOOM_LEVEL);
            }
        }
    }

    private void updateLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (lastLocation != null) {
            mLastLocationLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CurrentOrderActivity.this);
        builder.setMessage("Do you want to go back?\nResume is possible.");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(CurrentOrderActivity.this, OrderListActivity.class));
                finish();
            }
        });
        builder.create().show();
    }
}
