package app.athleteunbound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

import app.athleteunbound.Interfaces.AsyncResponse;
import app.athleteunbound.Interfaces.AsyncResponse1;
import app.athleteunbound.RESTapiUtils.ApiRequestAsync;
import app.athleteunbound.RESTapiUtils.PostPutRequestAsync;
import app.athleteunbound.RESTmodels.Athlete;

public class MainViewActivity extends AppCompatActivity {
    private RadarChart chart;
    private ProgressBar spinner;
    //private JSONObject athlete;
    private JSONObject competencyRatings;
    private Athlete athlete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        this.spinner = (ProgressBar)findViewById(R.id.progressBar4);
        this.spinner.setVisibility(View.GONE);
        this.chart = (RadarChart) findViewById(R.id.chart);
        setUpRadarChart();
    }

    private void setUpRadarChart() {

        chart.setWebLineWidth(0);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(5f, 1));
        entries.add(new Entry(2f, 2));
        entries.add(new Entry(7f, 3));
        entries.add(new Entry(6f, 4));
        entries.add(new Entry(5f, 5));

        ArrayList<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(1f, 0));
        entries2.add(new Entry(5f, 1));
        entries2.add(new Entry(6f, 2));
        entries2.add(new Entry(3f, 3));
        entries2.add(new Entry(4f, 4));
        entries2.add(new Entry(8f, 5));

        RadarDataSet dataset_comp1 = new RadarDataSet(entries, "this week");

        RadarDataSet dataset_comp2 = new RadarDataSet(entries2, "lastweek");

        dataset_comp1.setColor(Color.CYAN);

        dataset_comp2.setColor(Color.RED);

        dataset_comp1.setDrawFilled(true);

        dataset_comp2.setDrawFilled(true);

        List<IRadarDataSet> dataSets = new ArrayList<IRadarDataSet>();
        dataSets.add(dataset_comp1);
        dataSets.add(dataset_comp2);

        List<String> labels = new ArrayList<>();


        labels.add("Communication");
        labels.add("Technical Knowledge");
        labels.add("Team Player");
        labels.add("Meeting Deadlines");
        labels.add("Problem Solving");
        labels.add("Punctuality");
        labels.add("competence 1");
        labels.add("competence 2");
        labels.add("competence 3");
        RadarData data1 = new RadarData();

        RadarData data = new RadarData(labels, dataSets);
        this.chart.setData(data);
    }

}
