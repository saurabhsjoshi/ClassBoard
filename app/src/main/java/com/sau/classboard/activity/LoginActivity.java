package com.sau.classboard.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sau.classboard.R;
import com.sau.classboard.utility.Constants;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by saurabh on 2015-10-17.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Toolbar toolbar;
    private ProgressBar progressBar;

    private Button loginButton;
    private Button exitButton;

    private TextView lblAppName;
    private TextView lblNewAccount;
    private TextView lblLoginTitle;

    private LinearLayout lytCredentials;
    private LinearLayout lytUserType;

    private EditText txtEmail;
    private EditText txtPassword;

    private TextInputLayout txtEmailHolder;
    private TextInputLayout txtPasswordHolder;

    private enum STATES { LOGIN_SCREEN, NEW_ACCOUNT, TYPE_SELECT, LOGGING_IN }

    private STATES state = STATES.LOGIN_SCREEN;

    // Student = 0 , Teacher = 1
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    private void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbar);

        lytCredentials = (LinearLayout) findViewById(R.id.layout_credentials);
        lytUserType = (LinearLayout) findViewById(R.id.layout_user_type);

        loginButton =  (Button) findViewById(R.id.loginButton);
        exitButton = (Button) findViewById(R.id.exitButton);

        loginButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);

        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);

        txtEmailHolder = (TextInputLayout) findViewById(R.id.txt_email_holder);
        txtPasswordHolder = (TextInputLayout) findViewById(R.id.txt_password_holder);

        lblNewAccount = (TextView) findViewById(R.id.lbl_new_account);
        lblNewAccount.setOnClickListener(this);

        lblLoginTitle = (TextView) findViewById(R.id.lbl_login_title);

        lblAppName = (TextView) findViewById(R.id.loginSupportText);

        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exitButton:
                if(state == STATES.LOGIN_SCREEN)
                    finish();
                else if(state == STATES.NEW_ACCOUNT){
                    state = STATES.LOGIN_SCREEN;
                    switchToSignIn();
                }
                break;
            case R.id.loginButton:
                if(state == STATES.LOGIN_SCREEN && checkInput())
                    startActivity(new Intent(this, HomeActivity.class));
                else if(state == STATES.NEW_ACCOUNT && checkInput()){
                    state = STATES.TYPE_SELECT;
                    switchToTypeSelect();
                }
                break;
            case R.id.lbl_new_account:
                state = STATES.NEW_ACCOUNT;
                switchToSignUp();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        loginButton.setEnabled(true);

        switch (i){
            case R.id.radio_student:
                type = 0;
                break;
            case R.id.radio_teacher:
                type = 1;
                break;
        }
    }

    private boolean checkInput() {
        if(txtEmail.getText().toString().isEmpty()) {
            txtEmailHolder.setError("Invalid email address");
            return false;
        } else if(txtPassword.getText().toString().isEmpty()) {
            txtPasswordHolder.setError("Invalid password");
            return false;
        }
        return true;
    }

    private void clearTextErrors(){
        txtEmailHolder.setError(null);
        txtPasswordHolder.setError(null);
    }

    private void switchToSignUp() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        lytCredentials.setAnimation(animation);
        lytCredentials.animate();
        lytCredentials.setVisibility(View.GONE);
        clearTextErrors();
        txtPassword.setText("");

        lytCredentials.setAnimation(animation1);
        lytCredentials.animate();

        lblNewAccount.setVisibility(View.GONE);
        lblLoginTitle.setText(getResources().getText(R.string.lbl_signup_title));
        lytCredentials.setVisibility(View.VISIBLE);

    }

    private void switchToSignIn(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        lytCredentials.setAnimation(animation);
        lytCredentials.animate();
        lytCredentials.setVisibility(View.GONE);
        clearTextErrors();
        txtPassword.setText("");

        lytCredentials.setAnimation(animation1);
        lytCredentials.animate();

        lblNewAccount.setVisibility(View.VISIBLE);
        lblLoginTitle.setText(getResources().getText(R.string.lbl_login_title));
        lytCredentials.setVisibility(View.VISIBLE);
    }

    private void switchToTypeSelect(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        lytCredentials.setAnimation(animation);
        lytCredentials.animate();
        lytCredentials.setVisibility(View.GONE);

        exitButton.setEnabled(false);
        loginButton.setEnabled(false);

        lytUserType.setAnimation(animation1);
        lytUserType.animate();

        lytUserType.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.requestLayout();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                //lblAppName.setVisibility(View.GONE);
                //collapseView(toolbar);
            }
        }, Constants.SPLASH_TIME_OUT);
    }

    private void collapseView(final View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        final int initialHeight = rect.bottom - rect.top;

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int targetHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    view.getLayoutParams().height = targetHeight;
                } else {
                    view.getLayoutParams().height = targetHeight + (int) ((initialHeight - targetHeight) * (1 - interpolatedTime));
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration((int) ((initialHeight - targetHeight) / view.getContext().getResources().getDisplayMetrics().density));

        view.startAnimation(animation);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
