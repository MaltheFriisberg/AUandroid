package app.athleteunbound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.athleteunbound.DatabaseHelpers.DatabaseHelper;
import app.athleteunbound.Interfaces.AsyncResponse;
import app.athleteunbound.Interfaces.AsyncResponse1;
import app.athleteunbound.RESTapiUtils.ApiRequestAsync;
import app.athleteunbound.RESTapiUtils.Services.BgProcessingResultReceiver;
import app.athleteunbound.RESTapiUtils.Services.SportService;
import app.athleteunbound.RESTmodels.OnSwipeTouchListener;
import app.athleteunbound.RESTmodels.Sport;

public class SportActivity extends AppCompatActivity implements BgProcessingResultReceiver.Receiver {
    DatabaseHelper db;
    TextView textView3;
    TextView textView5;
    TextView textView6;
    ListView listView;
    ListView listView2;
    RelativeLayout layout;
    private ProgressBar spinner;
    JSONObject appUser;
    JSONObject athlete;
    private Set<JSONObject> sportSet = new HashSet<JSONObject>();
    private HashMap<String, JSONObject> sportMap = new HashMap<>();
    private HashMap<String, Sport> sportMap1 = new HashMap<>();
    private List<Sport> sportList = new ArrayList<>();
    public BgProcessingResultReceiver mReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getApplicationContext().deleteDatabase("AthleteManager"); //DELETES the DB only use when changing the schema
        super.onCreate(savedInstanceState);
        mReceiver = new BgProcessingResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        this.db = new DatabaseHelper(getApplicationContext());
        if(savedInstanceState == null){
            // everything else that doesn't update UI
            final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null, this, SportService.class);
            serviceIntent.putExtra("subUrl", "api/sport");
            serviceIntent.putExtra("restMethod", "GET");
            serviceIntent.putExtra("receiver", this.mReceiver);
            //serviceIntent.putExtra("DatabaseHelper", this.db);
            //db.deleteAllSports();
            //db.
            startService(serviceIntent);
        }
        setContentView(R.layout.activity_sport);
        Intent intent = getIntent();
        try {
            this.appUser = new JSONObject(intent.getStringExtra("appUser"));
        } catch (Exception e) {

        }

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        spinner.setVisibility(View.GONE);
        this.textView6 = (TextView)findViewById(R.id.textView6);
        this.listView = (ListView)findViewById(R.id.listView);
        this.listView2 = (ListView)findViewById(R.id.listView2);
        this.layout = (RelativeLayout)findViewById(R.id.RelativeLayout);
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        String tokenString = settings.getString("AthleteUnboundApiToken", ""/*default value*/);


        //populateListView();

        /*ApiRequestAsync sports = (ApiRequestAsync) new ApiRequestAsync(new AsyncResponse1(){

            @Override
            public void processFinish(String result) {
                try {
                    JSONArray arr = new JSONArray(result);
                    DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                    for(int i = 0; i < arr.length(); i++) {
                        helper.deleteAllSports();
                    }
                    JSONArray array = new JSONArray(result);
                    populateListView();
                } catch (Exception e) {

                }

            }
        }, this.spinner).execute("api/sport", "GET", tokenString, "");*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String valueSelected =(String) parent.getItemAtPosition(position);
                textView6.setText(valueSelected);
                Log.d("val selected", valueSelected);
            }
        });

        layout.setOnTouchListener(new OnSwipeTouchListener(SportActivity.this){
            public void onSwipeLeft() {
                Toast.makeText(SportActivity.this, "Swiped Left", Toast.LENGTH_SHORT).show();
                //if the sport is chosen,
                if(textView6.getText().toString() != null && !textView6.getText().toString().equals("")) {
                    String sportChosen = textView6.getText().toString();
                    changeToCompetencyActivity(sportChosen);

                }
                //Go to the CompetenciesActivity
            }
            public void onSwipeRight() {
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                List<Sport> sports = db.getAllSports();
                Toast.makeText(SportActivity.this, sports.get(0).toString() +" "+ sports.size(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void populateListView() {
        List<Sport> sports = db.getAllSports();
        List<String> listContents = new ArrayList<>();
        for(Sport sport : sports) {
            sportList.add(sport);
            sportMap1.put(sport.getName(), sport);
            listContents.add(sport.getName());

        }
        /*for(int i = 0; i < sports.length(); i++) {
            try {
                String obj = array.getString(i);
                JSONObject jsonObject = new JSONObject(array.getString(i));
                sportSet.add(jsonObject);
                sportMap.put(jsonObject.getString("name"), jsonObject);
                listContents.add(jsonObject.getString("name"));
            } catch (Exception e) {

            }

        }*/
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContents));
    }
    private void changeToCompetencyActivity(String sportChosen) {
        try {
            this.appUser.put("sport", sportChosen);
            this.athlete = new JSONObject();
            this.athlete.put("sport", sportChosen);
            this.athlete.put("appUserId", this.appUser.getString("_id"));
        } catch (Exception e) {
            Log.d(" ",e.toString());
        }

        Intent intent = new Intent(this, CompetencyActivity.class);
        intent.putExtra("sport", sportMap1.get(sportChosen).toString());
        intent.putExtra("athlete", this.athlete.toString());
        intent.putExtra("appUser", this.appUser.toString());
        startActivity(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        populateListView();
    }
}
