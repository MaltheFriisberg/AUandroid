package app.athleteunbound.RESTapiUtils;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import app.athleteunbound.Interfaces.AsyncResponse;

/**
 * Created by Mal on 28-04-2016.
 */
public class FbloginButtonConfig {
    private ProfileTracker profileTracker;
    Profile mProfile;
    public AsyncResponse delegate = null;

    public FbloginButtonConfig(AsyncResponse delegate) {
        this.delegate = delegate; //The login activity registers to the async callback here
    }
    public void Register(CallbackManager callbackManager, LoginButton FBlogin_button) {
        FBlogin_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
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


                //requester.requestGraphApi(loginResult);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    object.put("FacebookToken", loginResult.getAccessToken().getToken());
                                    delegate.processFinish(object);
                                    Log.d("name ", object.getString("name"));
                                    Log.d("email ", object.getString("email"));
                                    //Log.d("token",object.getString("token"));
                                    //Log.d("FbID ", object.getString("id"));
                                    //ApiCommunicator.saveNewAppUser1(object);

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
        });
    }
}
