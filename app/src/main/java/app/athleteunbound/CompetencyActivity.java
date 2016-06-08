package app.athleteunbound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import app.athleteunbound.DatabaseHelpers.DatabaseHelper;
import app.athleteunbound.RESTmodels.Competency;
import app.athleteunbound.RESTmodels.OnSwipeTouchListener;

//hjælp her http://theopentutorials.com/tutorials/android/listview/android-multiple-selection-listview/
public class CompetencyActivity extends AppCompatActivity {
    ListView listView3;
    //JSONObject appUser;
    JSONObject athlete;
    ArrayAdapter<String> adapter;
    RelativeLayout layout;
    List<String> competencies = new ArrayList<>();
    HashMap<String, Competency> competencyMap = new HashMap<>();
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.db = new DatabaseHelper(getApplicationContext());
        setContentView(R.layout.activity_competency);
        this.layout = (RelativeLayout)findViewById(R.id.RelativeLayoutCompetencies);
        listView3 = (ListView)findViewById(R.id.listView3);
        Intent intent = getIntent();
        try {
            //this.appUser = new JSONObject(intent.getStringExtra("appUser")); //we also need the id??
            this.athlete = new JSONObject(intent.getStringExtra("athlete"));

        } catch (Exception e) {

        }

        String sport = intent.getStringExtra("sportChosen");
        addCompetencies(sport);
        this.adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, this.competencies);
        listView3.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView3.setAdapter(adapter);
        configureTouchListener();
        int x = 1;
        int y = 7;
        int k = x+y;
    }
    private void addCompetencies(String sportJson) {
        Log.d("addCompetencies", "");
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        try {
            JSONObject sport = new JSONObject(sportJson);
            //JSONArray competenciesJson = appUser.getJSONArray("competencies");

            List<Competency> competenciesFromDb = dbHelper.getCompetencies(sport.getString("_androidId"));

            for(Competency competency : competenciesFromDb) {
                this.competencies.add(competency.getName());
                this.competencyMap.put(competency.getName(), competency);
            }
            int x = 1;
            int y = 2;
            int k = x+y;
            /*if (competenciesJson != null) {
                int len = competenciesJson.length();
                for (int i=0;i<len;i++){
                    JSONObject competency = (JSONObject) competenciesJson.get(i);
                    this.competencies.add(competency.getString("name"));
                }
            }*/
            //dbHelper.close();
        }catch (Exception e) {
            Log.d("addCompetencies ", e.toString());
        }
    }
    private void configureTouchListener() {
        this.layout.setOnTouchListener(new OnSwipeTouchListener(CompetencyActivity.this){
            @Override
            public void onSwipeLeft() {
                //Toast.makeText(CompetencyActivity.this, "swiped left", Toast.LENGTH_SHORT).show();
                SparseBooleanArray checked = listView3.getCheckedItemPositions();
                if(checked.size() == 4) {

                    ArrayList<String> selectedItems = new ArrayList<String>();
                    for (int i = 0; i < checked.size(); i++) {
                        // Item position in adapter
                        int position = checked.keyAt(i);
                        // Add sport if it is checked i.e.) == TRUE!
                        if (checked.valueAt(i))
                            selectedItems.add(adapter.getItem(position));
                    }

                    String[] outputStrArr = new String[selectedItems.size()];

                    for (int i = 0; i < selectedItems.size(); i++) {
                        outputStrArr[i] = selectedItems.get(i);
                    }
                    changeToGoalActivity(outputStrArr);
                } else {
                    Toast.makeText(CompetencyActivity.this, "You have to pick 4 minimum", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onSwipeRight() {
                //db.getCompetencies()

            }
        });
    }
    private void changeToGoalActivity(String[] competenciesChosen) {
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        ArrayList<Competency> competencies = new ArrayList<>();
        for(String chosen : competenciesChosen) {
            competencies.add(competencyMap.get(chosen));
        }
        helper.saveAthleteCompetencies(competencies);
        try {

            JSONArray arr = new JSONArray(Arrays.asList(competenciesChosen));
            //this.appUser.put("competencies", arr);
            this.athlete.put("competencies", arr);
        }catch (Exception e) {

        }
        Intent intent = new Intent(this, GoalActivity.class);
        //intent.putExtra("appUser", this.appUser.toString());
        intent.putExtra("athlete", this.athlete.toString());
        startActivity(intent);
    }
}
