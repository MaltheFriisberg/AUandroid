package app.athleteunbound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        Intent intent = getIntent();
        String athlete = intent.getStringExtra("athlete");
        //String appUser = intent.getStringExtra("appUSer");

    }
}
