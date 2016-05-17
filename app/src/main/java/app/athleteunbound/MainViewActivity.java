package app.athleteunbound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.athleteunbound.DatabaseHelpers.DatabaseHelper;
import app.athleteunbound.Interfaces.AsyncResponse;
import app.athleteunbound.Interfaces.AsyncResponse1;
import app.athleteunbound.RESTapiUtils.ApiRequestAsync;
import app.athleteunbound.RESTapiUtils.PostPutRequestAsync;
import app.athleteunbound.RESTmodels.Athlete;
import app.athleteunbound.RESTmodels.Competency;
import layout.RadarChartFragment;

public class MainViewActivity extends FragmentActivity implements RadarChartFragment.OnFragmentInteractionListener {

    private ProgressBar spinner;
    private ProgressBar bar5;
    //private JSONObject athlete;
    private JSONObject competencyRatings;
    private Athlete athlete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        this.spinner = (ProgressBar)findViewById(R.id.progressBar4);
        this.bar5 = (ProgressBar)findViewById(R.id.progressBar5);
        this.spinner.setVisibility(View.GONE);
        this.bar5.setVisibility(View.GONE);
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            RadarChartFragment radarChartFragment = new RadarChartFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            radarChartFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, radarChartFragment).commit();
        }
        //this.chart = (RadarChart) findViewById(R.id.chart);
        //setUpRadarChart();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("Fragment interation ", "");

    }
}
