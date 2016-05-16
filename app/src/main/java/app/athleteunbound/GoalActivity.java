package app.athleteunbound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import app.athleteunbound.Interfaces.AsyncResponse;
import app.athleteunbound.RESTapiUtils.PostPutRequestAsync;
import app.athleteunbound.RESTapiUtils.Services.AthleteService;
import app.athleteunbound.RESTapiUtils.Services.BgProcessingResultReceiver;
import app.athleteunbound.RESTapiUtils.Services.LoginService;
import app.athleteunbound.RESTmodels.OnSwipeTouchListener;

public class GoalActivity extends AppCompatActivity implements BgProcessingResultReceiver.Receiver {
    EditText editText;
    RelativeLayout layout;
    JSONObject athlete;
    public BgProcessingResultReceiver mReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        this.editText = (EditText)findViewById(R.id.editText);
        this.layout = (RelativeLayout)findViewById(R.id.RelativeLayoutGoal);
        mReceiver = new BgProcessingResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = getIntent();
        try {
            this.athlete = new JSONObject(intent.getStringExtra("athlete"));
        }catch (Exception e) {

        }
        configureOnTouchListener();
    }
    private void configureOnTouchListener() {
        this.layout.setOnTouchListener(new OnSwipeTouchListener(GoalActivity.this){
            @Override
            public void onSwipeLeft() {

                //Toast.makeText(CompetencyActivity.this, "swiped left", Toast.LENGTH_SHORT).show();
                //apicall to register the athlete

                //load the mainView
                goToNextActivity();


            }
        });

    }
    private void goToNextActivity() {
        try {
            this.athlete.put("goal", editText.getText());
            Intent athleteService = new Intent(Intent.ACTION_SYNC, null, this, AthleteService.class);
            athleteService.putExtra("athlete", this.athlete.toString());
            athleteService.putExtra("receiver", this.mReceiver);
            startService(athleteService);
            Toast.makeText(GoalActivity.this, this.athlete.toString(), Toast.LENGTH_SHORT).show();
            Log.d("athlete", this.athlete.toString());
            //saveAthlete();

        }catch (Exception e) {

        }

    }
    /*private void saveAthlete() {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        String apiToken = settings.getString("AthleteUnboundApiToken", "");
        AsyncTask postAthlete = new PostPutRequestAsync(new AsyncResponse() {
            @Override
            public void processFinish(JSONObject obj) {
                Log.d("athlete posted= ", obj.toString());
            }
        }).execute("api/athlete", "POST", apiToken, this.athlete.toString());
    }*/


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        goToNextActivity();
    }
}
