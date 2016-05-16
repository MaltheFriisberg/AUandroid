package app.athleteunbound.RESTapiUtils.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import app.athleteunbound.RESTapiUtils.FacebookUtil;
import app.athleteunbound.RESTapiUtils.IPFactory;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class LoginService extends IntentService {

    public LoginService() {

        super("LoginService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor;
        String facebook_username = intent.getStringExtra("facebook_username");
        String facebook_userId = intent.getStringExtra("facebook_userId");
        String facebook_loginResult = intent.getStringExtra("facebook_loginResult");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String token;

        try {
            JSONObject facebookLoginResult = new JSONObject(facebook_loginResult);
            //authenticate
            JSONObject authenticationResponse = new JSONObject(authenticate(facebookLoginResult.getString("username"), facebookLoginResult.getString("FacebookId")));
            if(authenticationResponse.getBoolean("success") != true) { //if authentication fails ( user doesnt exist)
                //save a new appUser
                JSONObject appUser = new JSONObject(saveNewAppUser(facebook_loginResult));
                //authenticate the new appUSer
                JSONObject newAuthenticationResponse = new JSONObject(authenticate(facebook_username, facebook_userId));
                //Save the token in appPreferences
                token = newAuthenticationResponse.getString("token");
                SaveNewToken(newAuthenticationResponse.getString("token"));
                receiver.send(1, new Bundle());

            } else {
                token = authenticationResponse.getString("token");
                SaveNewToken(token);
            }

        }catch (Exception e) {
            Log.d("Exception", e.toString());
            receiver.send(2, new Bundle());
        }

    }
    private String saveNewAppUser(String newAppUser) {

        String subUrl = "api/appuser/facebook";

        String restMethod = "POST";


        try {
            JSONObject toPost = new JSONObject(newAppUser);
            JSONObject obj = FacebookUtil.formatForPost(toPost);
            URL url = new URL(IPFactory.getBaseUrlHome()+subUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            /*if(jsonWebToken.length() > 0 && !jsonWebToken.equals("")) {
                conn.setRequestProperty("x-access-token", params[2]);
            }*/
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(restMethod);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(newAppUser);
            writer.flush();
            writer.close();
            os.close();
            int HttpResult =conn.getResponseCode();
            int x = 7;

            conn.connect();
            if(HttpResult ==HttpURLConnection.HTTP_OK){

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(),"utf-8"));
                //String line;
                String test = br.readLine();

                return test;



            }else{
                //System.out.println(urlConnection.getResponseMessage());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    private String authenticate(String facebook_username, String facebook_userId) {
        try {

            URL url = new URL(IPFactory.getBaseUrlHome()+"api/appuser/authenticate/facebook");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("facebookId", facebook_userId);
            conn.setRequestProperty("username", facebook_username);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            //conn.setDoOutput(true);
            int HttpResult =conn.getResponseCode();
            conn.connect();
            if(HttpResult ==HttpURLConnection.HTTP_OK){

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(),"utf-8"));
                //String line;
                String response = br.readLine();

                return response;
            }else{
                //System.out.println(urlConnection.getResponseMessage());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void getAthlete(String token) {
        //Get the athlete
        //Save to Database
    }
    protected boolean hasToken() {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor;
        return settings.contains("AthleteUnboundApiToken");
    }
    private void SaveNewToken(String token) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
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
