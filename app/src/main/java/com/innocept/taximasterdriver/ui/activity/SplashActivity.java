package com.innocept.taximasterdriver.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import com.innocept.taximasterdriver.ApplicationPreferences;
import com.innocept.taximasterdriver.R;
/**
 * Created by Dulaj on 16-Apr-16.
 */
public class SplashActivity extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent;
                if(ApplicationPreferences.getDriver()!=null){
                    intent = new Intent(SplashActivity.this, DriverStateActivity.class);
                }
                else{
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };

        timer.schedule(timerTask, 2000);
    }
}

