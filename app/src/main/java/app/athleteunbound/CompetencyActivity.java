package app.athleteunbound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//hjælp her http://theopentutorials.com/tutorials/android/listview/android-multiple-selection-listview/
public class CompetencyActivity extends AppCompatActivity {
    ListView listView3;
    JSONObject appUser;
    ArrayAdapter<String> adapter;
    List<String> competencies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competency);
        listView3 = (ListView)findViewById(R.id.listView3);
        Intent intent = getIntent();
        try {
            this.appUser = new JSONObject(intent.getStringExtra("appUser")); //we also need the id??
        } catch (Exception e) {

        }

        String sport = intent.getStringExtra("sport");
        addCompetencies(sport);
        this.adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, this.competencies);
        listView3.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView3.setAdapter(adapter);
        int x = 1;
        int y = 7;
        int k = x+y;
    }
    private void addCompetencies(String sportJson) {
        try {
            JSONObject appUser = new JSONObject(sportJson);
            JSONArray competenciesJson = appUser.getJSONArray("competencies");

            if (competenciesJson != null) {
                int len = competenciesJson.length();
                for (int i=0;i<len;i++){
                    JSONObject competency = (JSONObject) competenciesJson.get(i);
                    this.competencies.add(competency.getString("name"));
                }
            }
        }catch (Exception e) {

        }
    }
}
