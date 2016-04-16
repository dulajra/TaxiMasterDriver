package com.innocept.taximasterdriver.ui.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.innocept.taximasterdriver.R;
import com.innocept.taximasterdriver.model.foundation.State;
import com.innocept.taximasterdriver.presenter.DriverStatePresenter;

public class DriverStateActivity extends AppCompatActivity {

    DriverStatePresenter driverStatePresenter;

    Button buttonAvailable;
    Button buttonGoingForHire;
    Button buttonInHire;
    Button buttonNotInService;
    Switch switchLocationUpdates;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_state);

        buttonAvailable = (Button) findViewById(R.id.button_state_available);
        buttonGoingForHire = (Button) findViewById(R.id.button_state_going_for_hire);
        buttonInHire = (Button) findViewById(R.id.button_state_in_hire);
        buttonNotInService = (Button) findViewById(R.id.button_state_not_in_service);
        switchLocationUpdates = (Switch)findViewById(R.id.switch_location_updates);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_update_state);

        if (driverStatePresenter == null) {
            driverStatePresenter = DriverStatePresenter.getInstance();
        }
        driverStatePresenter.setView(this);
    }

    public void stateChangePressed(View view) {
        switch (view.getId()) {
            case R.id.button_state_available:
                driverStatePresenter.changeState(State.AVAILABLE);
                break;
            case R.id.button_state_going_for_hire:
                driverStatePresenter.changeState(State.GOING_FOR_HIRE);
                break;
            case R.id.button_state_in_hire:
                driverStatePresenter.changeState(State.IN_HIRE);
                break;
            case R.id.button_state_not_in_service:
                driverStatePresenter.changeState(State.NOT_IN_SERVICE);
                break;
        }
    }

    public void lockUI(){
        progressBar.setVisibility(View.VISIBLE);
        buttonAvailable.setClickable(false);
        buttonGoingForHire.setClickable(false);
        buttonInHire.setClickable(false);
        buttonNotInService.setClickable(false);
        switchLocationUpdates.setClickable(false);
    }

    public void releaseUI(State state, boolean response) {
        progressBar.setVisibility(View.GONE);
        buttonAvailable.setClickable(true);
        buttonGoingForHire.setClickable(true);
        buttonInHire.setClickable(true);
        buttonNotInService.setClickable(true);
        switchLocationUpdates.setClickable(true);

        if(response){
            switch (state) {
                case AVAILABLE:
                    buttonAvailable.setEnabled(false);
                    buttonGoingForHire.setEnabled(true);
                    buttonInHire.setEnabled(true);
                    buttonNotInService.setEnabled(true);
                    break;
                case GOING_FOR_HIRE:
                    buttonAvailable.setEnabled(true);
                    buttonGoingForHire.setEnabled(false);
                    buttonInHire.setEnabled(true);
                    buttonNotInService.setEnabled(true);
                    break;
                case IN_HIRE:
                    buttonAvailable.setEnabled(true);
                    buttonGoingForHire.setEnabled(true);
                    buttonInHire.setEnabled(false);
                    buttonNotInService.setEnabled(true);
                    break;
                case NOT_IN_SERVICE:
                    buttonAvailable.setEnabled(true);
                    buttonGoingForHire.setEnabled(true);
                    buttonInHire.setEnabled(true);
                    buttonNotInService.setEnabled(false);
                    break;
            }
        }
        else{
            Toast.makeText(DriverStateActivity.this, getResources().getString(R.string.message_driver_update_failed), Toast.LENGTH_SHORT).show();
        }
    }

    public  void setLocationMode(boolean isOn){
        switchLocationUpdates.setChecked(isOn);
    }

}