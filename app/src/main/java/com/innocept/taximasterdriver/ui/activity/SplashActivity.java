package com.innocept.taximasterdriver.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView imageView = new ImageView(SplashActivity.this);
        imageView.setBackgroundResource(R.drawable.ic_splash);
        imageView.setAdjustViewBounds(true);
        setContentView(imageView);

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

        timer.schedule(timerTask, 1500);
    }
}

