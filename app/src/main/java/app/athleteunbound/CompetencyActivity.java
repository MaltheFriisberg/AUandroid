package app.athleteunbound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class CompetencyActivity extends AppCompatActivity {
    ListView listView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competency);
        listView3 = (ListView)findViewById(R.id.listView3);
        Intent intent = getIntent();

        String appUser = intent.getStringExtra("appUser"); //we also need the id??
        String sport = intent.getStringExtra("sport");
        int x = 1;
        int y = 7;
        int k = x+y;
    }
}
