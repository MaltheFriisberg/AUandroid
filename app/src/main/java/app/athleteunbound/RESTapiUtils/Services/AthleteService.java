package app.athleteunbound.RESTapiUtils.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;

import org.json.JSONObject;

import app.athleteunbound.DatabaseHelpers.DatabaseHelper;
import app.athleteunbound.RESTapiUtils.ApiCommunicator;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class AthleteService extends IntentService {


    public AthleteService() {

        super("AthleteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        String token = settings.getString("AthleteUnboundApiToken", "");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String athlete =  intent.getStringExtra("athlete");
        try {
            JSONObject athleteObj = new JSONObject(intent.getStringExtra("athlete"));
            String athletePosed = PostAthlete(athlete, token);
            //save in DB
            DatabaseHelper DbHelper = new DatabaseHelper(getApplicationContext());
            long dbResult = DbHelper.saveAthlete(athletePosed);
            DbHelper.close();

        }catch (Exception e) {

            e.printStackTrace();
            receiver.send(1, new Bundle());
        }
        receiver.send(1, new Bundle());
        this.stopSelf();


    }
    private String PostAthlete(String athlete, String token) {
        return ApiCommunicator.PostPutRequest(athlete, "api/athlete", "POST",  token);
    }
}
