package com.sau.classboard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sau.classboard.R;
import com.sau.classboard.utility.Constants;

/**
 * Created by saurabh on 2015-10-17.
 */
public class LaunchScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchScreenActivity.this, HomeActivity.class));
            }
        }, Constants.SPLASH_TIME_OUT);

    }
}
