package app.athleteunbound.RESTmodels;

import org.json.JSONObject;

/**
 * Created by Mal on 15-05-2016.
 */
public class Sport {
    private int primaryKey;
    private String name;
    private String createdAt;

    public Sport() {

    }

    public Sport(int primaryKey, String name, String createdAt) {
        this.primaryKey = primaryKey;
        this.name = name;
        this.createdAt = createdAt;
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
