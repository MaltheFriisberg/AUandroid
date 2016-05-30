package layout;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.athleteunbound.DatabaseHelpers.DatabaseHelper;
import app.athleteunbound.R;
import app.athleteunbound.RESTmodels.Competency;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevelopmentFragment extends Fragment {

    //RoundCornerProgressBar bar;
    private HashMap<TextView, RoundCornerProgressBar> competencies;
    public DevelopmentFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_development, container, false);
        this.competencies = new HashMap<>();
        LinearLayout layout =(LinearLayout) v.findViewById(R.id.LinearLayoutDevelopment);
        TextView textView11 = (TextView) v.findViewById(R.id.textView11);
        RoundCornerProgressBar bar1 = (RoundCornerProgressBar)v.findViewById(R.id.competencyProgress1);
        competencies.put(textView11, bar1);
        TextView textView12 = (TextView) v.findViewById(R.id.textView12);
        RoundCornerProgressBar bar2 = (RoundCornerProgressBar)v.findViewById(R.id.competencyProgress2);
        competencies.put(textView12, bar2);
        TextView textView13 = (TextView) v.findViewById(R.id.textView13);
        RoundCornerProgressBar bar3 = (RoundCornerProgressBar)v.findViewById(R.id.competencyProgress3);
        competencies.put(textView13, bar3);
        TextView textView14 = (TextView) v.findViewById(R.id.textView14);
        RoundCornerProgressBar bar4 = (RoundCornerProgressBar)v.findViewById(R.id.competencyProgress4);
        competencies.put(textView14, bar4);
        //bar.setVisibility(View.INVISIBLE);
        //getActivity().findViewById(R.id.L)
        //add(v);
        setAthleteCompetencies();
        return v;
    }
    private void setAthleteCompetencies() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        List<Competency> athleteCompetencies = dbHelper.getAthleteCompetencies();
        int i = 0;
        for(Map.Entry<TextView, RoundCornerProgressBar> entry : competencies.entrySet())
        {
            TextView key=entry.getKey();
            RoundCornerProgressBar val=entry.getValue();
            key.setText(athleteCompetencies.get(i).getName());
            i++;
        }

    }
    /*private void add(View v) {
        Context context = getActivity().getApplicationContext();
        RoundCornerProgressBar bar1 = new RoundCornerProgressBar(context, new AttributeSet() {
            @Override
            public int getAttributeCount() {
                return 0;
            }

            @Override
            public String getAttributeName(int index) {
                return null;
            }

            @Override
            public String getAttributeValue(int index) {
                return null;
            }

            @Override
            public String getAttributeValue(String namespace, String name) {
                return null;
            }

            @Override
            public String getPositionDescription() {
                return null;
            }

            @Override
            public int getAttributeNameResource(int index) {
                return 0;
            }

            @Override
            public int getAttributeListValue(String namespace, String attribute, String[] options, int defaultValue) {
                return 0;
            }

            @Override
            public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue) {
                return false;
            }

            @Override
            public int getAttributeResourceValue(String namespace, String attribute, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeIntValue(String namespace, String attribute, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue) {
                return 0;
            }

            @Override
            public float getAttributeFloatValue(String namespace, String attribute, float defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeListValue(int index, String[] options, int defaultValue) {
                return 0;
            }

            @Override
            public boolean getAttributeBooleanValue(int index, boolean defaultValue) {
                return false;
            }

            @Override
            public int getAttributeResourceValue(int index, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeIntValue(int index, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeUnsignedIntValue(int index, int defaultValue) {
                return 0;
            }

            @Override
            public float getAttributeFloatValue(int index, float defaultValue) {
                return 0;
            }

            @Override
            public String getIdAttribute() {
                return null;
            }

            @Override
            public String getClassAttribute() {
                return null;
            }

            @Override
            public int getIdAttributeResourceValue(int defaultValue) {
                return 0;
            }

            @Override
            public int getStyleAttribute() {
                return 0;
            }
        });
    }*/

}
