package com.innocept.taximasterdriver.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.innocept.taximasterdriver.R;
import com.innocept.taximasterdriver.model.foundation.Order;
import com.innocept.taximasterdriver.presenter.NewOrderPresenter;

import java.text.SimpleDateFormat;

/**
 * Created by dulaj on 5/25/16.
 */
public class NewOrderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private float DEFAULT_ZOOM_LEVEL = 11f;

    NewOrderPresenter newOrderPresenter;

    private Toolbar toolbar;

    private GoogleMap mMap;
    private TextView textViewFromTo;
    private TextView textViewTime;
    private TextView textViewContact;
    private TextView textViewNote;

    ProgressDialog progressDialog;
    MediaPlayer player;

    private Order order;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Newly received order");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (newOrderPresenter == null) {
            newOrderPresenter = NewOrderPresenter.getInstance();
        }
        newOrderPresenter.setView(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textViewFromTo = (TextView) findViewById(R.id.text_new_order_from_to);
        textViewTime = (TextView) findViewById(R.id.text_new_order_time);
        textViewContact = (TextView) findViewById(R.id.text_new_order_contact);
        textViewNote = (TextView) findViewById(R.id.text_new_order_note);

        intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");
        textViewFromTo.setText(order.getOrigin() + " to " + order.getDestination());
        textViewTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(order.getTime()));
        textViewContact.setText(order.getContact());
        textViewNote.setText(order.getNote());
    }

    public void playOrTopSound(boolean isPlay) {
        if (player == null) {
            player = MediaPlayer.create(this,
                    Settings.System.DEFAULT_RINGTONE_URI);
            player.setLooping(true);
        }
        if (isPlay) {
            player.start();
        } else {
            player.stop();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(new LatLng(order.getOriginCoordinates().getLatitude(), order.getOriginCoordinates().getLongitude())).title(order.getOrigin()).snippet("Start").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(order.getDestinationCoordinates().getLatitude(), order.getDestinationCoordinates().getLongitude())).title(order.getDestination()).snippet("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(order.getOriginCoordinates().getLatitude(), order.getOriginCoordinates().getLongitude()), DEFAULT_ZOOM_LEVEL));

        if (!intent.getBooleanExtra("isSilence", false)) {
            playOrTopSound(true);
            showSilenceDialog();
        }
    }

    public void onAcceptPressed(View view) {
        newOrderPresenter.respondToNewOrder(order.getId(), true);
    }

    public void onRejectPressed(View view) {
        newOrderPresenter.respondToNewOrder(order.getId(), false);
    }

    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void closeProgressDialog() {
        progressDialog.dismiss();
    }

    public void onError() {
        Toast.makeText(NewOrderActivity.this, getResources().getString(R.string.message_new_order_response_failed), Toast.LENGTH_SHORT).show();
    }

    public void onSuccess(boolean isAccepted) {
        if(isAccepted){
            startActivity(new Intent(NewOrderActivity.this, OrderListActivity.class));
        }
        else{
            startActivity(new Intent(NewOrderActivity.this, DriverStateActivity.class));
        }
        this.finish();
    }

    public void showSilenceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewOrderActivity.this);
        builder.setMessage("New hire received.");
        builder.setCancelable(false);
        builder.setPositiveButton("Silence", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playOrTopSound(false);
            }
        });
        builder.create().show();
    }
}
