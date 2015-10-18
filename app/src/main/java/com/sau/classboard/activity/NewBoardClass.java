package com.sau.classboard.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sau.classboard.R;
import com.sau.classboard.utility.Constants;

/**
 * Created by saurabh on 2015-10-18.
 */
public class NewBoardClass extends AppCompatActivity {

    private Toolbar toolbar;
    private Context context;

    private RelativeLayout lytHeader;
    private LinearLayout lytDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_board);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        lytHeader = (RelativeLayout) findViewById(R.id.layout_header);
        lytDescription = (LinearLayout) findViewById(R.id.layout_board_description);

        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.requestLayout();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                expandView(toolbar);
            }
        }, Constants.REVEAL_TIME);
        setUpToolBar();
    }

    private void expandView(final View v) {
        v.measure(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.getLayoutParams().height = Toolbar.LayoutParams.WRAP_CONTENT;
                    showChildElements();
                }
                else{
                    v.getLayoutParams().height =(int)(targetHeight * interpolatedTime);
                }
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration(((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)) * 3);
        v.startAnimation(a);
    }

    private void showChildElements(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lytHeader.setVisibility(View.VISIBLE);

                Animation animation =  AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
                lytDescription.setAnimation(animation);
                lytDescription.animate();
                lytDescription.setVisibility(View.VISIBLE);
            }
        }, 200);
    }

    private void setUpToolBar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("New Board");
        }
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
