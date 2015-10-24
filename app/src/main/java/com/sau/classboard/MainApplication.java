package com.sau.classboard;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.magnet.mmx.client.api.MMX;

import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.RushCore;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by saurabh on 2015-10-17.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext());
        RushCore.initialize(config);

        //Check if we are on pre marshmallow device
        if(Build.VERSION.SDK_INT < 23){
            MMX.init(this, R.raw.classboard);
            MMX.registerWakeupBroadcast(new Intent("NEW_MESSAGE"));
        }

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build());
    }


}
