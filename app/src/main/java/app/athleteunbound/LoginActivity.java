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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import app.athleteunbound.RESTapiUtils.ApiCommunicator;
import app.athleteunbound.RESTapiUtils.ApiCommunicatorAsync;
import app.athleteunbound.RESTapiUtils.FacebookUtil;

/**
 * Created by Mal on 19-04-2016.
 */
public class LoginActivity extends Activity {

    ProgressDialog progressDialog;
    TextView textField_username;
    TextView textField_password;
    LoginButton FBlogin_button;
    private CallbackManager callbackManager;

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
        FBlogin_button.setReadPermissions("email","public_profile");
        //FBlogin_button.setReadPermissions(FacebookUtil.facebookPermissions());
        //FBlogin_button.setRead


        textField_username = (TextView) findViewById(R.id.textField_username);
        textField_password = (TextView) findViewById(R.id.textField_password);
        JsonObject loginResult;
        FBlogin_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("token ", loginResult.getAccessToken().getToken());
                //Log.d("email", loginResult.getAccessToken().getPermissions());
                //loginResult.
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Log.d("name ",object.getString("name"));
                                    Log.d("email ", object.getString("email"));
                                    //Log.d("token",object.getString("token"));
                                    //Log.d("FbID ", object.getString("id"));
                                    //ApiCommunicator.saveNewAppUser1(object);
                                    new ApiCommunicator().execute(object);
                                    //Intent intent = new Intent(this, SportActivity.class);

                                    //txtState.setText("Hi, " + object.getString("name"));
                                } catch(JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.d("cancel ", "Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("error ", "Login attempt failed.");
            }
        });

    }

    public void onClickLoginBtn(View v) {
        //getApplicationContext();
        if(v.getId()== R.id.btn_login) {

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
    private class AuthenticateTask extends AsyncTask<String, String, JsonObject> {

        @Override
        protected JsonObject doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            ApiCommunicatorAsync apiCommunicatorAsync = new ApiCommunicatorAsync();
            return apiCommunicatorAsync.Login(username, password);
        }
        protected void onProgressUpdate(String... progress) {
            //progressDialog.setProgressPercent(progress[0]);
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        protected void onPostExecute(Long result) {
            progressDialog.dismiss();
        }
    }
}
