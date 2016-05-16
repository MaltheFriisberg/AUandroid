package app.athleteunbound.RESTapiUtils.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import app.athleteunbound.DatabaseHelpers.DatabaseHelper;
import app.athleteunbound.RESTapiUtils.IPFactory;

public class SportService extends IntentService {

    public SportService() {
        super(SportService.class.getName());
    }

    @Override
    protected synchronized void onHandleIntent(Intent intent) {
        Log.d("onHandleIntent ", intent.toString());
        //all the code here runs on a seperate Handler thread (seperate from the GUI thread)
        boolean threadLocked = false;
        while(!threadLocked) {
            threadLocked = true;
            final ResultReceiver receiver = intent.getParcelableExtra("receiver");
            String subUrl = intent.getStringExtra("subUrl");
            String restMethod = intent.getStringExtra("restMethod");
            SharedPreferences settings = PreferenceManager
                    .getDefaultSharedPreferences(this);
            String jsonWebToken = settings.getString("AthleteUnboundApiToken", "");
            String getResult = getRequest(subUrl, restMethod, jsonWebToken);
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            Bundle b = new Bundle();
            try {
                JSONArray sportArray = new JSONArray(getResult);
                for (int i = 0; i < sportArray.length(); i++) {
                    JSONObject sport = sportArray.getJSONObject(i);

                    db.createSport(sport);

                }
            }catch (Exception e) {

            }
            db.close();
            receiver.send(1, b);
        }
        //threadLocked = false;
        this.stopSelf();
    }

    private synchronized String getRequest(String subUrl, String restMethod, String jsonWebToken) {

        try {

            URL url = new URL(IPFactory.getBaseUrlHome()+subUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setRequestProperty("fb-access-token", params[0]); //token in the header
            conn.setRequestProperty("x-access-token", jsonWebToken);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(restMethod);
            conn.setDoInput(true);

            int HttpResult =conn.getResponseCode();
            conn.connect();
            if(HttpResult ==HttpURLConnection.HTTP_OK){

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(),"utf-8"));
                //String line;
                String response = br.readLine();





                return response;
                /*while ((br.readLine()) != null) {
                    sb.append(br.readLine() + "\n");
                }*/
                //br.close();

                //System.out.println(""+sb.toString());
                //return sb.toString();


            }else{
                //System.out.println(urlConnection.getResponseMessage());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
