package app.athleteunbound.RESTmodels;

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
}
