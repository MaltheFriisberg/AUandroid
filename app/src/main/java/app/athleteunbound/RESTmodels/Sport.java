package app.athleteunbound.RESTmodels;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mal on 15-05-2016.
 */
public class Sport {
    private int primaryKey;
    private String backendId;
    private String name;
    private String description;
    private String createdAt;
    private List<Competency> competencies;

    public Sport() {

    }

    public Sport(int primaryKey, String name, String createdAt, List<Competency> competencies) {
        this.primaryKey = primaryKey;
        this.name = name;
        this.createdAt = createdAt;
    }
    public Sport(JSONObject sport) {
        try {
            this.backendId = sport.getString("_id");
            this.name = sport.getString("name");
            this.description = sport.getString("description");
            JSONArray competencyArray = sport.getJSONArray("competencies");
            List<Competency> competencies = new ArrayList<>();
            for(int i = 0; i < competencyArray.length(); i++) {
                JSONObject competencyJson = competencyArray.getJSONObject(i);
                Competency competency = new Competency(
                        0,
                        competencyJson.getString("name"),
                        competencyJson.getString("category"),
                        competencyJson.getString("_competencyId")
                );
                competencies.add(competency);
            }
            this.competencies = competencies;
        }catch (Exception e) {

        }
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Sport{" +
                "primaryKey=" + primaryKey +
                ", name='" + name + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
    public JSONObject getJSONobject() {
        try {
            JSONObject toReturn = new JSONObject();
            toReturn.put("_androidId", this.getPrimaryKey());
            toReturn.put("name", this.getName());
            toReturn.put("createdAt", this.getCreatedAt());
            return toReturn;
        }catch (Exception e) {

        }
        return new JSONObject();
    }
}
