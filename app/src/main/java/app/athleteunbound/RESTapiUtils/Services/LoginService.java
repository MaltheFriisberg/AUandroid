package app.athleteunbound.RESTapiUtils.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONObject;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class LoginService extends IntentService {
    private SharedPreferences settings;
    SharedPreferences.Editor editor;
    public LoginService() {
        super("LoginService");
        this.settings = PreferenceManager
                .getDefaultSharedPreferences(this);
         this.editor = this.settings.edit();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String facebook_username = intent.getStringExtra("facebook_username");
        String facebook_userId = intent.getStringExtra("facebook_userId");
        String token;

        try {
            //authenticate
            JSONObject authenticationResponse = new JSONObject(authenticate(facebook_username, facebook_userId));
            if(authenticationResponse.getBoolean("success") != true) { //if authentication fails ( user doesnt exist)
                //save a new appUser
                JSONObject appUser = new JSONObject(saveNewAppUser());
                //authenticate the new appUSer
                JSONObject newAuthenticationResponse = new JSONObject(authenticate(facebook_username, facebook_userId));
                SaveNewToken(newAuthenticationResponse.getString("token"));
                

            } else {
                token = authenticationResponse.getString("token");
                SaveNewToken(token);
            }

        }catch (Exception e) {
            Log.d("Exception", e.toString());
        }







    }
    private String saveNewAppUser() {
        return "";
    }

    private String saveNewAthlete() {
        return "";
    }

    private String authenticate(String facebook_username, String facebook_userId) {
        return "";
    }
    protected boolean hasToken() {

        return settings.contains("AthleteUnboundApiToken");
    }
    private void SaveNewToken(String token) {
        if(hasToken()) {
            //update existing entry for token
            editor.putString("AthleteUnboundApiToken", token);
            editor.apply();
        } else {
            //save a new entry of token
            editor.putString("AthleteUnboundApiToken", token);
            editor.commit();
        }

    }
}
