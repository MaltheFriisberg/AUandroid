package app.athleteunbound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.SparseBooleanArray;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import app.athleteunbound.RESTmodels.OnSwipeTouchListener;

public class GoalActivity extends AppCompatActivity {
    EditText editText;
    RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        this.editText = (EditText)findViewById(R.id.editText);
        this.layout = (RelativeLayout)findViewById(R.id.RelativeLayoutGoal);
    }
    private void configureOnTouchListener() {
        this.layout.setOnTouchListener(new OnSwipeTouchListener(GoalActivity.this){
            @Override
            public void onSwipeLeft() {
                //Toast.makeText(CompetencyActivity.this, "swiped left", Toast.LENGTH_SHORT).show();
                //apicall to register the athlete
                //load the mainView


            }
        });
    }


}
