package app.athleteunbound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.athleteunbound.Interfaces.AsyncResponse;
import app.athleteunbound.Interfaces.AsyncResponse1;
import app.athleteunbound.RESTapiUtils.ApiRequestAsync;

public class SportActivity extends AppCompatActivity {
    ListView listView;
    private Set<JSONObject> sportSet = new HashSet<JSONObject>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        this.listView = (ListView)findViewById(R.id.listView);
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        String tokenString = settings.getString("AthleteUnboundApiToken", ""/*default value*/);

        ApiRequestAsync sports = (ApiRequestAsync) new ApiRequestAsync(new AsyncResponse1(){

            @Override
            public void processFinish(String result) {
                try {

                    JSONArray array = new JSONArray(result);
                    populateListView(array);
                } catch (Exception e) {

                }

            }
        }).execute("api/sport", "GET", tokenString, "");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String valueSelected =(String) parent.getItemAtPosition(position);
                Log.d("val selected", valueSelected);
            }
        });
    }
    private void populateListView(JSONArray array) {
        List<String> listContents = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            try {
                String obj = array.getString(i);
                JSONObject jsonObject = new JSONObject(array.getString(i));
                sportSet.add(jsonObject);
                listContents.add(jsonObject.getString("name"));
            } catch (Exception e) {

            }

        }
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContents));
    }


}
