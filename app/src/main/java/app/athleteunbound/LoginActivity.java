package app.athleteunbound;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

/**
 * Created by Mal on 19-04-2016.
 */
public class LoginActivity extends Activity implements AsyncResponse {

    ProgressDialog progressDialog;
    TextView textField_username;
    TextView textField_password;
    LoginButton FBlogin_button;
    ProgressBar spinner;
    String tokenString;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private Profile mProfile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                int k = 1;
                int f = 7;
                handleUserLogin((JSONObject)obj);


            }
        });
        config.Register(callbackManager, FBlogin_button);
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        this.tokenString = settings.getString("AthleteUnboundApiToken", ""/*default value*/);

        spinner = (ProgressBar)findViewById(R.id.progressBar3);
        spinner.setVisibility(View.GONE);
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
    private void runMainViewActivity(JSONObject obj) {
        Intent intent = new Intent(this, MainViewActivity.class);
        intent.putExtra("AppUser",obj.toString());
        startActivity(intent);
    }
    private void runSignupFlow(JSONObject obj){
        Intent intent = new Intent(this, SportActivity.class);
        intent.putExtra("appUser",obj.toString());
        startActivity(intent);
    }

    private void saveNewAppUser(JSONObject obj) {
        ApiRequestAsync apiCommunicator = (ApiRequestAsync)new ApiRequestAsync(new AsyncResponse1() {
            @Override
            public void processFinish(String obj) {
                try {
                    JSONObject jsonObj = new JSONObject(obj);
                    if(!jsonObj.has("error")) {

                        //Log.d("error", obj.getString("error"));
                        SaveToken(jsonObj.getString("token"));
                        runSignupFlow(jsonObj);
                    } else {
                        //the user already exists
                        //load the main Window

                        runMainViewActivity(jsonObj);
                    }
                } catch (Exception e) {
                    int k = 1;
                    int y = 7;
                    int u = 10;
                    e.printStackTrace();
                }

            }
        }, this.spinner).execute(obj.toString());
        /*AsyncTask apiRequestAsync = new ApiRequestAsync(new AsyncResponse1() {
            @Override
            public void processFinish(String result) {

            }
        }, this.spinner).execute();*/
        //ApiCommunicator.execute((JSONObject)obj);
        //if the user is created
        //go to signup flow
        //else if user exists go to mainView
    }

    private void handleUserLogin(JSONObject obj) {
        ApiRequestAsync apiCommunicator = (ApiRequestAsync)new ApiRequestAsync(new AsyncResponse1() {
            @Override
            public void processFinish(String obj) {
                try {
                    JSONObject jsonObj = new JSONObject(obj);
                    if(!jsonObj.has("error")) {

                        //Log.d("error", obj.getString("error"));
                        SaveToken(jsonObj.getString("token"));
                        runSignupFlow(jsonObj);
                    } else {
                        //the user already exists
                        //load the main Window

                        runMainViewActivity(jsonObj);
                    }
                } catch (Exception e) {
                    int k = 1;
                    int y = 7;
                    int u = 10;
                    e.printStackTrace();
                }

            }
        }, this.spinner).execute("api/appuser/authenticate/facebook", "GET", this.tokenString, obj.toString());
        /*AsyncTask FaceBookAuthAsync = new FacebookAuthAsync(new AsyncResponse() {
            @Override
            public void processFinish(JSONObject obj) {
                Log.d("auth ",obj.toString());
                //Object success = obj.getBoolean("success");
                try {
                    JSONObject appUser = obj.getJSONObject("AppUser");
                    if(obj.getBoolean("success")!=true) {

                        saveNewAppUser(appUser);
                        //start the signup flow
                        runSignupFlow(appUser);
                    } else {

                        String token = obj.getString("token");

                        SaveToken(token);
                        runMainViewActivity(appUser);

                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).execute(obj);*/

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
    public void processFinish(JSONObject output) { //when the async callback comes back..
        int y = 7;
        int x = 2;
        int k = y + x;



    }
}
