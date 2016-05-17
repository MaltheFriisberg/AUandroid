package app.athleteunbound.RESTmodels;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mal on 14-05-2016.
 */
public class Athlete {
    private int _id;
    private String appUserId;
    private String username;
    private String sport;
    private String goal;
    private List<String> competencyIds;
    private List<String> competencies;
    private String createdAt;

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Athlete{" +
                "_id=" + _id +
                ", appUserId='" + appUserId + '\'' +
                ", username='" + username + '\'' +
                ", sport='" + sport + '\'' +
                ", goal='" + goal + '\'' +
                ", competencyIds=" + competencyIds +
                ", competencies=" + competencies +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Athlete() {

    }
    public Athlete(JSONObject athlete) {
        try {
            this._id = athlete.getInt("_id");
            this.appUserId = athlete.getString("appUserId");
            this.sport = athlete.getString("sport");
            this.goal = athlete.getString("goal");
            this.competencyIds = new ArrayList<>();
            JSONArray jsonArray = (JSONArray)athlete.getJSONArray("competencyratingId");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    competencyIds.add(jsonArray.get(i).toString());
                }
            }

            this.competencies = new ArrayList<String>();
            JSONArray jsonArray1 = (JSONArray)athlete.getJSONArray("competencies");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    competencyIds.add(jsonArray.get(i).toString());
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public List<String> getCompetencyIds() {
        return competencyIds;
    }

    public void setCompetencyIds(List<String> competencyIds) {
        this.competencyIds = competencyIds;
    }

    public List<String> getCompetencies() {
        return competencies;
    }

    public void setCompetencies(List<String> competencies) {
        this.competencies = competencies;
    }


}
