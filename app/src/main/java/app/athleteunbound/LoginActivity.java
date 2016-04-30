package app.athleteunbound;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import app.athleteunbound.RESTapiUtils.ApiCommunicator;
import app.athleteunbound.RESTapiUtils.ApiCommunicatorAsync;
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
        /*FBlogin_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("token ", loginResult.getAccessToken().getToken());
                //Log.d("email", loginResult.getAccessToken().getPermissions());
                //loginResult.
                Profile profile = Profile.getCurrentProfile();
                if (Profile.getCurrentProfile() == null) {
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            mProfile = currentProfile;
                            profileTracker.startTracking();
                        }
                    };
                    profileTracker.startTracking();
                } else {
                    Profile profile1 = Profile.getCurrentProfile();
                }

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Log.d("name ", object.getString("name"));
                                    Log.d("email ", object.getString("email"));
                                    //Log.d("token",object.getString("token"));
                                    //Log.d("FbID ", object.getString("id"));
                                    ApiCommunicator.saveNewAppUser1(object);

                                    //Intent intent = new Intent(this, SportActivity.class);

                                    //txtState.setText("Hi, " + object.getString("name"));
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                Profile profile3 = mProfile;
                Profile profile2 = Profile.getCurrentProfile(); // Save with api and go to new Activity?

                //Log.d("profile ", profile.getName());

            }

            @Override
            public void onCancel() {
                Log.d("cancel ", "Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("error ", "Login attempt failed.");
            }
        });*/

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
        intent.putExtra("AppUser",obj.toString());
        startActivity(intent);
    }

    private void handleUserLogin(JSONObject obj) {
        AsyncTask apiCommunicator = new ApiCommunicator(new AsyncResponse() {
            @Override
            public void processFinish(JSONObject obj) {
                try {
                    if(obj.getString("error")== null) {
                        //the user already exists
                        //load the main Window
                        Log.d("error", obj.getString("error"));

                        runSignupFlow(obj);
                    } else {


                        runMainViewActivity(obj);
                    }
                } catch (Exception e) {
                    int k = 1;
                    int y = 7;
                    int u = 10;
                    e.printStackTrace();
                }

            }
        }).execute(obj);
        //ApiCommunicator.execute((JSONObject)obj);
        //if the user is created
        //go to signup flow
        //else if user exists go to mainView
    }

    @Override
    public void processFinish(JSONObject output) { //when the async callback comes back..
        int y = 7;
        int x = 2;
        int k = y + x;



    }
}
