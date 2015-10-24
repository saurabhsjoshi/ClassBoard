package com.sau.classboard.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.magnet.mmx.client.api.MMX;
import com.magnet.mmx.client.api.MMXUser;
import com.sau.classboard.Manifest;
import com.sau.classboard.R;
import com.sau.classboard.model.UserData;
import com.sau.classboard.utility.Constants;

/**
 * Created by saurabh on 2015-10-17.
 */
public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener, RadioGroup.OnCheckedChangeListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private Toolbar toolbar;
    private ProgressBar progressBar;

    private Button loginButton;
    private Button exitButton;

    private Context context;

    private TextView lblAppName;
    private TextView lblNewAccount;
    private TextView lblLoginTitle;

    private LinearLayout lytCredentials;
    private LinearLayout lytUserType;
    private LinearLayout lytName;
    private RadioGroup lytGroup;

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtFirstName;
    private EditText txtLastName;

    private TextInputLayout txtEmailHolder;
    private TextInputLayout txtPasswordHolder;
    private TextInputLayout txtFirstNameHolder;
    private TextInputLayout txtLastNameHolder;

    private String firstName, lastName, email, password, username;

    private SharedPreferences prefs;

    private enum STATES { LOGIN_SCREEN, NEW_ACCOUNT, NAME_SCREEN, TYPE_SELECT}

    private STATES state = STATES.LOGIN_SCREEN;

    // Student = 0 , Teacher = 1
    private int type = 0;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        init();
    }



    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbar);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(prefs.getBoolean("IS_LOGGED_IN", false)){
            logInUser();
        }

        lytCredentials = (LinearLayout) findViewById(R.id.layout_credentials);
        lytUserType = (LinearLayout) findViewById(R.id.layout_user_type);
        lytName = (LinearLayout) findViewById(R.id.layout_name);
        lytGroup = (RadioGroup) findViewById(R.id.layout_radio);

        loginButton =  (Button) findViewById(R.id.loginButton);
        exitButton = (Button) findViewById(R.id.exitButton);

        loginButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        lytGroup.setOnCheckedChangeListener(this);

        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        txtFirstName = (EditText) findViewById(R.id.txt_first_name);
        txtLastName = (EditText) findViewById(R.id.txt_last_name);

        txtEmailHolder = (TextInputLayout) findViewById(R.id.txt_email_holder);
        txtPasswordHolder = (TextInputLayout) findViewById(R.id.txt_password_holder);
        txtFirstNameHolder = (TextInputLayout) findViewById(R.id.txt_first_name_holder);
        txtLastNameHolder = (TextInputLayout) findViewById(R.id.txt_last_name_holder);

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
                //Exit on login screen
                if(state == STATES.LOGIN_SCREEN)
                    finish();
                //Switch back to login from Sign Up
                else if(state == STATES.NEW_ACCOUNT){
                    state = STATES.LOGIN_SCREEN;
                    switchToSignIn();
                }
                break;

            case R.id.loginButton:
                //Login button on login screen
                if(state == STATES.LOGIN_SCREEN && checkInput()){
                    initValues();
                    //startActivity(new Intent(this, HomeActivity.class));
                    logInUser();
                }

                //Next on Email and password screen to Name screen
                else if(state == STATES.NEW_ACCOUNT && checkInput()){
                    email = txtEmail.getText().toString();
                    password = txtPassword.getText().toString();

                    state = STATES.NAME_SCREEN;
                    switchToNameSelect();
                }

                //Next on name screen to type select
                else if(state == STATES.NAME_SCREEN && checkNameInput()){
                    firstName = txtFirstName.getText().toString();
                    lastName = txtLastName.getText().toString();
                    state = STATES.TYPE_SELECT;
                    switchToTypeSelect();
                }

                //Sign Up button on type screen
                else if(state == STATES.TYPE_SELECT){
                    loginButton.setEnabled(false);
                    signUpUser();
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

    private boolean checkNameInput(){
        if(txtFirstName.getText().toString().isEmpty()) {
            txtFirstNameHolder.setError("First name cannot be empty.");
            return false;
        } else if(txtLastName.getText().toString().isEmpty()) {
            txtLastNameHolder.setError("Last name cannot be empty.");
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

    private void switchToNameSelect(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        lytCredentials.setAnimation(animation);
        lytCredentials.animate();
        lytCredentials.setVisibility(View.GONE);

        exitButton.setEnabled(false);


        lytName.setAnimation(animation1);
        lytName.animate();

        lytName.setVisibility(View.VISIBLE);
    }

    private void switchToTypeSelect(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        lytName.setAnimation(animation);
        lytName.animate();
        lytName.setVisibility(View.GONE);

        loginButton.setText(getString(R.string.button_login_sign_up));
        exitButton.setEnabled(false);
        if(!lytGroup.isSelected())
            loginButton.setEnabled(false);
        else
            loginButton.setEnabled(true);


        lytUserType.setAnimation(animation1);
        lytUserType.animate();

        lytUserType.setVisibility(View.VISIBLE);
    }


    private void signUpUser(){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        loginButton.setText(getString(R.string.button_logging_in));
        lytUserType.setVisibility(View.GONE);

        String [] split = email.split("@");
        String domain = split[split.length-1];

        //Create a username with a domain
        username = domain + "@" + email;

        if(Build.VERSION.SDK_INT >= 23){
            getPermissions();
        }
        else {
            preMSignUp();
        }
    }

    private void postMInit(){
        com.magnet.mmx.client.common.Log.setLoggable(null, com.magnet.mmx.client.common.Log.VERBOSE);
        MMX.init(this, R.raw.classboard);
        MMX.registerWakeupBroadcast(new Intent("NEW_MESSAGE"));
    }

    private void preMSignUp(){
        MMXUser user = new MMXUser.Builder().username(username).build();
        user.register(password.getBytes(), new MMXUser.OnFinishedListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveCredentials();
                        logInUser();
                    }
                });
            }
            @Override
            public void onFailure(MMXUser.FailureCode failureCode, Throwable throwable) {
                if(throwable != null){
                    Toast.makeText(context, "Could not sign up." + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    throwable.printStackTrace();
                }
                else{
                    Toast.makeText(context, "Could not sign up. Please choose different username", Toast.LENGTH_LONG).show();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((LoginActivity) context).recreate();
                    }
                });

            }
        });
    }


    private void logInUser() {
        if(state == STATES.LOGIN_SCREEN){
            username = UserData.getCurrentUser(this).username;
            password = UserData.getCurrentUser(this).password;
        }

        MMX.login(username,
                password.getBytes(), new MMX.OnFinishedListener<Void>() {
            public void onSuccess(Void aVoid) {
                MMX.start();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        prefs.edit().putBoolean("IS_LOGGED_IN", true).commit();
                        startActivity(new Intent(context, HomeActivity.class));
                    }
                });

            }

            public void onFailure(MMX.FailureCode failureCode, final Throwable throwable) {
                if (MMX.FailureCode.SERVER_AUTH_FAILED.equals(failureCode)) {
                    Toast.makeText(context, "Login failed." + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((LoginActivity) context).recreate();
                        }
                    });
                }
            }
        });
    }

    private void saveCredentials(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("EMAIL", email);
        editor.putString("PASSWORD", password);
        editor.putString("FIRST_NAME", firstName);
        editor.putString("LAST_NAME", lastName);
        editor.putString("USERNAME", username);
        editor.putInt("TYPE", type);
        editor.commit();
    }

    private void initValues(){
        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();

        String [] split = email.split("@");
        String domain = split[split.length-1];

        //Create a username with a domain
        username = domain + "@" + email;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.requestLayout();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                lblAppName.setVisibility(View.GONE);
                collapseView(toolbar);
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

    private void getPermissions(){
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_PHONE_STATE);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            //Ask for permission
            if (!ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {
                showMessageOKCancel("ClassBoard requires device ID to encrypt data.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(LoginActivity.this,
                                        new String[] {Manifest.permission.READ_PHONE_STATE},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
        //Permission is already granted
        else{
            postMInit();
            if(state != STATES.LOGIN_SCREEN)
                preMSignUp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        if(requestCode == REQUEST_CODE_ASK_PERMISSIONS){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                signUpUser();
            }
        }
    }


}
