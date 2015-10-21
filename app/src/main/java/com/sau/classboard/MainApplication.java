package com.sau.classboard;

import android.app.Application;

import com.magnet.mmx.client.api.MMX;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by saurabh on 2015-10-17.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MMX.init(this, R.raw.classboard);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
