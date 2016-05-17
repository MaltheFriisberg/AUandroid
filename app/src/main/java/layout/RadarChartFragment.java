package layout;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;
import java.util.List;

import app.athleteunbound.DatabaseHelpers.DatabaseHelper;
import app.athleteunbound.R;
import app.athleteunbound.RESTmodels.Competency;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RadarChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RadarChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RadarChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RadarChart chart;
    protected View mView;

    private OnFragmentInteractionListener mListener;

    public RadarChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RadarChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RadarChartFragment newInstance(String param1, String param2) {
        RadarChartFragment fragment = new RadarChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //this.chart = (RadarChart) this.mView.findViewById(R.id.chart);
        this.chart = (RadarChart)getView().findViewById(R.id.chart);
        setUpRadarChart();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_radar_chart, container, false);
        this.mView = inflater.inflate(R.layout.fragment_radar_chart, container, false);
        this.chart = (RadarChart)rootView.findViewById(R.id.chart);
        setUpRadarChart();
        return inflater.inflate(R.layout.fragment_radar_chart, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void setUpRadarChart() {

        chart.setWebLineWidth(0);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(5f, 1));
        entries.add(new Entry(2f, 2));
        entries.add(new Entry(7f, 3));
        //entries.add(new Entry(6f, 4));
        //entries.add(new Entry(5f, 5));

        ArrayList<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(1f, 0));
        entries2.add(new Entry(5f, 1));
        entries2.add(new Entry(6f, 2));
        entries2.add(new Entry(3f, 3));
        //entries2.add(new Entry(4f, 4));
        //entries2.add(new Entry(8f, 5));

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
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        for(Competency competency : dbHelper.getAthleteCompetencies()) {
            labels.add(competency.getName());
        }

        /*labels.add("Communication");
        labels.add("Technical Knowledge");
        labels.add("Team Player");
        labels.add("Meeting Deadlines");
        labels.add("Problem Solving");
        labels.add("Punctuality");
        labels.add("competence 1");
        labels.add("competence 2");
        labels.add("competence 3");*/
        RadarData data1 = new RadarData();

        RadarData data = new RadarData(labels, dataSets);
        this.chart.setData(data);
    }
}
