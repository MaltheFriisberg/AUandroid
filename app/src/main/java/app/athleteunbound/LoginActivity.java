package app.athleteunbound;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.params.Face;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import app.athleteunbound.Interfaces.AsyncResponse;
import app.athleteunbound.Interfaces.AsyncResponse1;


import app.athleteunbound.RESTapiUtils.ApiCommunicatorAsync;
import app.athleteunbound.RESTapiUtils.ApiRequestAsync;
import app.athleteunbound.RESTapiUtils.FacebookAuthAsync;
import app.athleteunbound.RESTapiUtils.FacebookUtil;
import app.athleteunbound.RESTapiUtils.FbloginButtonConfig;
import app.athleteunbound.RESTapiUtils.PostPutRequestAsync;
import app.athleteunbound.RESTapiUtils.Services.BgProcessingResultReceiver;
import app.athleteunbound.RESTapiUtils.Services.LoginService;
import app.athleteunbound.RESTapiUtils.Services.SportService;

/**
 * Created by Mal on 19-04-2016.
 */
public class LoginActivity extends Activity implements BgProcessingResultReceiver.Receiver {

    ProgressDialog progressDialog;
    TextView textField_username;
    TextView textField_password;
    LoginButton FBlogin_button;
    ProgressBar spinner;
    String tokenString;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private Profile mProfile;
    public BgProcessingResultReceiver mReceiver;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        String token = settings.getString("AthleteUnboundApiToken", "");
        String athleteRegistered = settings.getString("athleteRegistered"," ");
        boolean hasToken = settings.contains("AthleteUnboundApiToken");
        boolean hasAthleteRegistered = settings.contains("AthleteRegistered");
        //clear the tokens, for testing signup flow
        editor.remove("AthleteUnboundApiToken");
        editor.remove("athleteRegistered");
        //editor.clear();
        editor.commit();
        if(hasToken() && hasSignedUp()) {
            runMainViewActivity();

        } else if(hasToken() && !hasSignedUp()) {
            runSignupFlow();
        }
        mReceiver = new BgProcessingResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        //setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        FBlogin_button = (LoginButton) findViewById(R.id.FBlogin_button);
        FBlogin_button.setReadPermissions(Arrays.asList("public_profile", "email"));

        //FBlogin_button.setReadPermissions(FacebookUtil.facebookPermissions());
        //FBlogin_button.setRead


        textField_username = (TextView) findViewById(R.id.textField_username);
        textField_password = (TextView) findViewById(R.id.textField_password);

        FbloginButtonConfig config = new FbloginButtonConfig(new AsyncResponse() {
            @Override
            public void processFinish(JSONObject obj) {
                //the facebook login result will notify us here
                handleUserLogin((JSONObject)obj);


            }
        });
        config.Register(callbackManager, FBlogin_button);
        /*SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        this.tokenString = settings.getString("AthleteUnboundApiToken", "");*/

        spinner = (ProgressBar)findViewById(R.id.progressBar3);
        spinner.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        if(settings.contains("AthleteUnboundApiToken") && settings.contains("AthleteRegistered")) {
            try {
                runMainViewActivity();
            }catch (Exception e) {

            }

        } else if(settings.contains("AthleteUnboundApiToken") && !settings.contains("AthleteRegistered")) {
            runSignupFlow();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        super.onResume();
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        if(settings.contains("AthleteUnboundApiToken") && settings.contains("AthleteRegistered")) {
            try {
                runMainViewActivity();
            }catch (Exception e) {

            }

        } else if(settings.contains("AthleteUnboundApiToken") && !settings.contains("AthleteRegistered")) {
            runSignupFlow();
        }
    }

    protected boolean hasToken() {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        return settings.contains("AthleteUnboundApiToken");
    }
    private boolean hasSignedUp() {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        return settings.contains("AthleteRegistered");
    }

    public void onClickLoginBtn(View v) {
        //getApplicationContext();
        if (v.getId() == R.id.btn_login) {

            String username = textField_username.getText().toString();
            String password = textField_password.getText().toString();
            Log.d("username", username);
            Log.d("password", password);
            //progressDialog.
            //new AuthenticateTask().execute(username, password);
            ApiCommunicatorAsync apiCommunicatorAsync = new ApiCommunicatorAsync();
            apiCommunicatorAsync.Login(username, password);
            String breakpoint;
        }


    }
    private void runMainViewActivity() {
        Intent intent = new Intent(this, MainViewActivity.class);
        //intent.putExtra("AppUser",obj.toString());
        startActivity(intent);
    }
    private void runSignupFlow(){
        Intent intent = new Intent(this, SportActivity.class);
        //intent.putExtra("appUser",obj.toString());
        startActivity(intent);
    }

    private void handleUserLogin(JSONObject obj) {

        Intent loginService = new Intent(Intent.ACTION_SYNC, null, this, LoginService.class);
        loginService.putExtra("receiver", this.mReceiver);
        loginService.putExtra("facebook_loginResult", obj.toString());
        try {
            loginService.putExtra("facebook_username", obj.getString("name").toString());
            loginService.putExtra("facebook_userId", obj.getString("id"));

        }catch (Exception e) {

        }
        startService(loginService);

    }
    private void SaveToken(String token) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("AthleteUnboundApiToken", token);
        editor.commit();
        String auth_token_string = settings.getString("AthleteUnboundApiToken", "");
        //Log.d("t", auth_token_string);
        int y = 7;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        //the callback from LoginService is handled here
        Log.d("login resultCode" , Integer.toString(resultCode));
        int k = resultCode +1;
        if(resultCode==1) {
            runSignupFlow();
        } else if(resultCode == 2) {
            //An error occured with the login Req
            Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();
        }
    }
}
